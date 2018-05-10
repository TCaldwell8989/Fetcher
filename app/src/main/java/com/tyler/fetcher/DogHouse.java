package com.tyler.fetcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tyler.fetcher.database.DbCursorWrapper;
import com.tyler.fetcher.database.DbHelper;
import com.tyler.fetcher.database.DbSchema.DogParkTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Database Management
public class DogHouse {

    private static DogHouse sDogHouse;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DogHouse get(Context context) {
        if (sDogHouse == null) {
            sDogHouse = new DogHouse(context);
        }
        return sDogHouse;
    }

    private DogHouse(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbHelper(mContext)
                .getWritableDatabase();
    }

    public void addDogPark(DogPark park) {
        ContentValues values = getContentValues(park);

        mDatabase.insert(DogParkTable.NAME, null, values);
    }

    public boolean deleteDogPark(DogPark park) {
        String where = DogParkTable.Cols.UUID + " = ? ";
        String[] whereArgs = { park.getId().toString() };
        int rowsDeleted = mDatabase.delete(DogParkTable.NAME, where, whereArgs);

        if (rowsDeleted == 1) {
            return true;
        }

        return false;
    }

    public List<DogPark> getDogPark() {
        List<DogPark> dogParks = new ArrayList<>();
        DbCursorWrapper cursor = queryDogParks(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dogParks.add(cursor.getDogPark());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return dogParks;
    }

    public List<DogPark> getDogParks(String where, String[] whereArgs) {
        List<DogPark> dogParks = new ArrayList<>();
        DbCursorWrapper cursor = queryDogParks(where, whereArgs);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dogParks.add(cursor.getDogPark());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return dogParks;
    }

    public DogPark getDogPark(UUID id) {
        DbCursorWrapper cursor = queryDogParks(
                DogParkTable.Cols.UUID + " = ?",
                new String[] { id.toString() });

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getDogPark();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(DogPark park) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, park.getPhotoFilename());
    }

    public void updateDogPark(DogPark park) {
        String uuidString = park.getId().toString();
        ContentValues values = getContentValues(park);

        mDatabase.update(DogParkTable.NAME, values,
                DogParkTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private DbCursorWrapper queryDogParks(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DogParkTable.NAME, null, where, whereArgs,
                null, null, DogParkTable.Cols.CREATED + " DESC");
        return new DbCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(DogPark park) {
        ContentValues values = new ContentValues();
        values.put(DogParkTable.Cols.UUID, park.getId().toString());
        values.put(DogParkTable.Cols.NAME, park.getName());
        values.put(DogParkTable.Cols.LOCATION, park.getLocation());
        values.put(DogParkTable.Cols.NOTE, park.getNote());
        values.put(DogParkTable.Cols.RATING, park.getRating());

        return values;
    }
}

