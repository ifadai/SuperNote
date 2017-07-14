package app.fadai.supernote.constants;


import com.app.fadai.supernote.R;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/07
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class NoteListConstans {

    // 是否显示多选操作
    public static boolean isShowMultiSelectAction = false;

    // note的RecyclerView的显示布局样式
    public static final int STYLE_LINEAR = 1; // 线性布局
    public static final int STYLE_GRID = 2; // 网格布局

    // 多选操作时，已选择的item数量
    public static int selectedCount = 0;

    // 已选择的便签夹名称
    public static String selectedFolderName = "全部便签";

    // 是否已全选
    public static boolean isChoiceAll = false;

    // 是否进入搜索模式
    public static boolean isInSearch=false;

    // 显示模式菜单的图标
    public static final int MODE_LIST = R.drawable.ic_format_list_bulleted_white_24dp;
    public static final int MODE_GRID = R.drawable.ic_border_all_white_24dp;

    // 前往密码界面的requestCode
    public static final int REQUEST_CODE_LOCK=1;
    // 前往添加便签
    public static final int REQUEST_CODE_ADD=2;
    // 前往编辑便签
    public static final int REQUEST_CODE_EDIT=3;
    // 前往编辑便签夹
    public static final int REQUEST_CODE_EDIT_FOLDER=4;
}
