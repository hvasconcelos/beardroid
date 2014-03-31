## Bear Droid Library
Some usefull code to create Amazing Android Apps. 
The project lacks a lot of documentation.Please contribute to the project and share your thoughts about it. 
Our aim is create a bunch of boilerplate code for Android Application development.

Main Classes:

* GoogleAnalyticsTracker - Track Events and Exceptions
* InstallTracker -Track Application Install and Update
* FileUtil -
* ContactsUtil - Some usefull functions to get the phone contacts
* ImageCacheManager - Cache Manager for your downloaded images
* ImageLoader - Load and crop a image from a URL
* AssetDbHelper - load your database from asset folder
* FontManager - Manage your loaded fonts
* WebLinkView - View that opens a webpage on click
* Logging - Automatically disabled logs on production versions

Soon I will create a bunch of tutorials to use BearDroid in your applications.

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

# Activity Injection

    @Layout(R.layout.home_activity)
    public class Home extends Activity {

	    @StringRes(R.string.application_name)
	    public String app_name_string;
    
	    @StringRes(R.string.app_version_name)
	    public String app_version_string;
	
	    @GoogleAnalytics("Global")
	    public GoogleAnaliticsTracker mga;
	
	    @Log(level=Logger.INFO,logTag="Activity")
	    public Logger mLog;
		
	    @ClickOn(R.id.vocabulario_ll)
	    public void onButtonClick(){
	      //...
	    }
	   @Override
       protected void onCreate(Bundle savedInstanceState)
       {	  
	      super.onCreate(savedInstanceState);
	      Injector.injectActiviy(this);
          mga.trackView("home");
          mLog.info("Atcitvity Called");
        }
    }

# To use GoogleAnalyticsTracker

Create the file res/values/analytics.xml with this content

    <?xml version="1.0" encoding="utf-8" ?>
    <resources>
      <!--Replace placeholder ID with your tracking ID-->
      <string name="ga_trackingId">UA-XXXX-Y</string>

      <!--Enable automatic activity tracking-->
      <bool name="ga_autoActivityTracking">true</bool>

      <!--Enable automatic exception tracking-->
      <bool name="ga_reportUncaughtExceptions">true</bool>
     </resources>
     
## About me

Hélder Vasconcelos

Email me: heldervasc@bearstouch.com

Follow me: @heldervasc

## Contributors

