package app.fadai.supernote.module.notes.folderList;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/19
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface IFolderListView<N> {


    public void choiceItemAll();

    public void choiceItemPrimary();

    public void choiceItemRecycleBin();

    public void unChoiceItemAll();

    public void unChoiceItemPrimary();

    public void unChoiceItemRecycleBin();

    void setAllNoteCount(int count);

}
