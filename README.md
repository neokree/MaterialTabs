MaterialTabs
============

Custom Tabs with Material Design animations for pre-Lollipop devices<br>
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MaterialTabs-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1105)&ensp;&ensp;&ensp;&ensp;&ensp;[![Donate](https://www.paypalobjects.com/en_GB/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=TLLU42DEL36RY)


[Download example apk](https://raw.github.com/neokree/MaterialTabs/master/example.apk)

It requires 14+ API and android support v7 (Toolbar)

## IMPORTANT NOTICE
This library is no longer supported.<BR>
I have not enough time to continue developing at this time and the android design support library implements the tabs features in the better way, so I think it is useless now. If anyone wants to keep alive this library they can send pull requests AFTER testing the code and exmplaining what they have changed and why. Thanks to all contributors

Dependency: [Android-UI](https://github.com/markushi/android-ui) Reveal Color View <br>
If you are using MaterialTabs in your app and would like to be listed here, please let me know via [email](mailto:neokree@gmail.com)! <br>

<h3>How to use:</h3>
define it in xml layout with custom attributes
```xml
<!-- for Text Tabs -->
<it.neokree.materialtabs.MaterialTabHost
        android:id="@+id/materialTabHost"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        app:textColor="#FFFFFF"
        app:primaryColor="YOUR_PRIMARY_COLOR"
        app:accentColor="YOUR_ACCENT_COLOR" />
<!-- for icon tabs --> 
<it.neokree.materialtabs.MaterialTabHost
        android:id="@+id/materialTabHost"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        app:iconColor="#FFFFFF"
        app:primaryColor="YOUR_PRIMARY_COLOR"
        app:accentColor="YOUR_ACCENT_COLOR"
        app:hasIcons="true"/>
```
<em>( I'm working on use wrap_content instead 48dp)</em>

Connect to java code and add to viewPager
```java
MaterialTabHost tabHost;

@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
		pager = (ViewPager) this.findViewById(R.id.viewpager);
		
		// init view pager
		pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	// when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
		
		// insert all tabs from pagerAdapter data
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab() 
                            .setIcon(getIcon(i))
                            .setTabListener(this)
                            );
    }
}

@Override
	public void onTabSelected(MaterialTab tab) {
		// when the tab is clicked the pager swipe content to the tab position
		pager.setCurrentItem(tab.getPosition());
		
	}
```

N.B. Your activity must <code>extends ActionBarActivity implements MaterialTabListener</code>


### How to import
###### Android Studio
Add this to your build.gradle:
```java 
dependencies {
    compile 'it.neokree:MaterialTabs:0.11'
}
```

<h3>Limitations</h3>
Actually, this library have some limitations: 
- No selector animations

These problems are currently in development

### Fixed and Scrollable tabs. 
###### With text tabs
[1 - 3] Fixed Tabs <br>
[4 - &infin;] Scrollable Tabs
###### With icon tabs
[1 - 5] Fixed Tabs <br>
[6 - &infin;] Scrollable Tabs

<img src="https://raw.github.com/neokree/MaterialTabs/master/screen.jpg" alt="screenshot" width="300px" height="auto" />
<img src="https://raw.github.com/neokree/MaterialTabs/master/screen-icon.jpg" alt="screenshot" width="300px" height="auto" />
<img src="https://raw.github.com/neokree/MaterialTabs/master/screen-multitab.jpg" alt="screenshot" width="300px" height="auto" />
<img src="https://raw.github.com/neokree/MaterialTabs/master/screen-tablet.jpg" alt="screenshot" width="600px" height="auto" />
