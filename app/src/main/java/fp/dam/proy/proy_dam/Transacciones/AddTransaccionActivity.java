package fp.dam.proy.proy_dam.Transacciones;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import fp.dam.proy.proy_dam.R;

public class AddTransaccionActivity extends AppCompatActivity {

    String email;
    FirebaseFirestore db;
    Calendar calendario;
    private EditText catText, ctaText, lugText, comText, prcText;
    //private EditText fchText;
    private TextView fchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaccion);
        email = getIntent().getExtras().getString("email");
        db = FirebaseFirestore.getInstance();
         catText = findViewById(R.id.addtrans_categoria);
         ctaText = findViewById(R.id.addtrans_cuenta);
         comText = findViewById(R.id.addtrans_comentario);
         fchText = findViewById(R.id.addtrans_fecha);
         prcText = findViewById(R.id.addtrans_dinero);

         calendario = Calendar.getInstance();
         int year, month, day;
         year = calendario.get(Calendar.YEAR);
         month = calendario.get(Calendar.MONTH);
         day = calendario.get(Calendar.DAY_OF_MONTH);
         fchText.setOnClickListener(c -> {
             DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    fchText.setText(new StringBuilder().append(dayOfMonth + "/" + (month+1) + "/" + year));
                    calendario.set(year, month, day);
                 }
             }, year, month, day);
             dpd.show();

         });
    }


    public void goto_MainActivity(View view) {
        finish();
    }

    public void confirm(View view) {
        Double dinero;
        String comentario, categoria, cuenta;
        Timestamp fecha;
        try {
            dinero = Double.parseDouble(prcText.getText().toString());
            dinero = BigDecimal.valueOf(dinero).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); //formating
            categoria = catText.getText().toString();
            cuenta = ctaText.getText().toString();
            fecha = new Timestamp(calendario.getTime());
            comentario = comText.getText().toString();
            if (dinero.isNaN() || categoria.isBlank() || cuenta.isBlank())
                throw new IllegalArgumentException();
            Transacciones transaccion = new Transacciones(dinero, fecha, categoria, cuenta, comentario);
            db.collection("users").document(email).collection("transacciones").add(transaccion).addOnCompleteListener(task -> {
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