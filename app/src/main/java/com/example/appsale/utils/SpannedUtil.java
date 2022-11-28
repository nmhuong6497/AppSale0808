package com.example.appsale.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.appsale.R;

public class SpannedUtil {
    public static SpannableStringBuilder setClickColorLink(String text, Context context, OnListenClick onListenClick) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text);
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                onListenClick.onClick();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.primary));
            }
        }, 0, builder.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }

    public interface OnListenClick {
        void onClick();
    }
}