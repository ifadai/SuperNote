package app.fadai.supernote.module.notes.Image;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import app.fadai.supernote.module.base.BaseActivity;
import app.fadai.supernote.utils.PermissionUtils;
import app.fadai.supernote.utils.ProgressDialogUtils;
import butterknife.BindView;

/**
 * Created by admin on 2017/3/3 0003.
 */

public class ImageInfoActivity extends BaseActivity<IImageView, ImagePresenter> implements IImageView, View.OnClickListener {

    @BindView(R.id.iv_image)
    PhotoView mIv;

    @BindView(R.id.tv_image_down)
    TextView mBtnSave;

    @BindView(R.id.tv_image_del)
    TextView mBtnDel;

    @BindView(R.id.ll_image_bottom_bar)
    LinearLayout mLlBottomBar;

    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_image;
    }

    @Override
    protected ImagePresenter initPresenter() {
        ImagePresenter imagePresenter = new ImagePresenter();
        imagePresenter.attch(this);
        return imagePresenter;
    }

    @Override
    protected void initViews() {

        mPresenter.getIntentData(this);

        getSupportActionBar().setTitle("图片");
        mToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        mBtnSave.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);

        Glide.with(mContext)
                .load(mPresenter.getImageFile(this))
                .into(mIv);

        mIv.setOnClickListener(this);

    }

    @Override
    protected void updateViews() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_image_down:
                mPresenter.saveImage(this);
                break;
            case R.id.tv_image_del:
                showDeleteDialog();
                break;
            case R.id.iv_image:
                hideOrShowToolbarAndBottomBar();
                break;
        }
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("删除图片")
                .setMessage("确定删除该图片吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteImage(ImageInfoActivity.this);
                    }
                })
                .show();
    }

    private boolean mIsToolbarAndBottomBarHided = false;

    private void hideOrShowToolbarAndBottomBar() {
        if (mIsToolbarAndBottomBarHided) {
            showToolbarAndBottomBar();
        } else {
            hideToolbarAndBottomBar();
        }
    }

    private void showToolbarAndBottomBar() {
        mIsToolbarAndBottomBarHided = false;

        ObjectAnimator animator = ObjectAnimator.ofFloat(mAppBarLayout, "translationY", -SizeUtils.dp2px(56), 0);
        animator.setDuration(300);
        animator.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mLlBottomBar, "translationY", SizeUtils.dp2px(56), 0);
        animator1.setDuration(300);
        animator1.start();
    }

    private void hideToolbarAndBottomBar() {
        mIsToolbarAndBottomBarHided = true;

        ObjectAnimator animator = ObjectAnimator.ofFloat(mAppBarLayout, "translationY", 0, -SizeUtils.dp2px(56));
        animator.setDuration(300);
        animator.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mLlBottomBar, "translationY", 0, SizeUtils.dp2px(56));
        animator1.setDuration(300);
        animator1.start();
    }

    private ProgressDialogUtils mProgressDialog = new ProgressDialogUtils(this);

    @Override
    public void showLoading(String message) {
        mProgressDialog.show(message);
    }

    @Override
    public void unShowLoading() {
        mProgressDialog.hide();
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

    @Override
    public void setResultAndFinish() {
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
