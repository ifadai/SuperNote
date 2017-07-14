package app.fadai.supernote.module.notes.main;

import android.content.Intent;
import android.view.MenuItem;

import app.fadai.supernote.adapter.RvNoteListAdapter;
import app.fadai.supernote.bean.NoteFolder;
import app.fadai.supernote.module.notes.folderList.IFolderListPresenter;

import java.util.List;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/01
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface INoteMainPresenter {

    void setFolderPresenter(IFolderListPresenter presenter);

    void setAdapter(RvNoteListAdapter adapter);

    /**
     * 首次进入应用，初始化数据库
     *
     * @describe
     */
    void initDataBase();

    /**
     *  开始
     *  @describe
     */
    void start();

    /**
     *  显示便签
     *  @describe
     */
    void showNormalNote(String title,int folderId);

    /**
     *  显示全部便签
     *  @describe
     */
    void showAllNote();

    /**
     *  显示私密便签
     *  @describe
     */
    void showPrivacyNote(boolean isShow);

    /**
     *  显示废纸篓便签
     *  @describe
     */
    void showRecycleBinNote();

    /**
     * 添加便签
     *
     * @describe
     */
    void addNote(String noteId,String content,long modifiedTime);

    /**
     *  更新便签
     *  @describe
     */
    void updateNote(int position,String content,long modifiedTime);

    /**
     *  添加私密
     *  @describe
     */
    void putNoteToPrivacy();

    /**
     * 删除便签
     *
     * @describe
     */
    void deleteNotes();

    /**
     * 移动便签
     *
     * @describe
     */
    void moveNotes();

    /**
     *  恢复便签
     *  @describe
     */
    void recoverNote(int position);

    /**
     *  移动便签到Folder
     *  @describe
     */
    void moveNotesToFolder(NoteFolder noteFolder);

    /**
     *  获取便签夹List
     *  @describe
     */
    List<NoteFolder> getFolderDataList();

    /**
     *  如果当前便签已选中，则取消选中，反之亦然
     */
    void choiceNote(int position);

    /**
     * 获取并显示已选中的便签的数量
     *
     * @describe
     */
    void showSelectedNoteCount();

    /**
     * 获取并显示当前便签夹名称
     *
     * @describe
     */
    void showCurrentFolderName();

    /**
     *  便签列表点击事件
     *  @describe
     */
    void onNoteRvClick(int position);

    /**
     *  初始化便签Rv的布局
     *  @describe
     */
    void initNoteRvLayoutManager();

    /**
     *  初始化显示模式的菜单icon
     *  @describe
     */
    void initShowModeMenuIcon(MenuItem item);

    /**
     * 修改便签RecyclerView的layoutManager,并修改显示模式的菜单图标
     *
     * @describe
     */
    void changeNoteRvLayoutManagerAndMenuIcon(MenuItem item);

    /**
     * 执行多选操作
     *
     * @describe
     */
    void doMultiSelectActionAndChoiceThisItem(int position);

    /**
     * 取消多选操作
     *
     * @describe
     */
    void cancelMultiSelectAction();

    /**
     * 是否进入了多选操作中
     *
     * @describe
     */
    boolean isShowMultiSelectAction();


    /**
     * 执行全选操作
     *
     * @describe
     */
    void doChoiceAllNote();

    /**
     *  进入搜索
     *  @describe
     */
    void setInSearch();

    /**
     *  退出搜索
     *  @describe
     */
    void setOutSearch();

    /**
     *  ActivityResult
     *  @describe
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    void logCheckList();

}
