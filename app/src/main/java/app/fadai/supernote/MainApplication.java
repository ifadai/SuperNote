package app.fadai.supernote;

import android.content.Context;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.Utils;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import app.fadai.supernote.constants.CacheManager;
import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.constants.FolderListConstans;
import app.fadai.supernote.constants.NoteListConstans;
import app.fadai.supernote.utils.PreferencesUtil;
import cn.bmob.v3.Bmob;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/02
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class MainApplication extends LitePalApplication {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        init();
        initBmob();
        getCacheData();
        setUpdateForVersionCode1();
    }

    private void init(){
        Utils.init(getApplicationContext());
        Connector.getDatabase();
}

    private void initBmob(){
        Bmob.initialize(this,getResources().getString(R.string.bmob_app_id));
    }

    private void getCacheData(){
        Constans.isFirst= PreferencesUtil.getBoolean(Constans.IS_FIRST,true);
        Constans.currentFolder= PreferencesUtil.getInt(Constans.CURRENT_FOLDER, FolderListConstans.ITEM_ALL);
        Constans.noteListShowMode=PreferencesUtil.getInt(Constans.NOTE_LIST_SHOW_MODE, NoteListConstans.STYLE_GRID);
        Constans.theme=PreferencesUtil.getInt(Constans.THEME,Constans.theme);
        Constans.isUseRecycleBin=PreferencesUtil.getBoolean(Constans.IS_USE_RECYCLE,Constans.isUseRecycleBin);
        Constans.isLocked=PreferencesUtil.getBoolean(Constans.IS_LOCKED,Constans.isLocked);
        Constans.lockPassword=PreferencesUtil.getString(Constans.LOCK_PASSWORD,"");
    }

    // 为了兼容1.0.1版本，将其的缓存信息进行备份修改
    private void setUpdateForVersionCode1(){
        // 1.0.1版本使用的key值，如果是false，说明之前是V1.0.1版本
        boolean isFirst= PreferencesUtil.getBoolean("isFirst",true);
        if(!isFirst){
            boolean isGrid= PreferencesUtil.getBoolean("is_grid",false);
            boolean isUseRecycleBin=PreferencesUtil.getBoolean("recycle_bin",false);

            CacheManager.setAndSaveIsFirst(false);
            CacheManager.setAndSaveCurrentFolder(FolderListConstans.ITEM_ALL);
            CacheManager.setAndSaveIsUseRecycleBin(isUseRecycleBin);
            if(isGrid){
                CacheManager.setAndSaveNoteListShowMode(NoteListConstans.MODE_GRID);
            } else {
                CacheManager.setAndSaveNoteListShowMode(NoteListConstans.MODE_LIST);
            }
            // isLock、lockPassword key值一样；主题key值不用修改。

        }
    }
}
