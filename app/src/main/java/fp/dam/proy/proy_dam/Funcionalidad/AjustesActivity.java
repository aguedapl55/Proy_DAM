package fp.dam.proy.proy_dam.Funcionalidad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fp.dam.proy.proy_dam.R;

public class AjustesActivity extends AppCompatActivity {

    private String usuario, email, password, usuarioOG, emailOG, passwordOG;
    private FirebaseAuth fbAuth;
    private FirebaseFirestore db;
    private EditText emailInput, passwordInput, passConfInput;
    Spinner selectUserSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        try {
            email = getIntent().getExtras().getString("email");
            password = getIntent().getExtras().getString("password");
            usuario = getIntent().getExtras().getString("usuario");
        } catch (NullPointerException e) {}
        emailOG = email; usuarioOG = usuario; passwordOG = password;

        Log.wtf("APL USUARIO SETTINGS", usuario);
        Log.wtf("APL USUARIO EMAIL SETTINGS", usuario);
        fbAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        emailInput = findViewById(R.id.sett_NewUser);
        passwordInput = findViewById(R.id.sett_NewPass);
        passConfInput = findViewById(R.id.sett_ConfPass);
        selectUserSpinner();
    }

    public void selectUserSpinner() {
        selectUserSpin = findViewById(R.id.sett_SelectUserSpin);
        DocumentReference docRefUsers = db.collection("users").document(usuario);

        docRefUsers.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> usersAccesibles = new ArrayList<>();
                DocumentSnapshot doc = task.getResult();

                usersAccesibles.addAll(Arrays.asList(
                        doc.get("vinculadas").toString()
                        .replace("[", "")
                        .replace("]", "")
                        .split(", ")));
                usersAccesibles.addAll(Arrays.asList(
                        doc.get("hijos").toString()
                        .replace("[", "")
                        .replace("]", "")
                        .split(", ")));
                usersAccesibles.removeIf(d -> d.equals(""));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, usersAccesibles);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                selectUserSpin.setAdapter(adapter);
                selectUserSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        parent.setSelection(position);
                        email = parent.getItemAtPosition(position).toString();
                        Log.wtf("APL selectUserSpiner", "se ha cambiado la selección a " + email);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                selectUserSpin.setSelection(usersAccesibles.indexOf(email));
            }
        });

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
                            Toast.makeText(this, "HACER QUE FUNCIONE", Toast.LENGTH_SHORT).show(); //TODO hacer que funcione
/*
                            Toast.makeText(this, "Se ha cambiado el email con éxito", Toast.LENGTH_SHORT).show();
*/
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
        } catch (NullPointerException e) {
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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

/*
    public void goto_Activity(View v) {
        Class clase = null;
        int id = v.getId();
        if (id == R.id.sett_changeSharedData) {
            clase = SharedDataActivity.class;
        } else if (id == R.id.sett_SelectUserAddUser) {
            clase = AddFriendActivity.class;
        } else if (id == R.id.backBut) {
            email = emailOG;
            usuario = usuarioOG;
            password = passwordOG;
            clase = MainActivity.class;
        } else if (id == R.id.confirmBut) {
            clase = MainActivity.class;
        }
        if (clase.getClass() != null) {
            Intent i = new Intent(this, clase.getClass());
            i.putExtra("email", email);
            i.putExtra("usuario", usuario);
            i.putExtra("password", password);
            startActivity(i);
            finish();
        }
    }
*/

    public void goto_AddFriend(View v) {
        Intent i = new Intent(this, AddFriendActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }

    public void goto_SharedData(View v) {
        Intent i = new Intent(this, SharedDataActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }

    public void revert(View v) {
        email = emailOG; usuario = usuarioOG; password = passwordOG;
        exit(v);
    }

    public void exit(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }
}