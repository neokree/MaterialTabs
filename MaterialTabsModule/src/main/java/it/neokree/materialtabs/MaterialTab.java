package it.neokree.materialtabs;

import java.util.Locale;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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

    // Not Final to Edit Parrameters
    private int REVEAL_DURATION = 400;
    private int HIDE_DURATION = 500;
	
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
    private int rippleColor;

	private boolean active;
	private int position;
    private boolean hasIcon;
    private float density;
    private Point lastTouchedPoint;

	public MaterialTab(Context ctx,boolean hasIcon) {
        this.hasIcon = hasIcon;
        density = ctx.getResources().getDisplayMetrics().density;
		res = ctx.getResources();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if(!hasIcon) {
                completeView = LayoutInflater.from(ctx).inflate(R.layout.tab, null);

                text = (TextView) completeView.findViewById(R.id.text);
            }
            else {
                completeView = LayoutInflater.from(ctx).inflate(R.layout.tab_icon, null);

                icon = (ImageView) completeView.findViewById(R.id.icon);
            }

            selector = (ImageView) completeView.findViewById(R.id.selector);
        }
        else {
            if (!hasIcon) {
                // if there is no icon
                completeView = LayoutInflater.from(ctx).inflate(R.layout.material_tab, null);

                text = (TextView) completeView.findViewById(R.id.text);
            } else {
                // with icon
                completeView = LayoutInflater.from(ctx).inflate(R.layout.material_tab_icon, null);

                icon = (ImageView) completeView.findViewById(R.id.icon);
            }

            background = (RevealColorView) completeView.findViewById(R.id.reveal);
            selector = (ImageView) completeView.findViewById(R.id.selector);

        }
		// set the listener
		completeView.setOnTouchListener(this);

		active = false;
		textColor = Color.WHITE; // default white text 
		iconColor = Color.WHITE; // and icon
	}
	
	
	public void setAccentColor(int color) {
		this.accentColor = color;
        this.textColor = color;
        this.iconColor = color;
        this.rippleColor = Color.argb(0x80, Color.red(color), Color.green(color), Color.blue(color));
	}
	
	public void setPrimaryColor(int color) {
		this.primaryColor = color;

        if(deviceHaveRippleSupport()) {
            background.setBackgroundColor(color);
        }
        else {
            completeView.setBackgroundColor(color);
        }

	}
	
	public void setTextColor(int color) {
		textColor = color;
		if(text != null) {
			text.setTextColor(color);
		}
	}
	
	public void setIconColor(int color)
	{
	    this.iconColor = color;
	    if (this.icon != null)
	      this.icon.setColorFilter(color);
	}

	public MaterialTab setText(CharSequence text) {
        if(hasIcon)
            throw new RuntimeException("You had setted tabs with icons, uses icons instead text");

		this.text.setText(text.toString().toUpperCase(Locale.US));
        return this;
	}
	
	public MaterialTab setIcon(Drawable icon) {
        if(!hasIcon)
            throw new RuntimeException("You had setted tabs without icons, uses text instead icons");

		iconDrawable = icon;
		
		this.icon.setImageDrawable(icon);
		this.setIconColor(this.iconColor);
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


    public void setRippleDuration (int REVEAL_DURATION, int HIDE_DURATION) {
        this.REVEAL_DURATION = REVEAL_DURATION;
        this.HIDE_DURATION = HIDE_DURATION;
    }
    public void setRippleColor (int rippleColor, int rippleAlpha) {
        this.rippleColor = Color.argb(rippleAlpha, Color.red(rippleColor), Color.green(rippleColor), Color.blue(rippleColor))
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
        lastTouchedPoint = new Point();
		lastTouchedPoint.x = (int) event.getX();
        lastTouchedPoint.y = (int) event.getY();

        int rippleColor = this.rippleColor;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!deviceHaveRippleSupport()) {
                completeView.setBackgroundColor(rippleColor);
            }

            // do nothing
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_CANCEL) {
            if(!deviceHaveRippleSupport()) {
                completeView.setBackgroundColor(primaryColor);
            }
            return true;
        }

        // new effects
        if(event.getAction() == MotionEvent.ACTION_UP) {

            if(!deviceHaveRippleSupport()) {
                completeView.setBackgroundColor(primaryColor);
            }
            else {
                // set the backgroundcolor
                this.background.reveal(lastTouchedPoint.x, lastTouchedPoint.y, rippleColor, 0, REVEAL_DURATION, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        background.reveal(lastTouchedPoint.x, lastTouchedPoint.y, primaryColor, 0, HIDE_DURATION, null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }

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

    public MaterialTabListener getTabListener() {
        return listener;
    }


	public int getPosition() {
		return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}
	
	@SuppressLint({"NewApi"})
	private void setIconAlpha(int paramInt)
	{
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
	    {
	      this.icon.setImageAlpha(paramInt);
	      return;
	    }
	    this.icon.setColorFilter(Color.argb(paramInt, Color.red(this.iconColor), Color.green(this.iconColor), Color.blue(this.iconColor)));
	}

   private int getTextLenght() {
       String textString = text.getText().toString();
        Rect bounds = new Rect();
        Paint textPaint = text.getPaint();
        textPaint.getTextBounds(textString,0,textString.length(),bounds);
        return bounds.width();
   }

    private boolean deviceHaveRippleSupport() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return false;
        }
        else {
            return true;
        }

    }

    private int getIconWidth() {
        return (int) (density * 24);
    }

   public int getTabMinWidth() {
        if(hasIcon) {
            return getIconWidth();
        }
       else {
            return getTextLenght();
        }
   }

}
