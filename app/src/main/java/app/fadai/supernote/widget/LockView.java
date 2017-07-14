package app.fadai.supernote.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.List;

import app.fadai.supernote.bean.Point;

/**
 * 九宫格解锁控件
 * Created by Jerry on 2015/9/21.
 */
public class LockView extends View {

    private Bitmap mNormalBitmap;
    private Bitmap mPressBitmap;
    private Bitmap mErrorBitmap;
    private float mPointRadius= SizeUtils.dp2px(3);

    private int mNormalColor= Utils.getContext().getResources().getColor(R.color.grey500);
    private int mPressColor=Utils.getContext().getResources().getColor(R.color.grey600);
    private int mErrorColor=Utils.getContext().getResources().getColor(R.color.red600);



    // 手指在屏幕上的位置
    private float mX, mY;

    // 标记当前是否在绘制状态
    private boolean isDraw = false;

    // 三种状态下的画笔
    private Paint mPaint, mPressPaint, mErrorPaint;

    // 九个点
    private Point[][] mPoints = new Point[3][3];
    // 被选中的点
    private List<Point> mSelectedPoints = new ArrayList<>();
    // 绘制正确的点位置
    private List<Integer> mPassPositions = new ArrayList<>();

//    是否可以点击
    private boolean mClickable=true;

    private OnDrawFinishedListener mListener;

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureDimension(widthMeasureSpec,heightMeasureSpec);
    }

    private void measureDimension(int widthMeasureSpec,int heightMeasureSpec){
//        测量的值
        int measureWidth=0;
        int measureHeight=0;
//        默认值
        int defaultSize=400;

        int widthSpecMode= MeasureSpec.getMode(widthMeasureSpec);
        int heightSpcMode= MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize= MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize= MeasureSpec.getSize(heightMeasureSpec);
        switch (widthSpecMode){
            case MeasureSpec.EXACTLY:
                measureWidth=widthSpecSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                measureWidth=SizeUtils.dp2px(defaultSize);
                break;
            case MeasureSpec.AT_MOST:
                measureWidth= Math.min(widthSpecSize,defaultSize);
                break;
        }
        switch (heightSpcMode){
            case MeasureSpec.EXACTLY:
                measureHeight=heightSpecSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                measureHeight=SizeUtils.dp2px(defaultSize);
                break;
            case MeasureSpec.AT_MOST:
//                为了让解锁界面为方形，让高的值和宽一样。
                measureHeight=measureWidth;
                break;
        }
        setMeasuredDimension(measureWidth,measureHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制点
        drawPoints(canvas);

        // 绘制连线
        drawLines(canvas);
    }

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 按下状态的画笔
        mPressPaint.setColor(mPressColor);
        mPressPaint.setAlpha(66);
        mPressPaint.setStrokeWidth(mPointRadius*2);
        // 错误状态的画笔
        mErrorPaint.setColor(mErrorColor);
        mErrorPaint.setAlpha(66);
        mErrorPaint.setStrokeWidth(mPointRadius*2);

        // 当前视图的大小
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 九宫格点的偏移量
        int offSet = Math.abs(width - height) / 2;
        // x、y轴上的偏移量
        int offSetX = 0, offSetY = 0;
        int pointItemWidth = 0; // 每个点所占用方格的宽度
        if (width >= height){ // 横屏的时候
            offSetX = offSet;
            offSetY = 0;
            pointItemWidth = height / 4;
        }
        if (width <= height){ // 竖屏的时候
            offSetX = 0;
            offSetY = offSet;
            pointItemWidth = width / 4;
        }

        // 初始化九个点
        mPoints[0][0] = new Point(offSetX + pointItemWidth, offSetY + pointItemWidth);
        mPoints[0][1] = new Point(offSetX + pointItemWidth * 2, offSetY + pointItemWidth);
        mPoints[0][2] = new Point(offSetX + pointItemWidth * 3, offSetY + pointItemWidth);

        mPoints[1][0] = new Point(offSetX + pointItemWidth, offSetY + pointItemWidth * 2);
        mPoints[1][1] = new Point(offSetX + pointItemWidth * 2, offSetY + pointItemWidth * 2);
        mPoints[1][2] = new Point(offSetX + pointItemWidth * 3, offSetY + pointItemWidth * 2);

        mPoints[2][0] = new Point(offSetX + pointItemWidth, offSetY + pointItemWidth * 3);
        mPoints[2][1] = new Point(offSetX + pointItemWidth * 2, offSetY + pointItemWidth * 3);
        mPoints[2][2] = new Point(offSetX + pointItemWidth * 3, offSetY + pointItemWidth * 3);
    }

    /**
     * 绘制所有的点
     * @param canvas
     */
    private void drawPoints(Canvas canvas){
        for (int i = 0; i < mPoints.length; i++){
            for (int j = 0; j < mPoints[i].length; j++){
                Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
                Point point = mPoints[i][j];
                // 不同状态绘制点
                switch (point.state){
                    case Point.STATE_NORMAL:
                        paint.setColor(mNormalColor);
                        canvas.drawCircle(point.x, point.y,mPointRadius,paint);
                        break;
                    case Point.STATE_PRESS:
                        paint.setColor(mPressColor);
                        canvas.drawCircle(point.x , point.y ,mPointRadius,paint);
                        break;
                    case Point.STATE_ERROR:
                        paint.setColor(mErrorColor);
                        canvas.drawCircle(point.x, point.y,mPointRadius,paint);
                        break;
                }
            }
        }
    }

    /**
     * 绘制所有的线
     * @param canvas
     */
    private void drawLines(Canvas canvas){
        if (mSelectedPoints.size() > 0){
            // 从第一个被选中的点开始绘制
            Point a = mSelectedPoints.get(0);
            for (int i = 1; i < mSelectedPoints.size(); i++){
                Point b = mSelectedPoints.get(i);
                drawLine(canvas, a, b); // 连接两个点
                a = b; // 把下一个点作为下一次绘制的第一个点
            }
            if (isDraw){// 如果还在绘制状态，那就继续绘制连接线
                drawLine(canvas, a, new Point(mX, mY));
            }
        }
    }

    /**
     * 绘制两点之间的线
     * @param canvas
     * @param a
     * @param b
     */
    private void drawLine(Canvas canvas, Point a, Point b){
        if (a.state == Point.STATE_PRESS){
            canvas.drawLine(a.x, a.y, b.x, b.y, mPressPaint);
        }
        if (a.state == Point.STATE_ERROR){
            canvas.drawLine(a.x, a.y, b.x, b.y, mErrorPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = event.getX();
        mY = event.getY();
        int[] position;
        int i, j;
        if(mClickable){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    // 重置所有的点
                    resetPoints();
                    // 获取选择的点的位置
                    position = getSelectedPointPosition();
                    if (position != null){
                        isDraw = true; // 标记为绘制状态
                        i = position[0];
                        j = position[1];
                        mPoints[i][j].state = Point.STATE_PRESS;
                        // 被选择的点存入一个集合中
                        mSelectedPoints.add(mPoints[i][j]);
                        mPassPositions.add(i * 3 + j); // 把选中的点的路径转换成一位数组存储起来
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isDraw){
                        position = getSelectedPointPosition();
                        if (position != null){
                            i = position[0];
                            j = position[1];
                            if (!mSelectedPoints.contains(mPoints[i][j])){
                                mPoints[i][j].state = Point.STATE_PRESS;
                                mSelectedPoints.add(mPoints[i][j]);
                                mPassPositions.add(i * 3 + j);
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    boolean valid = false;
                    if (mListener != null && isDraw){
                        // 获取绘制路径是否正确
                        valid = mListener.onDrawFinished(mPassPositions);
                    }
                    if (!valid) {// 判断绘制路径不正确的所有被选中的点的状态改为出错
                        for (Point p : mSelectedPoints) {
                            p.state = Point.STATE_ERROR;
                        }
                        android.os.Handler handler = new android.os.Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!isDraw){
                                    resetPoints();
                                }
                            }
                        }, 1 * 1000);
                    }
                    isDraw = false;
                    break;
            }
            invalidate();
        }
        return true;
    }

    /**
     * 获取选择的点的位置
     * @return
     */
    private int[] getSelectedPointPosition(){
        Point point = new Point(mX, mY);
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                // 判断触摸的点和遍历的当前点的距离是否小于48dp
                if(mPoints[i][j].getInstance(point) <mPointRadius*9){
                    // 小于则获取作为被选中，并返回选中点的位置
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 重置所有的点
     */
    public void resetPoints(){
        mPassPositions.clear();
        mSelectedPoints.clear();
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                mPoints[i][j].state = Point.STATE_NORMAL;
            }
        }
        invalidate();
    }

    @Override
    public void setClickable(boolean clickable) {

        mClickable=clickable;
    }

    public interface OnDrawFinishedListener{
        boolean onDrawFinished(List<Integer> passPositions);
    }

    /**
     * 设置绘制完成监听接口
     * @param listener
     */
    public void setOnDrawFinishedListener(OnDrawFinishedListener listener){
        this.mListener = listener;
    }
}