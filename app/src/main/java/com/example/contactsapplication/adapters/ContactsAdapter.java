package com.example.contactsapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapplication.datamodels.Contact;
import com.example.contactsapplication.R;
import com.example.contactsapplication.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private final ArrayList<Contact> m_ContactsList;
    private final OnItemClickListener m_OnItemClickListener;

    public ContactsAdapter(ArrayList<Contact> i_ContactsList, OnItemClickListener i_OnItemClickListener) {
        m_ContactsList = i_ContactsList;
        m_OnItemClickListener = i_OnItemClickListener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView m_TextViewFirstName;
        private final TextView m_TextViewLastName;
        private final TextView m_TextViewGender;
        private final TextView m_TextViewPhoneNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            m_TextViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            m_TextViewLastName = itemView.findViewById(R.id.textViewLastName);
            m_TextViewGender = itemView.findViewById(R.id.textViewGender);
            m_TextViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            itemView.setOnClickListener(view -> m_OnItemClickListener.OnItemClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactViewHolder holder, int position) {
        Contact contact = m_ContactsList.get(position);

        holder.m_TextViewFirstName.setText(contact.GetFirstName());
        holder.m_TextViewLastName.setText(contact.GetLastName());
        holder.m_TextViewGender.setText(contact.GetGender());
        holder.m_TextViewPhoneNumber.setText(contact.GetPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return m_ContactsList.size();
    }
}
