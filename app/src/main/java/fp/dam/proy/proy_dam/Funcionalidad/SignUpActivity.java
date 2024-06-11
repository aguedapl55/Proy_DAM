package fp.dam.proy.proy_dam.Funcionalidad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, passConfInput;
    private String email;

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
            email =  emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String passwordC = passConfInput.getText().toString();
            if (password.equals(passwordC)) {
                FirebaseAuth fbAuth = FirebaseAuth.getInstance();
                fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (!task.isSuccessful())
                        Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
                else {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    configNewUser(db);

                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("email", email);
                    i.putExtra("usuario", email);
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
        DocumentReference usuario = db.collection("users").document(email);
        usuario.delete();
        base.put("email", email);
        base.put("vinculadas", List.of(email));
        base.put("hijos", List.of());
        Map<String, Boolean> visibilidad = new HashMap<>();
        visibilidad.put("cuentas", true);
        visibilidad.put("categorias", true);
        visibilidad.put("transacciones", true);
        visibilidad.put("estadisticas", true);
        base.put("visibilidad", visibilidad);
        base.put("code", Math.toIntExact((long) Math.floor(Math.random() * 100000))); //codigo de cinco nums para añadir cuenta
        usuario.set(base).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Se ha creado la cuenta con éxito", Toast.LENGTH_SHORT).show();
            }
        });

/*
        Map<String, Object> params = new HashMap<>();
        params.put("language", "es");
        params.put("theme", "light");
        usuario.collection("parametros").add(params);
*/

        usuario.collection("categorias").add(new CategoriasCuentas("food", 0, 0, 0));
        usuario.collection("categorias").add(new CategoriasCuentas("entertainment", 0, 0, 0));
        usuario.collection("categorias").add(new CategoriasCuentas("transport", 0, 0, 0));
        usuario.collection("categorias").add(new CategoriasCuentas("health", 0, 0, 0));
        usuario.collection("categorias").add(new CategoriasCuentas("pets", 0, 0, 0));
        usuario.collection("categorias").add(new CategoriasCuentas("family", 0, 0, 0));
        usuario.collection("categorias").add(new CategoriasCuentas("clothes", 0, 0, 0));
//        usuario.collection("categorias").add(new CategoriasCuentas("food", "", 0, 0));
//        usuario.collection("categorias").add(new CategoriasCuentas("entertainment", "", 0, 0));
//        usuario.collection("categorias").add(new CategoriasCuentas("transport", "", 0, 0));
//        usuario.collection("categorias").add(new CategoriasCuentas("health", "", 0, 0));
//        usuario.collection("categorias").add(new CategoriasCuentas("pets", "", 0, 0));
//        usuario.collection("categorias").add(new CategoriasCuentas("family", "", 0, 0));
//        usuario.collection("categorias").add(new CategoriasCuentas("clothes", "", 0, 0));

        Map<String, Object> transacciones = new HashMap<>();
        usuario.collection("transacciones").add(transacciones);

        usuario.collection("cuentas").add(new CategoriasCuentas("debit card", 0, 0, 0));
        usuario.collection("cuentas").add(new CategoriasCuentas("cash", 0, 0, 0));
//        usuario.collection("cuentas").add(new CategoriasCuentas("debit card", "credit_card", 0, 0));
//        usuario.collection("cuentas").add(new CategoriasCuentas("cash", "wallet", 0, 0));
    }

    public void goto_login(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
