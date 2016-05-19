package com.android.customview.tools;

import android.content.Context;

import com.android.customview.view.LockPatternView;

import java.util.List;

/**
 * Created by litonghui on 2016/5/18.
 */
public class LockUtils {

    /**
     * Password type class.
     * SLIDE 滑动
     * PATTERN 图形，即九宫格
     * DIGITAL 数字
     */
    public static enum PwdType {
        SLIDE, PATTERN, DIGITAL
    }

    public static PwdType getCurrentPwdType(Context context) {
        String data = Preferences.getLockType(context);
        try {
            int type = Integer.valueOf(data);
            if (type == PwdType.SLIDE.ordinal()) {
                return PwdType.SLIDE;
            } else if (type == PwdType.PATTERN.ordinal()) {
                return PwdType.PATTERN;
            } else if (type == PwdType.DIGITAL.ordinal()) {
                return PwdType.DIGITAL;
            }
        } catch (Exception e) {
        }
        return PwdType.SLIDE;
    }
    public static void setCurrentPwdType(Context context,PwdType type) {
        int data = PwdType.SLIDE.ordinal();
        if (type != null) {
            switch (type) {
                case SLIDE:
                    data = PwdType.SLIDE.ordinal();
                    break;
                case PATTERN:
                    data = PwdType.PATTERN.ordinal();
                    break;
                case DIGITAL:
                    data = PwdType.DIGITAL.ordinal();
                    break;
            }
        }
        Preferences.setLockType(context,String.valueOf(data));
    }
    public static String convertToPwd(List<LockPatternView.Cell> pattern){
        StringBuilder builder = new StringBuilder();
        for(LockPatternView.Cell c: pattern){
            int tmp = c.mRow * LockPatternView.MATRIX_WIDTH + c.mColumn + 1;
            builder.append(tmp);
        }

        return builder.toString();
    }
}
