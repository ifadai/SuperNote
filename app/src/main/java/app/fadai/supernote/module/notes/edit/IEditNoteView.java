package app.fadai.supernote.module.notes.edit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import app.fadai.supernote.bean.ImageEntity;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/30
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IEditNoteView {

    /**
     *  获取Intent
     *  @describe
     */
    Intent getActivityIntent();

    Activity getActivity();

    void setTitle(String title);

    void showNoteContent(String content);

    void replaceImage(String imageName, Bitmap bitmap);

    void insertImage(String imageName, Bitmap bitmap);

    /**
     *   删除图片
     */
    void deleteImage(ImageEntity imageEntity);

    void setResultAndFinish(@Nullable Intent intent);

    void showLoading(String message);

    void unShowLoading();

    /**
     *   显示去往app设置页的dialog
     */
    void showToAppSettingDialog();

    /**
     *   显示统计Dialog
     */
    void showStatisticsDialog(int imageCount,int textCount);

    /**
     *   分享前 进行对EditText的调整：
     *   以图片形式分享便签时，因需要将EditText转为Bitmap，
     *   因此需要调整其大小，应：关闭键盘、不可便签（去除光标），设置minHeight为0
     */
    void setEditTextBeforeGetBitmap();

    /**
     *   分享后 调整回来
     */
    void setEditTextAfterGetBitmap();
}
