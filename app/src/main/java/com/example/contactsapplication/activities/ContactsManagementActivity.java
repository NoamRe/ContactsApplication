package com.example.contactsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsapplication.datamodels.Contact;
import com.example.contactsapplication.logicengine.LogicEngineManager;
import com.example.contactsapplication.R;
import com.example.contactsapplication.adapters.ContactsAdapter;
import com.example.contactsapplication.interfaces.OnItemClickListener;

import java.util.Objects;

public class ContactsManagementActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView m_RecyclerviewContactsList;
    private Button m_ButtonAddContact;
    private LogicEngineManager m_LogicEngineManager;
    private Dialog m_ContactEditingAdditionDialog;
    private TextView m_TextViewDialogTitle;
    private Button m_ButtonDialogBack;
    private Button m_ButtonDialogSubmit;
    private Button m_ButtonDialogDelete;
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
        m_ButtonDialogDelete.setVisibility(View.VISIBLE);
        m_ContactEditingAdditionDialog.show();
    }

    private void initializeMembers() {
        m_RecyclerviewContactsList = (RecyclerView) findViewById(R.id.recyclerViewContactsList);
        m_ButtonAddContact = (Button) findViewById(R.id.buttonAddContact);
        m_LogicEngineManager = LogicEngineManager.GetInstance();
        m_ButtonAddContact.setOnClickListener(view -> handleAddContact());
        m_ContactsAdapter = new ContactsAdapter(m_LogicEngineManager.GetContactsList(), this);
        m_RecyclerviewContactsList.setAdapter(m_ContactsAdapter);
        m_ContactEditingAdditionDialog = new Dialog(ContactsManagementActivity.this);
        m_ContactEditingAdditionDialog.setContentView(R.layout.dialog_add_edit_contact);
        m_ContactEditingAdditionDialog.setOnCancelListener(view -> handleDialogCancellationOrDismission());
        m_ContactEditingAdditionDialog.setOnDismissListener(view -> handleDialogCancellationOrDismission());
        m_TextViewDialogTitle = (TextView) m_ContactEditingAdditionDialog.findViewById(R.id.textViewAddEditContactTitle);
        m_ButtonDialogBack = (Button) m_ContactEditingAdditionDialog.findViewById(R.id.buttonBack);
        m_ButtonDialogSubmit = (Button) m_ContactEditingAdditionDialog.findViewById(R.id.buttonSubmit);
        m_ButtonDialogDelete = (Button) m_ContactEditingAdditionDialog.findViewById(R.id.buttonDelete);
        m_EditTextFirstName = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextFirstName);
        m_EditTextLastName = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextLastName);
        m_EditTextGender = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextGender);
        m_EditTextPhoneNumber = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextPhoneNumber);
        m_EditTextPhoneType = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextPhoneType);
        m_EditTextAddress = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextAddress);
        m_EditTextCity = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextCity);
        m_EditTextEmail = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextEmail);
        m_EditTextDateOfBirth = (EditText) m_ContactEditingAdditionDialog.findViewById(R.id.editTextDateOfBirth);
        m_ButtonDialogBack.setOnClickListener(view -> m_ContactEditingAdditionDialog.dismiss());
        m_ButtonDialogSubmit.setOnClickListener(view -> handleContactSubmission());
        m_ButtonDialogDelete.setOnClickListener(view -> handleContactDeletion());
    }

    private void handleDialogCancellationOrDismission() {
        m_EditTextFirstName.setText("");
        m_EditTextLastName.setText("");
        m_EditTextPhoneNumber.setText("");
        m_EditTextPhoneType.setText("");
        m_EditTextAddress.setText("");
        m_EditTextCity.setText("");
        m_EditTextEmail.setText("");
        m_EditTextDateOfBirth.setText("");
        m_EditTextGender.setVisibility(View.GONE);
        m_ButtonDialogDelete.setVisibility(View.GONE);
    }

    private void handleContactDeletion() {
        //TODO: add deletion button option when editing a contact.
        String phoneNumber = m_EditTextPhoneNumber.getText().toString();
        int position = m_LogicEngineManager.DeleteContactByPhoneNumber(phoneNumber);
        Objects.requireNonNull(m_RecyclerviewContactsList.getAdapter()).notifyItemRemoved(position);
        m_ContactEditingAdditionDialog.dismiss();
    }

    private void handleAddContact() {
        m_TextViewDialogTitle.setText("Add Contact:");
        m_ContactEditingAdditionDialog.show();
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

                m_ContactEditingAdditionDialog.dismiss();
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