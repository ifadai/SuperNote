package app.fadai.supernote.widget;

import android.graphics.drawable.GradientDrawable;

import com.blankj.utilcode.util.SizeUtils;


/**
 * Created by miaoyongyong on 2017/2/23.
 */

public class MyDrawable {

//    便签夹 图标 的样式
    public static GradientDrawable getIcFolderSelectedDrawable( int color){
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setSize(SizeUtils.dp2px(24), SizeUtils.dp2px(24));
        gradientDrawable.setBounds(0,0,SizeUtils.dp2px(24), SizeUtils.dp2px(24));
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

////    反馈页面的提交按钮
//    public static StateListDrawable getFeedbackBtnDrawable(){
//
//    }

}
