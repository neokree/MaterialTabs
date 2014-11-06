package it.neokree.materialtabs;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import at.markushi.ui.RevealColorView;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
/**
 * A simple Tab with Material Design style
 * @author neokree
 *
 */ 
public class MaterialTab implements View.OnTouchListener {
	
	private View completeView;
	private ImageView icon;
	private TextView text;
	private RevealColorView background;
	private ImageView selector;
	
	private Resources res;
	private MaterialTabListener listener;
	private Drawable iconDrawable;
	
	private int textColor; 
	private int iconColor;
	private int primaryColor;
	private int accentColor;
	
	private boolean active;
	private int position;
	
	public MaterialTab(Context ctx,boolean hasIcon) {
		res = ctx.getResources();
		
		if(!hasIcon) {
			// if there is no icon
			completeView = LayoutInflater.from(ctx).inflate(R.layout.material_tab, null);
			
			text = (TextView) completeView.findViewById(R.id.text);
		}
		else {
			// with icon
			completeView = LayoutInflater.from(ctx).inflate(R.layout.material_tab_icon, null);
			
			icon = (ImageView) completeView.findViewById(R.id.icon);
		}
		
		background = (RevealColorView) completeView.findViewById(R.id.reveal);
		selector = (ImageView) completeView.findViewById(R.id.selector);
		
		// set the listener
		completeView.setOnTouchListener(this);
		
		active = false;
		textColor = Color.WHITE; // default white text
		iconColor = Color.WHITE;
	}
	
	
	public void setAccentColor(int color) {
		this.accentColor = color;
	}
	
	public void setPrimaryColor(int color) {
		this.primaryColor = color;
		background.setBackgroundColor(color);
	}
	
	public void setTextColor(int color) {
		textColor = color;
		if(text != null) {
			text.setTextColor(color);
		}
	}
	
	public void setIconColor(int color) {
		iconColor = color;
		if(icon != null) {
			icon.setImageBitmap(colorDrawable(iconDrawable,color));
		}
	}

	public MaterialTab setText(CharSequence text) {
		this.text.setText(text.toString().toUpperCase(Locale.US));
		return this;
	}
	
	public MaterialTab setIcon(Drawable icon) {
		iconDrawable = icon;
		return this;
	}
	
	public void disableTab() {
		// set 60% alpha to text color
		if(text != null)
			this.text.setTextColor(Color.argb(0x99 ,Color.red(textColor), Color.green(textColor), Color.blue(textColor)));
		// set 60% alpha to icon 
		if(icon != null)
			setIconAlpha(0x99);

		// set transparent the selector view
		this.selector.setBackgroundColor(res.getColor(android.R.color.transparent));
		
		active = false;
		
		if(listener != null)
			listener.onTabUnselected(this);
	}
	
	public void activateTab() {
		// set full color text
		if(text != null)
			this.text.setTextColor(textColor);
		// set 100% alpha to icon
		if(icon != null)
			setIconAlpha(0xFF);
			
		// set accent color to selector view
		this.selector.setBackgroundColor(accentColor);
		
		active = true;
	}
	
	public boolean isSelected() {
		return active;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Point point = new Point();
		point.x = (int) event.getX();
		point.y = (int) event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_DOWN ) {
			// reveal the tab
			this.background.reveal(point.x, point.y, Color.argb(0x80, Color.red(accentColor), Color.green(accentColor), Color.blue(accentColor)),0,250, null);
			
			return true;
		}
		
		if(event.getAction() == MotionEvent.ACTION_UP) {
			// hide reveal
			this.background.reveal(point.x, point.y, primaryColor,0,700, null);
			
			// set the click 
			if(listener != null) {
				
				if(active) {
					// if the tab is active when the user click on it it will be reselect
					listener.onTabReselected(this);
				}
				else {
					listener.onTabSelected(this);
				}
			}
			// if the tab is not activated, it will be active
			if(!active)
				this.activateTab();
			
			return true;
		}
		
		return false;
	}
	
	public View getView() {
		return completeView;
	}
	
	public MaterialTab setTabListener(MaterialTabListener listener) {
		this.listener = listener;
		return this;
	}


	public int getPosition() {
		return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}


	private Bitmap colorDrawable(Drawable iconDrawable, int color) {
		// TODO
		return null;
	}

	
	@SuppressLint("NewApi")
	private void setIconAlpha(int alpha) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			this.icon.setImageAlpha(alpha);
		else {
			this.icon.setImageBitmap(colorDrawable(iconDrawable,Color.argb(alpha, Color.red(iconColor), Color.green(iconColor), Color.blue(iconColor))));
		}
	}

	
}
