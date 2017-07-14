package app.fadai.supernote.module.notes.folderList;

import app.fadai.supernote.adapter.RvNoteFolderAdapter;
import app.fadai.supernote.bean.Note;
import app.fadai.supernote.bean.NoteFolder;
import app.fadai.supernote.module.notes.main.INoteMainPresenter;

import java.util.List;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/19
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IFolderListPresenter {

    void setActivityPresenter(INoteMainPresenter presenter);

    void setAdapter(RvNoteFolderAdapter adapter);

    /**
     * 首次进入应用，初始化数据库
     *
     * @describe
     */
    int initDataBase();

     void start() ;
    /**
     * 获取便签夹
     *
     * @describe
     */
    void getFolders();

    /**
     *  获取Adapter的dataList
     *  @describe
     */
    List<NoteFolder> getFoldersForAdapter();

    /**
     *  获取便签夹Rv的真实item
     *  @describe
     */
    int getRealItemForAdapter(int position);

    /**
     *  从当前已选中的便签夹中移除便签
     *  @describe
     */
    void removeNoteForFolder(Note note);

    /**
     * 移除私密
     *  @describe
     */
    void removePrivacyNote(Note note);

    /**
     *  移动便签到便签夹
     *  @describe
     */
    void moveNoteToFolder(Note note,NoteFolder noteFolder);

    /**
     *  恢复便签
     *  @describe
     */
    void recoverNote(Note note);

    /**
     *  选择便签夹
     *  @param isInit  是否是初始化时调用（初始化时，不判断是否已被选中）
     */
    void choiceFolder(int pos,boolean isInit);

    /**
     *  选中私密
     *  @describe
     */
    void choicePrivacy();

    /**
     *  取消选中便签夹
     *  @describe
     */
    void unChoiceFolder(int pos);

    void addNote2Folder(Note note);

    void deleteNoteFromFolder(Note note);

    /**
     *  获取当前便签夹ID
     *  @describe
     */
    int getCurrentFolderId();
    
    /**
     *   刷新FolderRv
     */
    void refreshFolderList();
}
