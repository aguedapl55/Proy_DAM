package fp.dam.proy.proy_dam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, passConfInput;
    private String email, password, passwordC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        emailInput = findViewById(R.id.supEdtUsuario);
        passwordInput = findViewById(R.id.supEdtContrasena);
        passConfInput = findViewById(R.id.supEdtContrasena2);
    }

    public void signup(View v) {
        try {
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();
            passwordC = passConfInput.getText().toString();
            if (password.equals(passwordC)) {
                FirebaseAuth fbAuth = FirebaseAuth.getInstance();
                fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (!task.isSuccessful())
                        Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
                else {
                    Map<String, Object> newUser = new HashMap<>();
                    newUser.put("email", email);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    configNewUser(db);
                    //db.collection("users").document(email).set(newUser);
                    db.terminate();

                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                    finish();
                }
                });
            } else
                Toast.makeText(this, "Las contraseñas introducidas no son iguales", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException | IllegalArgumentException e) {
            Toast.makeText(this, "No se han rellenado todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void configNewUser(FirebaseFirestore db) {
        Map<String, Object> base = new HashMap<>();
        base.put("email", email);
        db.collection("users").document(email).set(base).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "OLEEEEEEE", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "FALLO TODO MAL", Toast.LENGTH_SHORT).show();
            }
        });

        Map<String, Object> params = new HashMap<>();
        //TODO sustituir por parametros reales
        db.collection("users").document(email).collection("parametros").add(params);

        Map<String, Object> categorias = new HashMap<>();
        //TODO sustituir por categorias reales
        db.collection("users").document(email).collection("categorias").add(categorias);

        Map<String, Object> transacciones = new HashMap<>();
        //TODO
        db.collection("users").document(email).collection("transacciones").add(transacciones);

        Map<String, Object> cuentas = new HashMap<>();
        //TODO sustituir por cuentas reales
        db.collection("users").document(email).collection("cuentas").add(cuentas);

        /*
        Map<String, Object> controlParental = new HashMap<>();
        controlParental.put()
        DocumentReference CP = db
                .collection("users").document(email)
                .collection("controlParental").document("message1");
         */

        Map<String, Object> budgets = new HashMap<>();
        //budgets.put("budgets", "budgets"); //TODO resolver problema "no puede haber col con 0 docs"
        db.collection("users").document(email).collection("budgets").add(budgets);
    }

    public void goto_login(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
