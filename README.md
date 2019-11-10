# Android Academy Fundamentals Course 2019 - Entrance Test
The entrance test to Android Academy's Fundamentals Course 2019.

## Files Overview

### OrdersAnalyzer.kt
Has the function that needed implementing, and also has some helper code for reading the JSON file.
### Main.kt
Interface to test `OrdersAnalyzer`. It handles the read/write to the JSON files.
### input.json
An input file example.
### build.gradle
An example `build.gradle` file with the necessary dependencies and repositories.

## Dependencies
Inorder to read from a JSON file, this code uses a library called [Klaxon](https://github.com/cbeust/klaxon).
### Installing Klaxon
Assuming you are using gradle just add these to `build.gradle`:
```gradle
repositories {
    jcenter()
}

dependencies {
    implementation 'com.beust:klaxon:5.0.1'
}
```
