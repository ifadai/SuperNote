package app.fadai.supernote.module.notes.main;

import android.support.v7.widget.RecyclerView;

import app.fadai.supernote.bean.Note;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/01
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface INoteMainView<N> {

    /**
     *  便签移动到第一个
     *  @describe
     */
    void setRvScrollToFirst();
    /**
     * 设置Toolbar的title
     *
     * @describe
     */
    void showChoiceNotesCount(String title);

    /**
     * 显示当前选中的便签夹名字
     *
     * @describe
     */
    void showCurrentNoteFolderName(String title);

    /**
     * 更新toolbar的菜单
     *
     * @describe
     */
    void updateOptionMenu();

    /**
     * fab滑出
     *
     * @describe
     */
    void setAddFabOut();

    /**
     * fab滑入
     *
     * @describe
     */
    void setAddFabIn();

    /**
     *  fab 显示
     *  @describe
     */
    void showAddFab();

    /**
     *  fab 隐藏
     *  @describe
     */
    void hideAddFab();

    /**
     *  隐藏侧滑栏
     *  @describe
     */
    void hideDrawer();

    /**
     * 隐藏BottomBar
     *
     * @describe
     */
    void hideBottomBar();

    /**
     * 显示bottomBar
     *
     * @describe
     */
    void showBottomBar();

    /**
     *  底部的多选菜单可使用
     *  @describe
     */
    void setCheckMenuEnable();

    /**
     *  底部的多选菜单不可使用
     *  @describe
     */
    void setCheckMenuUnEnable();

    /**
     *  底部菜单For全部和普通便签
     *  @describe
     */
    void setCheckMenuForAllAndNormal();

    /**
     *  底部菜单For私密
     *  @describe
     */
    void setCheckMenuForPrivacy();

    /**
     *  底部菜单For废纸篓
     *  @describe
     */
    void setCheckMenuForRecycleBin();

    /**
     *  显示移动便签的bottom sheet
     *  @describe
     */
    void showMoveBottomSheet();

    /**
     *  显示便签恢复dialog
     *  @describe
     */
    void showNoteRecoverDialog(int position);

    /**
     * 设置便签列表的布局管理器
     *
     * @describe
     */
    void changeNoteRvLayoutManager(RecyclerView.LayoutManager manager);

    /**
     *  前往私密密码解锁界面
     *  @describe
     */
    void toLockActivity();

    /**
     *  前往编辑便签界面 For 添加
     *  @describe
     */
    void toEditNoteForAdd();

    /**
     *  前往编辑便签界面 For 编辑
     *  @describe
     */
    void toEditNoteForEdit(Note note,int position);

    /**
     *   显示进度条
     */
    void showLoading(String message);

    /**
     *   取消显示进度条
     */
    void unShowLoading();

    void showSnackbar(String message);
}
