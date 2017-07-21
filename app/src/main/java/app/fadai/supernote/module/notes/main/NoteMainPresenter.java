package app.fadai.supernote.module.notes.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.fadai.supernote.MainApplication;
import app.fadai.supernote.adapter.RvNoteListAdapter;
import app.fadai.supernote.bean.Note;
import app.fadai.supernote.bean.NoteFolder;
import app.fadai.supernote.constants.CacheManager;
import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.constants.FolderListConstans;
import app.fadai.supernote.constants.NoteListConstans;
import app.fadai.supernote.model.INoteModel;
import app.fadai.supernote.model.LoadDataCallBack;
import app.fadai.supernote.model.NoteModel;
import app.fadai.supernote.module.base.BasePresenter;
import app.fadai.supernote.module.notes.folderList.IFolderListPresenter;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/01
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class NoteMainPresenter extends BasePresenter<INoteMainView> implements INoteMainPresenter {

    // Note Model
    private INoteModel mINoteModel = new NoteModel();

    private IFolderListPresenter mFolderListPresenter;

    private RvNoteListAdapter mAdapter;

    @Override
    public void setFolderPresenter(IFolderListPresenter presenter) {
        mFolderListPresenter = presenter;
    }

    @Override
    public void setAdapter(RvNoteListAdapter adapter) {
        this.mAdapter = adapter;

    }

    @Override

    public void initDataBase() {
        if (Constans.isFirst) {
            int folderId = mFolderListPresenter.initDataBase();
            mINoteModel.initNote(folderId);
            CacheManager.setAndSaveIsFirst(false);
        }
    }

    @Override
    public void start() {
        mFolderListPresenter.getFolders();
        mFolderListPresenter.choiceFolder(Constans.currentFolder, true);
    }

    @Override
    public void showNormalNote(String title, int folderId) {
        mINoteModel.loadNormalNoteList(folderId, new LoadDataCallBack() {
            @Override
            protected void onSuccedd(List list) {
                mAdapter.setNewData(list);
            }
        });
        mView.hideDrawer();
        showFolderNameFotTitle(title);
        mView.showAddFab();
    }

    @Override
    public void showAllNote() {
        mINoteModel.loadAllNoteList(new LoadDataCallBack() {
            @Override
            protected void onSuccedd(List list) {
                mAdapter.setNewData(list);
            }
        });
        mView.hideDrawer();
        showFolderNameFotTitle("全部");
        mView.showAddFab();
    }

    @Override
    public void showPrivacyNote(boolean isShow) {
        if (isShow) {
            mINoteModel.loadPrivacyNoteList(new LoadDataCallBack() {
                @Override
                protected void onSuccedd(List list) {
                    mAdapter.setNewData(list);
                }
            });
            showFolderNameFotTitle("私密");
            mFolderListPresenter.choicePrivacy();
            mView.showAddFab();
        } else {
            mView.toLockActivity();
        }
        mView.hideDrawer();
    }

    @Override
    public void showRecycleBinNote() {
        mINoteModel.loadRecycleBinNoteList(new LoadDataCallBack() {
            @Override
            protected void onSuccedd(List list) {
                mAdapter.setNewData(list);
            }
        });
        mView.hideDrawer();
        showFolderNameFotTitle("废纸篓");
        mView.hideAddFab();
    }

    private void showFolderNameFotTitle(String title) {
        mView.showCurrentNoteFolderName(title);
        NoteListConstans.selectedFolderName = title;
    }

    @Override
    public void addNote(String noteId, String content, long modifiedTime) {
        Note note = new Note();
        note.setNoteId(noteId);
        note.setNoteContent(content);
        note.setCreatedTime(modifiedTime);
        note.setModifiedTime(modifiedTime);
        mINoteModel.addNote(note);                 // 保存便签
        mFolderListPresenter.addNote2Folder(note); // 添加便签到便签夹
        mAdapter.addData(note);
        mView.setRvScrollToFirst();
    }

    @Override
    public void updateNote(int position, String content, long modifiedTime) {
        Note note = mAdapter.getData().get(position);

        if (TextUtils.isEmpty(content)) {
            removeNote(position);
            deleteNote(note, true);
            refreshRv();
        } else {
            note.setNoteContent(content);
            note.setModifiedTime(modifiedTime);
            note.save();
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void putNoteToPrivacy() {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                // 如果当前便签夹是私密便签夹,则执行移除私密操作，否则，执行添加私密操作
                if (Constans.currentFolder == FolderListConstans.ITEM_PRIMARY) {
                    mView.showLoading("移除中...");
                } else {
                    mView.showLoading("添加中...");
                }
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        toPrivacy(i);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                refreshRv();
                cancelMultiSelectAction();
            }
        }.execute();

    }

    private void toPrivacy(int position) {
        Note note = mAdapter.getData().get(position);
        if (mAdapter.mAllDataList != null) {
            mAdapter.mAllDataList.remove(note);
        }

        mAdapter.getData().remove(position);
        mAdapter.mCheckList.remove(position);

        // 如果当前便签夹是私密便签夹,则执行移除私密操作，否则，执行添加私密操作
        if (Constans.currentFolder == FolderListConstans.ITEM_PRIMARY) {
            note.setIsPrivacy(0);
            mFolderListPresenter.removePrivacyNote(note);
        } else {
            note.setIsPrivacy(1);
            mFolderListPresenter.removeNoteForFolder(note);
        }
        note.save();
    }

    @Override
    public void deleteNotes() {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("删除中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        Note note = mAdapter.getData().get(i);
                        removeNote(i);
                        deleteNote(note, false);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();

                refreshRv();
                cancelMultiSelectAction();
            }
        }.execute();

    }

    private void removeNote(int position) {
        if (mAdapter.mAllDataList != null) {  // 如果已进入搜索模式
            mAdapter.mAllDataList.remove(mAdapter.getData().get(position));
        }
        Note note = mAdapter.getData().get(position);
        mAdapter.getData().remove(position);    // 从Adapter的数据中删除
        mAdapter.mCheckList.remove(position);

        mFolderListPresenter.removeNoteForFolder(note);   // 从便签夹中移除
    }

    /**
     * @param isRealDelete 是否是永久删除（不指定的话，根据便签夹进行判断）
     * @describe
     */
    private void deleteNote(Note note, boolean isRealDelete) {

        // 删除操作
        if (isRealDelete || Constans.currentFolder == FolderListConstans.ITEM_RECYCLE) {       // 当前便签夹是废纸篓，则直接永久删除
            deleteFile(note.getNoteId());
            note.delete();
        } else {
            if (Constans.isUseRecycleBin) { // 已启用废纸篓
                toRecycleBin(note);
            } else {                       // 已关闭废纸篓
                note.delete();
                deleteFile(note.getNoteId());
            }
        }
    }

    private void deleteFile(String mNoteId) {
        File file = Utils.getContext().getExternalFilesDir(mNoteId);
        if (file.exists()) {
            FileUtils.deleteDir(file);
        }
    }

    private void toRecycleBin(Note note) {
        note.setInRecycleBin(1);
        note.save();
    }

    @Override
    public void moveNotes() {

        // 如果是废纸篓，则执行恢复操作
        if (Constans.currentFolder == FolderListConstans.ITEM_RECYCLE) {
            recoverNotes();
        } else {
            mView.showMoveBottomSheet();
        }
    }

    private void recoverNotes() {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("恢复中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        recoverNote(i);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                refreshRv();
                cancelMultiSelectAction();
            }
        }.execute();

    }

    @Override
    public void recoverNote(int position) {
        Note note = mAdapter.getData().get(position);
        if (mAdapter.mAllDataList != null) {
            mAdapter.mAllDataList.remove(note);
        }
        mAdapter.getData().remove(position);
        mAdapter.mCheckList.remove(position);
        mFolderListPresenter.recoverNote(note);
    }

    @Override
    public void moveNotesToFolder(final NoteFolder noteFolder) {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("移动中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        moveNote(i, noteFolder);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                int count = NoteListConstans.selectedCount;
                cancelMultiSelectAction();
                refreshRv();
                mView.showSnackbar("已将" + count + "条便签移动到" + noteFolder.getFolderName());
            }
        }.execute();

    }

    private void moveNote(int notePos, NoteFolder noteFolder) {
        Note note = mAdapter.getData().get(notePos);
        // 如果当前便签为全部便签，则不用从当前NoteRv中移除
        if (Constans.currentFolder == FolderListConstans.ITEM_ALL) {
            mFolderListPresenter.moveNoteToFolder(note, noteFolder);
        } else {
            if (noteFolder.getId() != mFolderListPresenter.getCurrentFolderId()) {
                if (mAdapter.mAllDataList != null) {
                    mAdapter.mAllDataList.remove(note);
                }
                mAdapter.getData().remove(notePos);
                mAdapter.mCheckList.remove(notePos);

                mFolderListPresenter.moveNoteToFolder(note, noteFolder);

            }
        }
    }

    @Override
    public List<NoteFolder> getFolderDataList() {
        return mFolderListPresenter.getFoldersForAdapter();
    }

    @Override
    public void choiceNote(int position) {
        boolean isChoice = mAdapter.mCheckList.get(position);
        if (isChoice) {
            setNoteSelectedCount(getNoteSelectedCount() - 1);
        } else {
            setNoteSelectedCount(getNoteSelectedCount() + 1);
        }
        mAdapter.mCheckList.set(position, !isChoice);
        mAdapter.notifyItemChanged(position);
        showSelectedNoteCount();
    }


    @Override
    public void showSelectedNoteCount() {
        mView.showChoiceNotesCount(NoteListConstans.selectedCount + "");
    }

    @Override
    public void showCurrentFolderName() {
        mView.showCurrentNoteFolderName(NoteListConstans.selectedFolderName);
    }

    @Override
    public void onNoteRvClick(int position) {
        if (isShowMultiSelectAction()) {
            choiceNote(position);
        } else {
            if (Constans.currentFolder == FolderListConstans.ITEM_RECYCLE) {
                mView.showNoteRecoverDialog(position);
            } else {
                mView.toEditNoteForEdit(mAdapter.getData().get(position), position);
            }
        }
    }

    @Override
    public void initNoteRvLayoutManager() {
        if (Constans.noteListShowMode == NoteListConstans.STYLE_LINEAR) {
            mView.changeNoteRvLayoutManager(new LinearLayoutManager(MainApplication.mContext));
        } else {
            mView.changeNoteRvLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void initShowModeMenuIcon(MenuItem item) {
        if (Constans.noteListShowMode == NoteListConstans.STYLE_LINEAR) {
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_GRID));
        } else {
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_LIST));
        }
    }


    @Override
    public void changeNoteRvLayoutManagerAndMenuIcon(MenuItem item) {

        if (Constans.noteListShowMode == NoteListConstans.STYLE_LINEAR) {
            CacheManager.setAndSaveNoteListShowMode(NoteListConstans.STYLE_GRID);
            mView.changeNoteRvLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_LIST));
        } else {
            CacheManager.setAndSaveNoteListShowMode(NoteListConstans.STYLE_LINEAR);
            mView.changeNoteRvLayoutManager(new LinearLayoutManager(MainApplication.mContext));
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_GRID));
        }
    }

    @Override
    public void doMultiSelectActionAndChoiceThisItem(int position) {
        if (!NoteListConstans.isShowMultiSelectAction) {
            NoteListConstans.isShowMultiSelectAction = true;
            // 更新toolbar菜单
            mView.updateOptionMenu();
            // 隐藏添加按钮
            mView.hideAddFab();
            // 显示BottomBar
            mView.showBottomBar();
            // 设置三个按钮的可使用性
            mView.setCheckMenuEnable();
            // 设置三个按钮
            setCheckMenuForFolderType();
            // 选中当前便签
            choiceNote(position);
            // 显示已选中的数量
            mView.showChoiceNotesCount(NoteListConstans.selectedCount + "");
            // 刷新便签RecyclerView
            refreshRv();
        }
    }


    private void setCheckMenuForFolderType() {
        switch (Constans.currentFolder) {
            case FolderListConstans.ITEM_PRIMARY:
                mView.setCheckMenuForPrivacy();
                break;
            case FolderListConstans.ITEM_RECYCLE:
                mView.setCheckMenuForRecycleBin();
                break;
            default:
                mView.setCheckMenuForAllAndNormal();
                break;
        }
    }

    @Override
    public void cancelMultiSelectAction() {
        if (NoteListConstans.isShowMultiSelectAction) {
            NoteListConstans.isShowMultiSelectAction = false;
            // 更新toolbar菜单
            mView.updateOptionMenu();
            // 隐藏BottomBar
            mView.hideBottomBar();
            // 将所有便签的选中状态设为false
            unChoiceAllNote();
            // 显示已选中的便签夹名称
            mView.showCurrentNoteFolderName(NoteListConstans.selectedFolderName);
            // 刷新便签RecyclerView
            refreshRv();
            // 显示添加按钮
            mView.showAddFab();
        }
    }

    @Override
    public boolean isShowMultiSelectAction() {
        return NoteListConstans.isShowMultiSelectAction;
    }

    @Override
    public void doChoiceAllNote() {
        if (NoteListConstans.isChoiceAll) {
            unChoiceAllNote();
        } else {
            choiceAllNote();
        }
    }

    @Override
    public void setInSearch() {
        mView.hideAddFab();
        NoteListConstans.isInSearch = true;
    }

    @Override
    public void setOutSearch() {
        mView.showAddFab();
        NoteListConstans.isInSearch = false;
    }

    /*---------------------------------------------------------------------------------------*/

    public void setFilter(String text) {
        if (mAdapter.mAllDataList == null) {
            mAdapter.mAllDataList = new ArrayList<Note>();
            mAdapter.mAllDataList.addAll(mAdapter.getData());
        }
        if (mAdapter.mAllCheckList == null) {
            mAdapter.mAllCheckList = new ArrayList<Boolean>();
            mAdapter.mAllCheckList.addAll(mAdapter.mCheckList);
        }

        mAdapter.getData().clear();
        mAdapter.mCheckList.clear();
//        转换为小写
        String lowerCaseQuery = text.toLowerCase();
        //　此处使用倒叙进行检索，这样搜索出来的顺序是正序
        for (int i = mAdapter.mAllDataList.size() - 1; i >= 0; i--) {
            if (mAdapter.mAllDataList.get(i).getNoteContent().toLowerCase().contains(lowerCaseQuery)) {
                mAdapter.addData(mAdapter.mAllDataList.get(i));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void cancelFilter() {
        NoteListConstans.isInSearch = false;
        if (mAdapter.mAllDataList != null || mAdapter.mAllCheckList != null) {
            mAdapter.getData().clear();
            mAdapter.mCheckList.clear();
            mAdapter.setNewData(mAdapter.mAllDataList);
            for (int i = 0; i < mAdapter.mAllDataList.size(); i++) {
                mAdapter.mCheckList.add(false);
            }
            mAdapter.mAllDataList = null;
            mAdapter.mAllCheckList = null;
            mAdapter.notifyDataSetChanged();
        }
    }

    public int getNoteSelectedCount() {
        return NoteListConstans.selectedCount;
    }

    public void setNoteSelectedCount(int count) {
        NoteListConstans.selectedCount = count;
        setChoiceAllState();
        setBottomMenuEnable();
    }

    private void setChoiceAllState() {
        if (NoteListConstans.selectedCount == mAdapter.getData().size()) {
            NoteListConstans.isChoiceAll = true;
        } else {
            NoteListConstans.isChoiceAll = false;
        }
    }


    private void setBottomMenuEnable() {
        if (NoteListConstans.selectedCount > 0) {
            mView.setCheckMenuEnable();
        } else {
            mView.setCheckMenuUnEnable();
        }
    }

    public void choiceAllNote() {
        NoteListConstans.isChoiceAll = true;
        for (int i = 0; i < mAdapter.mCheckList.size(); i++) {
            mAdapter.mCheckList.set(i, true);
        }
        setNoteSelectedCount(mAdapter.mCheckList.size());
        mAdapter.notifyDataSetChanged();

        showSelectedNoteCount();
    }

    public void unChoiceAllNote() {
        NoteListConstans.isChoiceAll = false;
        for (int i = 0; i < mAdapter.mCheckList.size(); i++) {
            mAdapter.mCheckList.set(i, false);
        }
        setNoteSelectedCount(0);
        mAdapter.notifyDataSetChanged();

        showSelectedNoteCount();
    }

    public void uncertainChoiceNote(int position) {
        boolean isChoice = mAdapter.mCheckList.get(position);
        if (isChoice) {
            setNoteSelectedCount(getNoteSelectedCount() - 1);
        } else {
            setNoteSelectedCount(getNoteSelectedCount() + 1);
        }
        mAdapter.mCheckList.set(position, !isChoice);
        mAdapter.notifyItemChanged(position);
    }

    public void refreshRv() {
        mAdapter.notifyDataSetChanged();
        mFolderListPresenter.refreshFolderList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        com.orhanobut.logger.Logger.d("requestCode=" + requestCode + "   resultCode=" + resultCode);
        switch (requestCode) {
            case NoteListConstans.REQUEST_CODE_LOCK:
                resultForLock(resultCode);
                break;
            case NoteListConstans.REQUEST_CODE_ADD:
                resultForAddNote(resultCode, data);
                break;
            case NoteListConstans.REQUEST_CODE_EDIT:
                resultForEditNote(resultCode, data);
                break;
            case NoteListConstans.REQUEST_CODE_EDIT_FOLDER:
                resultForEditFolder(resultCode, data);
                break;
        }
    }

    @Override
    public void logCheckList() {
        for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
            Logger.d("lllll" + mAdapter.mCheckList.get(i) + "   " + i);

        }
    }

    private void resultForLock(int resultCode) {
        if (resultCode == RESULT_OK) {
            showPrivacyNote(true);
        }
    }

    private void resultForAddNote(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String noteId = data.getStringExtra("note_id");
            String content = data.getStringExtra("note_content");
            long modifiedTime = data.getLongExtra("modified_time", 0);
            addNote(noteId, content, modifiedTime);
        }
    }

    private void resultForEditNote(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", 0);
            String content = data.getStringExtra("note_content");
            long modifiedTime = data.getLongExtra("modified_time", 0);
            updateNote(position, content, modifiedTime);
        }
    }

    private void resultForEditFolder(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            boolean isCurrentFolderDeleted = data.getBooleanExtra("is_current_folder_deleted", false);
            if (isCurrentFolderDeleted) {
                Constans.currentFolder = FolderListConstans.ITEM_ALL;
            }
            start();
        }
    }
}
