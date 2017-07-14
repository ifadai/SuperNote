package app.fadai.supernote.module.notes.edit;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.fadai.supernote.bean.ImageEntity;
import app.fadai.supernote.constants.EditNoteConstans;
import app.fadai.supernote.module.base.BasePresenter;
import app.fadai.supernote.module.notes.Image.ImageInfoActivity;
import app.fadai.supernote.module.notes.share.ShareActivity;
import app.fadai.supernote.utils.PermissionUtils;
import app.fadai.supernote.utils.ThemeUtils;
import app.fadai.supernote.widget.MyEditText;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/30
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class EditNotePresenter extends BasePresenter<IEditNoteView> implements IEditNotePresenter {

    public static final int REQUEST_CODE_TO_CAMERA = 1;  //前往相机
    public static final int REQUEST_CODE_TO_PHOTO = 2;   //前往图库
    public static final int REQUEST_CODE_TO_IMAGE_INFO = 3;

    private final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private final int REQUEST_PERMISSION_TO_CAMERA = 1;    // 请求权限前往相机
    private final int REQUEST_PERMISSION_TO_PHOTO = 2;     // 请求权限前往图库

    private boolean mIsAdd; //　是否是新增便签

    private String mNoteId;
    private long mModifiedTime;
    private String mNoteContent;

    private int mPosition = 0;

    private String mImageName = new String();

    private File mImageFolder;
    private File mImageFile;

    private ImageEntity mSelectedImageEntity;

    @Override
    public void initData() {
        Intent intent = mView.getActivityIntent();
        mIsAdd = intent.getBooleanExtra("is_add", false);
        if (mIsAdd) {
            mNoteId = UUID.randomUUID().toString();
            mModifiedTime = TimeUtils.getNowMills();
            mNoteContent = new String("");
        } else {
            mPosition = intent.getIntExtra("position", 0);
            mNoteId = intent.getStringExtra("note_id");
            mModifiedTime = intent.getLongExtra("modified_time", 0);
            mNoteContent = intent.getStringExtra("note_content");
        }
        mView.setTitle(TimeUtils.millis2String(mModifiedTime));
    }

    @Override
    public void setTextForNoteContent() {
        mView.showNoteContent(mNoteContent);
    }

    @Override
    public void parseNoteContent() {
        if (mIsAdd)
            return;
        mView.showNoteContent(mNoteContent);
        String flag = EditNoteConstans.imageTabBefore + "([^<]*)" + EditNoteConstans.imageTabAfter;

        // 利用正则找出文档中的图片
        Pattern p = Pattern.compile(flag);
        Matcher m = p.matcher(mNoteContent);
        List<String> array = new ArrayList<String>();
        while (m.find()) {
//            匹配到的数据中，第一个括号的中的内容（这里只有一个括号）
            String temp = m.group(1);
            array.add(temp);
        }
        for (int i = 0; i < array.size(); i++) {
            final String imageName = array.get(i);
            replaceImage(imageName);
        }
    }

    private void replaceImage(final String imageName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        File imageFile = new File(mView.getActivity().getExternalFilesDir(mNoteId).getPath() + "/" + imageName);

        BitmapFactory.decodeFile(imageFile.getPath(), options);

        int imageRequestWidth = getRequestImeWidth();
        int imageRequestHeight = setNeedHeight(options);


        Glide.with(mView.getActivity())
                .load(imageFile)
                .asBitmap()
                .override(imageRequestWidth, imageRequestHeight)
                .fitCenter()
                .priority(Priority.HIGH)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mView.replaceImage(imageName, resource);
                    }
                });
    }


    private int setNeedHeight(BitmapFactory.Options options) {
        int imageRequestHeight = getRequestImeHeight(options);
        if (imageRequestHeight <= 0) {
            return getNoteEditNeedHeight();
        } else
            return imageRequestHeight;
    }

    @Override
    public void checkPermissionAndToCamera(final Context context) {
        PermissionUtils.checkPermission(context, PERMISSION_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                toCamera((Activity) context);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                PermissionUtils.requestPermission(context, permission[0], REQUEST_PERMISSION_TO_CAMERA);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(context, permission[0], REQUEST_PERMISSION_TO_CAMERA);
            }
        });
    }

    @Override
    public void checkPermissionAndToPhoto(final Context context) {
        PermissionUtils.checkPermission(context, PERMISSION_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                toPhoto((Activity) context);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                PermissionUtils.requestPermission(context, permission[0], REQUEST_PERMISSION_TO_PHOTO);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(context, permission[0], REQUEST_PERMISSION_TO_PHOTO);
            }
        });
    }

    @Override
    public void toCamera(Activity activity) {
        try {
            setFile(activity);
            createImageFile();
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(activity, "com.app.fadai.supernote.fileprovider", mImageFile);
            } else {
                imageUri = Uri.fromFile(mImageFile);
            }

            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activity.startActivityForResult(getImageByCamera, REQUEST_CODE_TO_CAMERA);
        } catch (Exception e) {
            ToastUtils.showShort("打开相机失败");
        }
    }

    private void setFile(Activity activity) throws IOException {
        mImageName = TimeUtils.getNowString() + ".jpg";
        mImageFolder = activity.getExternalFilesDir(mNoteId);
        mImageFile = new File(mImageFolder, mImageName);

        checkImageFolder();
    }

    private void checkImageFolder() throws IOException {
        if (!mImageFolder.exists()) {
            mImageFolder.createNewFile();
        }
    }

    private void createImageFile() throws IOException {
        mImageFile.createNewFile();
    }

    @Override
    public void toPhoto(Activity activity) {
        try {
            setFile(activity);
            Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
            getImage.addCategory(Intent.CATEGORY_OPENABLE);
            getImage.setType("image/*");
            activity.startActivityForResult(getImage, REQUEST_CODE_TO_PHOTO);
        } catch (IOException e) {
            ToastUtils.showShort("打开图库失败");
        }
    }

    private int getRequestImeWidth() {
        return (int) (ScreenUtils.getScreenWidth() - EditNoteConstans.imageMargin);
    }

    @Override
    public int getRequestImeHeight(BitmapFactory.Options option) {
        float width = option.outWidth;
        float height = option.outHeight;
//        屏幕宽
        float screenWidth = ScreenUtils.getScreenWidth();
        //计算宽、高缩放率
        float scanleWidth = (getRequestImeWidth()) / width;
        return (int) (height * scanleWidth);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CODE_TO_PHOTO:
                if (resultCode == RESULT_OK) {
                    toPhotoResult(intent);
                }
                break;
            case REQUEST_CODE_TO_CAMERA:
                if (resultCode == RESULT_OK)
                    displayImage();
                break;
            case REQUEST_CODE_TO_IMAGE_INFO:
                Logger.d("REQUEST_CODE_TO_IMAGE_INFO" + resultCode);
                if (resultCode == RESULT_OK) {
                    mView.deleteImage(mSelectedImageEntity);
                }
                break;
        }
    }

    private void toPhotoResult(Intent intent) {
        if (Build.VERSION.SDK_INT >= 19) {
            handleImageOnKitKat(intent);
        } else {
            handleImageBeforeKitKat(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_TO_CAMERA:
                onRequestCameraResult(grantResults);
                break;
            case REQUEST_PERMISSION_TO_PHOTO:
                onRequestPhotoResult(grantResults);
                break;
        }
    }

    private void onRequestCameraResult(int[] grantResults) {
        PermissionUtils.onRequestPermissionResult(mView.getActivity(), PERMISSION_STORAGE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                toCamera(mView.getActivity());
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                ToastUtils.showShort("请允许读取储存权限");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                mView.showToAppSettingDialog();
            }
        });
    }

    private void onRequestPhotoResult(int[] grantResults) {
        PermissionUtils.onRequestPermissionResult(mView.getActivity(), PERMISSION_STORAGE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                toPhoto(mView.getActivity());
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                ToastUtils.showShort("请允许读取储存权限");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                mView.showToAppSettingDialog();
            }
        });
    }

    @Override
    public void saveNote(String content) {
        Intent intent = mView.getActivityIntent();
        // 内容改变时才保存
        if (!mNoteContent.equals(content)) {
            intent = mView.getActivityIntent();
            intent.putExtra("note_id", mNoteId);
            intent.putExtra("note_content", content);
            intent.putExtra("modified_time", mModifiedTime);
            intent.putExtra("position", mPosition);
            mView.setResultAndFinish(intent);
        }
    }

    @Override
    public void calculateContentAndImageCount(MyEditText myEditText) {
        int count = myEditText.getText().length();
        int imageCount = myEditText.mImageList.size();
        for (int i = 0; i < myEditText.mImageList.size(); i++) {
            count = count - (myEditText.mImageList.get(i).getImageFlag().length());
            // 再减去一个换行符
            count = count - 1;
        }
        mView.showStatisticsDialog(imageCount, count);
    }

    @Override
    public int getNoteEditNeedHeight() {
        // 屏幕高度减去 状态栏高度、toolbar高度、底部工具栏高度
        float height = ScreenUtils.getScreenHeight() - ThemeUtils.getStatusBarHeight()
                - SizeUtils.dp2px(56) - SizeUtils.dp2px(48);
        return (int) height;
    }

    @Override
    public void clickNoteEditText(MyEditText editText) {
        // 获取光标位置
        int selectionAfter = editText.getSelectionStart();
        Logger.d("光标位置：" + selectionAfter);

        for (int i = 0; i < editText.mImageList.size(); i++) {

            ImageEntity imageEntity = editText.mImageList.get(i);

            if (selectionAfter >= imageEntity.getStart()
                    && selectionAfter <= imageEntity.getEnd()) { // 光标位置在照片的位置内
                Logger.d("起点:" + imageEntity.getStart() + "  终点:" + imageEntity.getEnd());
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) Utils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                // 光标移到图片末尾的换行符后面
                editText.setSelection(imageEntity.getEnd() + 1);
                mSelectedImageEntity = imageEntity;
                toImageInfoActivity();
                break;
            }
        }
    }

    @Override
    public void shareNoteWithText() {

    }

    @Override
    public void shareNoteWithImage(final View view) {
        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("生成图片中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                Bitmap bitmap = getNoteShareBitmap(view);
                EditNoteConstans.shareBitmap=bitmap;
                return true;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                mView.unShowLoading();
                mView.setEditTextAfterGetBitmap();
                toShareActivity();
            }
        }.execute();
    }

    private void toShareActivity() {
        Intent intent = new Intent(mView.getActivity(), ShareActivity.class);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void closeKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public Bitmap getNoteShareBitmap(View view) {
        Bitmap bitmap = ImageUtils.view2Bitmap(view);
        int x = bitmap.getWidth() - SizeUtils.sp2px(72);
        int y = bitmap.getHeight() - SizeUtils.sp2px(16);
        int textWaterMarkColor = Utils.getContext().getResources().getColor(R.color.colorBlackAlpha54);
        bitmap = ImageUtils.addTextWatermark(bitmap, EditNoteConstans.watermarkText, 24, textWaterMarkColor, x, y);
        return bitmap;
    }

    private void toImageInfoActivity() {
        Intent intent = new Intent(mView.getActivity(), ImageInfoActivity.class);
        intent.putExtra("image_name", mSelectedImageEntity.getImageName());
        intent.putExtra("note_id", mNoteId);
        mView.getActivity().startActivityForResult(intent, REQUEST_CODE_TO_IMAGE_INFO);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(mView.getActivity(), uri)) {
//            如果是documentlent类型的URI，则通过docment id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePatch(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePatch(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            如果是content类型的uri的话，则使用普通方式处理
            imagePath = getImagePatch(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            若果是file类型的uri，则直接获取图片路径
            imagePath = uri.getPath();
        }
        copyFileInOtherThread(imagePath);
    }

    private String getImagePatch(Uri uri, String selection) {
        String path = null;
//        通过URi和selection 来获取真实的图片路径
        Cursor cursor = mView.getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //    新线程中对图片进行复制。
    private void copyFileInOtherThread(String imagePath) {
        final String imagePaths = imagePath;
        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("加载中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                return FileUtils.copyFile(new File(imagePaths), mImageFile);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                if (aBoolean) {
                    displayImage();
                } else {
                    ToastUtils.showShort("图片读取失败");
                }
            }
        }.execute();
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePatch(uri, null);
        copyFileInOtherThread(imagePath);
    }


    private void displayImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  // 对图片进行设置 但不形成示例，不耗费内存

        BitmapFactory.decodeFile(mImageFile.getPath(), options);

        int imageRequestWidth = getRequestImeWidth();
        int imageRequestHeight = getRequestImeHeight(options);
        Logger.d("width " + imageRequestWidth + "   height:" + imageRequestHeight);
        Logger.d("bitmap1 width " + options.outWidth + "   height:" + options.outHeight);

        Glide.with(mView.getActivity())
                .load(mImageFile)
                .asBitmap()
                .override(imageRequestWidth, imageRequestHeight)  // 设置大小
                .fitCenter()                                     // 不按照比例
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //根据Bitmap对象创建ImageSpan对象
                        Logger.d("bitmap width:" + resource.getWidth() + "   height:" + resource.getHeight());
                        mView.insertImage(mImageName, resource);
                    }
                });
    }
}
