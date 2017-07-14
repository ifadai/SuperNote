package app.fadai.supernote.module.notes.share;

import android.app.Activity;
import android.net.Uri;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/12
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IShareView {

    Activity getActivity();

    /**
     *   显示分享Dialog
     */
    void showShareDialog(Uri uri);
    
    /**
     *   前往应用设置Dialog
     */
    void showToAppSettingDialog();

    void showLoadingDialog(String message);

    void unShowLoadingDialog();
}
