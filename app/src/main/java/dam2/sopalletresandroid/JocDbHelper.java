package dam2.sopalletresandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class JocDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Joc.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE SopaDeLletres " +
                    "(id integer primary key,fecha TEXT, puntuacio integet)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS SopaDeLletres";

    public JocDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Object[] getDataPuntuacio(){
        ArrayList<String> fechas = new ArrayList<String>();
        ArrayList<Integer> puntuaciones = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            fechas.add(res.getString(res.getColumnIndex("fecha")));
            puntuaciones.add(res.getInt(res.getColumnIndex("fecha")));
            res.moveToNext();
        }
        return new Object[]{fechas,puntuaciones};
    }
}