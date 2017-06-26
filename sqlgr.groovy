/**********************************************************
SqLgr
Helper methods to make SQL Developer Data Modeler scripting easier

Dave Schleis
2017-06-17
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
*/
def grabTablesWith(String includeMe='') {
  grabThings(model,'TABLE', includeMe)
}

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
def grabColumns(oracle.dbtools.crest.model.design.relational.Table table) {
  grabColumnsWith(table)
}

def grabColumnsWith(oracle.dbtools.crest.model.design.relational.Table table, String includeMe='') {
  grabThings(table,'COLUMN', includeMe)
}

def grabColumnsWithout(oracle.dbtools.crest.model.design.relational.Table table, String excludeMe='') {
  grabThings(table,'COLUMN', excludeMe, true)
}

def grabColumnWith(oracle.dbtools.crest.model.design.relational.Table table, String includeMe='') {
  def retVal = grabSingleThing(table,'COLUMN', includeMe)
}


def grabEntitiesWith(String includeMe='') {
  grabThings(model,'ENTITY', includeMe)
}

def grabEntities() {
  grabThings(model,'ENTITY')
}

def grabEntitiesWithout(String excludeMe='') {
  grabThings(model,'ENTITY', excludeMe, true)
}

def grabViewsWith(String includeMe='') {
  grabThings(model,'VIEW', includeMe)
}

def grabViews() {
  grabThings(model,'VIEW')
}

def grabViewsWithout(String excludeMe='') {
  grabThings(model,'VIEW', excludeMe, true)
}
def grabAttributes(entity) {
  grabAttributesWith(entity)
}

def grabAttributesWith(entity, String includeMe='') {
  grabThings(entity, 'ATTRIBUTE', includeMe)
}

def grabAttributesWithout(entity, String excludeMe='') {
  grabThings(entity, 'ATTRIBUTE', excludeMe, true)
}

def grabIndexes(table) {
	grabIndexesWith(table)
}

def grabIndexesWith(table, String includeMe='') {
  grabThings(table,'INDEX', includeMe)
}


def grabIndexesWithout(table, String excludeMe='') {
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
  */
  retVal = false
  needleList.each { needle ->
    retVal = retVal || haystack?.toUpperCase().contains (needle?.trim().toUpperCase())
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

def grabThings(grabFrom, String whatToGrab, String includeMe='', Boolean exclude=false) {
  def retVal = []
  def withList = []
  def thingList = grabTheThings(grabFrom, whatToGrab)

  if (includeMe) {
    if (includeMe.contains(',')) {
       // break the comma seperated string into a list
       withList = includeMe.split(',')
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
     } else { // a single value
       thingList.each { thing ->
         if (exclude) {
           if (!thing.name?.toUpperCase().contains(includeMe?.toUpperCase())) {
             retVal << thing
           }
         } else {
           if (thing.name?.toUpperCase().contains(includeMe?.toUpperCase())) {
             retVal << thing
           }
         }
       }
     }
   } else {  // if (includeMe)
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
