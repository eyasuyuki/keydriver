Config
====

```
src/main/resources/config_<locale>.properties
```

# Directory settings

| key| value            | description                             |
----- |------------------|-----------------------------------------
| keydriver.log.directory | log file path | ex. ```keydriver.log.directory=./log``` |   

# Keyword aliases

```
keydriver.<keyword>=<alias>
```

Alias uses for column name.

example

```
keydriver.number=No
keydriver.keyword=Keyword
keydriver.target=Target
keydriver.argment=Argment
keydriver.comment=Comment
keydriver.objec=Object
keydriver.option=Option
```

# Browser settings

| key| value                                                                | description                                               |
----|----------------------------------------------------------------------|-----------------------------------------------------------
| keydriver.browser | ```chrome```<br>```edge```<br>```firefox```<br>```safari```<br>```ie``` | WebDriver executable name                                 |
| keydriver.browser.wait | integer value                                                        | Browser wait seconds                                      |
| keydriver.auto.capture | boolean vale                                                         | ```true```: always capture<br>```false```: manual capture |
| keydriver.browser.quit | boolean vale                                                         | ```true```: quit WebDriver<br>```false```: does not quit WebDriver |
| keydriver.edge.executable | full path of Edge executable | ex.```C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe``` |
| keydriver.ie.driver.path | full path of IEDriver.exe | ex.```C:\\IEDriverServer_x64_4.3.0\\IEDriverServer.exe``` |

```ie``` is Edge's IE mode.

# Param settings

Settings for [Param](Param.md) parenthesis.

| key| value                            | description                               |
------|----------------------------------|-------------------------------------------
| keydriver.value.head | plain string                     | ex. ```value_head=[```                    |
| keydriver.value.tail | plain string                     | ex. ```value_tail=]```                    |
| keydriver.attribute.separator | sepalator of value and attribute | ex. ```keydriver.attribute.separator=#``` |

# Reader settings

| key             | value | description                                     |
------------------|-------|-------------------------------------------------
| keydriver.skip.sheets | integer value | To skip Excel sheet count from Excel workbook   |
| keydriver.skip.headers | integer value | To skip sheet row count from top of excel sheet | 

# JDBC driver settings

Dynamic loading JDBC driver settings

| key             | value | description                                                                            |
------------------|-------|----------------------------------------------------------------------------------------
| keydriver.jdbc.driver.path | path string | ex.```keydriver.jdbc.driver.path=/home/yasuyuki/lib/mysql-connector-java-8.0.29.jar``` |
| keydriver.jdbc.class.name  | class name | ex.```keydriver.jdbc.class.name=com.mysql.jdbc.Driver```                               | 

# Output settings

| key| value            | description                             |
----- |------------------|-----------------------------------------
| keydriver.output.directory | output file path | ex. ```keydriver.output.directory=.```  |
| keydriver.log.directory | log file path | ex. ```keydriver.log.directory=./log``` |   
| keydriver.output.extension | output file extension | ex ```xlsx```                           |
| keydriver.output_prefix | output file prefix | ex. ```Test_Result_```                  |
| keydriver.summary.sheet.name | summary sheet name | ex. ```Summary```                       |
| keydriver.error.sheet.prefix | error sheet prefix | ex. ```[Error]_```                      |

## Test result label settings

| key| value                         | description |
----- |-------------------------------|--------------
| keydriver.timestamp.format | Excel timestamp format        | ex. ```yyyy-mm-dd hh:mm:ss.000``` |
| keydriver.test.file.name.label | Test file name label | ex. ```Test file name``` |
| keydriver.expecting.test.count.label | Expecting testCase count label    | ex. ```Expecting testCase count``` |
| keydriver.expecting.failure.count.label | Expecting failure count label | ex. ```Expecting failure count``` |
| keydriver.executed.test.count.label | Executed testCase count label     | ex. ```Executed testCase count``` |
| keydriver.success.test.count.label | Success testCase count label      | ex. ```Success testCase count``` |
| keydriver.failed.test.count.label | Failed testCase count label       | ex. ```Failed testCase count``` |
| keydriver.uncompleted.test.count.label | Uncompleted testCase count label  | ex. ```Uncompleted testCase count``` |
| keydriver.start.time.label | Start time label              | ex. ```Start time``` |
| keydriver.duration.label | Duration label                | ex. ```Duration``` |


## Test environment label settings

| key| value                   | description |
----- |-------------------------|--------------
| keydriver.arch.label | Architecture label      | Architecture |
| keydriver.processor.count.label | Processor count label   | Processor count |
| keydriver.load.average.label | Load average label      | Load average |
| keydriver.max.memory.label | Max memory label        | Max memory |
| keydriver.free.memory.label | Free memory label       | Free memory |
| keydriver.total.memory.label | Total memory label      | Total memory |
| keydriver.usable.disk.label | Usable disk space label | Usable disk space |
| keydriver.free.disk.label | Free disk space label   | Free disk space |
| keydriver.total.disk.label | Total disk space label  | Total disk space |