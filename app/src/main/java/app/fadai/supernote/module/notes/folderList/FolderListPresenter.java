package app.fadai.supernote.module.notes.folderList;

import app.fadai.supernote.adapter.RvNoteFolderAdapter;
import app.fadai.supernote.bean.Note;
import app.fadai.supernote.bean.NoteFolder;
import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.constants.FolderListConstans;
import app.fadai.supernote.constants.CacheManager;
import app.fadai.supernote.model.INoteFolderModel;
import app.fadai.supernote.model.LoadDataCallBack;
import app.fadai.supernote.model.NoteFolderModel;
import app.fadai.supernote.module.base.BasePresenter;
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

public class FolderListPresenter extends BasePresenter<IFolderListView> implements IFolderListPresenter {

    private INoteFolderModel<NoteFolder> mINoteFolderModel =new NoteFolderModel();

    private INoteMainPresenter mNoteMainPresenter;

    private RvNoteFolderAdapter mAdapter;


    @Override
    public void setActivityPresenter(INoteMainPresenter presenter) {
        mNoteMainPresenter=presenter;
        mNoteMainPresenter.setFolderPresenter(this);
    }

    @Override
    public void setAdapter(RvNoteFolderAdapter adapter) {
        this.mAdapter=adapter;
    }

    @Override
    public int initDataBase() {
        int folderId= mINoteFolderModel.initNoteFolderAndGetFolderId();
        return folderId;
    }

    @Override
    public void start() {
        mNoteMainPresenter.start();
    }

    @Override
    public void getFolders() {
        mINoteFolderModel.loadNoteFoldersList(new LoadDataCallBack<NoteFolder>() {
            @Override
            protected void onSuccedd(List<NoteFolder> list) {
                mAdapter.setNewData(list);
                setAllNoteCount(list);
            }
        });
    }

    @Override
    public List<NoteFolder> getFoldersForAdapter() {
        return mAdapter.getData();
    }

    @Override
    public int getRealItemForAdapter(int position) {
        return position+mAdapter.getHeaderLayoutCount();
    }

    private void setAllNoteCount(List<NoteFolder> list){
        FolderListConstans.noteFolderCount=0;
        for(int i=0;i<list.size();i++){
            FolderListConstans.noteFolderCount+=list.get(i).getNoteCount();
        }
        mView.setAllNoteCount(FolderListConstans.noteFolderCount);
    }

    @Override
    public void removeNoteForFolder(Note note) {
        switch (Constans.currentFolder){
            case FolderListConstans.ITEM_ALL:
                removeNoteForAllNoteAndNormal(note);
                break;
            case FolderListConstans.ITEM_PRIMARY:
                // 删除私密便签，不需要执行移除操作。
                break;
            case FolderListConstans.ITEM_RECYCLE:
                // 删除废纸篓便签，不需要执行移除操作。
                break;
            default:
                removeNoteForAllNoteAndNormal(note);
                break;
        }
    }

    private void removeNoteForAllNoteAndNormal(Note note){

        for (int i = 0; i < mAdapter.getData().size(); i++) {
            NoteFolder folder1 = mAdapter.getData().get(i);
            if (folder1.getId() == note.getNoteFolderId()) {
                folder1.setNoteCount(folder1.getNoteCount() - 1);
                folder1.save();
//                        全部 item
                FolderListConstans.noteFolderCount--;
                break;
            }
        }
    }

    @Override
    public void removePrivacyNote(Note note) {
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            NoteFolder folder = mAdapter.getData().get(i);
            if (folder.getId() == note.getNoteFolderId()) {
                folder.setNoteCount(folder.getNoteCount() + 1);
                folder.save();

                FolderListConstans.noteFolderCount++;
                break;
            }
        }
    }

    @Override
    public void moveNoteToFolder(Note note, NoteFolder noteFolder) {
        //  要移动到的便签夹如果不是当前便签夹的话，才能移除便签。
        if(note.getNoteFolderId()!=noteFolder.getId()){
            removeNoteForFolder(note);
            addNoteToNewFolder(note,noteFolder);
        }
    }

    @Override
    public void recoverNote(Note note) {
        mNoteMainPresenter.logCheckList();

        //        inRecycleBin 设为0
        note.setInRecycleBin(0);
        note.save();
        // 私密便签原有便签夹不加一
        if(note.getIsPrivacy()==1)
            return;
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            NoteFolder folder = mAdapter.getData().get(i);
            if (folder.getId() == note.getNoteFolderId()) {

                folder.setNoteCount(folder.getNoteCount() + 1);
                folder.save();

                FolderListConstans.noteFolderCount++;
                break;
            }
        }

    }

    private void addNoteToNewFolder(Note note, NoteFolder noteFolder){
        noteFolder.setNoteCount(noteFolder.getNoteCount()+1);
        noteFolder.save();

        note.setNoteFolderId(noteFolder.getId());
        note.save();

        FolderListConstans.noteFolderCount++;
    }

    @Override
    public void choiceFolder(int pos,boolean isInit) {
        if (!isInit && pos == Constans.currentFolder) {
            return;
        }

        // 检查当前Folder是否存在
        pos=checkCurrentFolderIsExist(pos);

        switch (pos) {
            case FolderListConstans.ITEM_ALL:
                unChoiceFolder(Constans.currentFolder);
                CacheManager.setAndSaveCurrentFolder(pos);
                mView.choiceItemAll();
                mNoteMainPresenter.showAllNote();
                break;
            case FolderListConstans.ITEM_PRIMARY:
                // 私密便签等密码正确再选中便签夹
                mNoteMainPresenter.showPrivacyNote(false);
                // 私密便签不保存状态
                break;
            case FolderListConstans.ITEM_RECYCLE:
                unChoiceFolder(Constans.currentFolder);
                Constans.currentFolder = pos;
                mView.choiceItemRecycleBin();
                mNoteMainPresenter.showRecycleBinNote();
                // 私密便签不保存状态
                break;
            default:
                List<NoteFolder> data=mAdapter.getData();
                if (data != null){
                    unChoiceFolder(Constans.currentFolder);
                    CacheManager.setAndSaveCurrentFolder(pos);
                    mAdapter.notifyItemChanged(mAdapter.getHeaderLayoutCount()+pos);
                    mNoteMainPresenter.showNormalNote(data.get(pos).getFolderName(), data.get(pos).getId());
                }
                break;
        }
    }

    /**
     *    检查当前Folder是否存在 如果不存在，则修改为全部便签
     */
    private int checkCurrentFolderIsExist(int pos){
        switch (pos) {
            case FolderListConstans.ITEM_ALL:
                return pos;
            case FolderListConstans.ITEM_PRIMARY:
                return pos;
            case FolderListConstans.ITEM_RECYCLE:
                return pos;
            default:
                if(pos>=mAdapter.getData().size()){
                    return FolderListConstans.ITEM_ALL;
                } else
                    return pos;
        }
    }

    @Override
    public void choicePrivacy() {
        unChoiceFolder(Constans.currentFolder);
        Constans.currentFolder=FolderListConstans.ITEM_PRIMARY;
        mView.choiceItemPrimary();
    }

    public void unChoiceFolder(int pos) {
        switch (pos) {
            case FolderListConstans.ITEM_ALL:
                mView.unChoiceItemAll();
                break;
            case FolderListConstans.ITEM_PRIMARY:
                mView.unChoiceItemPrimary();
                break;
            case FolderListConstans.ITEM_RECYCLE:
                mView.unChoiceItemRecycleBin();
                break;
            default:
                mAdapter.notifyItemChanged(mAdapter.getHeaderLayoutCount()+pos);
                break;
        }
    }


    @Override
    public void addNote2Folder(Note note) {
        switch (Constans.currentFolder){
            case FolderListConstans.ITEM_ALL:
                addNote2ALL(note);
                break;
            case FolderListConstans.ITEM_PRIMARY:
                addNote2Privacy(note);
                break;
            case FolderListConstans.ITEM_RECYCLE:
                // 废纸篓不做添加便签操作
                break;
            default:
                addNote2Normal(note);
                break;
        }
    }

    @Override
    public void deleteNoteFromFolder(Note note) {

    }

    @Override
    public int getCurrentFolderId() {
        switch (Constans.currentFolder){
            case FolderListConstans.ITEM_ALL:
                return Constans.currentFolder;
            case FolderListConstans.ITEM_PRIMARY:
                return Constans.currentFolder;
            case FolderListConstans.ITEM_RECYCLE:
                return Constans.currentFolder;
            default:
                return mAdapter.getData().get(Constans.currentFolder).getId();
        }
    }

    @Override
    public void refreshFolderList() {
        mAdapter.notifyDataSetChanged();
        mView.setAllNoteCount(FolderListConstans.noteFolderCount);
    }

    private void addNote2ALL(Note note){
        // 全部便签时，默认储存在随手记中
        NoteFolder folder=mAdapter.getData().get(0);
        mINoteFolderModel.addNote2Folder(note,folder);
        mView.setAllNoteCount(++FolderListConstans.noteFolderCount);
        mAdapter.notifyItemChanged(getRealItemForAdapter(0));
    }

    private void addNote2Privacy(Note note){
        // 私密便签时，默认储存在随手记中
        NoteFolder folder=mAdapter.getData().get(0);
        mINoteFolderModel.addNote2Privacy(note,folder);
    }

    private void addNote2Normal(Note note){
        NoteFolder folder=mAdapter.getData().get(Constans.currentFolder);
        mINoteFolderModel.addNote2Folder(note,folder);
        mView.setAllNoteCount(++FolderListConstans.noteFolderCount);
        mAdapter.notifyItemChanged(getRealItemForAdapter(Constans.currentFolder));
    }

}
