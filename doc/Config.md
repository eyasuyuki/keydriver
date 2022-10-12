Config
====

```
src/main/resources/config_<locale>.properties
```

# Directory settings

| key | value            | description |
----- |------------------|--------------
| log_directory | log file path | ex. ```log_directory=./log``` |   

# Keyword aliases

```
<keyword>=<alias>
```

Alias uses for column name.

example

```
number=No
keyword=Keyword
target=Target
argment=Argment
comment=Comment
objec=Object
option=Option
```

# Browser settings

|key| value                                                                | description                                               |
----|----------------------------------------------------------------------|-----------------------------------------------------------
| browser | ```chrome```<br>```edge```<br>```firefox```<br>```safari```<br>```ie``` | WebDriver executable name                                 |
| browser_wait | integer value                                                        | Browser wait seconds                                      |
| auto_capture | boolean vale                                                         | ```true```: always capture<br>```false```: manual capture |
| browser_quit | boolean vale                                                         | ```true```: quit WebDriver<br>```false```: does not quit WebDriver |
| edge_executable | full path of Edge executable | ex.```C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe``` |
| ie_driver_path | full path of IEDriver.exe | ex.```C:\\IEDriverServer_x64_4.3.0\\IEDriverServer.exe``` |

```ie``` is Edge's IE mode.

# Param settings

Settings for [Param](Param.md) parenthesis.

| key | value                            | description |
------|----------------------------------|--------------
| value_head | plain string                     | ex. ```value_head=[``` |
| value_tail | plain string                     | ex. ```value_tail=]``` |
| attribute_separator | sepalator of value and attribute | ex. ```attribute_separator=#``` |

# JDBC driver settings

Dynamic loading JDBC driver settings

| key              | value | description                                                                  |
------------------|-------|------------------------------------------------------------------------------
| jdbc_driver_path | path string | ex.```jdbc_driver_path=/home/yasuyuki/lib/mysql-connector-java-8.0.29.jar``` |
| jdbc_class_name  | class name | ex.```jdbc_class_name=com.mysql.jdbc.Driver``` | 

# Output settings

| key | value            | description |
----- |------------------|--------------
| output_directory | output file path | ex. ```output_directory=.``` |
| log_directory | log file path | ex. ```log_directory=./log``` |   
| output_extension | output file extension | ex ```xlsx``` |
| output_prefix | output file prefix | ex. ```Test_Result_``` |
| summary_sheet_name | summary sheet name | ex. ```Summary``` |
| error_sheet_prefix | error sheet prefix | ex. ```[Error]_``` |

## Test result label settings

| key | value                         | description |
----- |-------------------------------|--------------
| timestamp_format | Excel timestamp format        | ex. ```yyyy-mm-dd hh:mm:ss.000``` |
| test_file_name_label | Test file name label | ex. ```Test file name``` |
| expecting_test_count_label | Expecting test count label    | ex. ```Expecting test count``` |
| expecting_failure_count_label | Expecting failure count label | ex. ```Expecting failure count``` |
| executed_test_count_label | Executed test count label     | ex. ```Executed test count``` |
| success_test_count_label | Success test count label      | ex. ```Success test count``` |
| failed_test_count_label | Failed test count label       | ex. ```Failed test count``` |
| uncompleted_test_count_label | Uncompleted test count label  | ex. ```Uncompleted test count``` |
| start_time_label | Start time label              | ex. ```Start time``` |
| duration_label | Duration label                | ex. ```Duration``` |


## Test environment label settings

| key | value                   | description |
----- |-------------------------|--------------
arch_label | Architecture label      | Architecture
processor_count_label | Processor count label   | Processor count
load_average_label | Load average label      | Load average
max_memory_label | Max memory label        | Max memory
free_memory_label | Free memory label       | Free memory
total_memory_label | Total memory label      | Total memory
usable_disk_label | Usable disk space label | Usable disk space
free_disk_label | Free disk space label   | Free disk space
total_disk_label | Total disk space label  | Total disk space