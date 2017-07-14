package app.fadai.supernote.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.Utils;

import java.lang.reflect.Field;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/20
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class ThemeUtils {

    private static TypedValue value=new TypedValue();

    public static int getColorPrimary(Context context){
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value,true);
        return value.data;
    }

    public static int getColorPrimaryDark(Context context){
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value,true);
        return value.data;
    }

    //    状态栏颜色
    public static void setWindowStatusBarColor(Window window, int color){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            状态栏
            window.setStatusBarColor(color);
//            底部导航栏
//            window.setNavigationBarColor(color);
        }
    }

    //    Toolbar颜色
    public static void setToolbarColor(ActionBar actionBar, int color){
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }

    public static void resetToolbarColor(Context context){
        int color=getColorPrimary(context);
        ActionBar actionBar=((AppCompatActivity)context).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }

    public static void resetWindowStatusBarColor(Context context){
        int color=getColorPrimaryDark(context);
        Window window=((Activity)context).getWindow();
        setWindowStatusBarColor(window,color);
    }

    /**
     *  获取状态栏高度
     *  @describe
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = Utils.getContext().getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }
}
