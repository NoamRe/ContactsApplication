package com.example.contactsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactsapplication.logicengine.LogicEngineManager;
import com.example.contactsapplication.R;

public class LandingActivity extends AppCompatActivity {
    private static Context s_AppContext;
    private LogicEngineManager m_LogicEngineManager;
    private EditText m_EditTextUsername;
    private EditText m_EditTextPassword;
    private Button m_ButtonLogin;
    private Button m_ButtonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initializeMembers();
    }

    public static Context getAppContext() {
        return s_AppContext;
    }

    private void initializeMembers() {
        m_EditTextUsername = (EditText) findViewById(R.id.editTextUsername);
        m_EditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        m_ButtonLogin = (Button) findViewById(R.id.buttonLogin);
        m_ButtonSignup = (Button) findViewById(R.id.buttonSignup);
        m_ButtonLogin.setOnClickListener(view -> handleLogin());
        m_ButtonSignup.setOnClickListener(view -> handleSignup());
        s_AppContext = getApplicationContext();
        m_LogicEngineManager = LogicEngineManager.GetInstance();
    }

    private boolean areUsernameAndPasswordFieldsValid(String i_Username, String i_Password) {
        boolean areUsernameAndPasswordFieldsValid = true;

        if (i_Username.isEmpty()) {
            Toast.makeText(this, "Please enter a username!", Toast.LENGTH_SHORT).show();
            areUsernameAndPasswordFieldsValid = false;
        }

        if (i_Password.isEmpty()) {
            Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show();
            areUsernameAndPasswordFieldsValid = false;
        }

        return areUsernameAndPasswordFieldsValid;
    }

    private void handleLogin() {
        String userName = m_EditTextUsername.getText().toString();
        String password = m_EditTextPassword.getText().toString();

        if (areUsernameAndPasswordFieldsValid(userName, password)) {
            if (!m_LogicEngineManager.DoesUserExist(userName)) {
                Toast.makeText(this, "User does not exist! Please sign up.", Toast.LENGTH_SHORT).show();
            } else {
                if (!m_LogicEngineManager.IsPasswordCorrect(userName, password)) {
                    Toast.makeText(this, "Incorrect password! Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    m_LogicEngineManager.Login(userName);
                    Intent intent = new Intent(this, ContactsManagementActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    private void handleSignup() {
        String userName = m_EditTextUsername.getText().toString();
        String password = m_EditTextPassword.getText().toString();

        if (areUsernameAndPasswordFieldsValid(userName, password)) {
            if (m_LogicEngineManager.DoesUserExist(userName)) {
                Toast.makeText(this, "This user already exists! Please log in.", Toast.LENGTH_SHORT).show();
            } else {
                m_LogicEngineManager.Signup(userName, password);
                Intent intent = new Intent(this, ContactsManagementActivity.class);
                startActivity(intent);
            }
        }
    }
}