package com.studio.shade;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.studio.shade.statusbar.phone.PhoneStatusBarView;
import com.studio.shade.statusbar.phone.StatusBarWindowView;
import com.studio.shade.R;

public class CustomStatusBarService extends AccessibilityService {
    private static final int REQUEST_SYSTEM_ALERT_WINDOW = 1;
    private WindowManager windowManager;
    private View customStatusBarView;
    private boolean isSwipeDownEnabled;
    private boolean isPermissionGranted = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Handle accessibility events if needed
    }

    @Override
    public void onInterrupt() {
        // Handle interruption of the service if needed
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Permission not granted, set the flag accordingly
            isPermissionGranted = false;
        } else {
            // Permission granted, proceed with creating the custom status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createCustomStatusBar();
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        removeCustomStatusBar();
        return super.onUnbind(intent);
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createCustomStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Request the SYSTEM_ALERT_WINDOW permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                //getResources().getDimensionPixelSize(R.dimen.custom_status_bar_height),
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;

        LayoutInflater inflater = LayoutInflater.from(this);
        customStatusBarView = inflater.inflate(R.layout.super_status_bar, null);
        customStatusBarView = inflater.inflate(R.layout.status_bar, null);
        isPermissionGranted = true;

        windowManager.addView(customStatusBarView, layoutParams);

        // Set the custom status bar view as the accessibility focus
        customStatusBarView.setFocusable(true);
        customStatusBarView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
    }

    private void removeCustomStatusBar() {
        if (windowManager != null && customStatusBarView != null) {
            windowManager.removeView(customStatusBarView);
        }
    }
}
