package com.example.contactsapplication.logicengine;

import org.mindrot.jbcrypt.BCrypt;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapplication.activities.LandingActivity;
import com.example.contactsapplication.database.ApplicationDatabaseHelper;
import com.example.contactsapplication.database.ContactsApplicationContract;
import com.example.contactsapplication.datamodels.Contact;
import com.example.contactsapplication.genderapi.GenderApiResponse;
import com.example.contactsapplication.genderapi.RetrofitInstance;
import com.example.contactsapplication.interfaces.IGenderApiCallback;
import com.example.contactsapplication.interfaces.IGenderApiService;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogicEngineManager {
    private static LogicEngineManager s_Instance = null;
    private final ApplicationDatabaseHelper m_DatabaseHelper;
    private ArrayList<Contact> m_ContactsList;
    private String m_CurrentUsername;

    private LogicEngineManager() {
        m_DatabaseHelper = new ApplicationDatabaseHelper(LandingActivity.getAppContext());
    }

    private void initializeContactsListFromDatabase() {
        m_ContactsList = new ArrayList<>();
        try (SQLiteDatabase database = m_DatabaseHelper.getReadableDatabase()) {
            String[] projection = {ContactsApplicationContract.Contacts.COLUMN_NAME_FirstName, ContactsApplicationContract.Contacts.COLUMN_NAME_LastName,
                    ContactsApplicationContract.Contacts.COLUMN_NAME_Gender, ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber,
                    ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneType, ContactsApplicationContract.Contacts.COLUMN_NAME_Address,
                    ContactsApplicationContract.Contacts.COLUMN_NAME_City, ContactsApplicationContract.Contacts.COLUMN_NAME_Email,
                    ContactsApplicationContract.Contacts.COLUMN_NAME_DateOfBirth};
            String tables = ContactsApplicationContract.Contacts.TABLE_NAME + " AS c" +
                    " INNER JOIN " + ContactsApplicationContract.UsersContacts.TABLE_NAME + " AS uc" +
                    " ON c." + ContactsApplicationContract.Contacts._ID + " = uc." + ContactsApplicationContract.UsersContacts.COLUMN_NAME_ContactID;
            String selection = "uc." + ContactsApplicationContract.UsersContacts.COLUMN_NAME_UserID + " = (SELECT " +
                    ContactsApplicationContract.Users._ID + " FROM " + ContactsApplicationContract.Users.TABLE_NAME +
                    " WHERE " + ContactsApplicationContract.Users.COLUMN_NAME_Username + " = ?)";
            String[] selectionArgs = {m_CurrentUsername};
            try (Cursor cursor = database.query(tables, projection, selection, selectionArgs, null, null, null)) {
                while (cursor.moveToNext()) {
                    m_ContactsList.add(new Contact(cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_FirstName)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_LastName)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_Gender)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneType)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_Address)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_City)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_Email)),
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Contacts.COLUMN_NAME_DateOfBirth))));
                }
            }
        }
    }

    public ArrayList<Contact> GetContactsList() {
        return m_ContactsList;
    }

    public static synchronized LogicEngineManager GetInstance() {
        if (s_Instance == null) {
            s_Instance = new LogicEngineManager();
        }

        return s_Instance;
    }

    public boolean DoesUserExist(String i_Username) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM " + ContactsApplicationContract.Users.TABLE_NAME +
                " WHERE " + ContactsApplicationContract.Users.COLUMN_NAME_Username + "= ?";

        try (SQLiteDatabase database = m_DatabaseHelper.getReadableDatabase()) {
            try (Cursor cursor = database.rawQuery(query, new String[]{i_Username})) {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        count = cursor.getInt(0);
                    }
                }
            }
        }

        return count > 0;
    }

    public boolean IsPasswordCorrect(String i_Username, String i_Password) {
        boolean IsPasswordCorrect = false;
        String query = "SELECT " + ContactsApplicationContract.Users.COLUMN_NAME_Password +
                " FROM " + ContactsApplicationContract.Users.TABLE_NAME +
                " WHERE " + ContactsApplicationContract.Users.COLUMN_NAME_Username + "= ?";

        try (SQLiteDatabase database = m_DatabaseHelper.getReadableDatabase()) {
            try (Cursor cursor = database.rawQuery(query, new String[]{i_Username})) {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        String hashedPasswordFromDatabase = cursor.getString(0);
                        IsPasswordCorrect = BCrypt.checkpw(i_Password, hashedPasswordFromDatabase);
                    }
                }
            }
        }

        return IsPasswordCorrect;
    }

    public void Login(String i_Username) {
        m_CurrentUsername = i_Username;
        initializeContactsListFromDatabase();
    }

    public void Signup(String i_Username, String i_Password) {
        String hashedPassword = BCrypt.hashpw(i_Password, BCrypt.gensalt());
        ContentValues values = new ContentValues();

        values.put(ContactsApplicationContract.Users.COLUMN_NAME_Username, i_Username);
        values.put(ContactsApplicationContract.Users.COLUMN_NAME_Password, hashedPassword);
        try (SQLiteDatabase database = m_DatabaseHelper.getWritableDatabase()) {
            if (database.insert(ContactsApplicationContract.Users.TABLE_NAME, null, values) == -1) {
                throw new SQLiteException("User insertion failed of user: " + i_Username);
            }
        }

        Login(i_Username);
    }

    public boolean DoesPhoneNumberExistForCurrentUser(String i_PhoneNumber) {
        int count = 0;
        String tables = ContactsApplicationContract.Contacts.TABLE_NAME + " AS c" +
                " INNER JOIN " + ContactsApplicationContract.UsersContacts.TABLE_NAME + " AS uc" +
                " ON c." + ContactsApplicationContract.Contacts._ID + " = uc." + ContactsApplicationContract.UsersContacts.COLUMN_NAME_ContactID;
        String selection = "uc." + ContactsApplicationContract.UsersContacts.COLUMN_NAME_UserID + " = (SELECT " +
                ContactsApplicationContract.Users._ID + " FROM " + ContactsApplicationContract.Users.TABLE_NAME +
                " WHERE " + ContactsApplicationContract.Users.COLUMN_NAME_Username + " = ?" +
                " AND c." + ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber + " = ?)";
        String query = "SELECT COUNT(*) FROM " + tables +
                " WHERE " + selection;

        try (SQLiteDatabase database = m_DatabaseHelper.getReadableDatabase()) {
            try (Cursor cursor = database.rawQuery(query, new String[]{m_CurrentUsername, i_PhoneNumber})) {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        count = cursor.getInt(0);
                    }
                }
            }
        }

        return count > 0;
    }

    public void AddContactForCurrentUser(String i_FirstName, String i_LastName, String i_PhoneNumber, String i_PhoneType, String i_Address, String i_City,
                                         String i_Email, String i_DateOfBirth, RecyclerView.Adapter i_Adapter) {
        m_ContactsList.add(new Contact(i_FirstName, i_LastName, "", i_PhoneNumber, i_PhoneType, i_Address, i_City, i_Email, i_DateOfBirth));
        getGenderByNameFromAPI(i_FirstName, i_Gender -> {
            m_ContactsList.get(FindContactPositionInListByPhoneNumber(i_PhoneNumber)).SetGender(i_Gender);
            ContentValues genderValues = new ContentValues();
            String whereClause = ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber + " = ?";
            String[] whereArgs = {i_PhoneNumber};

            genderValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_Gender, i_Gender);
            try (SQLiteDatabase database = m_DatabaseHelper.getWritableDatabase()) {
                database.update(ContactsApplicationContract.Contacts.TABLE_NAME, genderValues, whereClause, whereArgs);
                i_Adapter.notifyItemChanged(m_ContactsList.size() - 1);
            }
        });
        ContentValues contactValues = new ContentValues();

        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_FirstName, i_FirstName);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_LastName, i_LastName);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_Gender, "");
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber, i_PhoneNumber);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneType, i_PhoneType);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_Address, i_Address);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_City, i_City);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_Email, i_Email);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_DateOfBirth, i_DateOfBirth);
        try (SQLiteDatabase database = m_DatabaseHelper.getWritableDatabase()) {
            long contactRowId = database.insert(ContactsApplicationContract.Contacts.TABLE_NAME, null, contactValues);

            if (contactRowId == -1) {
                throw new SQLiteException("Could not add contact " + i_PhoneNumber + " to the database.");
            } else {
                ContentValues usersContactsValues = new ContentValues();

                usersContactsValues.put(ContactsApplicationContract.UsersContacts.COLUMN_NAME_UserID, getUserIDByUsername(m_CurrentUsername, database));
                usersContactsValues.put(ContactsApplicationContract.UsersContacts.COLUMN_NAME_ContactID, contactRowId);
                if (database.insert(ContactsApplicationContract.UsersContacts.TABLE_NAME, null, usersContactsValues) == -1) {
                    throw new SQLiteException("Failed to associate contact " + i_PhoneNumber + " with user " + m_CurrentUsername);
                }
            }
        }
    }

    private void getGenderByNameFromAPI(String i_FirstName, IGenderApiCallback i_Callback) {
        IGenderApiService apiService = RetrofitInstance.GetGenderApiServiceInstance();
        Call<GenderApiResponse> call = apiService.GetGenderInfo(i_FirstName);
        final String[] gender = new String[1];

        call.enqueue(new Callback<GenderApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenderApiResponse> call, @NonNull Response<GenderApiResponse> response) {
                if (response.isSuccessful()) {
                    GenderApiResponse responseData = response.body();
                    assert responseData != null;
                    gender[0] = responseData.GetGender();
                } else {
                    gender[0] = "unspecified";
                }

                i_Callback.OnGenderReceived(gender[0]);
            }

            @Override
            public void onFailure(@NonNull Call<GenderApiResponse> call, @NonNull Throwable t) {
                gender[0] = "unspecified";
                i_Callback.OnGenderReceived(gender[0]);
            }
        });
    }

    public void UpdateContactForCurrentUser(String i_FirstName, String i_LastName, String i_Gender, String i_PhoneNumber, String i_PhoneType, String i_Address, String i_City,
                                            String i_Email, String i_DateOfBirth) {
        ContentValues contactValues = new ContentValues();
        String selection = ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber + " = ?";
        String[] selectionArgs = {i_PhoneNumber};
        Contact contactToUpdate = GetContactsList().get(FindContactPositionInListByPhoneNumber(i_PhoneNumber));
        contactToUpdate.SetFirstName(i_FirstName);
        contactToUpdate.SetLastName(i_LastName);
        contactToUpdate.SetGender(i_Gender);
        contactToUpdate.SetPhoneNumber(i_PhoneNumber);
        contactToUpdate.SetPhoneType(i_PhoneType);
        contactToUpdate.SetAddress(i_Address);
        contactToUpdate.SetCity(i_City);
        contactToUpdate.SetEmail(i_Email);
        contactToUpdate.SetDateOfBirth(i_DateOfBirth);

        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_FirstName, i_FirstName);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_LastName, i_LastName);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_Gender, i_Gender);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneType, i_PhoneType);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_Address, i_Address);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_City, i_City);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_Email, i_Email);
        contactValues.put(ContactsApplicationContract.Contacts.COLUMN_NAME_DateOfBirth, i_DateOfBirth);
        try (SQLiteDatabase database = m_DatabaseHelper.getWritableDatabase()) {
            if (database.update(
                    ContactsApplicationContract.Contacts.TABLE_NAME,
                    contactValues,
                    selection,
                    selectionArgs) == 0) {
                throw new SQLiteException("Failed to update contact " + i_PhoneNumber);
            }
        }
    }

    private long getUserIDByUsername(String i_Username, SQLiteDatabase i_Database) {
        String[] projection = {ContactsApplicationContract.Users._ID};
        String selection = ContactsApplicationContract.Users.COLUMN_NAME_Username + " = ?";
        String[] selectionArgs = {i_Username};

        try (Cursor cursor = i_Database.query(
                ContactsApplicationContract.Users.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndexOrThrow(ContactsApplicationContract.Users._ID));
            } else {
                throw new SQLiteException("User " + i_Username + " not found.");
            }
        }
    }

    public int FindContactPositionInListByPhoneNumber(String i_PhoneNumber) {
        int position = -1;

        for (Contact contact : m_ContactsList) {
            position++;
            if (Objects.equals(contact.GetPhoneNumber(), i_PhoneNumber)) {
                return position;
            }
        }

        return position;
    }

    public int DeleteContactByPhoneNumber(String i_PhoneNumber) {
        int position = FindContactPositionInListByPhoneNumber(i_PhoneNumber);

        try (SQLiteDatabase database = m_DatabaseHelper.getWritableDatabase()) {
            String selection = ContactsApplicationContract.UsersContacts.COLUMN_NAME_UserID + " = (SELECT " +
                    ContactsApplicationContract.Users._ID + " FROM " + ContactsApplicationContract.Users.TABLE_NAME +
                    " WHERE " + ContactsApplicationContract.Users.COLUMN_NAME_Username + " = ?" +
                    ") AND " + ContactsApplicationContract.UsersContacts.COLUMN_NAME_ContactID + " IN (SELECT " +
                    ContactsApplicationContract.Contacts._ID + " FROM " + ContactsApplicationContract.Contacts.TABLE_NAME +
                    " WHERE " + ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber + " = ?)";
            String[] selectionArgs = {m_CurrentUsername, i_PhoneNumber};

            database.delete(
                    ContactsApplicationContract.UsersContacts.TABLE_NAME,
                    selection,
                    selectionArgs);
            m_ContactsList.remove(position);

            boolean contactExistsInUsersContacts = false;

            String checkContactExistsQuery = "SELECT COUNT(*) FROM " + ContactsApplicationContract.Contacts.TABLE_NAME + " AS c" +
                    " INNER JOIN " + ContactsApplicationContract.UsersContacts.TABLE_NAME + " AS uc" +
                    " ON c." + ContactsApplicationContract.Contacts._ID + " = uc." + ContactsApplicationContract.UsersContacts.COLUMN_NAME_ContactID +
                    " WHERE " + ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber + " = ?";
            String[] checkContactExistsSelectionArgs = {String.valueOf(i_PhoneNumber)};

            try (Cursor checkCursor = database.rawQuery(checkContactExistsQuery, checkContactExistsSelectionArgs)) {
                if (checkCursor != null) {
                    if (checkCursor.moveToFirst()) {
                        int count = checkCursor.getInt(0);
                        contactExistsInUsersContacts = count > 0;
                        Log.i("COUNT", "COUNT: " + count);
                    }
                }
            }

            if (!contactExistsInUsersContacts) {
                String contactsSelection = ContactsApplicationContract.Contacts.COLUMN_NAME_PhoneNumber + " = ?";
                String[] contactsSelectionArgs = {String.valueOf(i_PhoneNumber)};

                database.delete(
                        ContactsApplicationContract.Contacts.TABLE_NAME,
                        contactsSelection,
                        contactsSelectionArgs
                );
            }

            try (Cursor cursor = database.rawQuery("SELECT * FROM Contacts", null)) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int columnIndex = 0;
                        int columnCount = cursor.getColumnCount();
                        StringBuilder queryResult = new StringBuilder("Query Result: ");

                        // Append column names and values to the queryResult StringBuilder
                        for (columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                            String columnName = cursor.getColumnName(columnIndex);
                            String columnValue = cursor.getString(columnIndex);
                            queryResult.append(columnName).append(": ").append(columnValue).append(", ");
                        }

                        // Remove the trailing ", " and log the result
                        if (queryResult.length() > 2) {
                            queryResult.delete(queryResult.length() - 2, queryResult.length());
                        }

                        Log.i("QueryLog", queryResult.toString());
                    }
                }
            }

            return position;
        }
    }
}