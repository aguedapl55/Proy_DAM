package fp.dam.proy.proy_dam.Transacciones;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.Principal.MainActivity;
import fp.dam.proy.proy_dam.R;

public class AddTransaccionActivity extends AppCompatActivity {

    String email, password, usuario;
    FirebaseFirestore db;
    Calendar calendario;
    private Spinner catSpin, ctaSpin;
    private EditText lugText, comText, prcText;
    private TextView fchText;

    private ArrayList<String> categorias, cuentas;
    private List<String> catAux, ctaAux;
    String categoria, cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaccion);

        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        usuario = getIntent().getExtras().getString("usuario");
        db = FirebaseFirestore.getInstance();
        spinnerSetup();
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

    public void confirm(View view) {
        Double dinero;
        String comentario /*, categoria, cuenta*/;
        Timestamp fecha;
        boolean failsCategoria = false;
        try {
            dinero = Double.parseDouble(prcText.getText().toString());
            dinero = BigDecimal.valueOf(dinero).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); //formating
            categoria = catSpin.getSelectedItem().toString();
            cuenta = ctaSpin.getSelectedItem().toString();
            comentario = comText.getText().toString();
            fecha = new Timestamp(calendario.getTime());
            if (dinero.isNaN())
                throw new IllegalStateException();
            if (!categorias.contains(categoria) || !cuentas.contains(cuenta)) {
                failsCategoria = categorias.contains(categoria) ? false : true;
                throw new IllegalArgumentException();
            }
            Transacciones transaccion = new Transacciones(dinero, fecha, categoria, cuenta, comentario);
            Double finalDinero = dinero;
            db.collection("users").document(email).collection("transacciones").add(transaccion).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    actualizarCatCta(view, finalDinero);
                    Toast.makeText(this, "Se ha realizado la transacción", Toast.LENGTH_SHORT).show();
                } else
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

    private void actualizarCatCta(View view, Double dinero) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)-1 < 0 ? 12 : calendar.get(Calendar.MONTH) - 1, 1);
        Date fecha = calendar.getTime();

        Log.wtf("APL date", fecha.getDay() + "/" + fecha.getMonth() + "/" + fecha.getYear());
        CollectionReference colrefCats = db.collection("users").document(email).collection("categorias");
        CollectionReference colrefCtas = db.collection("users").document(email).collection("cuentas");
        colrefCats.get().addOnCompleteListener(taskCats -> {
            colrefCtas.get().addOnCompleteListener(taskCtas -> {
                if (taskCats.isSuccessful() && taskCtas.isSuccessful()) {
                    //Categorias
                    for (DocumentSnapshot doc : taskCats.getResult().getDocuments()) {
                        if (doc.get("nombre").toString().equals(categoria)) {
                            CategoriasCuentas cat = new CategoriasCuentas(
                                    doc.getString("nombre"),
                                    doc.getDouble("gastos"),
                                    doc.getDouble("gastoMens"),
                                    doc.getDouble("budget"));

                            Query qTrans = db.collection("users").document(email).collection("transacciones")
                                    .whereEqualTo("categoria", doc.getString("nombre"))
                                    .orderBy("fecha");
                            qTrans.get().addOnCompleteListener(taskTrans -> {
                                if (taskTrans.isSuccessful()) {
                                    List<DocumentSnapshot> mensual = new ArrayList<>();
                                    for (DocumentSnapshot transDoc : taskTrans.getResult())
                                        if (transDoc.getDate("fecha").compareTo(fecha) < 31)
                                            mensual.add(transDoc);
                                    double aux = mensual.stream().mapToDouble(d -> d.getDouble("dinero")).sum();
                                    if (cat.getGastoMens() != aux)
                                        cat.setGastoMens(aux);

                                }
                            });

                            colrefCats.document(doc.getId()).update("gastos", cat.getGastos() + dinero);
                            colrefCats.document(doc.getId()).update("gastoMens", cat.getGastoMens() + dinero);
                            break;
                        }
                    }

                    //Cuentas
                    for (DocumentSnapshot doc : taskCtas.getResult().getDocuments()) {
                        if (doc.get("nombre").toString().equals(cuenta)) {
                            CategoriasCuentas cta = new CategoriasCuentas(
                                    doc.getString("nombre"),
                                    doc.getDouble("gastos"),
                                    doc.getDouble("gastoMens"),
                                    doc.getDouble("budget"));

                            Query qTrans = db.collection("users").document(email).collection("transacciones")
                                    .whereEqualTo("categoria", doc.getString("nombre"))
                                    .orderBy("fecha");
                            qTrans.get().addOnCompleteListener(taskTrans -> {
                                if (taskTrans.isSuccessful()) {
                                    List<DocumentSnapshot> mensual = new ArrayList<>();
                                    for (DocumentSnapshot transDoc : taskTrans.getResult())
                                        if (transDoc.getDate("fecha").compareTo(fecha) < 31)
                                            mensual.add(transDoc);
                                    double aux = mensual.stream().mapToDouble(d -> d.getDouble("dinero")).sum();
                                    if (cta.getGastoMens() != aux)
                                        cta.setGastoMens(aux);

                                }
                            });

                            colrefCtas.document(doc.getId()).update("gastos", cta.getGastos() + dinero);
                            colrefCtas.document(doc.getId()).update("gastoMens", cta.getGastoMens() + dinero);
                            break;
                        }
                    }
                }
            });
        });
    }

    public void goto_MainActivity(View view) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("email", email);
        i.putExtra("usuario", usuario);
        i.putExtra("password", password);
        startActivity(i);
        finish();
    }

    public void spinnerSetup() {
        catSpin = findViewById(R.id.addtrans_categoria);
        ctaSpin = findViewById(R.id.addtrans_cuenta);
        CollectionReference colRefCat = db.collection("users").document(email).collection("categorias");
        CollectionReference colRefCta = db.collection("users").document(email).collection("cuentas");
        categorias = new ArrayList<>();
        cuentas = new ArrayList<>();

        colRefCat.orderBy("nombre", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    if (document.contains("nombre"))
                        categorias.add(document.getString("nombre"));
                    else Log.wtf("APL SALTADO", document.getId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                catSpin.setAdapter(adapter);
                catSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        parent.setSelection(position);
                        categoria = parent.getItemAtPosition(position).toString();
                        //Toast.makeText(getBaseContext(), "Item seleccionado: " + categoria, Toast.LENGTH_SHORT).show();
                        Log.wtf("APL CATEGORIA", categoria);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

            } else
                Log.wtf("APL ColRefCategorias", "Ha fallado");
        });

        colRefCta.orderBy("nombre", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    if (document.contains("nombre"))
                        cuentas.add(document.getString("nombre"));
                    else Log.wtf("APL SALTADO", document.getId());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cuentas);
                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                ctaSpin.setAdapter(adapter);
                ctaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        parent.setSelection(position);
                        categoria = parent.getItemAtPosition(position).toString();
                        //Toast.makeText(getBaseContext(), "Item seleccionado: " + categoria, Toast.LENGTH_SHORT).show();
                        Log.wtf("APL CATEGORIA", categoria);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

            } else
                Log.wtf("APL ColRefCategorias", "Ha fallado");
        });

        catSpin.setSelection(0);
        ctaSpin.setSelection(0);
    }

}