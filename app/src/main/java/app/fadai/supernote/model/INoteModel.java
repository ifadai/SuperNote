package app.fadai.supernote.model;

import app.fadai.supernote.bean.Note;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/05
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public interface INoteModel<T> {
    void initNote(int folderId);

    void loadAllNoteList(LoadDataCallBack<T> callBack);

    void loadPrivacyNoteList(LoadDataCallBack<T> callBack);

    void loadRecycleBinNoteList(LoadDataCallBack<T> callBack);

    void loadNormalNoteList(int folderId,LoadDataCallBack<T> callBack);

    void addNote(T note);

    void deleteNote(Note note);

    void deleteNotes();
}
