package app.fadai.supernote.module.notes.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;

import app.fadai.supernote.widget.MyEditText;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/30
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IEditNotePresenter {

    /**
     *  初始化数据
     *  @describe
     */
    void initData();

    /**
     *   设置内容
     */
    void setTextForNoteContent();
    
    /**
     *   解析便签内容（解析图片）
     */
    void parseNoteContent();

    /**
     *   检测权限并前往相机
     */
    void checkPermissionAndToCamera(Context context);
    
    /**
     *   检测权限并前往图库
     */
    void checkPermissionAndToPhoto(Context context);

    /**
     *   前往相机
     */
    void toCamera(Activity activity);

    /**
     *   前往图库
     */
    void toPhoto(Activity activity);

    /**
     *   获取需要的图片高度
     */
    int getRequestImeHeight(BitmapFactory.Options option);
    
    /**
     *   Activity返回
     */
    void onActivityResult(int requestCode, int resultCode, Intent intent);

    /**
     *   请求权限返回
     */
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    /**
     *   保存便签
     */
    void saveNote(String content);

    /**
     *  统计字数和图片数量
     *  @describe
     */
    void calculateContentAndImageCount(MyEditText myEditText);

    /**
     *   获取便签Edit需要的高度
     */
    int getNoteEditNeedHeight();

    /**
     *   点击便签EditText
     */
    void clickNoteEditText(MyEditText editText);

    /**
     *   分享便签 文字
     */
    void shareNoteWithText();

    /**
     *   分享便签 图片
     */
    void shareNoteWithImage(View view);
    
    // 关闭键盘
    void closeKeyboard(View view);
    
    /**
     *   获取便签分享的图片
     */
    Bitmap getNoteShareBitmap(View view);
}
