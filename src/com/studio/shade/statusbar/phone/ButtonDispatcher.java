/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.studio.shade.statusbar.phone;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.studio.shade.statusbar.policy.KeyButtonView;

import java.util.ArrayList;

/**
 * Dispatches common view calls to multiple views.  This is used to handle
 * multiples of the same nav bar icon appearing.
 */
public class ButtonDispatcher {

    private final ArrayList<View> mViews = new ArrayList<>();

    private final int mId;

    private View.OnClickListener mClickListener;
    private View.OnTouchListener mTouchListener;
    private View.OnLongClickListener mLongClickListener;
    private Boolean mLongClickable;
    private Integer mAlpha;
    private Integer mVisibility = -1;
    private int mImageResource = -1;
    private Drawable mImageDrawable;
    private View mCurrentView;

    public ButtonDispatcher(int id) {
        mId = id;
    }

    void clear() {
        mViews.clear();
    }

    void addView(View view) {
        mViews.add(view);
        view.setOnClickListener(mClickListener);
        view.setOnTouchListener(mTouchListener);
        view.setOnLongClickListener(mLongClickListener);
        if (mLongClickable != null) {
            view.setLongClickable(mLongClickable);
        }
        if (mAlpha != null) {
            view.setAlpha(mAlpha);
        }
        if (mVisibility != null) {
            view.setVisibility(mVisibility);
        }
        if (mImageResource > 0) {
            ((ImageView) view).setImageResource(mImageResource);
        } else if (mImageDrawable != null) {
            ((ImageView) view).setImageDrawable(mImageDrawable);
        }
    }

    public int getId() {
        return mId;
    }

    public int getVisibility() {
        return mVisibility != null ? mVisibility : View.VISIBLE;
    }

    public float getAlpha() {
        return mAlpha != null ? mAlpha : 1;
    }

    public void setImageDrawable(Drawable drawable) {
        mImageDrawable = drawable;
        mImageResource = -1;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            ((ImageView) mViews.get(i)).setImageDrawable(mImageDrawable);
        }
    }

    public void setImageResource(int resource) {
        mImageResource = resource;
        mImageDrawable = null;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            ((ImageView) mViews.get(i)).setImageResource(mImageResource);
        }
    }

    public void setVisibility(int visibility) {
        if (mVisibility == visibility) return;
        mVisibility = visibility;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            mViews.get(i).setVisibility(mVisibility);
        }
    }

    public void abortCurrentGesture() {
        // This seems to be an instantaneous thing, so not going to persist it.
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            ((KeyButtonView) mViews.get(i)).abortCurrentGesture();
        }
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            mViews.get(i).setAlpha(alpha);
        }
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            mViews.get(i).setOnClickListener(mClickListener);
        }
    }

    public void setOnTouchListener(View.OnTouchListener touchListener) {
        mTouchListener = touchListener;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            mViews.get(i).setOnTouchListener(mTouchListener);
        }
    }

    public void setLongClickable(boolean isLongClickable) {
        mLongClickable = isLongClickable;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            mViews.get(i).setLongClickable(mLongClickable);
        }
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
        final int N = mViews.size();
        for (int i = 0; i < N; i++) {
            mViews.get(i).setOnLongClickListener(mLongClickListener);
        }
    }

    public View getCurrentView() {
        return mCurrentView;
    }

    public void setCurrentView(View currentView) {
        mCurrentView = currentView.findViewById(mId);
    }
}
