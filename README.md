# Georgi-Iliev-employees

Spring Boot application for aggregating input data provided by a CSV file and display the result with Thymeleaf and Bootstrap in a DataGrid.

### Start the project:
 - build the project using Maven - `mvn clean install`
 - run the jar file - `java -jar target/commonProjects-0.0.1-SNAPSHOT.jar`

OR

 - execute the command `mvn spring-boot:run`

### Endpoints
 - GET `/` - upload the input file and date format pattern
 - POST `/datagrid` - DataGrid of the aggregated data

### Input parameters

 - CSV file with the following data on each row EmpID, ProjectID, DateFrom, DateTo (DateTo can be NULL, equivalent to today)

 Example:
 
143,12,2013-11-01,2013-11-05<br/>
218,10,2012-05-16,NULL<br/> 
14,12,2013-11-06,2013-11-07<br/>
4,12,2013-10-06,2013-11-07

 - Date Format Pattern could be any format provided in the DateTimeFormatter class. This format will be used to parse the dates in the CSV file. 

