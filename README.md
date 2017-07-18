# README #

### What is this repository for? ###

This script is to assist the user in writing scripts for Oracle SQL Developer Data Modeler.

 * Version 0.1

### How do I get set up? ###

To use this toolset you will need the following on your machine.

 * Oracle SQL Developer or Data Modeler with groovy-all.jar in the Java extension directory


The contents of this script must be included in the script that is run from within Data Modeler.

Available methods:

_Boolean_ log (String message)
example: log("this is a message")

_Boolean_ show (String message)
example: show ("this will show in an alert dialog")

_String_ ask (String message)
example: ask ("this will show in an input dialog and return user input")

_List of Objects_ grab [ Tables, Entities, Views ] ()
example: grabTables()

_Object_ grab [ Table, Entity, View ] With (String matcher)
example: grabTableWith('test')

_List of Objects_ grab [ Tables, Entities, Views ] [With, Without] (String matcher)
example: grabTablesWithout('test')

_List of Elements_ grab [ Columns, Indexes] (Table table)
example: grabColumns(emp_table)

_Element_ grab [ Column, Index] With (Table table, String matcher)
example: grabColumnWith(emp_table, 'first_name')

_List of Elements_ grab [ Columns, Indexes] [With, Without] (Table table, String matcher)
example: grabColumnsWith(emp_table, 'name')


### Who do I talk to? ###

Contacts: Dave Schleis
