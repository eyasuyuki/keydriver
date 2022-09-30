Config
====

```
src/main/resources/config_<locale>.properties
```

# Directory settings

| key | value            | description |
----- |------------------|--------------
| output_directory | output file path | ex. ```output_directory=.``` |
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
