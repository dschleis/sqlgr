/**********************************************************
Sqlgr
Helper methods to make SQL Developer Data Modeler scripting easier

Dave Schleis
2017-Jul-07
**********************************************************/
/*
  grabTables()
  Get a list of table objects from the current relational model.
*/
def grabTables() {
  grabThings(model,'TABLE')}

/*
  grabTablesWith(String includeMe)
  Get a list of table objects from the current relational model.
  If includeMe is not null then only those tables that contain includeMe are returned.
  If includeMe is a string containing comma-seperated values, tables containing any of those strings will be returned.
  includeMe uses a double wildcard match by default. Adding a '*' or '%' to the beginning or end
  of the string will match "ends with" and "begins with" respectivly.
*/
def grabTablesWith(String includeMe='') {
  grabThings(model,'TABLE', includeMe)
}

/*
  grabTableWith(String includeMe='')
  This method will return a single table object that matches includeMe.
  The method will return null if there are no matches or more than one match
*/
def grabTableWith(String includeMe='') {
  grabSingleThing(model,'TABLE', includeMe)
}

/*
  grabTablesWithout(String excludeMe)
  Get a list of table objects from the current relational model.
  If excludeMe is not null then only those tables that do not contain excludeMe are returned.
  If excludeMe is a string containing comma-seperated values, tables not containing any of those strings will be returned.
*/
def grabTablesWithout(String excludeMe='') {
  grabThings(model,'TABLE', excludeMe, true)
}

/*
  Get the list of column objects from the indicated table or view object (not table or view name).
  If excludeList is provided, columns with names containing any of the strings
  in the excludeList will not be returned.
*/
def grabColumns(table) {
  grabColumnsWith(table)
}

def grabColumnsWith(table, includeMe='') {
  grabThings(table,'COLUMN', includeMe)
}

def grabColumnWith(table, includeMe='') {
  grabSingleThing(table,'COLUMN', includeMe)
}

def grabColumnsWithout(table, excludeMe='') {
  grabThings(table,'COLUMN', excludeMe, true)
}

def grabViews() {
  grabThings(model,'VIEW')
}

def grabViewsWith(includeMe='') {
  grabThings(model,'VIEW', includeMe)
}

def grabViewWith(String includeMe='') {
  grabSingleThing(model,'VIEW', includeMe)
}

def grabViewsWithout(excludeMe='') {
  grabThings(model,'VIEW', excludeMe, true)
}

def grabEntities() {
  grabThings(model,'ENTITY')
}

def grabEntitiesWith(includeMe='') {
  grabThings(model,'ENTITY', includeMe)
}

def grabEntityWith(includeMe='') {
  grabSingleThing(model,'ENTITY', includeMe)
}

def grabEntitiesWithout(excludeMe='') {
  grabThings(model,'ENTITY', excludeMe, true)
}

def grabAttributes(entity) {
  grabAttributesWith(entity)
}

def grabAttributesWith(entity, includeMe='') {
  grabThings(entity, 'ATTRIBUTE', includeMe)
}

def grabAttributeWith(entity, includeMe='') {
  grabSingleThing(entity, 'ATTRIBUTE', includeMe)
}

def grabAttributesWithout(entity, excludeMe='') {
  grabThings(entity, 'ATTRIBUTE', excludeMe, true)
}

def grabIndexes(table) {
	grabIndexesWith(table)
}

def grabIndexesWith(table, includeMe='') {
  grabThings(table,'INDEX', includeMe)
}

def grabIndexWith(table, includeMe='') {
  grabSingleThing(table,'INDEX', includeMe)
}

def grabIndexesWithout(table, excludeMe='') {
  grabThings(table,'INDEX', excludeMe, true)
}



/*********************************************************
"Private" methods follow
*********************************************************/
import javax.swing.JOptionPane
import javax.swing.JDialog
import javax.swing.JFrame

def ask (quesiton = 'Please provide input') {
   JOptionPane.showInputDialog(quesiton)
}

def show (message = 'Just wanted to say Hi') {
  JOptionPane.showMessageDialog(null,message)
}

/*
  log(String logItem)
  displays logItem in the Data Modeler Message Log.
*/
def log(String logItem) {
  def app = oracle.dbtools.crest.swingui.ApplicationView;
  app.log(logItem)
}

def logIt(String logItem) {
  log(logItem)
}


def nameContains (needleList, haystack) {
  /*
  returns true if haystack contains any of the strings in the needleList.
  Each needle uses a double wildcard match by default. Adding a '*' or '%' to the beginning or end
  of the string will match "ends with" and "begins with" respectivly.
*
  */
  def retVal = false
  def wildCards = ['*', '%']
  needleList.each { needle ->
    if (wildCards.contains(needle[0])) {
      // wild card in fist position
      retVal = retVal || haystack?.toUpperCase().endsWith (needle[1..-1]?.trim().toUpperCase())
    } else if (wildCards.contains(needle[-1])) {
      // wild card in last position
      retVal = retVal || haystack?.toUpperCase().startsWith (needle[0..-2]?.trim().toUpperCase())
    } else {
      // double wildcard
      retVal = retVal || haystack?.toUpperCase().contains (needle?.trim().toUpperCase())
    }
  }
  return retVal
}

def grabTheThings(container, String elementType) {
	switch (elementType.toUpperCase()) {
	  case 'TABLE':
	    container.tableSet
	    break
	  case 'ENTITY':
	    container.entitySet
	    break
	  case 'VIEW':
	    container.tableViewSet
      break
    case 'INDEX':
      container.getAllInds_FKeyInds()
      break
    case 'COLUMN':
    case 'ATTRIBUTE':
      container.elements
      break
	}
}

def grabThings(grabFrom, String whatToGrab,  includeMe='', Boolean exclude=false) {
  def retVal = []
  def withList = []
  def thingList = grabTheThings(grabFrom, whatToGrab)


  if (includeMe) {
    // if it is a list just use it
    if (includeMe.class.toString() == 'class java.util.ArrayList') {
      withList = includeMe
    } else {
      if (includeMe.contains(',')) {
         // break the comma seperated string into a list
         withList = includeMe.split(',')
      } else {  // single value put it in a list
          withList = [includeMe]
      }
    }
    thingList.each { thing ->
      if (exclude) {
        if (!nameContains (withList, thing.name)) {
          retVal << thing
        }
      } else {
        if (nameContains (withList, thing.name)) {
          retVal << thing
        }
      }
    }
   } else {  // if (includeMe)
     // return everything
     retVal = thingList
   }
   return retVal
}

def grabSingleThing(grabFrom, String whatToGrab, String includeMe='') {
  def retVal = grabThings(grabFrom, whatToGrab, includeMe)
  if (retVal.size() == 1) {
  	return retVal[0]
  } else
    return null
}
