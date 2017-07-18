
grabTables().each {
	log("grabTables() : $it")
}

grabTablesWith('emp').each {
	log("grabTablesWith() : $it")
}

grabTablesWithout('emp').each {
	log("grabTablesWithout() : $it")
}

grabTables().each { table ->
	log("${table}  grabCloumns")
	grabColumns(table).each {
		log("  col $it")
	}
}

grabTables().each { table ->
	log("${table}  grabColumnsWithout")
	grabColumnsWithout(table,'date').each {
		log("  col $it")
	}
}


log("indexes")
grabTables().each { table ->
	log("${table} grabIndexesWith")
	grabIndexesWith(table,'pk').each {
		log("  idx $it")
	}
}


log("views")
grabViews().each { view ->
	log("${view}")
}
