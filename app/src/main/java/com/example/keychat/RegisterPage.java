package com.example.keychat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RegisterPage extends AppCompatActivity {

    private EditText name;
    private EditText confirmPassword;
    private EditText email;
    private EditText password;
    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);

        name = findViewById(R.id.editTextText);
        confirmPassword = findViewById(R.id.editTextNumber);
        email = findViewById(R.id.editTextTextEmailAddress2);
        password = findViewById(R.id.editTextText2);
        registerBtn =  findViewById(R.id.button4);
        Socket socket = ServerConnection.getServerConnection();

        registerBtn.setOnClickListener(v -> {
            String empName = name.getText().toString();
            String empPassword = password.getText().toString();
            String empEmail = email.getText().toString();
            String empPasswordConfirm = confirmPassword.getText().toString();
            if(empPasswordConfirm.equals(empPassword)) {
                JSONObject employeeData = new JSONObject();
                try {
                    employeeData.accumulate("username", empName);
                    employeeData.accumulate("email", empEmail);
                    employeeData.accumulate("position", "Employee");
                    employeeData.accumulate("passwordHash", empPassword);
                    employeeData.accumulate("displayName", empName);
                    employeeData.accumulate("profilePicture", "N/A");
                    employeeData.accumulate("status", "N/A");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                socket.emit("add_employee", employeeData);
            } else {
                Toaster.toast("Passwords do not match", RegisterPage.this);
            }
        });

        socket.on("employee_added", args -> {
            Toaster.toast("Registration was successful", RegisterPage.this);
            JSONObject info = (JSONObject) args[0];
            try {
                Log.d("employee_id", info.getString("employee_id"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            finish();
        });

        socket.on("employee_add_error", args -> {
            Toaster.toast("Registration unsuccessful", RegisterPage.this);
        });
    }
}