package com.shrikanthravi.collapsiblecalendarview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.shrikanthravi.collapsiblecalendarview.R;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.view.LockScrollView;


public abstract class UICalendar extends LinearLayout {


    // Day of Week
    public static final int SUNDAY    = 0;
    public static final int MONDAY    = 1;
    public static final int TUESDAY   = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY  = 4;
    public static final int FRIDAY    = 5;
    public static final int SATURDAY  = 6;
    // State
    public static final int STATE_EXPANDED   = 0;
    public static final int STATE_COLLAPSED  = 1;
    public static final int STATE_PROCESSING = 2;

    protected Context mContext;
    protected LayoutInflater mInflater;

    // UI
    protected LinearLayout mLayoutRoot;
    protected TextView mTxtTitle;
    protected TableLayout mTableHead;
    protected LockScrollView mScrollViewBody;
    protected TableLayout mTableBody;
    protected RelativeLayout mLayoutBtnGroupMonth;
    protected ImageView mBtnPrevMonth;
    protected ImageView mBtnNextMonth;

    // Attributes
    private boolean mShowWeek = true;
    private int mFirstDayOfWeek = SUNDAY;

    private int mTextColor = Color.BLACK;
    private int mPrimaryColor = Color.WHITE;

    private int mTodayItemTextColor = Color.BLACK;
    private int mTodayItemBackgroundColor = Color.BLACK;
    private Drawable mTodayItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_stroke_background);
    private int mSelectedItemTextColor = Color.WHITE;
    private int mSelectedItemBackgroundColor = Color.WHITE;
    private Drawable mSelectedItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_solid_background);
    private int mPassedDateItemTextColor = Color.WHITE;
    private int mPassedDateItemBackgroundColor = Color.WHITE;
    private Drawable mPassedDateItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_solid_background);
    private int mNormalDateItemTextColor = Color.WHITE;
    private int mNormalDateItemBackgroundColor = Color.WHITE;
    private Drawable mNormalDateItemBackgroundDrawable =
            getResources().getDrawable(R.drawable.circle_black_solid_background);

    private Drawable mButtonLeftDrawable =
            getResources().getDrawable(R.drawable.left_icon);
    private Drawable mButtonRightDrawable =
            getResources().getDrawable(R.drawable.right_icon);

    private Day mSelectedItem = null;

    private int mButtonLeftDrawableTintColor=Color.BLACK;
    private int mButtonRightDrawableTintColor=Color.BLACK;

    private int mEventColor=Color.BLACK;

    public UICalendar(Context context) {
        this(context, null);
    }

    public UICalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UICalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.UICalendar, defStyleAttr, 0);
        setAttributes(attributes);
        attributes.recycle();
    }

    protected abstract void redraw();
    protected abstract void reload();

    protected void init(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        // load rootView from xml
        View rootView = mInflater.inflate(R.layout.widget_collapsible_calendarview, this, true);

        // init UI
        mLayoutRoot          = rootView.findViewById(R.id.layout_root);
        mTxtTitle            = rootView.findViewById(R.id.txt_title);

        mTableHead           = rootView.findViewById(R.id.table_head);
        mScrollViewBody      = rootView.findViewById(R.id.scroll_view_body);
        mTableBody           = rootView.findViewById(R.id.table_body);
        mLayoutBtnGroupMonth = rootView.findViewById(R.id.layout_btn_group_month);
        mBtnPrevMonth        = rootView.findViewById(R.id.btn_prev_month);
        mBtnNextMonth        = rootView.findViewById(R.id.btn_next_month);
    }

    protected void setAttributes(TypedArray attrs) {
        // set attributes by the values from XML
        //setStyle(attrs.getInt(R.styleable.UICalendar_style, mStyle));
        setShowWeek(attrs.getBoolean(R.styleable.UICalendar_showWeek, mShowWeek));
        setFirstDayOfWeek(attrs.getInt(R.styleable.UICalendar_firstDayOfWeek, mFirstDayOfWeek));

        setTextColor(attrs.getColor(R.styleable.UICalendar_textColor, mTextColor));
        setPrimaryColor(attrs.getColor(R.styleable.UICalendar_primaryColor, mPrimaryColor));

        setEventColor(attrs.getColor(R.styleable.UICalendar_eventColor, mEventColor));


        setTodayItemTextColor(attrs.getColor(
                R.styleable.UICalendar_todayItem_textColor, mTodayItemTextColor));
        setTodayItemBackgroundColor(attrs.getColor(
                R.styleable.UICalendar_todayItem_background_color,mTodayItemBackgroundColor));
        Drawable todayItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_todayItem_background);
        if (todayItemBackgroundDrawable != null) {
            setTodayItemBackgroundDrawable(todayItemBackgroundDrawable);
        } else {
            setTodayItemBackgroundDrawable(mTodayItemBackgroundDrawable);
        }

        setSelectedItemTextColor(attrs.getColor(
                R.styleable.UICalendar_selectedItem_textColor, mSelectedItemTextColor));
        setSelectedItemBackgroundColor(attrs.getColor(
                R.styleable.UICalendar_selectedItem_background_color,mSelectedItemBackgroundColor));
        Drawable selectedItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_selectedItem_background);
        if (selectedItemBackgroundDrawable != null) {
            setSelectedItemBackgroundDrawable(selectedItemBackgroundDrawable);
        } else {
            setSelectedItemBackgroundDrawable(mSelectedItemBackgroundDrawable);
        }

        setPassedDateItemTextColor(attrs.getColor(
                R.styleable.UICalendar_passedDateItem_textColor,mPassedDateItemTextColor));
        setPassedDateItemBackgroundColor(attrs.getColor(
                R.styleable.UICalendar_passedDateItem_background_color,mPassedDateItemBackgroundColor));
        Drawable passedDateItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_passedDateItem_background);
        if(passedDateItemBackgroundDrawable != null){
            setPassedDateItemBackgroundDrawable(passedDateItemBackgroundDrawable);
        } else {
            setPassedDateItemBackgroundDrawable(mPassedDateItemBackgroundDrawable);
        }

        setNormalDateItemTextColor(attrs.getColor(
                R.styleable.UICalendar_normalDateItem_textColor,mNormalDateItemTextColor));
        setNormalDateItemBackgroundColor(attrs.getColor(
                R.styleable.UICalendar_normalDateItem_background_color,mNormalDateItemBackgroundColor));
        Drawable normalDateItemBackgroundDrawable =
                attrs.getDrawable(R.styleable.UICalendar_normalDateItem_background);
        if(normalDateItemBackgroundDrawable != null){
            setNormalDateItemBackgroundDrawable(normalDateItemBackgroundDrawable);
        } else {
            setNormalDateItemBackgroundDrawable(mNormalDateItemBackgroundDrawable);
        }

        Drawable buttonLeftDrawable =
                attrs.getDrawable(R.styleable.UICalendar_buttonLeft_drawable);
        if (buttonLeftDrawable != null) {
            setButtonLeftDrawable(buttonLeftDrawable);
        } else {
            setButtonLeftDrawable(mButtonLeftDrawable);
        }

        Drawable buttonRightDrawable =
                attrs.getDrawable(R.styleable.UICalendar_buttonRight_drawable);
        if (buttonRightDrawable != null) {
            setButtonRightDrawable(buttonRightDrawable);
        } else {
            setButtonRightDrawable(mButtonRightDrawable);
        }

        setButtonLeftDrawableTintColor(attrs.getColor(R.styleable.UICalendar_buttonLeft_drawableTintColor,mButtonLeftDrawableTintColor));
        setButtonRightDrawableTintColor(attrs.getColor(R.styleable.UICalendar_buttonRight_drawableTintColor,mButtonRightDrawableTintColor));
        Day selectedItem   = null;
    }

    public void setButtonLeftDrawableTintColor(int color){
        this.mButtonLeftDrawableTintColor = color;
        mBtnPrevMonth.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        redraw();
    }

    public void setButtonRightDrawableTintColor(int color){

        this.mButtonRightDrawableTintColor = color;
        mBtnNextMonth.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        redraw();
    }

    public boolean isShowWeek() {
        return mShowWeek;
    }

    public void setShowWeek(boolean showWeek) {
        this.mShowWeek = showWeek;

        if (showWeek) {
            mTableHead.setVisibility(VISIBLE);
        } else {
            mTableHead.setVisibility(GONE);
        }
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.mFirstDayOfWeek = firstDayOfWeek;
        reload();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        redraw();

        mTxtTitle.setTextColor(mTextColor);
    }

    public int getPrimaryColor() {
        return mPrimaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.mPrimaryColor = primaryColor;
        redraw();

        mLayoutRoot.setBackgroundColor(mPrimaryColor);
    }

    private void setEventColor(int eventColor) {
        this.mEventColor = eventColor;
        redraw();

    }
    public int getEventColor() {
        return mEventColor;
    }

    public int getTodayItemTextColor() {
        return mTodayItemTextColor;
    }

    public void setTodayItemTextColor(int todayItemTextColor) {
        this.mTodayItemTextColor = todayItemTextColor;
        redraw();
    }

    public Drawable getTodayItemBackgroundDrawable() {
        return mTodayItemBackgroundDrawable;
    }

    public void setTodayItemBackgroundDrawable(Drawable todayItemBackgroundDrawable) {
        this.mTodayItemBackgroundDrawable = todayItemBackgroundDrawable;
        redraw();
    }

    public int getSelectedItemTextColor() {
        return mSelectedItemTextColor;
    }

    public void setSelectedItemTextColor(int selectedItemTextColor) {
        this.mSelectedItemTextColor = selectedItemTextColor;
        redraw();
    }

    public Drawable getSelectedItemBackgroundDrawable() {
        return mSelectedItemBackgroundDrawable;
    }

    public void setSelectedItemBackgroundDrawable(Drawable selectedItemBackground) {
        this.mSelectedItemBackgroundDrawable = selectedItemBackground;
        redraw();
    }

    public int getPassedDateItemTextColor() {
        return mPassedDateItemTextColor;
    }

    public void setPassedDateItemTextColor(int mPassedDateItemTextColor) {
        this.mPassedDateItemTextColor = mPassedDateItemTextColor;
    }

    public Drawable getPassedDateItemBackgroundDrawable() {
        return mPassedDateItemBackgroundDrawable;
    }

    public void setPassedDateItemBackgroundDrawable(Drawable mPassedDateItemBackgroundDrawable) {
        this.mPassedDateItemBackgroundDrawable = mPassedDateItemBackgroundDrawable;
    }

    public int getNormalDateItemTextColor() {
        return mNormalDateItemTextColor;
    }

    public void setNormalDateItemTextColor(int mNormalDateItemTextColor) {
        this.mNormalDateItemTextColor = mNormalDateItemTextColor;
    }

    public Drawable getNormalDateItemBackgroundDrawable() {
        return mNormalDateItemBackgroundDrawable;
    }

    public void setNormalDateItemBackgroundDrawable(Drawable mNormalDateItemBackgroundDrawable) {
        this.mNormalDateItemBackgroundDrawable = mNormalDateItemBackgroundDrawable;
    }

    public int getTodayItemBackgroundColor() {
        return mTodayItemBackgroundColor;
    }

    public void setTodayItemBackgroundColor(int mTodayItemBackgroundColor) {
        this.mTodayItemBackgroundColor = mTodayItemBackgroundColor;
    }

    public int getSelectedItemBackgroundColor() {
        return mSelectedItemBackgroundColor;
    }

    public void setSelectedItemBackgroundColor(int mSelectedItemBackgroundColor) {
        this.mSelectedItemBackgroundColor = mSelectedItemBackgroundColor;
    }

    public int getPassedDateItemBackgroundColor() {
        return mPassedDateItemBackgroundColor;
    }

    public void setPassedDateItemBackgroundColor(int mPassedDateItemBackgroundColor) {
        this.mPassedDateItemBackgroundColor = mPassedDateItemBackgroundColor;
    }

    public int getNormalDateItemBackgroundColor() {
        return mNormalDateItemBackgroundColor;
    }

    public void setNormalDateItemBackgroundColor(int mNormalDateItemBackgroundColor) {
        this.mNormalDateItemBackgroundColor = mNormalDateItemBackgroundColor;
    }

    public Drawable getButtonLeftDrawable() {
        return mButtonLeftDrawable;
    }

    public void setButtonLeftDrawable(Drawable buttonLeftDrawable) {
        this.mButtonLeftDrawable = buttonLeftDrawable;
        mBtnPrevMonth.setImageDrawable(buttonLeftDrawable);
    }

    public Drawable getButtonRightDrawable() {
        return mButtonRightDrawable;
    }

    public void setButtonRightDrawable(Drawable buttonRightDrawable) {
        this.mButtonRightDrawable = buttonRightDrawable;
        mBtnNextMonth.setImageDrawable(buttonRightDrawable);
    }

    public Day getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(Day selectedItem) {
        this.mSelectedItem = selectedItem;
    }
}
