MaterialTabs
============

Custom Tabs with Material Design animations for pre-Lollipop devices

[Download example apk with Text](https://raw.github.com/neokree/MaterialTabs/master/Sample-MaterialTab/bin/Sample-MaterialTab.apk)<br>
[Download example apk with Icon](https://raw.github.com/neokree/MaterialTabs/master/Sample-MaterialTabWithIcons/bin/Sample-MaterialTab.apk)

It requires 14+ API and android support v7 (Toolbar)

Dependency: [Android-UI](https://github.com/markushi/android-ui) Reveal Color View

<h3>How to use:</h3>
define it in xml layout
```xml
<!-- for Text Tabs -->
<it.neokree.materialtabs.MaterialTabHost
        android:id="@+id/materialTabHost"
        android:layout_width="match_parent"
        android:layout_height="48dp" >
<!-- for icon tabs --> 
<it.neokree.materialtabs.MaterialTabHost
        android:id="@+id/materialTabHost"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        app:hasIcons="true" >
```
<em>( I'm working on use wrap_content instead 48dp)</em>

change your base theme style to AppCompat ( res/values/styles.xml ) and add Lollipop default variables.
```xml
<!-- Base application theme. -->
<style name="AppTheme" parent="Theme.AppCompat.Light">
<!-- Customize your theme here. -->
<item name="colorPrimary">@color/light_blue_500</item>
<item name="colorPrimaryDark">@color/light_blue_800</item>
<item name="colorAccent">@color/grey_1000</item>
<item name="windowActionBar">false</item>
</style>
```

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


<h3>How to import</h3>
<h6>Eclipse </h6>
Import Android UI Library into eclipse <br>
Import Library-MaterialTabs into eclipse and add Android UI as dependancy. <br>
Now right click on your project -> Properties -> Android -> Add (under library).<br>
Click Apply.
<h6>Android Studio</h6>
Add [Android-UI module](https://github.com/markushi/android-ui) to your project.<br>
Download and add MaterialTabsModule to your project as module.<br>
Add this to your build.gradle:
```java 
dependencies {
    compile project(':MaterialTabsModule')
}
```


<img src="https://raw.github.com/neokree/MaterialTabs/master/screen.png" alt="screenshot" width="300px" height="auto" />
<img src="https://raw.github.com/neokree/MaterialTabs/master/screen-icon.png" alt="screenshot" width="300px" height="auto" />

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MaterialTabs-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1105)

