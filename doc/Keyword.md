Keyword
====

| keyword    | target                | argument                         | object            | option         | description                                                                                                               |
|------------|-----------------------|----------------------------------|-------------------|----------------|---------------------------------------------------------------------------------------------------------------------------|
| open       | url Param or Any text | null                             | url Param or null | null           | open object value url or argument value url                                                                               |
| clkc       | Any text              | null                             | Param             | null           | click the element who finded by object content                                                                            |
| input      | Any text              | Input text                       | Param             | null           | input text to the element who finded by object content                                                                    |
| clear      | Any text              | null                             | Param             | null           | Clear text to the element who finded by object content                                                                    |
| select     | Any text              | Any text                         | Param             | Param          | Select a select element who finded by object to option value                                                              |
| accept     | Any text              | null                             | Param             | null           | Accept an alert dialog                                                                                                    |
| dismiss    | Any text              | null                             | Param             | null           | Dismiss an alert dialog                                                                                                   |
| capture    | null                  | null                             | Param             | null           | "fullscreen"=Full Screen, rect[x:y:w:h]=Rectangle, default=window. Save a screen shot file. ex:```<sheet_name>_001.png``` |
| upload     | Any text              | url or full-path filename string | Param             | null           | Upload filename to type of file input element                                                                             |
| assert     | Any text              | Param                            | Param             | Param          | Assert object value compare to argument matcher                                                                           |
| execute    | Any text              | null                             | sql Param         | jdbc url Param | Execute object sql by option connection                                                                                   |
| switch     | Any text              | Any Text                         | Any text          | Any text       | Switch to child window. If child window closed, switch to parent window                                                   |
| _DIRECTIVE | Any text              | null                             | Param             | null           | Execute other sheet (default)                                                                                             |

see [Param](Param.md)
