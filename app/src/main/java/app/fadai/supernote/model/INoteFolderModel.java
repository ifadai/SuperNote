package app.fadai.supernote.model;

import app.fadai.supernote.bean.Note;
import app.fadai.supernote.bean.NoteFolder;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/05
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface INoteFolderModel<T> {

    int initNoteFolderAndGetFolderId();

    void loadNoteFoldersList(LoadDataCallBack<T> callBack);

    void addNoteFolder(T noteFolder);

    void deleteNoteFolder(NoteFolder folder);

    void deleteNoteFolders();

    void addNote2Folder(Note note, NoteFolder folder);

    void addNote2Privacy(Note note,NoteFolder folder);

}
