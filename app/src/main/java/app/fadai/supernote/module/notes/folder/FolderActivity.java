package app.fadai.supernote.module.notes.folder;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import app.fadai.supernote.adapter.RvEditFolderAdapter;
import app.fadai.supernote.constants.EditFolderConstans;
import app.fadai.supernote.module.base.BaseActivity;
import app.fadai.supernote.utils.ThemeUtils;
import butterknife.BindView;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/04
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class FolderActivity extends BaseActivity<IFolderView,FolderPresenter> implements IFolderView, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, View.OnClickListener, View.OnTouchListener {

    @BindView(R.id.rv_folder_folder)
    RecyclerView mRvFolder;

    @BindView(R.id.fab_folder_add)
    FloatingActionButton mFabAdd;

    private float mScrollLastY;
    private float mTouchSlop;
    private ProgressDialog mProgressDialog;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_edit_folder;
    }

    @Override
    protected FolderPresenter initPresenter() {
        FolderPresenter presenter=new FolderPresenter();
        presenter.attch(this);
        return presenter;
    }

    @Override
    protected void initViews() {

        mTouchSlop=ViewConfiguration.get(this).getScaledTouchSlop();

        mPresenter.getIntentData(getIntent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("编辑便签夹");

        mRvFolder.setLayoutManager(new LinearLayoutManager(mContext));
        mRvFolder.setOnTouchListener(this);

        RvEditFolderAdapter adapter=new RvEditFolderAdapter();
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);
        adapter.bindToRecyclerView(mRvFolder);

        mPresenter.setAdapter(adapter);

        mFabAdd.setOnClickListener(this);
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.editFolder(position);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.choiceItem(position);
        // 更新菜单
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_folder_add:
                mPresenter.addFolder();
                break;
        }
    }

    @Override
    public void hideAddBtn() {
        ObjectAnimator animator= ObjectAnimator.ofFloat(mFabAdd,"translationY", SizeUtils.dp2px(80));
        animator.setDuration(150);
        animator.start();
    }

    @Override
    public void showAddBtn() {
        ObjectAnimator animator= ObjectAnimator.ofFloat(mFabAdd,"translationY", SizeUtils.dp2px(0));
        animator.setDuration(150);
        animator.start();
    }

    @Override
    public void showSnackbar() {
        SnackbarUtils.with(mRvFolder)
                .setMessage("请选择要删除的便签夹")
                .setBgColor( ThemeUtils.getColorPrimary(mContext))
                .show();
    }

    @Override
    public void scrollToItem(int position){
        mRvFolder.scrollToPosition(position);
    }

    @Override
    public void setActivityResultAndFinish(int resultCode,Intent intent) {
        setResult(resultCode,intent);
    }

    @Override
    public void onBackPressed() {
        mPresenter.setResult();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.menu_folder_delete);
        mPresenter.setMenuAlpha(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_folder_delete:
                mPresenter.judgeToDelete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDeleteDialog(){
        new AlertDialog.Builder(mContext)
                .setTitle("删除便签夹")
                .setMessage("确定删除选中的便签夹吗？")
                .setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteMoreFolders();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public void showLoading(String message) {
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    @Override
    public void unShowLoading() {
        if(mProgressDialog!=null)
            mProgressDialog.cancel();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float rawY=event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mScrollLastY=rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                if(EditFolderConstans.selectedItem==-1){
                    if((rawY-mScrollLastY)>mTouchSlop){    // 手指向下滑动
                        showAddBtn();
                    } else if((mScrollLastY-rawY)>mTouchSlop){  // 手指向上滑动
                        hideAddBtn();
                    }
                    mScrollLastY=rawY;
                }

                break;
        }
        return false;
    }
}
