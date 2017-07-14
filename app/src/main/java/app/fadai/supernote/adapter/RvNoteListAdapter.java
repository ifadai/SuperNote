package app.fadai.supernote.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.fadai.supernote.bean.Note;
import app.fadai.supernote.constants.Constans;
import app.fadai.supernote.constants.EditNoteConstans;
import app.fadai.supernote.constants.FolderListConstans;
import app.fadai.supernote.constants.NoteListConstans;
import app.fadai.supernote.utils.DateUtils;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/02
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class RvNoteListAdapter extends BaseQuickAdapter<Note, BaseViewHolder> {

    public List<Boolean> mCheckList = new ArrayList<>();

    public List<Boolean> mAllCheckList;
    public List<Note> mAllDataList;

    public void addData(@NonNull Collection<? extends Note> newData) {
        addData(newData);
        for (int i = 0; i < newData.size(); i++) {
            mCheckList.add(false);
        }
    }

    public void addData(@NonNull Note data) {
        addData(0, data);
        mCheckList.add(false);
        notifyDataSetChanged();
    }

    public void setNewData(@Nullable List<Note> data) {
        super.setNewData(data);
        mCheckList.clear();
        for (int i = 0; i < data.size(); i++) {
            mCheckList.add(false);
        }
    }

    public RvNoteListAdapter() {
        super(R.layout.item_note);
    }


    @Override
    protected void convert(BaseViewHolder helper, Note item) {
        if (isLinearLayoutManager())
            setLinearLayout(helper, item);
        else if (isGridLayoutManager())
            setGridLayout(helper, item);
    }

    /**
     * 是否是线性布局
     *
     * @describe
     */
    private boolean isLinearLayoutManager() {
        if (Constans.noteListShowMode == NoteListConstans.STYLE_LINEAR)
            return true;
        return false;
    }

    /**
     * 是否是网格布局
     *
     * @describe
     */
    private boolean isGridLayoutManager() {
        if (Constans.noteListShowMode == NoteListConstans.STYLE_GRID)
            return true;
        return false;
    }

    /**
     * 设置网格布局
     *
     * @describe
     */
    private void setGridLayout(BaseViewHolder helper, Note item) {

        helper.addOnClickListener(R.id.cv_note_list_grid);
        helper.addOnLongClickListener(R.id.cv_note_list_grid);

        helper.setVisible(R.id.ll_note_list_linear, false);
        helper.setVisible(R.id.cv_note_list_grid, true);

        TextView tvContent=helper.getView(R.id.tv_note_list_grid_content);
        if(isPrivacyAndRecycle(item))
            helper.setText(R.id.tv_note_list_grid_content,Utils.getContext().getResources().getString(R.string.note_privacy_and_recycle));
        else
            parseTextContent(tvContent,item.getNoteContent());

        // 设置便签的时间显示
        setNoteTime(helper, item.getModifiedTime());
        // 设置多选按钮
        setCheckBox(helper);
    }

    private boolean isPrivacyAndRecycle(Note note) {
        if (Constans.currentFolder== FolderListConstans.ITEM_RECYCLE && note.getIsPrivacy() == 1 )
            return true;
        else
            return false;
    }

    /**
     *   解析文本中的图片
     */
    private void parseTextContent(TextView textView, String content) {
        // TODO: 2017/7/11 0011 后续可以找到图片后，显示在列表item上 

        textView.setText("");

        Pattern p = Pattern.compile(EditNoteConstans.imageTabBefore+"([^<]*)"+EditNoteConstans.imageTabAfter);
        Matcher m = p.matcher(content);
        int tempIndex = 0;
        List<String> textList=new ArrayList<>();
        while (m.find()) {
            
            //  匹配到的数据中，第一个括号的中的内容（这里只有一个括号）
            String temp = m.group(1);
            
            //  查找图片标签的位置
            int index = content.indexOf(EditNoteConstans.imageTabBefore, tempIndex);
            
            //  将本次开始位置到图片标签间的图片储存起来
            String text = content.substring(tempIndex, index);
            textList.add(text);
            
            // 将查询起始位置升级
            int flagLength=EditNoteConstans.imageTabBefore.length()+EditNoteConstans.imageTabAfter.length();
            tempIndex = index + flagLength + temp.length();
        }

        if(textList.size()!=0){
            for (int i=0;i<textList.size();i++){
                textView.append(textList.get(i));
                textView.append("[图片]");
            }
            // 将最后一个图片标签后面所有的文字添加
            textView.append(content.substring(tempIndex));
        }else{
            textView.setText(content);
        }
    }

    /**
     * 设置线性布局
     *
     * @describe
     */
    private void setLinearLayout(BaseViewHolder helper, Note item) {

        helper.addOnClickListener(R.id.ll_note_list_line);
        helper.addOnLongClickListener(R.id.ll_note_list_line);

        // 显示竖排布局，隐藏网格布局
        helper.setVisible(R.id.cv_note_list_grid, false);
        helper.setVisible(R.id.ll_note_list_linear, true);

        TextView tvContent=helper.getView(R.id.tv_note_list_linear_content);
        if(isPrivacyAndRecycle(item))
            helper.setText(R.id.tv_note_list_linear_content,Utils.getContext().getResources().getString(R.string.note_privacy_and_recycle));
        else
            parseTextContent(tvContent,item.getNoteContent());

        // 设置便签的时间显示
        setNoteTime(helper, item.getModifiedTime());

        // 设置便签的分组显示
        setLinearLayoutGroup(helper, item.getCreatedTime());
        // 设置多选按钮
        setCheckBox(helper);
    }

    /**
     * 设置便签的时间显示格式：
     * 便签修改时间与当前时间对比，
     * 同一天的显示为：HH：mm；
     * 同一年的显示为：MM-DD HH:mm
     * 其他显示为：yyyy-MM-DD HH:mm
     *
     * @param time 时间戳
     * @describe
     */
    private void setNoteTime(BaseViewHolder helper, long time) {

        // 系统当前时间，用于与便签的修改时间进行对比
        long nowTime = TimeUtils.getNowMills();

        if (DateUtils.isInSameDay(nowTime, time))  // 同一天
            setNoteTimeInfo(helper, time, new SimpleDateFormat("HH:mm"));
        else if (DateUtils.isInSameYear(nowTime, time))  // 同一年
            setNoteTimeInfo(helper, time, new SimpleDateFormat("MM-dd HH:mm"));
        else // 其他
            setNoteTimeInfo(helper, time, new SimpleDateFormat("yyyy-MM-dd HH:mm"));

    }

    /**
     * 设置便签显示的时间
     *
     * @param helper
     * @param time   时间戳
     * @param format 时间格式
     * @describe
     */
    private void setNoteTimeInfo(BaseViewHolder helper, long time, SimpleDateFormat format) {
        if (isLinearLayoutManager()) {
            helper.setText(R.id.tv_note_list_linear_time, TimeUtils.millis2String(time, format));
        } else {
            helper.setText(R.id.tv_note_list_grid_time, TimeUtils.millis2String(time, format));
        }
    }

    /**
     * 设置线性布局时，列表的分组
     *
     * @param helper
     * @param time   时间戳
     * @describe
     */
    private void setLinearLayoutGroup(BaseViewHolder helper, long time) {

        // 当前position
        int position = helper.getLayoutPosition();


        // 如果是列表第一项,或者与上一个便签的创建时间不是在同一月，显示分组信息
        if (position == 0 ||
                !DateUtils.isInSameMonth(time, getData().get(position - 1).getCreatedTime())) {
            showLineraLayoutGroup(true, helper, time);
            return;
        }
        showLineraLayoutGroup(false, helper, time);
    }

    /**
     * 显示是否线性布局时的分组信息
     *
     * @param helper
     * @param isShow 是否显示
     * @param time   时间戳
     * @describe
     */
    private void showLineraLayoutGroup(boolean isShow, BaseViewHolder helper, long time) {
        // 有分组的列，marginTop为8dp,否则，为0dp
        LinearLayout ll = helper.getView(R.id.ll_note_list_linear);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        if (isShow) {
            helper.setVisible(R.id.tv_note_list_linear_month, true);
            setLinearGroupStyle(helper, time);

            params.setMargins(SizeUtils.dp2px(0), SizeUtils.dp2px(8), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
            ll.setLayoutParams(params);

        } else {
            helper.setVisible(R.id.tv_note_list_linear_month, false);
            params.setMargins(SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0), SizeUtils.dp2px(0));
            ll.setLayoutParams(params);
        }
    }

    /**
     * 设置线性布局时的分组的格式
     *
     * @describe
     */
    private void setLinearGroupStyle(BaseViewHolder helper, long time) {
        long nowTime = TimeUtils.getNowMills();

        if (DateUtils.isInSameYear(nowTime, time)) { // 如果同一年 显示为：x月
            helper.setText(R.id.tv_note_list_linear_month, TimeUtils.millis2String(time, new SimpleDateFormat("MM月")));
        } else { //否则 显示为：xxxx年x月
            helper.setText(R.id.tv_note_list_linear_month, TimeUtils.millis2String(time, new SimpleDateFormat("yyyy年MM月")));
        }
    }


    /**
     * 设置多选按钮
     *
     * @describe
     */
    private void setCheckBox(BaseViewHolder helper) {

        int position = helper.getLayoutPosition();
        CheckBox checkBox;
        if (isLinearLayoutManager())
            checkBox = (CheckBox) helper.getView(R.id.cb_note_list_liear_check);
        else
            checkBox = (CheckBox) helper.getView(R.id.cb_note_list_grid_check);
        showCheckBox(checkBox, position);
    }

    /**
     * 是否显示多选按钮
     *
     * @describe
     */
    private void showCheckBox(CheckBox checkBox, int position) {
        if (NoteListConstans.isShowMultiSelectAction) {
            checkBox.setVisibility(View.VISIBLE);
            if (mCheckList.get(position))
                checkBox.setChecked(true);
            else
                checkBox.setChecked(false);
        } else {
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setChecked(false);
        }
    }


}
