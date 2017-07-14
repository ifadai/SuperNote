package app.fadai.supernote.module.notes.Image;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/10
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IImagePresenter {

    void getIntentData(Activity activity);

    File getImageFile(Activity activity);

    void saveImage(Activity activity);

    void deleteImage(Activity activity);

    void onRequestPermissionResult(Activity activity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
