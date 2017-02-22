package nu.annat.simplesqlexample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseExample extends SQLiteOpenHelper {

    public DataBaseExample(Context context) {
        super(context, "myDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table messages(_id INT PRIMARY KEY, timestamp BIGINT, user VARCHAR(250), text VARCHAR(4000))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
