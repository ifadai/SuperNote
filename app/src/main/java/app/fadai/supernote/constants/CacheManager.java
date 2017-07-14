package app.fadai.supernote.constants;

import app.fadai.supernote.utils.PreferencesUtil;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/11
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class CacheManager {

    /**
     *   修改并保存 是否是第一次进入的缓存
     */
    public static void setAndSaveIsFirst(boolean isFirst){
        Constans.isFirst=isFirst;
        PreferencesUtil.saveBoolean(Constans.IS_FIRST,isFirst);
    }

    /**
     *   修改并保存 当前便签夹
     */
    public static void setAndSaveCurrentFolder(int currentFolder){
        Constans.currentFolder=currentFolder;
        PreferencesUtil.saveInt(Constans.CURRENT_FOLDER,currentFolder);
    }

    /**
     *  便签列表显示模式
     */
    public static void setAndSaveNoteListShowMode(int showMode){
        Constans.noteListShowMode=showMode;
        PreferencesUtil.saveInt(Constans.NOTE_LIST_SHOW_MODE,showMode);
    }

    /**
     *   是否已启用废纸篓
     */
    public static void setAndSaveIsUseRecycleBin(boolean isUse){
        Constans.isUseRecycleBin=isUse;
        PreferencesUtil.saveBoolean(Constans.IS_USE_RECYCLE,isUse);
    }

    /**
     *   是否设置了私密密码
     */
    public static void setAndSaveIsLocked(boolean isLocked){
        Constans.isLocked=isLocked;
        PreferencesUtil.saveBoolean(Constans.IS_LOCKED,isLocked);
    }

    /**
     *   私密密码
     */
    public static void setAndSaveLockPassword(String lockPassword){
        Constans.lockPassword=lockPassword;
        PreferencesUtil.saveString(Constans.LOCK_PASSWORD,lockPassword);
    }
}
