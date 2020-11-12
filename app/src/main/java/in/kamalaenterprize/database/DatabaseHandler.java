package in.kamalaenterprize.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.ShowCustomView;
import in.kamalaenterprize.sncharsuchi.R;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static int DATABASE_VERSION = 11;
    private String DATABASE_PATH = "";
    private Context mContext = null;
    boolean isCopydatabase = false;

    private static String DATABASE_NAME = "database.db";

    public String tablenames[] = {"option_one"};
    public static String[][] fieldsNames = {
            {"name", "date", "yoddhaId", "travelType", "address", "locationLat", "locationLong", "modeOfTravel", "note", "meeter_remark",
            "isvrified", "meeter_user_id"}
    };
    private String[][] fieldsdatatypes = {
            {"TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT",
                    "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT"}
    };

	/*public DatabaseHandler(Context context, String dbName, String[] tablenames,
			String[][] fieldsNames, String[][] fieldsdatatypes) {
		super(context, dbName, null, DATABASE_VERSION);
		DATABASE_NAME = dbName;
		tablenames 				= new String[tablenames.length];
		fieldsNames 			= new String[fieldsNames.length][tablenames.length];
		fieldsdatatypes 		= new String[fieldsdatatypes.length][tablenames.length];
		this.tablenames 		= tablenames;
		this.fieldsNames 		= fieldsNames;
		this.fieldsdatatypes 	= fieldsdatatypes;
	}*/

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (isCopydatabase) {
            if (mContext == null) {
                mContext = context;
                Log.i("DatabaseHelper", "DatabaseHelper");
                DATABASE_PATH = "/data/data/" + context.getPackageName()
                        + "/databases/";
                creatingDB();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!isCopydatabase) {
            for (int i = 0; i < tablenames.length; i++) {
                String tabledata = "";
                for (int j = 0; j < fieldsNames[i].length; j++) {
                    if (fieldsdatatypes[i][j].equalsIgnoreCase("AI")) {
                        tabledata = tabledata + fieldsNames[i][j] + " INTEGER PRIMARY KEY AUTOINCREMENT,";
                    } else if (fieldsdatatypes[i][j].equalsIgnoreCase("INT")) {
                        tabledata = tabledata + fieldsNames[i][j] + " INTEGER,";
                    } else if (fieldsdatatypes[i][j].equalsIgnoreCase("TEXT")) {
                        tabledata = tabledata + fieldsNames[i][j] + " TEXT,";
                    }
                }
                tabledata = tabledata.substring(0, tabledata.length() - 1);
                String createTableProduct = "CREATE TABLE " + tablenames[i] + "("
                        + tabledata
                        + " )";
                db.execSQL(createTableProduct);
            }
        }
    }

    protected void creatingDB() {
        File path = new File(DATABASE_PATH);
        path.mkdirs();

        File dbFile = new File(path, DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                System.out.println("DataBase Created....");
                copyDataBase(mContext, dbFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("DataBase Already exists...");
        }
    }

    private void copyDataBase(Context context, File dbFile) throws IOException {
        dbFile.createNewFile();
        OutputStream dataOutputStream = new FileOutputStream(dbFile);
        InputStream dataInputStream;

        byte[] buffer = new byte[1024];
        dataInputStream = context.getResources().openRawResource(R.raw.db);

        while ((dataInputStream.read(buffer)) > 0) {
            dataOutputStream.write(buffer);
        }
        dataInputStream.close();
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = 0; i < tablenames.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + tablenames[i]);
        }
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + mContext.getPackageName() + "/databases/" + DATABASE_NAME;
        String backupDBPath = DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

            ShowCustomView.showCustomToast("DB Exported", mContext, ShowCustomView.ToastyInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addData(String[] adddata, int tableNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int j = 0; j < adddata.length; j++) {
            values.put(fieldsNames[tableNum][j], adddata[j]);
        }
        // Inserting Row
        db.insert(tablenames[tableNum], null, values);
        db.close(); // Closing database connection
    }

    public void addSubCatData(String[] adddata, int tableNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int j = 0; j < fieldsNames[tableNum].length; j++) {
            values.put(fieldsNames[tableNum][j], adddata[j]);
        }
        // Inserting Row
        db.insert(tablenames[tableNum], null, values);
        db.close(); // Closing database connection
    }

    public void updateValue(int Id, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CategoryID", name);
        database.update("MnemonicsTable", values, "ID" + " = " + Id, null);
        database.close();
    }

    public List<String[]> getData(String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String selectQuery = null;

        selectQuery = "SELECT  * FROM  " + tableName + ";";
        GlobalData.print(selectQuery + "");
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String[] strData = new String[cursor.getColumnCount()];
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    strData[i] = (cursor.getString(i));
                }
                list.add(strData);
            } while (cursor.moveToNext());
        }
        database.close();
        return list;
    }

//    public List<SubCategoryModel> getAllSubCategory(String tableName) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        String selectQuery = null;
//
//        selectQuery = "SELECT  * FROM  " + tableName;
//        List<SubCategoryModel> list = new ArrayList<SubCategoryModel>();
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                list.add(new SubCategoryModel(cursor.getString(0), cursor.getString(1), cursor.getString(2),
//                        cursor.getString(3)));
//            } while (cursor.moveToNext());
//        }
//        database.close();
//        return list;
//    }

    public List<String[]> getDataWithClassId(String tableName, String classId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String selectQuery = null;

        selectQuery = "SELECT  * FROM  " + tableName + " WHERE CategoryId = " + classId + ";";
        GlobalData.print(selectQuery + "");
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String[] strData = new String[cursor.getColumnCount()];
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    strData[i] = (cursor.getString(i));
                }
                list.add(strData);
            } while (cursor.moveToNext());
        }
        database.close();
        return list;
    }

    public String getCatNameWithCatId(String tableName, String catID) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String selectQuery = null;

        selectQuery = "SELECT  * FROM  " + tableName + " WHERE ID = " + catID + ";";
        GlobalData.print(selectQuery + "");
        Cursor cursor = database.rawQuery(selectQuery, null);
        String catName = cursor.getString(1);
        ;
        database.close();
        return catName;
    }

    public void deleteAllData(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    public boolean deleteTableWithId(String tableName, String currentSerialNum, String value) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(tableName, currentSerialNum + "=" + value, null) > 0;
    }

    public void deleteTableData(String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(tableName, null, null);
        database.close();
    }
}

