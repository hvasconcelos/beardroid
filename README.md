## Bear Droid Library
Some usefull code to create Amazing Android Apps. 
The project lacks a lot of documentation.Please contribute to the project and share your thoughts about it. 
Our aim is create a bunch of boilerplate code for Android Application development.

Main Classes:

* GoogleAnaliticsProvider
* InstallTracker
* FileUtil
* ContactsUtil
* ImageCacheManager
* ImageLoader
* AssetDbHelper
* BearActionbar
* WebLinkView

Soon I will create a bunch of tutorials to use BearDroid in your applications.

## Prerequisites

maven-android-sdk-deployer https://github.com/mosabua/maven-android-sdk-deployer

google-analytics - jar installed in your maven local repo

roboguice 1.1 - jar installed in you  maven local repo

## How to use it?
First you need to install the library in your maven local repositoty

    git clone git@github.com:hvasconcelos/beardroid.git
    cd beardroid
    mvn install
  
Add the library to your maven dependencies list 

    <dependency>
    <groupId>com.bearstouch</groupId>
    <artifactId>bsdroid</artifactId>
    <version>2.0.0</version>
    <type>apklib</type>
    </dependency>  



## About me

Hélder Vasconcelos

Email me: heldervasc@bearstouch.com

Follow me: @heldervasc

## Contributors

