package app.fadai.supernote.module.notes.folder;

import android.content.Intent;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/04
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IFolderView {

    void hideAddBtn();

    void showAddBtn();

    void showSnackbar();

    void showDeleteDialog();

    void showLoading(String message);

    void unShowLoading();

    void scrollToItem(int position);

    void setActivityResultAndFinish(int resultCode,Intent intent);
}
