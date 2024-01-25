# JioMeet Video KYC Template UI Quickstart

**Welcome to Jiomeet Video KYC Template UI**, a SDK that streamlines the integration of Jiomeet's powerful audio and video functionalities into your Android application with minimal coding effort. With just a few simple steps, you can enable high-quality real-time communication, allowing users to effortlessly connect, collaborate, and communicate.

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Prerequisites](#prerequisites)
   - [Jetpack Compose](#jetpack-compose)
4. [Setup](#setup)
   - [Register on JioMeet Platform](#register-on-jiomeet-platform)
   - [Get Your Application Keys](#get-your-application-keys)
   - [Get Your JioMeet Meeting ID and PIN](#get-your-jiomeet-meeting-id-and-pin)
5. [Configure JioMeet Template UI Inside Your App](#configure-jiomeet-template-ui-inside-your-app)
   - [Add permissions for network and device access](#add-permissions-for-network-and-device-access)
   - [Start Your App](#start-your-app)
6. [Sample App](#sample-app)
7. [Troubleshooting](#troubleshooting)

## Introduction

In this documentation, we'll guide you through the process of installation, enabling you to enhance your Android app with Jiomeet's real-time communication capabilities swiftly and efficiently.Let's get started on your journey to creating seamless communication experiences with Jiomeet Video KYC Template UI!

---

## Prerequisites

Before you begin, ensure you have met the following requirements:

#### Jetpack Compose:

JioMeet Template UI relies on Jetpack Compose for its user interface components.
Ensure that your Android project is configured to use Jetpack Compose. You can add the necessary configurations to your project's build.gradle file:

```gradle
  // Enable Jetpack Compose
  buildFeatures {
      compose true
  }

  // Set the Kotlin compiler extension version for Compose
  composeOptions {
      kotlinCompilerExtensionVersion = "1.3.2"
  }
```

---

## Setup

##### Register on JioMeet Platform:

You need to first register on Jiomeet platform.[Click here to sign up](https://platform.jiomeet.com/login/signUp)

##### Get your application keys:

Create a new app. Please follow the steps provided in the [Documentation guide](https://dev.jiomeet.com/docs/quick-start/introduction) to create apps before you proceed.

###### Get you Jiomeet meeting id and pin

Use the [create meeting api](https://dev.jiomeet.com/docs/JioMeet%20Platform%20Server%20APIs/create-a-dynamic-meeting) to get your room id and password

## Configure JioMeet Template UI inside your app

i. **Step 1** : Generate a Personal Access Token for GitHub

- Settings -> Developer Settings -> Personal Access Tokens -> Generate new token
- Make sure you select the following scopes (“ read:packages”) and Generate a token
- After Generating make sure to copy your new personal access token. You cannot see it again! The only option is to generate a new key.

ii. Update build.gradle inside the application module

```kotlin
    repositories {
    maven {
        credentials {
            <!--github user name-->
                username = ""
            <!--github user token-->
                password = ""
        }
        url = uri("https://maven.pkg.github.com/JioMeet/JioMeetVideoKYCSDK_Android")
    }
    google()
    mavenCentral()
}
```

iii. In Gradle Scripts/build.gradle (Module: <projectname>) add the Template UI dependency. The dependencies section should look like the following:

```gradle
dependencies {
    ...
    implementation "com.jiomeet.platform:jiomeetkyctemplatesdk:<version>"
    ...
}
```

Find the [Latest version](https://github.com/JioMeet/JioMeetVideoKYCSDK_Android/releases) of the UI Kit and replace <version> with the one you want to use. For example: 2.1.8.

### Add permissions for network and device access.

In /app/Manifests/AndroidManifest.xml, add the following permissions after </application>:

```gradle
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- The SDK requires Bluetooth permissions in case users are using Bluetooth devices. -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<!-- For Android 12 and above devices, the following permission is also required. -->
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```

### Requesting run time permissions

it's crucial to request some permissions like **_CAMERA ,RECORD_AUDIO, READ_PHONE_STATE_** at runtime since these are critical device access permissins to ensure a seamless and secure user experience. Follow these steps

1. Check Permissions

```kotlin
if (checkPermissions()) {
    // Proceed with using the features.
} else {
    // Request critical permissions at runtime.
}
```

2. Request Runtime Permissions:

```kotlin
private void requestCriticalPermissions() {
    ActivityCompat.requestPermissions(this,
        new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        },
        PERMISSION_REQUEST_CODE);

}
```

3. Handle Permission Results

```kotlin
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == PERMISSION_REQUEST_CODE) {
        if (areAllPermissionsGranted(grantResults)) {
            // Proceed with using the features that require critical permissions.
        } else {
            // Handle denied permissions, especially for camera and phone state, which are essential.
        }
    }
}
```

### Start your App

Initialize modules by calling 

```kotlin
CoreApplication().recreateModules(this)
```

Set the environment to PROD

```kotlin
BaseUrl.initializedNetworkInformation(this, Constant.Environment.PROD)
```

Update onCreate to run LaunchKYCCore() when the app starts. The updated code should like the provided code sample:

```kotlin

 private val jmJoinMeetingConfig =
            JMJoinMeetingConfig(
                userRole = Speaker,
                isInitialAudioOn = true,
                isInitialVideoOn = true,
                isShareScreen = false,
                isShareWhiteBoard = false
            )

        private val jioMeetConnectionListener =
            object : JioMeetConnectionListener {
                override fun onLeaveMeeting() {
                    finish()
                }
            }

        val jmJoinMeetingData =
            JMJoinMeetingData(
                meetingId = "",
                meetingPin = "",
                displayName = "",
                version = "",
                deviceId = "deviceId"
            )
            
            // You can select any KYC option from the following that can be passed to LaunchKYCCore. 

        <!--enum class KycOptions(val value: String) {-->
        <!--    CALL("Call"),-->
        <!--    FACE_UI("Face UI"),-->
        <!--    CARD_UI("Card UI")-->
        <!--}-->

        LaunchKYCCore(
            jioMeetConnectionListener = jioMeetConnectionListener,
            jmJoinMeetingConfig = jmJoinMeetingConfig,
            jmJoinMeetingData = jmJoinMeetingData,
            kycOption = kycOption.value, // KYC value KycOptions.CALL or KycOptions.FACE_UI or KycOptions.CARD_UI
            switchCameraState = switchCameraClicked.value, // Camera click state TRUE/FALSE
            captureImageClick = captureImageClicked.value, // Capture image click state TRUE/FALSE
            onCaptureImage = { imagePath ->
                println("Captured Image Path $imagePath") // Captured image PATH
            },
            leaveMeeting = leaveMeetingClicked.value // Leave meeting click state TRUE/FALSE
        )
```

## Sample app

Visit our [Jiomeet KYC Template UI Sample app](https://github.com/JioMeet/JioMeetVideoKYCSDK_Android) repo to run the sample app.

---

## Troubleshooting

- Facing any issues while integrating or installing the JioMeet Template UI Kit please connect with us via real time support present in jiomeet.support@jio.com or https://jiomeetpro.jio.com/contact-us

---
