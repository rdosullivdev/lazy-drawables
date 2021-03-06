/*
 * Copyright 2011 Roberto Tyley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.madgag.android.lazydrawables;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

public class AsyncLoadDrawable extends Drawable implements Drawable.Callback {
	private Drawable mCurrDrawable;
	private final static String TAG="ALD";


	public void setDelegate(Drawable delegate) {
		this.mCurrDrawable = delegate;
		delegate.setBounds(getBounds());
		if (delegate instanceof Animatable) {
			delegate.setCallback(this);
			delegate.setVisible(true, true);
			((Animatable) delegate).start();
			Log.d("ALD", "Started the bugger");
		}
	}

	public void onLoad(Drawable delegate) {
		setDelegate(delegate);
		invalidateSelf();
	}
	
	public void draw(Canvas canvas) {
		Log.d(TAG, "Asked to draw "+mCurrDrawable+" "+canvas.getClipBounds());
		mCurrDrawable.draw(canvas);
	}
	
    @Override
    protected void onBoundsChange(Rect bounds) {
        if (mCurrDrawable != null) {
        	mCurrDrawable.setBounds(bounds);
        }
    }

	public void setChangingConfigurations(int configs) {
		mCurrDrawable.setChangingConfigurations(configs);
	}

	public int getChangingConfigurations() {
		return mCurrDrawable.getChangingConfigurations();
	}

	public void setDither(boolean dither) {
		mCurrDrawable.setDither(dither);
	}

	public void setFilterBitmap(boolean filter) {
		mCurrDrawable.setFilterBitmap(filter);
	}

	public void setAlpha(int alpha) {
		mCurrDrawable.setAlpha(alpha);
	}

	public void setColorFilter(ColorFilter cf) {
		mCurrDrawable.setColorFilter(cf);
	}

	public void setColorFilter(int color, Mode mode) {
		mCurrDrawable.setColorFilter(color, mode);
	}

	public void clearColorFilter() {
		mCurrDrawable.clearColorFilter();
	}

	public boolean isStateful() {
		return true;
	}
	
    @Override
    public int getIntrinsicWidth() {
        return mCurrDrawable != null ? mCurrDrawable.getIntrinsicWidth() : -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return mCurrDrawable != null ? mCurrDrawable.getIntrinsicHeight() : -1;
    }
    
    @Override
    public int getMinimumWidth() {
        return mCurrDrawable != null ? mCurrDrawable.getMinimumWidth() : 0;
    }
    
    @Override
    public int getMinimumHeight() {
        return mCurrDrawable != null ? mCurrDrawable.getMinimumHeight() : 0;
    }
    
    @Override
    protected boolean onStateChange(int[] state) {
        if (mCurrDrawable != null) {
            return mCurrDrawable.setState(state);
        }
        return false;
    }

    @Override
    protected boolean onLevelChange(int level) {
        if (mCurrDrawable != null) {
            return mCurrDrawable.setLevel(level);
        }
        return false;
    }
	public int[] getState() {
		return mCurrDrawable.getState();
	}

	public Drawable getCurrent() {
		return mCurrDrawable;
	}

	public boolean setVisible(boolean visible, boolean restart) {
		return mCurrDrawable.setVisible(visible, restart);
	}

	public int getOpacity() {
		return mCurrDrawable.getOpacity();
	}

	public Region getTransparentRegion() {
		return mCurrDrawable.getTransparentRegion();
	}

	public boolean getPadding(Rect padding) {
		return mCurrDrawable.getPadding(padding);
	}

	public Drawable mutate() {
		return mCurrDrawable.mutate();
	}

	public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
			throws XmlPullParserException, IOException {
		mCurrDrawable.inflate(r, parser, attrs);
	}

	public ConstantState getConstantState() {
		return mCurrDrawable.getConstantState();
	}

	public String toString() {
		return TAG+mCurrDrawable.toString();
	}
	
	// Callback Methods called from the delegate
	
	public void invalidateDrawable(Drawable who) {
		if (who == mCurrDrawable) {
			Log.d("ALD", "invalidateDrawable who="+who);
			invalidateSelf();
		}
	}

	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		if (who == mCurrDrawable) {
			Log.d("ALD", "scheduleDrawable who="+who+" what="+what+" when="+when);
			scheduleSelf(what, when);
		}
	}

	public void unscheduleDrawable(Drawable who, Runnable what) {
		if (who == mCurrDrawable) {
			Log.d("ALD", "unscheduleDrawable who="+who+" what="+what);
			unscheduleSelf(what);
		}
	}

}
