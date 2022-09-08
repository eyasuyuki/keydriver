Config
====

```
src/main/resources/config_<locale>.properties
```

# Directory settings

| key | value            | description |
----- |------------------|--------------
| outputDirectory | output file path | ex. ```outputDirectory=.``` |
| logDirectory | log file path | ex. ```logDirectory=./log``` |   

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

|key| value                                                    | description                                               |
----|----------------------------------------------------------|-----------------------------------------------------------
| browser | ```chrome```<br>```edge```<br>```firefox```<br>```safari``` | WebDriver executable name                                 |
| browser_wait | integer value | Browser wait seconds                                      |
| auto_capture | boolean vale | ```true```: always capture<br>```false```: manual capture |

# Param settings

Settings for [Param](Param.md) parenthesis.

| key | value | description |
------|-------|--------------
| valueHead | regexp string | ex. ```valueHead=\\[``` |
| valueTail | plain string | ex. ```valueTail=]``` |

# JDBC driver settings

Dynamic loading JDBC driver settings

| key              | value | description                                                                  |
------------------|-------|------------------------------------------------------------------------------
| jdbc_driver_path | path string | ex.```jdbc_driver_path=/home/yasuyuki/lib/mysql-connector-java-8.0.29.jar``` |
| jdbc_class_name  | class name | ex.```jdbc_class_name=com.mysql.jdbc.Driver``` | 
