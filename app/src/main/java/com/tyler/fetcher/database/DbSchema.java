package com.tyler.fetcher.database;

// Class containing constants for identifying table and columns
public class DbSchema {
    public static final class DogParkTable {
        public static final String NAME = "DogParks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String LOCATION = "location";
            public static final String NOTE = "notes";
            public static final String RATING = "rating";
            public static final String CREATED = "created";
        }
    }
}