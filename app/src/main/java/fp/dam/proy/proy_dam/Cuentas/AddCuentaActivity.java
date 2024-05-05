package fp.dam.proy.proy_dam.Cuentas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.Calendar;

import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.R;

public class AddCuentaActivity extends AppCompatActivity {
    String email;
    FirebaseFirestore db;
    private EditText nombreTxt, iconTxt, dineroTxt, budgetTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cuenta);
        email = getIntent().getExtras().getString("email");
        db = FirebaseFirestore.getInstance();
        nombreTxt = findViewById(R.id.addcta_nombre);
        iconTxt = findViewById(R.id.addcta_icon);
        dineroTxt = findViewById(R.id.addcta_dinero);
        budgetTxt = findViewById(R.id.addcta_budget);
    }


    public void goto_MainActivity(View view) {
        finish();
    }

    public void confirm(View view) {
        Double gastos, budget;
        String nombre, icon;
        try {
            gastos = Double.parseDouble(dineroTxt.getText().toString());
            //gastos = BigDecimal.valueOf(gastos).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); //formating
            budget = Double.parseDouble(budgetTxt.getText().toString());
            nombre = nombreTxt.getText().toString();
            icon = iconTxt.getText().toString();
            if (gastos.isNaN() || budget.isNaN() || nombre.isEmpty() || icon.isEmpty())
                throw new IllegalArgumentException();
            CategoriasCuentas cuenta = new CategoriasCuentas(nombre, icon, gastos, budget);
            db.collection("users").document(email).collection("cuentas").add(cuenta).addOnCompleteListener(task -> {
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