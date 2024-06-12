package fp.dam.proy.proy_dam.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fp.dam.proy.proy_dam.R;

public class AddFriendActivity extends AppCompatActivity {

    FirebaseFirestore db;
    String usuario, password, email;
    EditText emailInput, codeInput;
    TextView emailDisplay, codeDisplay;
    MaterialSwitch isChildSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        try {
            email = getIntent().getExtras().getString("email");
            password = getIntent().getExtras().getString("password");
            usuario = getIntent().getExtras().getString("usuario");
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG);
        }

        emailInput = findViewById(R.id.friend_inputEmail);
        codeInput = findViewById(R.id.friend_inputCode);
        emailDisplay = findViewById(R.id.friend_userEmail);
        codeDisplay = findViewById(R.id.friend_userCode);
        isChildSwitch = findViewById(R.id.friend_switchIsChild);

        db = FirebaseFirestore.getInstance();
        emailDisplay.setText(usuario);

        try {
            db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
                int code = (int) Math.floor(Double.parseDouble(task.getResult().getDouble("code").toString()));
                codeDisplay.setText(String.valueOf(code));
//            codeDisplay.setText(String.format("%5d", code));
            });
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG);
        }
    }

    public void confirm(View v) {
        try {
            String emailF = emailInput.getText().toString();
            int codeF = Integer.parseInt(codeInput.getText().toString());
            DocumentReference amigo = db.collection("users").document(emailF);

            if (usuario.equals(emailF))
                Toast.makeText(this, "No puedes agregarte a ti mismo como usuario", Toast.LENGTH_SHORT).show();
            else
                amigo.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        if (!task.getResult().exists()) {
                            Toast.makeText(this, "No existe el usuario indicado", Toast.LENGTH_SHORT).show();
                        } else if (Math.floor(task.getResult().getDouble("code")) != codeF) {
                            Toast.makeText(this, "El código introducido es erróneo", Toast.LENGTH_SHORT).show();
                        } else {
                            addUsuario(v, emailF);
                        }
                    else Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "No se han añadido todos los datos", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG);
        }
    }

    private void addUsuario(View v, String emailF) {
        List<String> added = new ArrayList<>();
        try {
            db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    added.addAll(Arrays.asList(task.getResult().get("vinculadas").toString()
                            .replace("[", "")
                            .replace("]", "")
                            .split(", ")));
                    Log.wtf("APL aaa", "aaa");
                    added.addAll(Arrays.asList(task.getResult().get("hijos").toString()
                            .replace("[", "")
                            .replace("]", "")
                            .split(", ")));
                    added.removeIf(d -> d.equals(""));

                    if (added.contains(emailF))
                        Toast.makeText(this, "El usuario indicado ya está vinculado a esta cuenta", Toast.LENGTH_SHORT).show();
                    else {
                        ArrayList<String> cuentas = new ArrayList<>();
                        String field = isChildSwitch.isChecked() ? "hijos" : "vinculadas";
                        cuentas.addAll(Arrays.asList(task.getResult().get(field).toString()
                                .replace("[", "")
                                .replace("]", "")
                                .split(", ")));
                        cuentas.add(emailF);
/*
                    db.collection("users").document(usuario).update(field, added);
*/
                        db.collection("users").document(usuario).update(field, cuentas).addOnCompleteListener(complete -> {
                            if (complete.isSuccessful())
                                Toast.makeText(getApplicationContext(), "Se ha añadido el usuario", Toast.LENGTH_SHORT).show();
                        });
                        exit(v);
                    }
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG);
        }
    }

    public void exit(View v) {
        Intent i = new Intent(this, AjustesActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }
}