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

Settings for Param parenthesis.

| key | value | description |
------|-------|--------------
| valueHead | regexp string | ex. ```valueHead=\\[``` |
| valueTail | plain string | ex. ```valueTail=]``` |