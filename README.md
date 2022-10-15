Keydriver
====

Keyword driven "like" testing tool.

# Applicable JDK version

JDK 11 and below.

Because Flee Spire.XLS 5.1.0 does not work JDK 16 or above.

# Sequence diagram

```mermaid
sequenceDiagram
    participant Keydriver
    participant POI
    participant Spire.XLS
    participant Excel
    participant WebDriver
    participant Browser
    Keydriver ->> POI: read Excel file
    POI ->> Excel: read
    Excel ->> POI: worksheet data
    POI ->> Keydriver: worksheet data
    Keydriver ->> WebDriver: manipurate browser
    WebDriver ->> Browser: manipurate browser
    Browser ->> WebDriver: browse result
    WebDriver ->> Keydriver: browse result
    Keydriver ->> Keydriver: assert result
    Keydriver ->> Spire.XLS: Output test report
    Spire.XLS ->> Excel: Output test report
```

# How to use

1. Install WebDriver executable
2. Build executable jar
3. Write ```.xlsx``` file for test directives.
4. Run executable jar file

## Instell WebDriver executable

ex.
```
brew install --cask chromedriver
```

## Build executable jar

```
mvn package
```

## Write ```.xlsx``` file for test directives.

see [FileFormat](doc/FileFormat.md)

## Run Keydriver jar file

```
java -jar keydriver-<version>.jar <test_directive>.xlsx
```

# Configuration

see [Config](doc/Config.md)

# ToDo

- Add JavaDoc comment
- More unit tests
- Dynamic loading ```config.properties```
