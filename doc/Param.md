Param
====

# Format

```
tag[value]
```

ex.

```
id[save_button]
name[
xpath[//title]
is[5]
```

# Tag

Tag is these types:

1. DataType
2. Matches

## DataType

| tag  | value               | example                                             |
------|---------------------|-----------------------------------------------------
| id   | HTML id attribute   | ```id[save_button]```                               |
| name | HTML name attribute | ```name[phone_number]```                            |
| xpath | HTML xpath value    | ```xpath[//title]```                                |
| url  | URL text            | ```url[http://www.javaopen.org]```                  |
| sql  | SQL text            | ```sql[SELECT name FROM employee WHERE id=94238]``` |
| text | Any text            | ```text[Foo Bar Buz]```                                |

## Matches

| tag  | value       | example                     |
------|-------------|-----------------------------
|is| Any text    | ```is[Excerent]```          |
|isNot| Any text    | ```isNot[Excerent]```       |
|isNull| null        | ```isNull[]```              |
|isNotNull| null        | ```isNotNull[]```           |
|greaterThan| Any integer | ```greaterThan[99]```       |
|greaterThanEqual| Any integer | ```greaterThanEqual[100]``` |
|lessThan| Any integer | ```lessThan[2]```           |
|lessThanEqual| Any integer | ```lessThanEqual[1]```         |
