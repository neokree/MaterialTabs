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
<com.neokree.materialtabs.MaterialTabHost
        android:id="@+id/materialTabHost"
        android:layout_width="match_parent"
        android:layout_height="48dp" >
<!-- for icon tabs --> 
<com.neokree.materialtabs.MaterialTabHost
        android:id="@+id/materialTabHost"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        app:hasIcons="true" >
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

<h3>Grandle compatibility can not work</h3>
Actually I'm not developing through Android Studio. That it means I build for my first time the library with gradle. If you cannot import it with android studio please open a Issues.

<img src="https://raw.github.com/neokree/MaterialTabs/master/screen.png" alt="screenshot" width="300px" height="auto" />
<img src="https://raw.github.com/neokree/MaterialTabs/master/screen-icon.png" alt="screenshot" width="300px" height="auto" />



