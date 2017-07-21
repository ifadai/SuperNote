package app.fadai.supernote.module.notes.folder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.fadai.supernote.R;

import java.util.ArrayList;
import java.util.List;

import app.fadai.supernote.adapter.RvEditFolderAdapter;
import app.fadai.supernote.bean.NoteFolder;
import app.fadai.supernote.constants.EditFolderConstans;
import app.fadai.supernote.model.INoteFolderModel;
import app.fadai.supernote.model.LoadDataCallBack;
import app.fadai.supernote.model.NoteFolderModel;
import app.fadai.supernote.module.base.BasePresenter;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/04
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class FolderPresenter extends BasePresenter<IFolderView> implements IFolderPresenter {

    // 当前已选中的便签夹
    private int mCurrentFolderId;
    // 当前已选中的便签夹是否已被删除
    private boolean mIsCurrentFolderDeleted=false;
    // 是否对便签夹做了修改
    private int mResultCode= Activity.RESULT_CANCELED;

    private RvEditFolderAdapter mAdapter;

    private INoteFolderModel<NoteFolder> mNoteFolderModel = new NoteFolderModel();

    @Override
    public void setAdapter(RvEditFolderAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void getIntentData(Intent intent) {
        mCurrentFolderId=intent.getIntExtra("current_folder_id",0);
    }

    @Override
    public void getData() {
        mNoteFolderModel.loadNoteFoldersList(new LoadDataCallBack<NoteFolder>() {
            @Override
            protected void onSuccedd(List<NoteFolder> list) {
                mAdapter.setNewData(list);
            }
        });
    }

    @Override
    public void choiceItem(int position) {
        boolean isChecked = mAdapter.mCheckList.get(position);
        if (isChecked) {
            EditFolderConstans.selectedCount--;
        } else {
            EditFolderConstans.selectedCount++;
        }
        mAdapter.mCheckList.set(position, !isChecked);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void editFolder(int position) {
        mResultCode=Activity.RESULT_OK;
        if (EditFolderConstans.selectedItem == position) {    // 如果当前item正在编辑
            saveCurrentItem(position);
        } else if(EditFolderConstans.selectedItem!=-1){       // 已有其他Item正在被编辑
            cancelEditItem();
            editItem(position);
        } else{                                              // 无任何item正在被编辑
            editItem(position);
        }
    }

    private void saveCurrentItem(int position) {
        EditText et = (EditText) mAdapter.getViewByPosition(position, R.id.et_edit_folder_name);
        String newName = et.getText().toString();
        verifyName(position,newName);
    }

    private void verifyName( int position,String newName) {

        // 新名字为空
        if (newName.isEmpty()){
            setEditError(position,"便签夹名为空");
            return ;
        }

        // 名字重复
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            if (i != position && mAdapter.getData().get(i).getFolderName().equals(newName)) {
                setEditError(position,"已存在");
                return;
            }
        }

        saveNewNameToFolder(position,newName);
    }

    private void setEditError(int position, String errorTip) {
        TextInputLayout textInputLayout=(TextInputLayout)mAdapter.getViewByPosition(position,R.id.textinput_edit_folder_name);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorTip);
    }

    private void saveNewNameToFolder(int position,String newName){
        NoteFolder folder = mAdapter.getData().get(position);
        folder.setFolderName(newName);
        folder.save();

        EditFolderConstans.selectedItem=-1;
        mAdapter.notifyItemChanged(position);
        closeKeyboard(mAdapter.getViewByPosition(position,R.id.et_edit_folder_name));

        mView.showAddBtn();
    }

    //　关闭键盘
    public void closeKeyboard(View view){
        InputMethodManager manager=(InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(manager.isActive()){
            manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void cancelEditItem(){
        int i=EditFolderConstans.selectedItem;
        EditFolderConstans.selectedItem=-1;
        mAdapter.notifyItemChanged(i);
    }


    private void editItem(int position){
        EditFolderConstans.selectedItem=position;
        mAdapter.notifyItemChanged(position);

        EditText editText=(EditText)mAdapter.getViewByPosition(position,R.id.et_edit_folder_name);
        editText.selectAll();
        setFoucus(editText);
        mView.hideAddBtn();
    }

    //    获取焦点并弹出键盘
    public void setFoucus(View view){
//        获取 接受焦点的资格
        view.setFocusable(true);
//        获取 焦点可以响应点触的资格
        view.setFocusableInTouchMode(true);
//        请求焦点
        view.requestFocus();
//        弹出键盘
        InputMethodManager manager=(InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0,0);
        manager.showSoftInput(view,0);
    }

    @Override
    public void addFolder() {
        mResultCode=Activity.RESULT_OK;
        mView.hideAddBtn();
        NoteFolder folder=new NoteFolder();
        // 已有item处于编辑状态，则让其恢复为正常状态
        if(EditFolderConstans.selectedItem!=-1){
            cancelEditItem();
        }

        String newName=getNewFolderName();
        folder.setFolderName(newName);
        folder.save();
        mAdapter.addData(folder);
        mView.scrollToItem(mAdapter.getData().size()-1);

        // 新建便签夹的弹出键盘在Adapter中设置（因为RecyclerView的scrollToItem需要一定的时间）
        EditFolderConstans.isNewFolder=true;
        EditFolderConstans.selectedItem=mAdapter.getData().size()-1;
    }

    @Override
    public void judgeToDelete() {
        if(EditFolderConstans.selectedCount>0){
            mView.showDeleteDialog();
        } else {
            mView.showSnackbar();
        }
    }

    @Override
    public void deleteMoreFolders() {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mResultCode=Activity.RESULT_OK;
                mView.showLoading("删除中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                // 当前正在编辑的便签夹是否被删除
                boolean isSelectedDeleted=false;
                for(int i=mAdapter.mCheckList.size()-1;i>=0;i--){
                    if(mAdapter.mCheckList.get(i)){
                        if(i==EditFolderConstans.selectedItem){
                            isSelectedDeleted=true;
                        }
                        deleteFolder(i);
                    }
                }
                return isSelectedDeleted;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                EditFolderConstans.selectedCount=0;

                // 判断当前编辑的便签是否被删除
                if(aBoolean){
                    mView.showAddBtn();
                    EditFolderConstans.selectedItem=-1;
                }
                mAdapter.notifyDataSetChanged();
            }
        }.execute();

    }

    @Override
    public void setMenuAlpha(MenuItem menuItem) {
        if(EditFolderConstans.selectedCount>0){
            menuItem.getIcon().setAlpha(255);
        } else {
            menuItem.getIcon().setAlpha(85);
        }
    }

    private void deleteFolder(int position){
        NoteFolder folder=mAdapter.getData().get(position);

        // 判断删除的是否是当前主页已选中的便签夹
        if(folder.getId()==mCurrentFolderId)
            mIsCurrentFolderDeleted=true;

        mAdapter.getData().remove(position);
        mAdapter.mCheckList.remove(position);
        mNoteFolderModel.deleteNoteFolder(folder);
    }

    @Override
    public void setResult() {
        initConstans();

        Intent intent=new Intent();
        intent.putExtra("is_current_folder_deleted",mIsCurrentFolderDeleted);
        mView.setActivityResultAndFinish(mResultCode,intent);
    }

    private void initConstans(){
        EditFolderConstans.selectedCount=0;
        EditFolderConstans.selectedItem=-1;
        EditFolderConstans.isNewFolder=false;
    }

    private String getNewFolderName(){

        List<NoteFolder> list=new ArrayList<>();

        // 找出所有包含新建便签夹的名字
        for(int i=0;i<mAdapter.getData().size();i++){
            NoteFolder folder=mAdapter.getData().get(i);
            if(folder.getFolderName().contains("新建便签夹")){
                list.add(folder);
            }
        }

        // 从新建便签夹1 -> 新建便签夹2 依次尝试
        int n=1;
        while (true){
            String newName;
            if(n==1){
                newName="新建便签夹";
            } else {
                newName="新建便签夹"+n;
            }
            int i;
            for(i=0;i<list.size();i++){
                if(list.get(i).getFolderName().equals(newName)){
                    break;
                }
            }
//            没有包含新建便签夹的项 或者 已有的项都与newName不一样
            if(list.size()==0 || i==list.size()){
                return newName;
            }
            n++;
        }
    }
}
