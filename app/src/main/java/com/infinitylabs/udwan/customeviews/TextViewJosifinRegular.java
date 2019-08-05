package com.infinitylabs.udwan.customeviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

public class TextViewJosifinRegular extends AppCompatTextView {

    public TextViewJosifinRegular(Context context) {
        super(context);
        init(context);
    }

    public TextViewJosifinRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewJosifinRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {

        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/JosefinSans-Regular.ttf"));
        this.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.0f, getResources().getDisplayMetrics()), 1.0f);
    }
}