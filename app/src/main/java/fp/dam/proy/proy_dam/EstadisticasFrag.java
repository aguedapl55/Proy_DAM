package fp.dam.proy.proy_dam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.Transacciones.Transacciones;

public class EstadisticasFrag extends Fragment {

    private String email;
    private FirebaseFirestore db;
    BarChart dineroMonth, dineroCategorias, dineroCuentas;

    public EstadisticasFrag() {}

    public static EstadisticasFrag newInstance(String email) {
        EstadisticasFrag fragment = new EstadisticasFrag();
        Bundle args = new Bundle();
        args.putString("email", "email");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        try {
            email = getArguments().getString("email");
        } catch (NullPointerException e) {
            Toast.makeText(this.getContext(), "EXCEPTION???", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        dineroMonth = rootView.findViewById(R.id.statsBCDineroMonth);
        dineroCategorias = rootView.findViewById(R.id.statsBCDineroCat);
        dineroCuentas = rootView.findViewById(R.id.statsBCDineroCta);

        Query dinMonthRef = db.collection("users").document(email).collection("transacciones").orderBy("fecha", Query.Direction.DESCENDING);
        //rellenarStats(dineroMonth, );

        rellenarStatsMonth();
        rellenarStatsCategorias();
        rellenarStatsCuentas();
        return rootView;
    }

    private void rellenarStats(Query query, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        List<Float> floats = new ArrayList<>();

        List<Transacciones> transacciones = new ArrayList<>();
        List<CategoriasCuentas> categoriasCuentas = new ArrayList<>();

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int iteraciones = 0;
                boolean esTrans = task.getResult().getDocuments().get(0).contains("dinero");
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (esTrans) { //Transacciones
                        //Transacciones trans = document.toObject(Transacciones.class);
                        transacciones.add(document.toObject(Transacciones.class));
                        /*
                        transacciones.add(new Transacciones(
                                document.getDouble("dinero"),
                                document.getTimestamp("fecha"),
                                document.getString("nombre"),
                                document.getString("cuenta"),
                                document.getString("comentario")
                        ));
                         */
                    } else { //CategoriasCuentas
                        categoriasCuentas.add(document.toObject(CategoriasCuentas.class));
                        /*
                        categoriasCuentas.add(new CategoriasCuentas(
                                document.getString("nombre"),
                                document.getString("icon"),
                                document.getDouble("gastos"),
                                document.getDouble("budget")
                        ));
                         */
                    }
                    if (iteraciones++ >= 10) break;
                }
                if (esTrans) { //Transacciones
                    transacciones.forEach(t -> floats.add(Float.parseFloat(String.valueOf(t.getDinero()))));
                    for (int i = 0; i < transacciones.size(); i++)
                        entries.add(new BarEntry(i, floats.get(i)));
                }
                else { //CategoriasCuentas
                    categoriasCuentas.forEach(cc -> floats.add(Float.parseFloat(String.valueOf(cc.getGastos()))));
                    for (int i = 0; i < categoriasCuentas.size(); i++)
                        entries.add(new BarEntry(i, floats.get(i)));
                }

                BarDataSet set = new BarDataSet(entries, "BarDataSet");
                BarData data = new BarData(set);
                data.setBarWidth(0.9f); // set custom bar width
                barChart.setData(data);
                barChart.setFitBars(true); // make the x-axis fit exactly all bars
                barChart.invalidate(); // refresh
            }
        });
    }

    private void rellenarStatsMonth() {
        List<BarEntry> entries = new ArrayList<>();
        List<Float> floats = new ArrayList<>();
        List<Transacciones> trans = new ArrayList<>();

        db.collection("users").document(email).collection("transacciones").orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //TODO maybe reemplazar por lambda?
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //int size = (task.getResult().size() < 10) ? task.getResult().size() : 10;
                            int iteraciones = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Transacciones t = document.toObject(Transacciones.class);
                                trans.add(t);
                                if (iteraciones >= 10)
                                    break;
                                iteraciones++;
                            }

                            trans.forEach(t -> floats.add(Float.parseFloat(String.valueOf(t.getDinero()))));
                            for (int i = 0; i<trans.size(); i++)
                                entries.add(new BarEntry(i, floats.get(i)));

                            BarDataSet set = new BarDataSet(entries, "BarDataSet");
                            BarData data = new BarData(set);
                            data.setBarWidth(0.9f); // set custom bar width
                            dineroMonth.setData(data);
                            dineroMonth.setFitBars(true); // make the x-axis fit exactly all bars
                            dineroMonth.invalidate(); // refresh
                        } else
                            Log.wtf("TASK FAILED", task.getException());
                    }
                });
    }

    private void rellenarStatsCategorias() {
        List<BarEntry> entries = new ArrayList<>();
        List<Float> floats = new ArrayList<>();
        List<CategoriasCuentas> categorias = new ArrayList<>();

        db.collection("users").document(email).collection("categorias").orderBy("nombre", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //int size = (task.getResult().size() < 10) ? task.getResult().size() : 10;
                            int iteraciones = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoriasCuentas c = new CategoriasCuentas(
                                        document.getString("nombre"),
                                        document.getString("icon"),
                                        document.getDouble("gastos"),
                                        document.getDouble("budget")
                                );
                                categorias.add(c);
                                if (iteraciones >= 10)
                                    break;
                                iteraciones++;
                            }

                            categorias.forEach(d -> floats.add(Float.parseFloat(String.valueOf(d.getGastos()))));
                            for (int i = 0; i<categorias.size(); i++)
                                entries.add(new BarEntry(i, floats.get(i)));

                            BarDataSet set = new BarDataSet(entries, "BarDataSet");
                            BarData data = new BarData(set);
                            data.setBarWidth(0.9f); // set custom bar width
                            dineroCategorias.setData(data);
                            dineroCategorias.setFitBars(true); // make the x-axis fit exactly all bars
                            dineroCategorias.invalidate(); // refresh
                        } else
                            Log.wtf("TASK FAILED", task.getException());
                    }
                });
    }

    private void rellenarStatsCuentas() {
        List<BarEntry> entries = new ArrayList<>();
        List<Float> floats = new ArrayList<>();
        List<CategoriasCuentas> cuentas = new ArrayList<>();

        db.collection("users").document(email).collection("cuentas").orderBy("gastos", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //int size = (task.getResult().size() < 10) ? task.getResult().size() : 10;
                            int iteraciones = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoriasCuentas c = new CategoriasCuentas(
                                        document.getString("nombre"),
                                        "",
                                        document.getDouble("gastos"),
                                        0.0
                                );
                                cuentas.add(c);
                                if (iteraciones >= 10)
                                    break;
                                iteraciones++;
                            }

                            cuentas.forEach(d -> floats.add(Float.parseFloat(String.valueOf(d.getGastos()))));
                            for (int i = 0; i<cuentas.size(); i++)
                                entries.add(new BarEntry(i, floats.get(i)));

                            BarDataSet set = new BarDataSet(entries, "BarDataSet");
                            BarData data = new BarData(set);
                            data.setBarWidth(0.9f); // set custom bar width
                            dineroCuentas.setData(data);
                            dineroCuentas.setFitBars(true); // make the x-axis fit exactly all bars
                            dineroCuentas.invalidate(); // refresh
                        } else
                            Log.wtf("TASK FAILED", task.getException());
                    }
                });
    }


}