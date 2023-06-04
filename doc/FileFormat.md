File Format
====

# Excel sheets

Excel sheet name to be uses:

1. Test sction name
2. Screen shot file name.

ex. ```Sheet1_001.png```

# Columns

| name    | default column name | date type                                          | description                                  |
---------|---------------------|----------------------------------------------------|----------------------------------------------
| number  | No                 | Integer                                            | Integer numeric of testCase number               |
| keyword | Keyword             | Keyword                                            | Test keywords                                |
|target| Target              | String or Param                                    | Human readable testCase target                   |
|argument| Argument            | String or Param                                    | Test argument                                |
|comment|Comment| String                                             | Comment text                                 |
|object|Object| Param                                              | HTML Element selector or something           |
|option|Option| Param | Additional argument. ex.JDBC connection url. |

## Keyword

see [Keyword](Keyword.md)

## Param

see [Param](Param.md)

