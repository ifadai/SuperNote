package app.fadai.supernote.model;

import app.fadai.supernote.bean.Note;
import app.fadai.supernote.bean.NoteFolder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/05
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class NoteFolderModel implements INoteFolderModel<NoteFolder> {

    private INoteModel<Note> mNoteModel=new NoteModel();

    @Override
    public int initNoteFolderAndGetFolderId() {

        NoteFolder folder1 = new NoteFolder();
        folder1.setFolderName("随手记");
        folder1.setNoteCount(5);
        addNoteFolder(folder1);

        NoteFolder noteFolder2 = new NoteFolder();
        noteFolder2.setFolderName("生活");
        noteFolder2.setNoteCount(0);
        addNoteFolder(noteFolder2);

        NoteFolder noteFolder3 = new NoteFolder();
        noteFolder3.setFolderName("工作");
        addNoteFolder(noteFolder3);

        NoteFolder folder=DataSupport.where("folderName = ? ","随手记").find(NoteFolder.class).get(0);
        return folder.getId();
    }

    @Override
    public void loadNoteFoldersList(LoadDataCallBack<NoteFolder> callBack) {
        List<NoteFolder> list = DataSupport.findAll(NoteFolder.class);
        callBack.onSuccedd(list);
    }

    @Override
    public void addNoteFolder(NoteFolder folder) {
        folder.save();
    }

    @Override
    public void deleteNoteFolder(NoteFolder folder) {
        int folderId=folder.getId();
        List<Note> list=new ArrayList<Note>();
        list= DataSupport.where("NoteFolderId = ? and inRecycleBin = ?",folderId+"","0").find(Note.class);
        for(int i=0;i<list.size();i++){
            Note note=list.get(i);
            mNoteModel.deleteNote(note);
        }
        folder.delete();
    }

    @Override
    public void deleteNoteFolders() {
    }

    @Override
    public void addNote2Folder(Note note, NoteFolder folder) {

        note.setNoteFolderId(folder.getId());
        note.save();

        folder.setNoteCount(folder.getNoteCount()+1);
        folder.save();
    }

    @Override
    public void addNote2Privacy(Note note, NoteFolder folder) {
        note.setNoteFolderId(folder.getId());
        note.setIsPrivacy(1);
        note.save();
    }


}
