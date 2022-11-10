Param
====

# Format

```
tag[value#attribute]
```

ex.

```
id[save_button]
id[save_button#enabled]
name[comment_input]
xpath[//title]
is[5]
```

## Default attribute

Default attribute is ```innerText```.

## Param parenthesis settings

see [Config](Config.md)

## Attribute and value separator settings

see [Config](Config.md)

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
| sheet | Excel worksheet name | ```sheet[Sheet1]``` |

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
