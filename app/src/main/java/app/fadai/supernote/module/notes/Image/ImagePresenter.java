package app.fadai.supernote.module.notes.Image;

import android.Manifest;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.module.base.BasePresenter;
import app.fadai.supernote.utils.PermissionUtils;

import java.io.File;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/10
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class ImagePresenter extends BasePresenter<IImageView> implements IImagePresenter{

    private final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private final int REQUEST_PERMISSION_CODE_SAVE=1;
    private final int REQUEST_PERMISSION_CODE_DEL=2;

    private String mImageName;
    private String mNoteId;

    @Override
    public void getIntentData(Activity activity) {
        mImageName=activity.getIntent().getStringExtra("image_name");
        mNoteId=activity.getIntent().getStringExtra("note_id");
    }

    @Override
    public File getImageFile(Activity activity) {
        String path=activity.getExternalFilesDir(mNoteId).getPath()+"/"+mImageName;
        return new File(path);
    }

    @Override
    public void saveImage(final Activity activity) {
        PermissionUtils.checkPermission(activity, PERMISSION_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                copyFile(activity);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                copyFile(activity);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(activity,PERMISSION_STORAGE,REQUEST_PERMISSION_CODE_SAVE);
            }
        });
    }

    private void copyFile( final Activity activity){
        new AsyncTask<String,Integer,Boolean>(){

            @Override
            protected void onPreExecute() {
                mView.showLoading("保存中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                return  FileUtils.copyFile(getImageFile(activity),
                        new File(Constans.imageSaveFolder+mImageName));
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                if(aBoolean){
                    ToastUtils.showLong("已保存至/SuperNote/image/中");
                } else {
                    ToastUtils.showLong("保存失败,请查看图片是否已存在");
                }
            }
        }.execute();
    }

    @Override
    public void deleteImage(final Activity activity) {
        PermissionUtils.checkPermission(activity, PERMISSION_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                deleteFile(activity);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                deleteFile(activity);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(activity,PERMISSION_STORAGE,REQUEST_PERMISSION_CODE_DEL);
            }
        });
    }

    @Override
    public void onRequestPermissionResult(Activity activity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE_SAVE:
                requestResultForSave(activity,grantResults);
                break;
            case REQUEST_PERMISSION_CODE_DEL:
                requestResultForDel(activity,grantResults);
                break;
        }
    }

    private void requestResultForSave(final Activity activity, @NonNull int[] grantResults){
        PermissionUtils.onRequestPermissionResult(activity, PERMISSION_STORAGE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                copyFile(activity);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                ToastUtils.showShort("保存失败");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                mView.showToAppSettingDialog();
            }
        });
    }

    private void requestResultForDel(final Activity activity, @NonNull int[] grantResults){
        PermissionUtils.onRequestPermissionResult(activity, PERMISSION_STORAGE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                deleteFile(activity);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                ToastUtils.showShort("删除失败");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                mView.showToAppSettingDialog();
            }
        });
    }

    private void deleteFile(final Activity activity){
        new AsyncTask<String,Integer,Boolean>(){

            @Override
            protected void onPreExecute() {
                mView.showLoading("删除中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                return FileUtils.deleteFile(getImageFile(activity));
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                if(aBoolean){
                    mView.setResultAndFinish();
                } else {
                    ToastUtils.showShort("删除失败");
                }
            }
        }.execute();
    }
}
