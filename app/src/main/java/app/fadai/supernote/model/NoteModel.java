package app.fadai.supernote.model;

import com.app.fadai.supernote.R;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;
import java.util.UUID;

import app.fadai.supernote.bean.Note;

import static org.litepal.crud.DataSupport.where;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/06/05
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class NoteModel implements INoteModel<Note> {

    @Override
    public void initNote(int folderId) {

        long years = (long)12 * 30 * 24 * 60 * 60 * 1000;
        long month =(long) 24 * 60 * 60 * 1000 * 30;
        long days = (long)24 * 60 * 60 * 1000;
        long m=(long)60*1000;

        long time = TimeUtils.getNowMills();


        Note note2 = new Note();
        note2.setCreatedTime(time-m-m-m-m);
        note2.setModifiedTime(time-m-m-m-m);
        note2.setNoteFolderId(folderId);
        note2.setNoteContent(Utils.getContext().getResources().getString(R.string.database_content_three));
        note2.setIsPrivacy(0);
        note2.setInRecycleBin(0);
        note2.setNoteId(UUID.randomUUID().toString());
        note2.save();

        Note note3 = new Note();
        note3.setCreatedTime(time-m-m-m);
        note3.setModifiedTime(time-m-m-m);
        note3.setNoteFolderId(folderId);
        note3.setNoteContent(Utils.getContext().getResources().getString(R.string.database_content_four));
        note3.setIsPrivacy(0);
        note3.setInRecycleBin(0);
        note3.setNoteId(UUID.randomUUID().toString());
        note3.save();

        Note note4 = new Note();
        note4.setCreatedTime(time-m-m);
        note4.setModifiedTime(time -m-m);
        note4.setNoteFolderId(folderId);
        note4.setNoteContent(Utils.getContext().getResources().getString(R.string.database_content_five));
        note4.setIsPrivacy(0);
        note4.setInRecycleBin(0);
        note4.setNoteId(UUID.randomUUID().toString());
        note4.save();

        Note note5 = new Note();
        note5.setCreatedTime(time -m);
        note5.setModifiedTime(time -m);
        note5.setNoteContent(Utils.getContext().getResources().getString(R.string.database_content_one));
        note5.setNoteFolderId(folderId);
        note5.setIsPrivacy(0);
        note5.setInRecycleBin(0);
        note5.setNoteId(UUID.randomUUID().toString());
        note5.save();

        Note note1 = new Note();
        note1.setCreatedTime(time );
        note1.setModifiedTime(time );
        note1.setNoteFolderId(folderId);
        note1.setNoteContent(Utils.getContext().getResources().getString(R.string.database_content_two));
        note1.setIsPrivacy(0);
        note1.setInRecycleBin(0);
        note1.setNoteId(UUID.randomUUID().toString());
        note1.save();


    }

    @Override
    public void loadAllNoteList(LoadDataCallBack<Note> callBack) {
        List<Note> data = where("isPrivacy = ? and inRecycleBin = ?", "0", "0").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void loadPrivacyNoteList(LoadDataCallBack<Note> callBack) {
        List<Note> data = where("isPrivacy = ? and inRecycleBin = ?", "1", "0").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void loadRecycleBinNoteList(LoadDataCallBack<Note> callBack) {
        List<Note> data = DataSupport.where("inRecycleBin = ?", "1").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void loadNormalNoteList(int folderId, LoadDataCallBack<Note> callBack) {
        List<Note> data = where("noteFolderId = ? and isPrivacy = ? and inRecycleBin = ?", folderId + "", "0", "0").order("createdTime desc").find(Note.class);
        callBack.onSuccedd(data);
    }

    @Override
    public void addNote(Note note) {
        note.save();
    }

    @Override
    public void deleteNote(Note note) {
        String noteId=note.getNoteId();
        deleteNoteFile(noteId);
        note.delete();
    }

    @Override
    public void deleteNotes() {

    }

    private void deleteNoteFile(String noteId){
        File file=Utils.getContext().getExternalFilesDir(noteId);
        if(file.exists()){
            FileUtils.deleteDir(file);
        }
    }
}
