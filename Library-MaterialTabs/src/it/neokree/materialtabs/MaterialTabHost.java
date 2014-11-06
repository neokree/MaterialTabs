package it.neokree.materialtabs;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;


/**
 * A Toolbar that contains multiple tabs
 * @author neokree
 *
 */
@SuppressLint("InflateParams")
public class MaterialTabHost extends android.support.v7.widget.Toolbar {
	
	private int primaryColor;
	private int accentColor;
	private int textColor;
	private List<MaterialTab> tabs;
	private boolean hasIcons;
	
	public MaterialTabHost(Context context) {
		this(context, null);
	}
	
	public MaterialTabHost(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MaterialTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		// remove padding left / right
		super.setContentInsetsRelative(0, 0); 
		
		// get primary and accent color from AppCompat theme
		Theme theme = context.getTheme();
		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
		primaryColor = typedValue.data;
		theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
		accentColor = typedValue.data;
		theme.resolveAttribute(R.attr.actionMenuTextColor, typedValue, true);
		textColor = typedValue.data;
		
		// get attributes
		if(attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MaterialTabHost, 0, 0);
			
			try {
				hasIcons = a.getBoolean(R.styleable.MaterialTabHost_hasIcons, false);
			} finally {
				a.recycle();
			}
		}
		else {
			hasIcons = false;
		}
		
		// initialize tabs list
		tabs = new LinkedList<MaterialTab>();
	}
	
	public void setPrimaryColor(int color) {
		this.primaryColor = color;
		
		for(MaterialTab tab : tabs) {
			tab.setPrimaryColor(color);
		}
	}
	
	public void setAccentColor(int color) {
		this.accentColor = color;
		
		for(MaterialTab tab : tabs) {
			tab.setAccentColor(color);
		}
	}
	
	public void setTextColor(int color) {
		this.textColor = color;
		
		for(MaterialTab tab : tabs) {
			tab.setTextColor(color);
		}
	}
	
	public void addTab(MaterialTab tab) {
		if(tabs.size() + 1 > 3) {
			throw new RuntimeException("Number of tab currently not supported");
		}
		else {
			// add properties to tab
			tab.setAccentColor(accentColor);
			tab.setPrimaryColor(primaryColor);
			tab.setTextColor(textColor);
			tab.setPosition(tabs.size());
			
			// insert new tab in list
			tabs.add(tab);
			
			// remove all views
			super.removeAllViews(); 
			
			// get the tab width
			int tabWidth = this.getScreenWidth() / tabs.size();
			
			// set params for resizing tabs width
			Toolbar.LayoutParams params = new Toolbar.LayoutParams(tabWidth,Toolbar.LayoutParams.MATCH_PARENT );
			for(MaterialTab t : tabs) {
				super.addView(t.getView(),params);
			}
		}
		
	}
	
	public MaterialTab newTab() {
		return new MaterialTab(this.getContext(),hasIcons);
	}
	
	public void setSelectedNavigationItem(int position) {
		if(position < 0 || position > tabs.size()) {
			throw new RuntimeException("Index overflow");
		} else {
			// tab at position will select, other will deselect
			for(int i = 0; i < tabs.size(); i++) {
				MaterialTab tab = tabs.get(i);
				
				if(i == position && !tab.isSelected()) {
					tab.activateTab();
				}
				else {
					tabs.get(i).disableTab();
				}
			}
		}
		
	}
	
	@Override
	public void removeAllViews() {
		for(int i = 0; i<tabs.size();i++) {
			tabs.remove(i);
		}
		super.removeAllViews();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private int getScreenWidth() {
		Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = 0;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Point p = new Point();
		    display.getSize(p);
		    width = p.x;
		}
		else {
			width = display.getWidth();
		}
	    
	    return width;
	}
}
