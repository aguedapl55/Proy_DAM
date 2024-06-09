package fp.dam.proy.proy_dam.Funcionalidad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        usuario = getIntent().getExtras().getString("usuario");
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

    /*public void setUpButtons() {
        addEmail = findViewById(R.id.sett_SelectUserAddUser);
        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogs(v, "vinculadas");
            }
        });

        addChild = findViewById(R.id.sett_SelectUserAddChild);
        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogs(v, "hijos");
            }
        });
    }

    private void setDialogs(View view, String coleccion) {
        Dialog dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.dialog_add_friend);

        EditText emailInput = dialog.findViewById(R.id.dialog_inputEmail);
        EditText codeInput = dialog.findViewById(R.id.dialog_inputCode);

        ImageButton confirmButton = dialog.findViewById(R.id.dialog_butConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                */
    /*try {
                    String emailDialog = emailInput.getText().toString();
                    String codeDialog = codeInput.getText().toString();
                    boolean emailExists = db.collection("users").document(emailDialog).get().isSuccessful();
                    db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (codeDialog.equals(task.getResult().get("code")) && emailExists) {
                                String[] aux = task.getResult().get(coleccion).toString()
                                        .replace("[", "")
                                        .replace("]", ", " + emailDialog)
                                        .split(", ");
                                db.collection("users").document(usuario).update(coleccion, aux);
                            } else if (emailExists)
                                Toast.makeText(getApplicationContext(), "El código introducido no es correcto", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "El email introducido no es correcto", Toast.LENGTH_SHORT).show();
                        } else
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), "No se han rellenado todos los campos", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e) {

                }*//*
                dialog.dismiss();
            }
        });

        ImageButton cancelButton = dialog.findViewById(R.id.dialog_butClose);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/

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

    public void goto_AddFriend(View v) {
        Intent i = new Intent(this, AddFriendActivity.class);
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