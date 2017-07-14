package app.fadai.supernote.local.table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

/**
 * Created by miaoyongyong on 2016/12/5.
 */

public class MySqliteDBConnect extends SQLiteOpenHelper {
    private String CREATE_NOTE="create table Note("
            +"noteId Integer primary key autoincrement,"
            +"createdTime Integer,"
            +"modifiedTime Integer,"
            +"noteContent text)";

    public MySqliteDBConnect(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Logger.d("MySqliteDBContent","before create");
        sqLiteDatabase.execSQL(CREATE_NOTE);
        Logger.d("MySqliteDBContent","数据库创建完毕");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
