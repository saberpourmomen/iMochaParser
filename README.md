# Imocha Parser

### Overview
this application is a batch example to parse a zip file that contains A telephone network system fixed-width text file with details of a number of
phone calls including the calling charges.

### Requirements

Develop a component to parse the uploaded files with the format described above and
to produce a report for each file. The report contains the following information:
* Total cost of calls for each account number in the file
* Total cost of calls for each call type in the file
* Total cost of calls for each day in the file (the day the call started)

### Report files Structure
The naming convention of the ASCII file is `NWID.DATE.TXT` where:
*   NWID is a three character string identifying the network system, for example,
    *   012, 016, 019
* DATE is the date of the test in format `YYYYMMDD` (you can assume only one file
per day).

### Record Structure
The format of the file is given in the table
below:

| FIELD | WIDTH | DESCRIPTION  |
|-------|:-----:| -----:|
| 1     |  10   |    ACCNUM-account number of customer (10 characters in length)|
| 2     |  15   |    A_NUM-phone number making the call (10-15 characters in length)|
| 3     |  15   |    B_NUM-phone number which was dialled (10-15 characters in length)|
| 4     |  14   |    STT TIME-timestamp when call was started in format "YYYYMMDDHHMISS"|
| 5     |  14   |    END TIME-timestamp when call was ended in format "YYYYMMDDHHMISS"|
| 6     |   1   |    CALL TYPE - "P" phone call, "S" = SMS, "M" = multimedia message|
| 7     |   5   |    CALL COST - integer value for cost of call in sen, e.g. 1, 2, 10|

Values are right-padded with space. An example of the input file is as follows:

    46001122 60123008888 60163005555 2020100113503020201001135500P15
    46002255 60126001234 60162006789 2020100113553020201001142000P75

### How To Run
in order to run this application 

* pull and run postgres in desktop docker
  
    `docker pull postgres:18`

    `docker run --name some-postgres -e POSTGRES_PASSWORD=***** -p 5432:5432 -d postgres:18` 
    you can use your own password
* build project using `mvn clean install`
* to run the application you can use:
  * PARSER_PORT: port of application
  * DB_HOST: database host
  * DB_PORT: port of database
  * DB_NAME:name of database
  * DB_USER: username for database
  * DB_PASSWORD: password of database

**NOTE:** if you faced batch table creation issue use spring link to create it:
  * [spring framework git address to create batch related tables](https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/schema-postgresql.sql)
### How To test
for testing application you can use:
* postman collection jason from the path:
  * `src/main/resources/resources/imocha-parser.postman_collection.json`
* report example zip file path:
  * `src/main/resources/resources/012.20260130.zip` 
