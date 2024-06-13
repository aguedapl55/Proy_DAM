package fp.dam.proy.proy_dam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EstadisticasFrag extends Fragment {

    private String email, usuario;
    private FirebaseFirestore db;
    BarChart dineroMonth, dineroCategorias, dineroCuentas;

    public EstadisticasFrag() {}

    public static EstadisticasFrag newInstance(String email, String usuario) {
        EstadisticasFrag fragment = new EstadisticasFrag();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            email = getArguments().getString("email");
            usuario = getArguments().getString("usuario");
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG).show();
        }
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        dineroMonth = rootView.findViewById(R.id.statsBCDineroMonth);
        dineroCategorias = rootView.findViewById(R.id.statsBCDineroCat);
        dineroCuentas = rootView.findViewById(R.id.statsBCDineroCta);

        checkRellenar();
        return rootView;
    }

    private void checkRellenar() {
        Log.wtf("APL usuario.equals(email)", "" + usuario.equals(email));
        if (usuario.equals(email)) {
            setupStats();
        }
        else {
            try {
                db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
                    DocumentSnapshot doc = task.getResult();
                    List<String> usersAccesibles = new ArrayList<>(Arrays.asList(
                            doc.get("hijos").toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(", ")));
                    usersAccesibles.removeIf(d -> d.equals(""));
                    Log.wtf("APL usersAccesibles.contains(email)", "" + usersAccesibles.contains(email));
                    if (usersAccesibles.contains(email))
                        setupStats();
                    else {
                        db.collection("users").document(email).get().addOnCompleteListener(task2 -> {
                            Map<String, Boolean> mapa = (Map<String, Boolean>) task2.getResult().get("visibilidad");
                            boolean valor = mapa.getOrDefault("cuentas", false);
                            Log.wtf("APL mapa.get(cuentas)", "" + mapa.get("cuentas") + "; valor = " + valor);
                            if (valor)
                                setupStats();
                            else {
                                Toast.makeText(getContext(), "No tienes acceso a los datos", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            } catch (NullPointerException e) {
                Toast.makeText(getContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupStats() {
        Query dinMonthRef = db.collection("users").document(email).collection("transacciones")
                .orderBy("fecha", Query.Direction.DESCENDING);
        rellenarStats(dinMonthRef, dineroMonth);

        Query dinCatRef = db.collection("users").document(email).collection("categorias")
                .orderBy("gastos", Query.Direction.DESCENDING);
        rellenarStats(dinCatRef, dineroCategorias);

        Query dinCtaRef = db.collection("users").document(email).collection("cuentas")
                .orderBy("gastos", Query.Direction.DESCENDING);
        rellenarStats(dinCtaRef, dineroCuentas);
    }

    private void rellenarStats(Query query, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        List<Float> floats = new ArrayList<>();

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    int iteraciones = 10;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.contains("dinero")) { //Transacciones
                            floats.add(Float.parseFloat(document.getDouble("dinero").toString()));

                        } else { //CategoriasCuentas
                            floats.add(Float.parseFloat(document.getDouble("gastos").toString()));
                        }

                        if (iteraciones-- <= 0) break;
                    }
                    for (int i = 0; i < floats.size(); i++)
                        entries.add(new BarEntry(i, floats.get(i)));

                    BarDataSet set = new BarDataSet(entries, "");
                    set.setColors(new int[]{R.color.accentPrimary, R.color.darkAccentPrimary}, getContext());
                    BarData data = new BarData(set);
                    data.setBarWidth(0.9f); // set custom bar width
                    barChart.setData(data);
                    barChart.setFitBars(true); // make the x-axis fit exactly all bars
                    barChart.setTouchEnabled(false);
                    barChart.invalidate(); // refresh
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(), "Ha habido un error al iniciar la actividad", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}