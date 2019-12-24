package com.kayoxu.androidlib.dialog.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

 

public class INumberPicker extends NumberPicker {

    Context context;

    public INumberPicker(Context context) {
        super(context);
        this.context = context;
    }

    public INumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public INumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    public void updateView(View view) {
        setDatePickerDividerColor(this);
        setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        if (view instanceof EditText) {
            //这里修改显示字体的属性，主要修改颜色和大小
//            ((EditText) view).setTextColor(Color.parseColor("#FF0000"));
//            ((EditText) view).setTextSize(14);
        }
    }

    private void setDatePickerDividerColor(NumberPicker picker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(Color.parseColor("#B8B8B8")));
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        // 分割线高度
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
//                    int result = dp2px(context, 1);
                    int result = 3;
                    pf.set(picker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

