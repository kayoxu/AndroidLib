package com.kayoxu.androidlib.dialog.timepicker;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kayoxu.android.utils.timepicker.IBottomSheetDialog;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * TFBlueAndroidXNew
 * cn.shomes.tfblue.ui.wj.widget
 * <p>
 * Created by kayoxu on 2019-08-09 10:49.
 * Copyright © 2019 kayoxu. All rights reserved.
 */
public class KayoTimePicker extends LinearLayout {

    private static final String TAG = "KayoTimePicker 🌶️";

    private Context context;

    private TextView cancelBtn;
    private Button okBtn;
    private TextView titleView, startTimeView, endTimeView;
    private TextView startTimeTitleView, endTimeTitleView;
    private FrameLayout endTimeRoot;
    private FrameLayout startTimeRoot;

    private boolean showEndTime = true;


    private String startTime = getNowTimeString();
    private String endTime = getNowTimeString();
    private String startTimeDefault;
    private String endTimeDefault;

    private String maxTime;
    private String minTime;
    private TimeResult timeResult;
    private String titleStr;
    private boolean showWeek = true;

    //    private BottomSheetDialog sheetDialog;
    private com.kayoxu.android.utils.timepicker.IBottomSheetDialog sheetDialog;
    private boolean[] timeTypeShow = new boolean[]{true, true, true, true, true, true};
    public String label_year, label_month, label_day, label_hours, label_minutes, label_seconds;


    public KayoTimePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public KayoTimePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KayoTimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View root = LayoutInflater.from(context).inflate(R.layout.kayo_time_picker,
                this, true);

        cancelBtn = root.findViewById(R.id.cancelBtn);
        okBtn = root.findViewById(R.id.okBtn);

        titleView = root.findViewById(R.id.titleView);
        startTimeRoot = root.findViewById(R.id.startTimeRoot);
        endTimeRoot = root.findViewById(R.id.endTimeRoot);

        startTimeView = root.findViewById(R.id.startTimeView);
        endTimeView = root.findViewById(R.id.endTimeView);
        startTimeTitleView = root.findViewById(R.id.startTimeTitleView);
        endTimeTitleView = root.findViewById(R.id.endTimeTitleView);

        setTimeTitle();


        cancelBtn.setOnClickListener(v -> {
            if (null != timeResult) {
                String endTime = showEndTime ? endTimeDefault : null;
                timeResult.onCancel(startTimeDefault, endTime);
            }
            if (null != sheetDialog) sheetDialog.dismiss();
        });
        okBtn.setOnClickListener(v -> {
            if (null != sheetDialog) {
                if (StringToTimestamp(startTime).after(StringToTimestamp(endTime)) && showEndTime) {
                    Toast.makeText(context, "开始时间不能大于结束时间！", Toast.LENGTH_LONG).show();
                    return;
                }

                if (null != timeResult) {
                    String endTime = showEndTime ? this.endTime : null;
                    timeResult.onTime(startTime, endTime);
                }
                sheetDialog.dismiss();
            }
        });


        sheetDialog = new IBottomSheetDialog(context);
        sheetDialog.setContentView(root);
        sheetDialog.getBehavior().setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        if (null != sheetDialog.getWindow()) {
            sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
        }
        setTimeTitle();
    }

    private void setTimeTitle() {
        startTimeView.setText(getTimeTitleStr(startTime));
        endTimeView.setText(getTimeTitleStr(endTime));
    }


    private void initTimePicker(ViewGroup pickerRoot, String minTime, String maxTime, String time) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTime(StringToTimestamp(time));

        Calendar startDate = Calendar.getInstance();
        if (null != minTime) {
            startDate.setTime(StringToTimestamp(minTime));
        } else {
            startDate.set(2000, 0, 1);
        }

        Calendar endDate = Calendar.getInstance();
        if (null != maxTime) {
            endDate.setTime(StringToTimestamp(maxTime));
        } else {
            endDate.set(2100, 11, 31);
        }

        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                if (v == startTimeRoot) {
                    Log.e("🌶️ startTimeRoot ", date.toString());
                } else if (v == endTimeRoot) {
                    Log.e("🌶️ endTimeRoot ", date.toString());
                }


            }

        })
                .setLayoutRes(R.layout.kayo_time_picker_item, v -> {
                })
                .setType(timeTypeShow)
                .setLabel(label_year, label_month, label_day, label_hours, label_minutes, label_seconds) //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.parseColor("#CCCCCC"))
//                .setDividerType(WheelView.DividerType.WRAP)
                .setContentTextSize(16)
                .setTitleColor(Color.parseColor("#333333"))
                .setItemVisibleCount(3)
                .setRangDate(startDate, endDate)
                .setDate(selectedDate)
                .setDecorView(pickerRoot)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .setOutSideColor(0x00000000)
                .setOutSideCancelable(false).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        if (pickerRoot == startTimeRoot) {
                            startTime = dateToString(date);
                            startTimeView.setText(getTimeTitleStr(startTime));
                        } else if (pickerRoot == endTimeRoot) {
                            endTime = dateToString(date);
                            endTimeView.setText(getTimeTitleStr(endTime));
                        }
                    }
                })
                .build();

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉


        pvTime.setDate(selectedDate);
        pvTime.show(pickerRoot, false);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
    }

    private String getTimeTitleStr(String time) {
        return dateToString(new Date(StringToTimestamp(time).getTime()), "MM月dd日 ") + getWeek(time);
    }

    private int stringToInt(String value) {
        if (!isStrEmpty(value)) value = value.trim();
        if (null != value && isNum(value)) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    private boolean isNum(String str) {
        String regex = "^[0-9]+$";//其他需要，直接修改正则表达式就好
        return str.matches(regex);
    }

    private boolean equals(String str1, String str2) {
        return null != str1 && null != str2 && str1.equals(str2);
    }

    private boolean isStrEmpty(String str) {
        return null == str || equals("", str);
    }

    private String getNowTimeString() {
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    private Timestamp StringToTimestamp(String str) {
        if (str == null || str.equals(""))
            return null;
        Timestamp ts = null;
        try {
            ts = Timestamp.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

    public String dateToString(Date date, String formatType) {
        return new SimpleDateFormat(formatType).format(date);
    }

    public String dateToString(Date date) {
        return dateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public String getWeek(String time) {
        Calendar calendarNow = Calendar.getInstance(Locale.CHINA);

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(StringToTimestamp(time));

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        int yearNow = calendarNow.get(Calendar.YEAR);
        int dayNow = calendarNow.get(Calendar.DAY_OF_YEAR);


        if (year == yearNow) {
            if (day == dayNow) {
                return "今天";
            } else if (dayNow - day == 1) {
                return "昨天";
            } else if (day - dayNow == 1) {
                return "明天";
            }
        }

        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 2) {
            return "周一";
        } else if (dayWeek == 3) {
            return "周二";
        } else if (dayWeek == 4) {
            return "周三";
        } else if (dayWeek == 5) {
            return "周四";
        } else if (dayWeek == 6) {
            return "周五";
        } else if (dayWeek == 7) {
            return "周六";
        } else if (dayWeek == 1) {
            return "周日";
        }
        return dayWeek + "";
    }

    //对外方法/////////////////

    public interface TimeResult {
        void onTime(String startTime, String endTime);

        void onCancel(String startTime, String endTime);
    }


    public KayoTimePicker setOnTimeResult(TimeResult timeResult) {
        this.timeResult = timeResult;
        return this;
    }

    public KayoTimePicker(Context context) {
        super(context);
        initView(context);
    }


    public KayoTimePicker(Context context, String startTime, String endTime, String minTime, String maxTime, TimeResult timeResult) {
        super(context);
        this.timeResult = timeResult;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxTime = maxTime;
        this.minTime = minTime;
        this.startTimeDefault = startTime;
        this.endTimeDefault = endTime;

        initView(context);
    }

    public KayoTimePicker(Context context, String startTime, String endTime, TimeResult timeResult) {
        super(context);
        this.timeResult = timeResult;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startTimeDefault = startTime;
        this.endTimeDefault = endTime;

        initView(context);
    }

    public KayoTimePicker show() {
        if (null != sheetDialog) {

            initTimePicker(startTimeRoot, minTime, maxTime, startTime);
            initTimePicker(endTimeRoot, minTime, maxTime, endTime);

            sheetDialog.show();
        }
        return this;
    }

    public KayoTimePicker setStartTime(String startTime) {
        this.startTime = startTime;
        this.startTimeDefault = startTime;
        setTimeTitle();
        return this;
    }

    public KayoTimePicker setTitle(String title) {
        this.titleStr = title;
        titleView.setText(titleStr);
        return this;
    }

    public KayoTimePicker showWeek(boolean showWeek) {
        this.showWeek = showWeek;
        startTimeView.setVisibility(showWeek ? VISIBLE : GONE);
        endTimeView.setVisibility(showWeek && showEndTime ? VISIBLE : GONE);
        return this;
    }

    public KayoTimePicker setEndTime(String endTime) {
        this.endTime = endTime;
        this.endTimeDefault = endTime;
        setTimeTitle();
        return this;
    }

    public KayoTimePicker setMaxTime(String maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public KayoTimePicker setMinTime(String minTime) {
        this.minTime = minTime;
        return this;
    }

    public KayoTimePicker setShowEndTime(boolean showEndTime) {
        this.showEndTime = showEndTime;

        startTimeTitleView.setVisibility(showEndTime ? VISIBLE : GONE);
        endTimeTitleView.setVisibility(showEndTime ? VISIBLE : GONE);
        endTimeView.setVisibility(showEndTime && showWeek ? VISIBLE : GONE);
        endTimeRoot.setVisibility(showEndTime ? VISIBLE : GONE);

        String text = showEndTime ? "选择时间段" : "请选择时间";
        titleView.setText(null == titleStr ? text : titleStr);

        return this;
    }

    public KayoTimePicker setTimeTypeShow(boolean[] timeTypeShow) {
        this.timeTypeShow = timeTypeShow;
        return this;
    }

    public KayoTimePicker setLabel(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        this.label_year = label_year;
        this.label_month = label_month;
        this.label_day = label_day;
        this.label_hours = label_hours;
        this.label_minutes = label_mins;
        this.label_seconds = label_seconds;
        return this;
    }


}
