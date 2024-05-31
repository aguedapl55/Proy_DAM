package fp.dam.proy.proy_dam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AjustesScreen extends AppCompatActivity {

    private String email, password;
    private FirebaseAuth fbAuth;
    private FirebaseFirestore db;
    private EditText emailInput, passwordInput, passConfInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        fbAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        emailInput = findViewById(R.id.sett_NewUser);
        passwordInput = findViewById(R.id.sett_NewPass);
        passConfInput = findViewById(R.id.sett_ConfPass);
    }

    public void exit(View v) {
        finish();
    }

    public void updateUser(View v) {
        AuthCredential creds = EmailAuthProvider.getCredential(email, password);
        fbAuth.getCurrentUser().reauthenticate(creds).addOnCompleteListener(reauth ->  {
            if (reauth.isSuccessful()) {
                fbAuth.getCurrentUser().reload();
                try {
                    String newEmail = emailInput.getText().toString();
                    fbAuth.getCurrentUser().verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Se ha cambiado el email con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IllegalStateException | IllegalArgumentException e) {
                    Toast.makeText(this, "No se ha rellenado el campo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No se ha podido autenticar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePass(View v) {
        try {
            String newPass = passwordInput.getText().toString();
            String confPass = passConfInput.getText().toString();
            if (newPass.equals(confPass)) {
                fbAuth.getCurrentUser().updatePassword(newPass);
                Toast.makeText(this, "Se ha cambiado la contraseña con éxito", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Las contraseñas introducidas no son iguales", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException | IllegalArgumentException e) {
            Toast.makeText(this, "No se han rellenado todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOut(View v) {
        fbAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void borrarCuenta(View v) {
        fbAuth.getCurrentUser().delete();
        db.collection("users").document(email).delete();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}