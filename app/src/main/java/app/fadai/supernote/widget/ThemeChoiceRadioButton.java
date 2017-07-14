package app.fadai.supernote.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by miaoyongyong on 2016/12/26.
 */

public class ThemeChoiceRadioButton extends RadioButton {

    private int backgroundColor;
    public ThemeChoiceRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.ThemeChoiceRadioButton);
        backgroundColor=ta.getColor(R.styleable.ThemeChoiceRadioButton_backgroundColor,0);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCorcle(canvas);
    }

    private void drawCorcle(Canvas canvas){

        Paint paint=new Paint();
        paint.setColor(backgroundColor);
        //  绘制圆形
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2, SizeUtils.dp2px(20),paint);

        //  绘制对勾
        if(isChecked()){
            Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.ic_check_white_24dp);
            canvas.drawBitmap(bitmap,getMeasuredWidth()/2-SizeUtils.dp2px(12),getMeasuredHeight()/2-SizeUtils.dp2px(12),paint);
        }
    }

    @Override
    public void toggle() {
        super.toggle();
    }
}
