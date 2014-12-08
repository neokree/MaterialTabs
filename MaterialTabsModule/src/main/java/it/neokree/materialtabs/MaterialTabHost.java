package it.neokree.materialtabs;

import java.util.LinkedList;
import java.util.List;

import it.neokree.materialtabs.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A Toolbar that contains multiple tabs
 * @author neokree
 *
 */
@SuppressLint("InflateParams")
public class MaterialTabHost extends HorizontalScrollView {
	
	private int primaryColor;
	private int accentColor;
	private int textColor;
	private int iconColor;
	private List<MaterialTab> tabs;
	private boolean hasIcons;
    private boolean isTablet;
    private float density;
    private boolean scrollable;

    private LinearLayout layout;
	
	public MaterialTabHost(Context context) {
		this(context, null);
	}
	
	public MaterialTabHost(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MaterialTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
        super.setOverScrollMode(this.OVER_SCROLL_NEVER);

        layout = new LinearLayout(context);
        this.addView(layout);
		
		// get attributes
		if(attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MaterialTabHost, 0, 0);
			
			try {
                // custom attributes
				hasIcons = a.getBoolean(R.styleable.MaterialTabHost_hasIcons, false);

                primaryColor = a.getColor(R.styleable.MaterialTabHost_primaryColor, Color.parseColor("#009688"));
                accentColor = a.getColor(R.styleable.MaterialTabHost_accentColor,Color.parseColor("#00b0ff"));
                iconColor = a.getColor(R.styleable.MaterialTabHost_iconColor,Color.WHITE);
                textColor = a.getColor(R.styleable.MaterialTabHost_textColor,Color.WHITE);
			} finally {
				a.recycle();
			}
		}
		else {
			hasIcons = false;
		}

        this.isInEditMode();
        density = this.getResources().getDisplayMetrics().density;
        scrollable = false;
        isTablet = this.getResources().getBoolean(R.bool.isTablet);

		// initialize tabs list
		tabs = new LinkedList<MaterialTab>();

        // set background color
        super.setBackgroundColor(primaryColor);
	}
	
	public void setPrimaryColor(int color) {
		this.primaryColor = color;

        this.setBackgroundColor(primaryColor);

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
	
	public void setIconColor(int color) {
		this.iconColor = color;
		
		for(MaterialTab tab : tabs) {
			tab.setIconColor(color);
		}
	}
	
	public void addTab(MaterialTab tab) {
        // add properties to tab
        tab.setAccentColor(accentColor);
        tab.setPrimaryColor(primaryColor);
        tab.setTextColor(textColor);
        tab.setIconColor(iconColor);
        tab.setPosition(tabs.size());

        // insert new tab in list
        tabs.add(tab);

        if(tabs.size() == 4) {
            // switch tabs to scrollable before its draw
            scrollable = true;

            if(isTablet)
                throw new RuntimeException("Tablet scrollable tabs are currently not supported");
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

            // move the tab if it is slidable
            if(scrollable) {
                int totalWidth = 0;//(int) ( 60 * density);
                for (int i = 0; i < position; i++) {
                    totalWidth += tabs.get(i).getView().getWidth();
                }
                this.smoothScrollTo(totalWidth, 0);
            }
		}
		
	}
	
	@Override
	public void removeAllViews() {
		for(int i = 0; i<tabs.size();i++) {
			tabs.remove(i);
		}
		layout.removeAllViews();
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(this.getWidth() != 0 && tabs.size() != 0)
            notifyDataSetChanged();
    }


    public void notifyDataSetChanged() {

        layout.removeAllViews();

        if(!scrollable) { // not scrollable tabs
            int tabWidth = this.getWidth() / tabs.size();

            // set params for resizing tabs width
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(tabWidth, LayoutParams.MATCH_PARENT);
            for (MaterialTab t : tabs) {
                layout.addView(t.getView(), params);
            }

        }
        else { //scrollable tabs

            for(int i = 0;i< tabs.size();i++) {
                LinearLayout.LayoutParams params;
                MaterialTab tab = tabs.get(i);

                int tabWidth = (int) (tab.getTabMinWidth() + (24 * density)); // 12dp + text/icon width + 12dp
                params = new LinearLayout.LayoutParams(tabWidth, LayoutParams.MATCH_PARENT);

                if(i == 0) {
                    // first tab
                    View view = new View(layout.getContext());
                    view.setMinimumWidth((int) (60 * density));
                    layout.addView(view);
                }

                params = new LinearLayout.LayoutParams(tabWidth, LayoutParams.MATCH_PARENT);
                layout.addView(tab.getView(),params);

                if(i == tabs.size() - 1) {
                    // last tab
                    View view = new View(layout.getContext());
                    view.setMinimumWidth((int) (60 * density));
                    layout.addView(view);
                }
            }

        }
        this.setSelectedNavigationItem(0);
    }
}
