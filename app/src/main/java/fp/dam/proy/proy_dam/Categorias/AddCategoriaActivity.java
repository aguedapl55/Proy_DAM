package fp.dam.proy.proy_dam.Categorias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.Funcionalidad.MainActivity;
import fp.dam.proy.proy_dam.R;

public class AddCategoriaActivity extends AppCompatActivity {

    String email, password, usuario;
    FirebaseFirestore db;
    private EditText nombreTxt, iconTxt, dineroTxt, budgetTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categoria);

        try {
            email = getIntent().getExtras().getString("email");
            password = getIntent().getExtras().getString("password");
            usuario = getIntent().getExtras().getString("usuario");
        } catch (NullPointerException e) {}

        db = FirebaseFirestore.getInstance();
        nombreTxt = findViewById(R.id.addcat_nombre);
        iconTxt = findViewById(R.id.addcat_icon);
        dineroTxt = findViewById(R.id.addcat_dinero);
        budgetTxt = findViewById(R.id.addcat_budget);
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
        Double dinero, budget;
        String nombre, icon;
        try {
            dinero = Double.parseDouble(dineroTxt.getText().toString());
            budget = Double.parseDouble(budgetTxt.getText().toString());
            nombre = nombreTxt.getText().toString();
            icon = iconTxt.getText().toString();
            if (nombre.isEmpty())
                throw new IllegalArgumentException();
            CategoriasCuentas categoria = new CategoriasCuentas(nombre, icon, dinero, budget);
            db.collection("users").document(email).collection("categorias").add(categoria).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Toast.makeText(this, "Se ha añadido la transacción", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "No se ha podido añadir", Toast.LENGTH_SHORT).show();

            });
            goto_MainActivity(view);
        } catch (IllegalStateException | IllegalArgumentException e) {
            Toast.makeText(this, "No se han rellenado todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}