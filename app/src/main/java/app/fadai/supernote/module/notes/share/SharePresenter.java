package app.fadai.supernote.module.notes.share;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.constants.EditNoteConstans;
import app.fadai.supernote.module.base.BasePresenter;
import app.fadai.supernote.utils.PermissionUtils;

import java.io.File;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/07/12
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class SharePresenter extends BasePresenter<IShareView> implements ISharePresenter {

    private final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private final int REQUEST_PERMISSION_CODE_SAVE = 1;

    private Bitmap mBitmap = EditNoteConstans.shareBitmap;

    @Override
    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public void sendBitmap() {
        shareBitmap();
    }

    private void shareBitmap(){
        new AsyncTask<String, Integer, Uri>() {

            @Override
            protected void onPreExecute() {
                mView.showLoadingDialog("加载中...");
            }

            @Override
            protected Uri doInBackground(String... params) {
                Uri uri = saveImageAndGetUri();
                return uri;
            }

            @Override
            protected void onPostExecute(Uri uri) {
                mView.unShowLoadingDialog();
                mView.showShareDialog(uri);
            }
        }.execute();
    }

    @Override
    public void saveImage() {
        final Activity activity = mView.getActivity();
        PermissionUtils.checkPermission(activity, PERMISSION_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                saveImageToLocation(mBitmap);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                saveImageToLocation(mBitmap);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(activity, PERMISSION_STORAGE, REQUEST_PERMISSION_CODE_SAVE);
            }
        });
    }

    @Override
    public Uri saveImageAndGetUri() {
        String filePath = mView.getActivity().getExternalFilesDir("share").getPath() + "/" + TimeUtils.getNowMills() + ".jpg";
        File file = new File(filePath);
        ImageUtils.save(mBitmap, file, Bitmap.CompressFormat.JPEG);
        return Uri.fromFile(file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE_SAVE:
                requestPermissionForSaveImageResult(grantResults);
                break;
        }
    }

    @Override
    public void onDestroy() {
        mBitmap.recycle();
        mBitmap = null;
    }

    private void requestPermissionForSaveImageResult(@NonNull int[] grantResults) {
        final Activity activity = mView.getActivity();
        PermissionUtils.onRequestPermissionResult(activity, PERMISSION_STORAGE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                saveImage();
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

    private void saveImageToLocation(Bitmap bitmap) {
        File file = new File(Constans.imageSaveFolder + "/" + TimeUtils.getNowMills() + ".jpg");
        ImageUtils.save(bitmap, file, Bitmap.CompressFormat.JPEG);
        ToastUtils.showLong("已保存至" + "/SuperNote/Image/" + "中");
    }
}
