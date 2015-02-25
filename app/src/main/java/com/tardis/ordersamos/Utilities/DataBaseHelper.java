package com.tardis.ordersamos.Utilities;

//ALL DONE HERE V.2

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.tardis.ordersamos.Logic.Structure.Restaurant;
import com.tardis.ordersamos.Logic.Structure.Food;
import com.tardis.ordersamos.Logic.Structure.Menu;

@SuppressLint("SdCardPath")
public class DataBaseHelper extends SQLiteOpenHelper {

    // The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.tardis.ordersamos/databases/";

    private static String DB_NAME = "OrderAtSamosDB";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public static void SetDatabaseLanguage(String name) {
        DB_NAME = name;
    }

    private static final int DATABASE_VERSION = 8;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
        DB_NAME = dbName;

        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist
            try {
                this.getWritableDatabase();
            } catch (Exception e) {

            }
        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            try {
                this.getReadableDatabase();
            } catch (Exception e) {

            }
            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        // Log.d("Test", "in onUpgrade. Old is: " + myDataBase.getVersion());

        SQLiteDatabase checkdb = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkdb = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);

            // Once the DB has been copied, set the new version
            checkdb.setVersion(DATABASE_VERSION);
        } catch (SQLiteException e) {
            // database doesnt exist yet.
        }

    }

    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + DB_NAME;
        try {
            myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (Exception e) {

        }

    }

    @Override
    public synchronized void close() {


        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {
            Log.v("Test", "Within onUpgrade. Old is: " + oldVersion
                    + " New is: " + newVersion);
            try {
                myContext.deleteDatabase(DB_NAME);
            } catch (Exception e) {

            }
            try {
                copyDataBase();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    // Add your public helper methods to access and get content from the
    // database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd
    // be easy
    // to you to create adapters for your views.

    public ArrayList<Restaurant> getRestaurantsFromDatabase() {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

        try {
            Cursor c = myDataBase.rawQuery("SELECT * FROM " + "Restaurants",
                    null);

            int Column1 = c.getColumnIndex("_id");
            int Column2 = c.getColumnIndex("Name");
            int Column3 = c.getColumnIndex("Description");
            int Column4 = c.getColumnIndex("Phone");
            int Column5 = c.getColumnIndex("PhoneCosmote");
            int Column6 = c.getColumnIndex("PhoneVodafone");
            int Column7 = c.getColumnIndex("PhoneWind");
            int Column8 = c.getColumnIndex("OpeningHour");
            int Column9 = c.getColumnIndex("ClosingHour");
            int Column10 = c.getColumnIndex("CloseDay");
            int Column11 = c.getColumnIndex("Image");
            int Column12 = c.getColumnIndex("TrueName");
            int Column13 = c.getColumnIndex("Facebook");
            int Column14 = c.getColumnIndex("Address");
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                do {
                    int id = c.getInt(Column1);
                    String name = c.getString(Column2);
                    String description = c.getString(Column3);
                    String phone = c.getString(Column4);
                    String phoneCosmote = c.getString(Column5);
                    String phoneVodafone = c.getString(Column6);
                    String phoneWind = c.getString(Column7);
                    String closeDay = c.getString(Column10);
                    String openingHour = c.getString(Column8);
                    String closingHour = c.getString(Column9);
                    String image = c.getString(Column11);
                    String trueName = c.getString(Column12);
                    String Facebook = c.getString(Column13);
                    String Address = c.getString(Column14);

                    restaurants.add(new Restaurant( trueName, name,
                            description, phone, phoneCosmote, phoneVodafone,
                            phoneWind, closeDay, openingHour, closingHour,
                            image, Facebook, Address,false));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myDataBase.close();
        }

        return restaurants;

    }

    public Menu getMenuFromDatabase(String restaurantTableName) {
      //  Log.d("Ekdosi Vasis:", "---->" + DATABASE_VERSION);
        ArrayList<Food> foods = new ArrayList<Food>();

        try {
            Cursor c = myDataBase.rawQuery("SELECT * FROM "
                    + restaurantTableName, null);

            int Column1 = c.getColumnIndex("_id");
            int Column2 = c.getColumnIndex("name");
            int Column3 = c.getColumnIndex("description");
            int Column4 = c.getColumnIndex("category");
            int Column5 = c.getColumnIndex("price");
            c.moveToFirst();

            if (c != null) {
                // Loop through all Results
                do {
                    int id = c.getInt(Column1);
                    String name = c.getString(Column2);
                    String description = c.getString(Column3);
                    String category = c.getString(Column4);
                    String price = c.getString(Column5);

                    foods.add(new Food( name, description, category, price));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myDataBase.close();
        }

        return new Menu(foods);

    }

}