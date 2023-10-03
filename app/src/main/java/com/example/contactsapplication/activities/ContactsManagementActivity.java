package com.example.contactsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsapplication.datamodels.ContactField;
import com.example.contactsapplication.datamodels.Contact;
import com.example.contactsapplication.logicengine.LogicEngineManager;
import com.example.contactsapplication.R;
import com.example.contactsapplication.adapters.ContactsAdapter;
import com.example.contactsapplication.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ContactsManagementActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView m_RecyclerviewContactsList;
    private Button m_ButtonAddContact;
    private LogicEngineManager m_LogicEngineManager;
    private Dialog m_DialogContactEditingAddition;
    private TextView m_TextViewDialogTitle;
    private Button m_ButtonContactDialogBack;
    private Button m_ButtonContactsDialogSubmit;
    private Button m_ButtonContactsDialogDelete;
    private EditText m_EditTextFirstName;
    private EditText m_EditTextLastName;
    private EditText m_EditTextGender;
    private EditText m_EditTextPhoneNumber;
    private EditText m_EditTextPhoneType;
    private EditText m_EditTextAddress;
    private EditText m_EditTextCity;
    private EditText m_EditTextEmail;
    private EditText m_EditTextDateOfBirth;
    private ContactsAdapter m_ContactsAdapter;
    private Button m_ButtonDisplaySettings;
    private Dialog m_DialogDisplaySettings;
    private Button m_ButtonDisplayDialogSubmit;

    private CheckBox m_CheckBoxFirstName;
    private CheckBox m_CheckBoxLastName;
    private CheckBox m_CheckBoxGender;
    private CheckBox m_CheckBoxPhoneNumber;
    private CheckBox m_CheckBoxPhoneType;
    private CheckBox m_CheckBoxAddress;
    private CheckBox m_CheckBoxCity;
    private CheckBox m_CheckBoxEmail;
    private CheckBox m_CheckBoxDateOfBirth;
    private HashMap<ContactField, Boolean> m_ContactsFieldHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_management);
        initializeMembers();
    }

    @Override
    public void OnItemClick(int i_Position) {
        Contact contact = m_LogicEngineManager.GetContactsList().get(i_Position);

        m_TextViewDialogTitle.setText("Edit Contact:");
        m_EditTextFirstName.setText(contact.GetFirstName());
        m_EditTextLastName.setText(contact.GetLastName());
        m_EditTextGender.setVisibility(TextView.VISIBLE);
        m_EditTextGender.setText(contact.GetGender());
        m_EditTextPhoneNumber.setText(contact.GetPhoneNumber());
        m_EditTextPhoneType.setText(contact.GetPhoneType());
        m_EditTextAddress.setText(contact.GetAddress());
        m_EditTextCity.setText(contact.GetCity());
        m_EditTextEmail.setText(contact.GetEmail());
        m_EditTextDateOfBirth.setText(contact.GetDateOfBirth());
        m_ButtonContactsDialogDelete.setVisibility(View.VISIBLE);
        m_DialogContactEditingAddition.show();
    }

    private void initializeMembers() {
        m_RecyclerviewContactsList = (RecyclerView) findViewById(R.id.recyclerViewContactsList);
        m_LogicEngineManager = LogicEngineManager.GetInstance();
        initializeRecyclerViewFields();
        m_ContactsAdapter = new ContactsAdapter(m_LogicEngineManager.GetContactsList(), this, m_ContactsFieldHashMap);
        m_RecyclerviewContactsList.setAdapter(m_ContactsAdapter);
        initializeDialogs();
        m_TextViewDialogTitle = (TextView) m_DialogContactEditingAddition.findViewById(R.id.textViewAddEditContactTitle);
        initializeEditTexts();
        initializeButtons();
        initializeCheckBoxes();

    }

    private void initializeRecyclerViewFields() {
        m_ContactsFieldHashMap = new HashMap<>();

        m_ContactsFieldHashMap.put(ContactField.FIRST_NAME, true);
        m_ContactsFieldHashMap.put(ContactField.LAST_NAME, true);
        m_ContactsFieldHashMap.put(ContactField.GENDER, true);
        m_ContactsFieldHashMap.put(ContactField.PHONE_NUMBER, true);
    }

    private void initializeDialogs() {
        m_DialogContactEditingAddition = new Dialog(ContactsManagementActivity.this);
        m_DialogContactEditingAddition.setContentView(R.layout.dialog_add_edit_contact);
        m_DialogContactEditingAddition.setOnCancelListener(view -> handleContactDialogCancellationOrDismission());
        m_DialogContactEditingAddition.setOnDismissListener(view -> handleContactDialogCancellationOrDismission());
        m_DialogDisplaySettings = new Dialog(ContactsManagementActivity.this);
        m_DialogDisplaySettings.setContentView(R.layout.dialog_display_settings);
        m_DialogDisplaySettings.setCancelable(false);
        m_DialogDisplaySettings.setOnDismissListener(view -> handleDisplayDialogDismission());
    }

    private void initializeButtons() {
        m_ButtonAddContact = (Button) findViewById(R.id.buttonAddContact);
        m_ButtonAddContact.setOnClickListener(view -> handleAddContact());
        m_ButtonContactDialogBack = (Button) m_DialogContactEditingAddition.findViewById(R.id.buttonBackContactDialog);
        m_ButtonContactsDialogSubmit = (Button) m_DialogContactEditingAddition.findViewById(R.id.buttonSubmit);
        m_ButtonContactsDialogDelete = (Button) m_DialogContactEditingAddition.findViewById(R.id.buttonDelete);
        m_ButtonContactDialogBack.setOnClickListener(view -> m_DialogContactEditingAddition.dismiss());
        m_ButtonContactsDialogSubmit.setOnClickListener(view -> handleContactSubmission());
        m_ButtonContactsDialogDelete.setOnClickListener(view -> handleContactDeletion());
        m_ButtonDisplaySettings = (Button) findViewById(R.id.buttonDisplaySettings);
        m_ButtonDisplayDialogSubmit = (Button) m_DialogDisplaySettings.findViewById(R.id.buttonSubmitDisplayDialog);
        m_ButtonDisplaySettings.setOnClickListener(view -> m_DialogDisplaySettings.show());
        m_ButtonDisplayDialogSubmit.setOnClickListener(view -> m_DialogDisplaySettings.dismiss());
    }

    private void handleDisplayDialogDismission() {
        ArrayList<Contact> contactsList = m_LogicEngineManager.GetContactsList();

        m_ContactsFieldHashMap.put(ContactField.FIRST_NAME, m_CheckBoxFirstName.isChecked());
        m_ContactsFieldHashMap.put(ContactField.LAST_NAME, m_CheckBoxLastName.isChecked());
        m_ContactsFieldHashMap.put(ContactField.GENDER, m_CheckBoxGender.isChecked());
        m_ContactsFieldHashMap.put(ContactField.PHONE_NUMBER, m_CheckBoxPhoneNumber.isChecked());
        m_ContactsFieldHashMap.put(ContactField.PHONE_TYPE, m_CheckBoxPhoneType.isChecked());
        m_ContactsFieldHashMap.put(ContactField.ADDRESS, m_CheckBoxAddress.isChecked());
        m_ContactsFieldHashMap.put(ContactField.CITY, m_CheckBoxCity.isChecked());
        m_ContactsFieldHashMap.put(ContactField.EMAIL, m_CheckBoxEmail.isChecked());
        m_ContactsFieldHashMap.put(ContactField.DATE_OF_BIRTH, m_CheckBoxDateOfBirth.isChecked());
        ((ContactsAdapter) Objects.requireNonNull(m_RecyclerviewContactsList.getAdapter())).SetContactsFieldsHashMap(m_ContactsFieldHashMap);

        for (int i = 0; i < contactsList.size(); i++) {
            Objects.requireNonNull(m_RecyclerviewContactsList.getAdapter()).notifyItemChanged(i);
        }
    }

    private void initializeEditTexts() {
        m_EditTextFirstName = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextFirstName);
        m_EditTextLastName = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextLastName);
        m_EditTextGender = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextGender);
        m_EditTextPhoneNumber = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextPhoneNumber);
        m_EditTextPhoneType = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextPhoneType);
        m_EditTextAddress = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextAddress);
        m_EditTextCity = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextCity);
        m_EditTextEmail = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextEmail);
        m_EditTextDateOfBirth = (EditText) m_DialogContactEditingAddition.findViewById(R.id.editTextDateOfBirth);
    }

    private void initializeCheckBoxes() {
        m_CheckBoxFirstName = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxFirstName);
        m_CheckBoxLastName = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxLastName);
        m_CheckBoxGender = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxGender);
        m_CheckBoxPhoneNumber = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxPhoneNumber);
        m_CheckBoxPhoneType = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxPhoneType);
        m_CheckBoxAddress = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxAddress);
        m_CheckBoxCity = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxCity);
        m_CheckBoxEmail = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxEmail);
        m_CheckBoxDateOfBirth = (CheckBox) m_DialogDisplaySettings.findViewById(R.id.checkboxDateOfBirth);
    }

    private void handleContactDialogCancellationOrDismission() {
        m_EditTextFirstName.setText("");
        m_EditTextLastName.setText("");
        m_EditTextPhoneNumber.setText("");
        m_EditTextPhoneType.setText("");
        m_EditTextAddress.setText("");
        m_EditTextCity.setText("");
        m_EditTextEmail.setText("");
        m_EditTextDateOfBirth.setText("");
        m_EditTextGender.setVisibility(View.GONE);
        m_ButtonContactsDialogDelete.setVisibility(View.GONE);
    }

    private void handleContactDeletion() {
        //TODO: add deletion button option when editing a contact.
        String phoneNumber = m_EditTextPhoneNumber.getText().toString();
        int position = m_LogicEngineManager.DeleteContactByPhoneNumber(phoneNumber);
        Objects.requireNonNull(m_RecyclerviewContactsList.getAdapter()).notifyItemRemoved(position);
        m_DialogContactEditingAddition.dismiss();
    }

    private void handleAddContact() {
        m_TextViewDialogTitle.setText("Add Contact:");
        m_DialogContactEditingAddition.show();
    }

    private void handleContactSubmission() {
        String firstName = m_EditTextFirstName.getText().toString();
        String lastName = m_EditTextLastName.getText().toString();
        String phoneNumber = m_EditTextPhoneNumber.getText().toString();

        if (areFirstLastNameAndPhoneNumberFieldsValid(firstName, lastName, phoneNumber)) {
            boolean doesContactExist = m_LogicEngineManager.DoesPhoneNumberExistForCurrentUser(phoneNumber);

            if (m_TextViewDialogTitle.getText().toString().equals("Add Contact:") && doesContactExist) {
                Toast.makeText(this, "Phone number " + phoneNumber + " already exists! Please edit the contact.", Toast.LENGTH_SHORT).show();
            } else {
                String phoneType = m_EditTextPhoneType.getText().toString();
                String address = m_EditTextAddress.getText().toString();
                String city = m_EditTextCity.getText().toString();
                String email = m_EditTextEmail.getText().toString();
                String dateOfBirth = m_EditTextDateOfBirth.getText().toString();

                if (doesContactExist) {
                    String gender = m_EditTextGender.getText().toString();
                    m_LogicEngineManager.UpdateContactForCurrentUser(firstName, lastName, gender, phoneNumber, phoneType, address, city, email, dateOfBirth);
                    Objects.requireNonNull(m_RecyclerviewContactsList.getAdapter()).notifyItemChanged(m_LogicEngineManager.FindContactPositionInListByPhoneNumber(phoneNumber));
                } else {
                    m_LogicEngineManager.AddContactForCurrentUser(firstName, lastName, phoneNumber, phoneType, address, city, email, dateOfBirth, m_RecyclerviewContactsList.getAdapter());
                    Objects.requireNonNull(m_RecyclerviewContactsList.getAdapter()).notifyItemInserted(m_LogicEngineManager.GetContactsList().size() - 1);
                }

                m_DialogContactEditingAddition.dismiss();
            }
        }
    }

    private boolean areFirstLastNameAndPhoneNumberFieldsValid(String i_FirstName, String i_LastName, String i_PhoneNumber) {
        boolean areFirstLastNameAndPhoneNumberFieldsValid = true;

        if (i_FirstName.isEmpty()) {
            Toast.makeText(this, "Please enter a first name!", Toast.LENGTH_SHORT).show();
            areFirstLastNameAndPhoneNumberFieldsValid = false;
        }

        if (i_LastName.isEmpty()) {
            Toast.makeText(this, "Please enter a last name!", Toast.LENGTH_SHORT).show();
            areFirstLastNameAndPhoneNumberFieldsValid = false;
        }

        if (i_PhoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
            areFirstLastNameAndPhoneNumberFieldsValid = false;
        }

        return areFirstLastNameAndPhoneNumberFieldsValid;
    }
}