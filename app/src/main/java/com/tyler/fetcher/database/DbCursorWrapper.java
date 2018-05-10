package com.tyler.fetcher.database;

import android.database.Cursor;

import com.tyler.fetcher.DogPark;
import com.tyler.fetcher.database.DbSchema.DogParkTable;

import java.util.UUID;

public class DbCursorWrapper extends android.database.CursorWrapper {
    public DbCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public DogPark getDogPark() {
        String uuidString = getString(getColumnIndex(DogParkTable.Cols.UUID));
        String name = getString(getColumnIndex(DogParkTable.Cols.NAME));
        String location = getString(getColumnIndex(DogParkTable.Cols.LOCATION));
        String note = getString(getColumnIndex(DogParkTable.Cols.NOTE));
        int rating = getInt(getColumnIndex(DogParkTable.Cols.RATING));

        DogPark park = new DogPark(UUID.fromString(uuidString));
        park.setName(name);
        park.setLocation(location);
        park.setNote(note);
        park.setRating(rating);

        return park;
    }
}
