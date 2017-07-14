package app.fadai.supernote.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import app.fadai.supernote.bean.ImageEntity;
import app.fadai.supernote.constants.EditNoteConstans;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by miaoyongyong on 2017/2/8.
 */

public class MyEditText extends android.support.v7.widget.AppCompatEditText {

    // 已插入的图片
    public List<ImageEntity> mImageList = new ArrayList<ImageEntity>();
    // 已删除的图片 在保存便签时应从储存中删除
    public List<ImageEntity> mDeleteImageList=new ArrayList<>();

    private Editable mEditable = getEditableText();

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *   将文本中已有的imageName转换为Bitmap
     */
    public void replaceDrawable(Bitmap bitmap, String imageName) {
        mEditable=getEditableText();

        String imageFlag = getImageFlag(imageName);

        SpannableString spannableString = getSpannableString(imageFlag, bitmap);


        String content = mEditable.toString();
        int start = content.indexOf(imageFlag);
        int end = start + imageFlag.length();
        mEditable.replace(start, end, spannableString);
        addImage2List(start, end, imageFlag, imageName);
    }

    private SpannableString getSpannableString(String imageFlag, Bitmap bitmap) {
        ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
        SpannableString spannableString = new SpannableString(imageFlag);

        spannableString.setSpan(imageSpan, 0, imageFlag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private String getImageFlag(String imageName) {
        return EditNoteConstans.imageTabBefore + imageName + EditNoteConstans.imageTabAfter;
    }

    /**
     *   插入图片到光标处
     */
    public void insertDrawable(Bitmap bitmap, String imageName) {

        int index = getSelectionStart(); //获取光标所在位置
        mEditable = getEditableText();

        if (index != 0) {
            String str = mEditable.toString().substring(index - 1, index);
            if (!str.equals("\n") && !str.equals("\r")) {            // 如果前一项不是换行符，则先添加换行符
                mEditable.insert(index, "\n");
                insertImageAndOneLine(bitmap, imageName, index + 1);
            } else {
                insertImageAndOneLine(bitmap, imageName, index);
            }
        } else {
            insertImageAndOneLine(bitmap, imageName, index);
        }
    }

    private void insertImageAndOneLine(Bitmap bitmap, String imageName, int index) {

        String imageFlag = getImageFlag(imageName);
        SpannableString spannableString = getSpannableString(imageFlag, bitmap);

        mEditable.insert(index, spannableString);
        mEditable.insert(index + imageFlag.length(), "\n");  // 后面再添加一行
        addImage2List(index, index + imageFlag.length(), imageFlag, imageName);

    }

    private void addImage2List(int start, int end, String imageFlag, String imageName) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setStart(start);
        imageEntity.setEnd(end);
        imageEntity.setImageFlag(imageFlag);
        imageEntity.setImageName(imageName);
        mImageList.add(imageEntity);
    }

    /**
     *   文字改变后，应更新图片列表中的位置参数
     */
    public void setTextCountChange(int start, int before, int count) {
        for (int i = 0; i < mImageList.size(); i++) {
            ImageEntity imageEntity = mImageList.get(i);
            int imageStart = imageEntity.getStart();
            int imageEnd = imageEntity.getEnd();
            int changeCount = count - before;
            //　如果图片在已改变文本的后面
            if (start < imageStart) {
                imageStart = imageStart + changeCount;
                imageEnd = imageEnd + changeCount;
                mImageList.get(i).setStart(imageStart);
                mImageList.get(i).setEnd(imageEnd);
            }
        }
    }

}
