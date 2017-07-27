package ltd.pvt.lucky.notepack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hemant on 21/04/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    final static String  dataBaseName="NotePackDataBase.db";
   final String tableName="RecordFile";
    SQLiteDatabase db;

    public DataBaseHelper(Context context) {
        super(context, dataBaseName, null, 1);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+tableName+" ( fileName text,data text )");
    }

    public  long saveFile(String fileName,String fileData){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data",fileData);
        contentValues.put("fileName",fileName);
        long status = db.insert(tableName,null,contentValues);
        return  status;
    }

    public Cursor readFile(String fileName){
        db = this.getWritableDatabase();
        Cursor cursor =db.rawQuery("select * from "+tableName+" where fileName = ?",new String[]{fileName});
        return  cursor;
    }

    public  Cursor getAllFileNames(){
        Cursor allFileNames;
        db = this.getWritableDatabase();
        allFileNames = db.rawQuery("select distinct fileName from "+tableName,null);
        return allFileNames;
    }

   public int deleteFile(String fileName){
   int status;
     db = this.getWritableDatabase();
   status =  db.delete(tableName," fileName = ? ", new String[]{fileName});
   return  status;
   }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+tableName);
        this.onCreate(db);
    }
}
