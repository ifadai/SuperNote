package app.fadai.supernote.module.notes.Image;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/10
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IImageView {

    void showLoading(String message);

    void unShowLoading();

    void showToAppSettingDialog();

    void setResultAndFinish();

}
