Keydriver
====

Keyword driven "like" testing tool.

# Applicable JDK version

JDK 11

# Sequence diagram

```mermaid
sequenceDiagram
    participant Keydriver
    participant POI
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
    Keydriver ->> POI: Output test report
    POI ->> Excel: Output test report
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

# Jacoco coverage report

```shell
mvn jacoco:prepare-agent test jacoco:report
```

# Use maven local repository

```shell
mvn <command> -Dmaven.repo.local=./lib
```

# ToDo

- Add JavaDoc comment
- More unit tests
