Instructions to use
========================
* Download the datasets and extract the data into the project Data directory in src/main/resources path.
* Don't put the dtds folder in Data directory as that is only the Document type definition of all the datasets. If you put then that
dtds will get parsed and indexed which should not be done.
* Extract the topics in the Query directory in src/main/resources path.
* Once above steps are done, build the project using maven as it is a maven project.
* Run it using main.
* Parameters for the main should be - 
```
-index src/main/resources/Index -docs src/main/resources/Data -queries src/main/resources/Query -numdocs 30 
```
* Parameter index is the directory path of index.
* Parameter docs is the directory path of datasets.
* Parameter query is the directory path of topics.
* Parameter numdocs is the number of documents one wants to retrieve when a query is fired.
* Output will be generated in output file in Query folder