# Fetcher
(Android Mobile App)

Lastest Update: INSTRUMENTED UNIT TESTS WORKING 5/10/18

FEATURES

Welcome to Fetcher, a mobile dog friendly app that will help you find the perfect spot for you and your dog.
My app will feature a drop down menu of local dog friendly spots. Upon selecting a spot you'll be able to
enjoy a wide variety of features including galleries, reviews, and its overall rating!!! 

SETUP

Step 1: Download Android Studio - https://developer.android.com/studio/

Step 2: Clone project from GitHub

Step 3: Run Project (From android device plugged in/or from the emulator)

(Known Bugs) Step's 4 - 5

Step 4: Go here and configure a google console API for your project.

     4b: It'll ask for your SHA1 token from your project 
     
     4c: Find your SHA1 token from the gradle tab on the right side of Android Studio
     
         (Gradle(sidepanel) > Project(root) > Taks > android > signingReport
         
     4d: Make sure your manifest has the following activity
     
         <activity 
         
         android:name="com.google.android.gms.auth.api.signin.internal.SignInHubActivity"
         
         android:screenOrientation="portrait"
         
         android:windowSoftInputMode="stateAlwaysHidden|adjustPan"/>
         
     4e: Add the library/dependecy
         implementation 'com.google.android.gms:play-services-auth:15.0.1”
         
Step 5: If you're using an Emulator and its still not working

     5b: Go into the emulator's side panel and select the three dots like (. . . )
     5c: Go to the Google Play Store and run an update (Will have you sign into Google)
     
Step 6: UnitTests need the following libraries/dependecies

     6b: androidTestImplementation 'com.android.support.test:rules:1.0.2'
         androidTestImplementation 'com.android.support.test:runner:1.0.2'
         androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
     6c: INSTRUMENTED UNIT TESTS WORKING







Hey! Here are some screen shots of my app <3 

<img height="500" src="https://user-images.githubusercontent.com/31251244/39865637-119f6480-5413-11e8-8363-dd0235c2a493.png">

<img height="500" src="https://user-images.githubusercontent.com/31251244/39870642-b830c6fe-5427-11e8-9867-91a6e5eb83b5.png">

<img height="500" src="https://user-images.githubusercontent.com/31251244/39903004-7e5edd48-5496-11e8-93c4-37b81c018049.png">

<img height="500" src="https://user-images.githubusercontent.com/31251244/39903016-8c3b4e24-5496-11e8-898d-111188c59a93.png">

<img height="500" src="https://user-images.githubusercontent.com/31251244/39903017-8f06d178-5496-11e8-89ab-c204698b2e7f.png">

