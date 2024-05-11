package fp.dam.proy.proy_dam.Transacciones;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.primitives.Chars;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fp.dam.proy.proy_dam.R;

public class AddTransaccionActivity extends AppCompatActivity {

    String email;
    FirebaseFirestore db;
    Calendar calendario;
    private Spinner catSpin, ctaSpin;
    private EditText /*catText, ctaText,*/ lugText, comText, prcText;
    //private EditText fchText;
    private TextView fchText;

    private List<String> categorias, cuentas;
    String categoria, cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaccion);

        email = getIntent().getExtras().getString("email");
        db = FirebaseFirestore.getInstance();
        setup();

        catSpin = findViewById(R.id.addtrans_categoria);
        ctaSpin = findViewById(R.id.addtrans_cuenta);
        //catText = findViewById(R.id.addtrans_categoria);
        //ctaText = findViewById(R.id.addtrans_cuenta);
        comText = findViewById(R.id.addtrans_comentario);
        fchText = findViewById(R.id.addtrans_fecha);
        prcText = findViewById(R.id.addtrans_dinero);

        calendario = Calendar.getInstance();
        int year, month, day;
        year = calendario.get(Calendar.YEAR);
        month = calendario.get(Calendar.MONTH);
        day = calendario.get(Calendar.DAY_OF_MONTH);

        catSpin.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias));
        catSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoria = "";
            }
        });
        ctaSpin.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cuentas));
        ctaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cuenta = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cuenta = "";
            }
        });

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



    public void confirm(View view) {
        Double dinero;
        String comentario /*, categoria, cuenta*/;
        Timestamp fecha;
        boolean failsCategoria = false;
        try {
            dinero = Double.parseDouble(prcText.getText().toString());
            dinero = BigDecimal.valueOf(dinero).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); //formating
            //categoria = catText.getText().toString();
            //cuenta = ctaText.getText().toString();
            comentario = comText.getText().toString();
            fecha = new Timestamp(calendario.getTime());
            if (dinero.isNaN() || categoria.isBlank() || cuenta.isBlank())
                throw new IllegalStateException();
            if (!categorias.contains(categoria) || !cuentas.contains(cuenta)) {
                failsCategoria = categorias.contains(categoria) ? false : true;
                throw new IllegalArgumentException();
            }
            Transacciones transaccion = new Transacciones(dinero, fecha, categoria, cuenta, comentario);
            db.collection("users").document(email).collection("transacciones").add(transaccion).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Toast.makeText(this, "Se ha añadido la transacción", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "No se ha podido añadir", Toast.LENGTH_SHORT).show();

            });
            goto_MainActivity(view);
        } catch (IllegalStateException e) {
            Toast.makeText(this, "No se han rellenado todos los campos", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            if (failsCategoria)
                Toast.makeText(this, "La categoría indicada no existe", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "La cuenta indicada no existe", Toast.LENGTH_SHORT).show();
        }
    }

    public void goto_MainActivity(View view) {
        finish();
    }

    public void setup() {
        categorias = new ArrayList<>();
        db.collection("users").document(email).collection("categorias").get().addOnCompleteListener(task -> {
            Log.wtf("TAMAÑO TASK", "" + task.getResult().size());
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (document.contains("nombre")) {
                    categorias.add(document.getString("nombre"));
                } else
                    Log.wtf("SALTADO", document.getId());
            }
        });

        cuentas = new ArrayList<>();
        db.collection("users").document(email).collection("cuentas").get().addOnCompleteListener(task -> {
            Log.wtf("TAMAÑO TASK", "" + task.getResult().size());
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (document.contains("nombre")) {
                    cuentas.add(document.getString("nombre"));
                } else
                    Log.wtf("SALTADO", document.getId());
            }
        });
    }
}