package com.kayoxu.androidlib.dialog.common;

import android.app.SearchManager;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import com.bigkoo.pickerview.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


/**
 * TFBlueAndroidXNew
 * com.kayoxu.commonlib.widget
 * <p>
 * Created by kayoxu on 2019-08-19 20:16.
 * Copyright © 2019 kayoxu. All rights reserved.
 */
public class IBottomSheetDialog extends BottomSheetDialog {
    int peekHeight = 0;
    private BottomSheetBehavior behavior;

    public IBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public IBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public IBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void show() {
        super.show();
        if (peekHeight == 0) {
            View container = findViewById(R.id.content_layout);

            if (null != container) {
                container.measure(0, 0);
                peekHeight = container.getMeasuredHeight();
            }
//            FrameLayout bottomSheet = (FrameLayout) findViewById(R.id.design_bottom_sheet);
            if (null != getBehavior()) {
                behavior.setPeekHeight(peekHeight);
            }
        }
    }

    @NonNull
    @Override
    public BottomSheetBehavior getBehavior() {
        if (null == behavior) {
            FrameLayout bottomSheet = (FrameLayout) findViewById(R.id.design_bottom_sheet);
            if (null != bottomSheet) {
                behavior = BottomSheetBehavior.from(bottomSheet);
            }
        }

        return behavior;
    }
}
