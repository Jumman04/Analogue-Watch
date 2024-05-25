<p align="center">
   <img src="https://github.com/Jumman04/Analogue-Watch/assets/113237846/1efff827-143a-49b2-988c-4b059ac0d7fa" alt="App Icon" width="222">
   <br>
   <img src="https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat" alt="API Level 17 and above"/>
   <a href="https://jitpack.io/#Jumman04/Analogue-Watch">
   <img src="https://jitpack.io/v/Jumman04/Analogue-Watch.svg" alt="JitPack Version"/>
   </a>
   <a href="https://github.com/Jumman04/Analogue-Watch/network/members">
   <img src="https://img.shields.io/github/forks/Jumman04/Analogue-Watch" alt="GitHub Forks"/>
   </a>
   <a href="https://github.com/Jumman04/Analogue-Watch/stargazers">
   <img src="https://img.shields.io/github/stars/Jumman04/Analogue-Watch" alt="GitHub Stars"/>
   </a>
   <a href="https://github.com/Jumman04/Analogue-Watch/blob/master/LICENSE.md">
   <img src="https://img.shields.io/github/license/Jumman04/Analogue-Watch" alt="GitHub License"/>
   </a>
</p>


Analog Clock Library is a customizable Android library for displaying time in a traditional analog
format. It provides developers with a range of customization options to adjust various aspects such
as hour markers, minute markers, hour indicator text, and clock hands (hour, minute, second) to suit
their application's design.

---

## Features

- Display time in a traditional analog format
- Customize background color, marker colors, and hand colors
- Adjust heights and widths of minute markers, hour markers, and hands
- Enable/disable minute markers, hour markers, and hour indicator text
- Ticking sound effects with adjustable volume
- Customize text size, font family, text color, and text style for hour indicator text

---

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
    implementation 'com.github.Jumman04:Analogue-Watch:4.1'
}
```

---

## Usage in XML Layouts

```xml

<com.jummania.AnalogClock 
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

## Usage in Programmatic

```java
AnalogClock analogClock = new AnalogClock(this);
```

---

## Customization Attributes

Customize the appearance of the `AnalogClock` view by using the following XML attributes:

### Color Attributes

- `background_color`: Background color of the clock face
- `minute_marker_color`: Color of minute markers
- `hour_marker_color`: Color of hour markers
- `second_hand_color`: Color of the second hand
- `minute_hand_color`: Color of the minute hand
- `hour_hand_color`: Color of the hour hand
- `textColor`: Text color for hour indicator text

### Dimension Attributes

- `minute_marker_height`: Height of minute markers
- `hour_marker_height`: Height of hour markers
- `textSize`: Text size for hour indicator text

### Boolean Attributes

- `minute_marker`: Enable/disable display of minute markers
- `hour_marker`: Enable/disable display of hour markers
- `enable_sound`: Enable/disable ticking sound effects
- `enable_hour_text`: Enable/disable display of hour indicator text

### Text Attributes

- `fontFamily`: Font family for hour indicator text

---

## Example XML usage with attributes:

```xml

<com.jummania.AnalogClock 
    android:layout_width="match_parent" 
    android:layout_height="match_parent"
    app:background_color="@color/white" 
    app:minute_marker_color="@color/gray"
    app:hour_marker_color="@color/black" 
    app:second_hand_color="@color/red"
    app:minute_hand_color="@color/blue" 
    app:hour_hand_color="@color/green"
    app:textColor="@color/black"

    app:minute_marker_height="0.1" 
    app:hour_marker_height="0.15" 
    app:textSize="14sp"

    app:minute_marker="true" 
    app:hour_marker="true" 
    app:enable_sound="true"
    app:enable_hour_text="true"

    app:fontFamily="sans-serif" />

```

## Programmatic Customization

You can also customize the AnalogClock view programmatically:

```ktx
val analogClock = findViewById<AnalogClock> analogClock.setBackgroundColor(Color.WHITE)
analogClock.setMarkerColor(minuteMarkerColor = Color.GRAY, hourMarkerColor = Color.BLACK)
analogClock.setHandColor(secondHandColor = Color.RED, minuteHandColor = Color.BLUE, hourHandColor = Color.GREEN)

analogClock.setMarkerHeight(minuteMarkerHeight = 0.1f, hourMarkerHeight = 0.15f)

analogClock.setTextSize(14f)
analogClock.setTypeface(Typeface.SANS_SERIF)
analogClock.setTextColor(Color.BLACK)

analogClock.enableHourText(true)
analogClock.enableSound(true)
analogClock.enableMarkers(minuteMarker = true, hourMarker = true)

```

## Feature Requests

If you have a feature request or a suggestion for improving this library, please feel free
to [open an issue](https://github.com/Jumman04/Analogue-Watch/issues/new) and let us know! We
appreciate your feedback and are always looking to make our library better.

#### How to Request a Feature

1. Click on the [Issues tab](https://github.com/Jumman04/Analogue-Watch/issues).
2. Click the green "New Issue" button.
3. Fill in the requested information and submit the issue.

# Thank you for using and contributing to the improvement of our library!
