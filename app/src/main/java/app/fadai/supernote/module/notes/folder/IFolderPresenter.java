package app.fadai.supernote.module.notes.folder;

import android.content.Intent;
import android.view.MenuItem;

import app.fadai.supernote.adapter.RvEditFolderAdapter;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/04
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IFolderPresenter {

    void setAdapter(RvEditFolderAdapter adapter);

    void getIntentData(Intent intent);

    void getData();

    void choiceItem(int position);

    void editFolder(int position);

    void addFolder();

    void judgeToDelete();

    void deleteMoreFolders();

    void setMenuAlpha(MenuItem menuItem);

    void setResult();

}
