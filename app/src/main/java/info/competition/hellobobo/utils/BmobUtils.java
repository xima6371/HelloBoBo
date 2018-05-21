package info.competition.hellobobo.utils;

import android.content.Context;

import cn.bmob.v3.Bmob;

public class BmobUtils {
    public static final String ID = "0c68a792ac8245e4c1302cd46b86fff0";

    public static void init(Context context) {
        Bmob.initialize(context, ID);
    }
}
