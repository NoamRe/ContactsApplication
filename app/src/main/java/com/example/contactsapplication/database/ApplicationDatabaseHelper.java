package com.example.contactsapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplicationDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contacts.db";
    private static final String SQL_CREATE_CONTACTS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ContactsApplicationContract.Contacts.TABLE_NAME + " (" +
                    ContactsApplicationContract.Contacts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_FirstName + " TEXT NOT NULL," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_LastName + " TEXT," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber + " TEXT NOT NULL," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneType + " TEXT," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_Address + " TEXT," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_City + " TEXT," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_Email + " TEXT," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_DateOfBirth + " DATE," +
                    ContactsApplicationContract.Contacts.COLUMN_NAME_Gender + " TEXT)";
    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ContactsApplicationContract.Users.TABLE_NAME + " (" +
                    ContactsApplicationContract.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ContactsApplicationContract.Users.COLUMN_NAME_Username + " TEXT NOT NULL UNIQUE," +
                    ContactsApplicationContract.Users.COLUMN_NAME_Password + " TEXT)";
    private static final String SQL_CREATE_USERSCONTACTS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ContactsApplicationContract.UsersContacts.TABLE_NAME + " (" +
                    ContactsApplicationContract.UsersContacts.COLUMN_NAME_UserID + " INTEGER NOT NULL," +
                    ContactsApplicationContract.UsersContacts.COLUMN_NAME_ContactID + " INTEGER NOT NULL," +
                    "PRIMARY KEY (UserID, ContactID)," +
                    "FOREIGN KEY (UserID) REFERENCES Users(" + ContactsApplicationContract.Users._ID + ")," +
                    "FOREIGN KEY (ContactID) REFERENCES Contacts(" + ContactsApplicationContract.Contacts._ID + "))";

    public ApplicationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CONTACTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERSCONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
