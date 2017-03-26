package mx.unam.ciencias.jcasas.pendejifest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etMail;
    private EditText etPass;
    private Button btSignup;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    private String email;
    private String pass;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        buildUI();
    }

    /**
     * Auxiliar method for building the UI.
     */
    private void buildUI() {
        etName = (EditText) findViewById(R.id.editTextNameSignup);
        etMail = (EditText) findViewById(R.id.editTextEmail);
        etPass = (EditText) findViewById(R.id.editTextPass);
        btSignup = (Button) findViewById(R.id.buttonSignup);
        btSignup.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignupActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v == btSignup) {
            register();
        }
    }

    /**
     * Method for registering the user.
     *
     */
    public void register() {
        name = etName.getText().toString();
        email = etMail.getText().toString();
        pass = etPass.getText().toString();
        if (validCredentials(email, pass)) {
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                makeAlert("Registered",
                                        "Hello! " + name + "\nYou are successfully registered")
                                        .show();
                                saveOnPreferences(name, email, pass);
                                updateProfile(firebaseAuth.getCurrentUser());
                                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                makeAlert("ERROR", "There was an error with, try again").show();
                            }
                        }
                    });
        }
    }

    /**
     * Checks if the credentials are valid
     * @param email The email
     * @param pass The password
     * @return <code>true</code> if the email and the password are valid credentials.
     */
    public boolean validCredentials(String email, String pass) {
        return (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && isValidPass(pass));
    }

    /**
     * Indicates if the password given is correct.
     * @param pass The string that contains the password.
     * @return <code>true</code> if the password is correct, <code>false</code> in other case.
     */
    public boolean isValidPass(String pass) {
        if (pass.length() < 6) {
            Toast.makeText(SignupActivity.this,
                        "The password length must be greater than 6 characters",
                        Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    /**
     *
     * @param email
     * @return
     */
    public boolean isValidEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this,
                        "The email address is not valid",
                        Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    public AlertDialog makeAlert(String title, String message) {
        AlertDialog.Builder alb = new AlertDialog.Builder(SignupActivity.this);
        AlertDialog ad = alb.create();
        ad.setTitle(title);
        ad.setMessage(message);
        return ad;
    }

    /**
     * Save the preferences.
     */
    private void saveOnPreferences(String name, String email, String pass) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.putString("name", name);
        editor.apply();
    }

    private void updateProfile(FirebaseUser user) {
        if (user != null) {
            SharedPreferences sp = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
            UserProfileChangeRequest profileChanges = new UserProfileChangeRequest.Builder()
                    .setDisplayName(sp.getString("name", "DEFAULT")).build();
            user.updateProfile(profileChanges);
        }
    }
}
