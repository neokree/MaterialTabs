package it.neokree.materialtabs;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import it.neokree.materialtabs.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A Toolbar that contains multiple tabs
 * @author neokree
 *
 */
@SuppressLint("InflateParams")
public class MaterialTabHost extends RelativeLayout implements View.OnClickListener {
	
	private int primaryColor;
	private int accentColor;
	private int textColor;
	private int iconColor;
	private List<MaterialTab> tabs;
	private boolean hasIcons;
    private boolean isTablet;
    private float density;
    private boolean scrollable;

    private HorizontalScrollView scrollView;
    private LinearLayout layout;
    private ImageButton left;
    private ImageButton right;

    private static int tabSelected;
	
	public MaterialTabHost(Context context) {
		this(context, null);
	}
	
	public MaterialTabHost(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MaterialTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

        scrollView = new HorizontalScrollView(context);
        scrollView.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_NEVER);
        scrollView.setHorizontalScrollBarEnabled(false);
        layout = new LinearLayout(context);
        scrollView.addView(layout);

		// get attributes
		if(attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MaterialTabHost, 0, 0);
			
			try {
                // custom attributes
				hasIcons = a.getBoolean(R.styleable.MaterialTabHost_hasIcons, false);

                primaryColor = a.getColor(R.styleable.MaterialTabHost_materialTabsPrimaryColor, Color.parseColor("#009688"));
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
        scrollable = false;
        isTablet = this.getResources().getBoolean(R.bool.isTablet);
        density = this.getResources().getDisplayMetrics().density;
        tabSelected = 0;

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

        if(tabs.size() == 4 && !hasIcons) {
            // switch tabs to scrollable before its draw
            scrollable = true;
        }

        if(tabs.size() == 6 && hasIcons) {
            scrollable = true;
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
				
				if(i == position) {
					tab.activateTab();
				}
				else {
					tabs.get(i).disableTab();
				}
			}

            // move the tab if it is slidable
            if(scrollable) {
                scrollTo(position);
            }

            tabSelected = position;
		}
		
	}

    private void scrollTo(int position) {
        int totalWidth = 0;//(int) ( 60 * density);
        for (int i = 0; i < position; i++) {
            int width = tabs.get(i).getView().getWidth();
            if(width == 0) {
                if(!isTablet)
                    width = (int) (tabs.get(i).getTabMinWidth() + (24 * density));
                else
                    width = (int) (tabs.get(i).getTabMinWidth() + (48 * density));
            }

            totalWidth += width;
        }
        scrollView.smoothScrollTo(totalWidth, 0);
    }
	
	@Override
	public void removeAllViews() {
		for(int i = 0; i<tabs.size();i++) {
			tabs.remove(i);
		}
		layout.removeAllViews();
        super.removeAllViews();
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(this.getWidth() != 0 && tabs.size() != 0)
            notifyDataSetChanged();
    }


    public void notifyDataSetChanged() {
        super.removeAllViews();
        layout.removeAllViews();


        if (!scrollable) { // not scrollable tabs
            int tabWidth = this.getWidth() / tabs.size();

            // set params for resizing tabs width
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(tabWidth, HorizontalScrollView.LayoutParams.MATCH_PARENT);
            for (MaterialTab t : tabs) {
                layout.addView(t.getView(), params);
            }

        } else { //scrollable tabs

            if(!isTablet) {
                for (int i = 0; i < tabs.size(); i++) {
                    LinearLayout.LayoutParams params;
                    MaterialTab tab = tabs.get(i);

                    int tabWidth = (int) (tab.getTabMinWidth() + (24 * density)); // 12dp + text/icon width + 12dp

                    if (i == 0) {
                        // first tab
                        View view = new View(layout.getContext());
                        view.setMinimumWidth((int) (60 * density));
                        layout.addView(view);
                    }

                    params = new LinearLayout.LayoutParams(tabWidth, HorizontalScrollView.LayoutParams.MATCH_PARENT);
                    layout.addView(tab.getView(), params);

                    if (i == tabs.size() - 1) {
                        // last tab
                        View view = new View(layout.getContext());
                        view.setMinimumWidth((int) (60 * density));
                        layout.addView(view);
                    }
                }
            }
            else {
                // is a tablet
                for (int i = 0; i < tabs.size(); i++) {
                    LinearLayout.LayoutParams params;
                    MaterialTab tab = tabs.get(i);

                    int tabWidth = (int) (tab.getTabMinWidth() + (48 * density)); // 24dp + text/icon width + 24dp

                    params = new LinearLayout.LayoutParams(tabWidth, HorizontalScrollView.LayoutParams.MATCH_PARENT);
                    layout.addView(tab.getView(), params);
                }
            }
        }

        if (isTablet && scrollable) {
            // if device is a tablet and have scrollable tabs add right and left arrows
            Resources res = getResources();

            left = new ImageButton(this.getContext());
            left.setId(R.id.left);
            left.setImageDrawable(res.getDrawable(R.drawable.left_arrow));
            left.setBackgroundColor(Color.TRANSPARENT);
            left.setOnClickListener(this);

            // set 56 dp width and 48 dp height
            RelativeLayout.LayoutParams paramsLeft = new LayoutParams((int)( 56 * density),(int) (48 * density));
            paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            this.addView(left,paramsLeft);

            right = new ImageButton(this.getContext());
            right.setId(R.id.right);
            right.setImageDrawable(res.getDrawable(R.drawable.right_arrow));
            right.setBackgroundColor(Color.TRANSPARENT);
            right.setOnClickListener(this);

            RelativeLayout.LayoutParams paramsRight = new LayoutParams((int)( 56 * density),(int) (48 * density));
            paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsRight.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            paramsRight.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            this.addView(right,paramsRight);

            RelativeLayout.LayoutParams paramsScroll = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            paramsScroll.addRule(RelativeLayout.LEFT_OF, R.id.right);
            paramsScroll.addRule(RelativeLayout.RIGHT_OF,R.id.left);
            this.addView(scrollView,paramsScroll);
        }
        else {
            // if is not a tablet add only scrollable content
            RelativeLayout.LayoutParams paramsScroll = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.addView(scrollView,paramsScroll);
        }

        this.setSelectedNavigationItem(tabSelected);
    }

    public MaterialTab getCurrentTab() {
        for(MaterialTab tab : tabs) {
            if (tab.isSelected())
                return tab;
        }

        return null;
    }

    @Override
    public void onClick(View v) { // on tablet left/right button clicked
        int currentPosition = this.getCurrentTab().getPosition();

        if (v.getId() == R.id.right && currentPosition < tabs.size() -1) {
            currentPosition++;

            // set next tab selected
            this.setSelectedNavigationItem(currentPosition);

            // change fragment
            tabs.get(currentPosition).getTabListener().onTabSelected(tabs.get(currentPosition));
            return;
        }

        if(v.getId() == R.id.left && currentPosition > 0) {
            currentPosition--;

            // set previous tab selected
            this.setSelectedNavigationItem(currentPosition);
            // change fragment
            tabs.get(currentPosition).getTabListener().onTabSelected(tabs.get(currentPosition));
            return;
        }

    }
}
