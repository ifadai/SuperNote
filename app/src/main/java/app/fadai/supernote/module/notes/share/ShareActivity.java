package app.fadai.supernote.module.notes.share;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.app.fadai.supernote.R;
import com.github.chrisbanes.photoview.PhotoView;

import app.fadai.supernote.constants.EditNoteConstans;
import app.fadai.supernote.module.base.BaseActivity;
import app.fadai.supernote.utils.PermissionUtils;
import app.fadai.supernote.utils.ProgressDialogUtils;
import butterknife.BindView;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/12
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class ShareActivity extends BaseActivity<IShareView,SharePresenter> implements IShareView {

    @BindView(R.id.iv_share_preview)
    PhotoView mIv;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_share;
    }

    @Override
    protected SharePresenter initPresenter() {
        SharePresenter presenter=new SharePresenter();
        presenter.attch(this);
        return presenter;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("预览");
        mIv.setImageBitmap(mPresenter.getBitmap());
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_share_send:
                mPresenter.sendBitmap();
                break;
            case R.id.menu_share_save:
                mPresenter.saveImage();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showShareDialog(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);//设置分享行为
        intent.setType("image/*");//设置分享内容的类型
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent = Intent.createChooser(intent, "分享");
        startActivity(intent);
    }

    @Override
    public void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("权限设置")
                .setMessage("您已禁止应用的储存权限，请前往应用设置中开启")
                .setPositiveButton("前往",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.toAppSetting(mContext);
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }

    private ProgressDialogUtils mProgressDialog=new ProgressDialogUtils(this);
    @Override
    public void showLoadingDialog(String message) {
        mProgressDialog.show(message);
    }

    @Override
    public void unShowLoadingDialog() {
        mProgressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        EditNoteConstans.shareBitmap.recycle();
        EditNoteConstans.shareBitmap=null;
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
