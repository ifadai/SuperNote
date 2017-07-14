package app.fadai.supernote.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.orhanobut.logger.Logger;

import app.fadai.supernote.MainApplication;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/10
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class MyScrollView extends ScrollView {


    final ViewConfiguration configuration = ViewConfiguration.get(MainApplication.mContext);
    private float mTouchSlop = configuration.getScaledTouchSlop();

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);
        Logger.d("scrollView dispatchTouchEvent:" + ret);
        return ret;
    }

    private float lastY;
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
////        boolean ret=super.onInterceptTouchEvent(ev);
////        Logger.d("scrollView onInterceptTouchEvent:"+ret);
////        return ret;
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                lastY = ev.getY();
//                return false;
//            case MotionEvent.ACTION_MOVE:
//                if (Math.abs(lastY - ev.getY()) > mTouchSlop)
//                    return true;
//                else super.onInterceptTouchEvent(ev);
//            default:
//                return super.onInterceptTouchEvent(ev);
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean ret = super.onTouchEvent(ev);
        Logger.d("scrollView onTouchEvent:" + ret);
        return ret;
    }
}
