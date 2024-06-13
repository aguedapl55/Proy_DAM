package fp.dam.proy.proy_dam.Cuentas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.Principal.MainActivity;
import fp.dam.proy.proy_dam.R;

public class AddCuentaActivity extends AppCompatActivity {
    String email, password, usuario;
    FirebaseFirestore db;
    private EditText nombreTxt;
    private EditText dineroTxt;
    private EditText budgetTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cuenta);

        try {
            email = getIntent().getExtras().getString("email");
            password = getIntent().getExtras().getString("password");
            usuario = getIntent().getExtras().getString("usuario");
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG).show();
        }

        db = FirebaseFirestore.getInstance();
        nombreTxt = findViewById(R.id.addcta_nombre);
        dineroTxt = findViewById(R.id.addcta_dinero);
        budgetTxt = findViewById(R.id.addcta_budget);
    }


    public void goto_MainActivity(View view) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password );
        startActivity(i);
        finish();
    }

    public void confirm(View view) {
        double gastos, gastoMens, budget;
        String nombre;
        try {
            gastos = gastoMens = Double.parseDouble(dineroTxt.getText().toString());
            budget = Double.parseDouble(budgetTxt.getText().toString());
            nombre = nombreTxt.getText().toString();
            if (nombre.isEmpty())
                throw new IllegalArgumentException();
            CategoriasCuentas cuenta = new CategoriasCuentas(email,
                    db.collection("users").document(email).collection("cuentas").document().getId(),
                    nombre,
                    gastos,
                    gastoMens,
                    budget,
                    false);
            db.collection("users").document(email).collection("cuentas").add(cuenta).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Toast.makeText(this, "Se ha añadido la cuenta", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "No se ha podido añadir", Toast.LENGTH_SHORT).show();

            });
            goto_MainActivity(view);
        } catch (IllegalStateException | IllegalArgumentException e) {
            Toast.makeText(this, "No se han rellenado todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}