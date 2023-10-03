package com.example.contactsapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapplication.datamodels.ContactField;
import com.example.contactsapplication.datamodels.Contact;
import com.example.contactsapplication.R;
import com.example.contactsapplication.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private final ArrayList<Contact> m_ContactsList;
    private final OnItemClickListener m_OnItemClickListener;
    private HashMap<ContactField, Boolean> m_ContactsFieldHashMap;

    public ContactsAdapter(ArrayList<Contact> i_ContactsList, OnItemClickListener i_OnItemClickListener, HashMap<ContactField, Boolean> i_ContactsFieldHashMap) {
        m_ContactsList = i_ContactsList;
        m_OnItemClickListener = i_OnItemClickListener;
        m_ContactsFieldHashMap = i_ContactsFieldHashMap;
    }

    public void SetContactsFieldsHashMap(HashMap<ContactField, Boolean> i_ContactsFieldsHashMap) {
        m_ContactsFieldHashMap = i_ContactsFieldsHashMap;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView m_TextViewFirstName;
        private final TextView m_TextViewLastName;
        private final TextView m_TextViewGender;
        private final TextView m_TextViewPhoneNumber;
        private final TextView m_TextViewPhoneType;
        private final TextView m_TextViewAddress;
        private final TextView m_TextViewCity;
        private final TextView m_TextViewEmail;
        private final TextView m_TextViewDateOfBirth;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            m_TextViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            m_TextViewLastName = itemView.findViewById(R.id.textViewLastName);
            m_TextViewGender = itemView.findViewById(R.id.textViewGender);
            m_TextViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            m_TextViewPhoneType = itemView.findViewById(R.id.textViewPhoneType);
            m_TextViewAddress = itemView.findViewById(R.id.textViewAddress);
            m_TextViewCity = itemView.findViewById(R.id.textViewCity);
            m_TextViewEmail = itemView.findViewById(R.id.textViewEmail);
            m_TextViewDateOfBirth = itemView.findViewById(R.id.textViewDateOfBirth);
            ((ViewGroup) itemView).getChildAt(0).setOnClickListener(view -> m_OnItemClickListener.OnItemClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    private void setTextViewVisibilityByField(TextView i_TextView, ContactField i_Field, String i_NewText) {
        if (Boolean.TRUE.equals(m_ContactsFieldHashMap.get(i_Field))) {
            i_TextView.setText(i_NewText);
            i_TextView.setVisibility(View.VISIBLE);
        } else {
            i_TextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactViewHolder holder, int position) {
        Contact contact = m_ContactsList.get(position);

        setTextViewVisibilityByField(holder.m_TextViewFirstName, ContactField.FIRST_NAME, contact.GetFirstName());
        setTextViewVisibilityByField(holder.m_TextViewLastName, ContactField.LAST_NAME, contact.GetLastName());
        setTextViewVisibilityByField(holder.m_TextViewGender, ContactField.GENDER, contact.GetGender());
        setTextViewVisibilityByField(holder.m_TextViewPhoneNumber, ContactField.PHONE_NUMBER, contact.GetPhoneNumber());
        setTextViewVisibilityByField(holder.m_TextViewPhoneType, ContactField.PHONE_TYPE, contact.GetPhoneType());
        setTextViewVisibilityByField(holder.m_TextViewAddress, ContactField.ADDRESS, contact.GetAddress());
        setTextViewVisibilityByField(holder.m_TextViewCity, ContactField.CITY, contact.GetCity());
        setTextViewVisibilityByField(holder.m_TextViewEmail, ContactField.EMAIL, contact.GetEmail());
        setTextViewVisibilityByField(holder.m_TextViewDateOfBirth, ContactField.DATE_OF_BIRTH, contact.GetDateOfBirth());
    }

    @Override
    public int getItemCount() {
        return m_ContactsList.size();
    }
}
