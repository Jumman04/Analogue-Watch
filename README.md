<p align="center">
  <img src="https://github.com/Jumman04/Analogue-Watch/assets/113237846/f4f180ac-7145-4d21-822d-e0b9bfc4113e" alt="App Icon" width="222">
</p>

  <h1 align="center">Analog Clock Library</h1>


Analog Clock Library is a customizable Android library for displaying time in a traditional analog format. It provides developers with a range of customization options to adjust various aspects such as hour markers, minute markers, hour indicator text, and clock hands (hour, minute, second) to suit their application's design.


## Features

- Display time in a traditional analog format
- Customize background color, marker colors, and hand colors
- Adjust heights and widths of minute markers, hour markers, and hands
- Enable/disable minute markers, hour markers, and hour indicator text
- Ticking sound effects with adjustable volume
- Customize text size, font family, text color, and text style for hour indicator text

## Usage

### Installation

Add JitPack repository to your root build.gradle file:
Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        // Other repositories...
        maven { url 'https://jitpack.io' }
    }
}
```
Step 2. Add the dependency
```groovy
	dependencies {
	        implementation 'com.github.Jumman04:Analogue-Watch:4.0'
	}
```

## Usage in XML Layouts
```xml
<com.jummania.AnalogClock
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
