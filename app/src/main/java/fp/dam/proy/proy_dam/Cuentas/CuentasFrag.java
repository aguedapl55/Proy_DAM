package fp.dam.proy.proy_dam.Cuentas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fp.dam.proy.proy_dam.CategoriasCuentas.CatCtaAdapter;
import fp.dam.proy.proy_dam.CategoriasCuentas.CategoriasCuentas;
import fp.dam.proy.proy_dam.R;

public class CuentasFrag extends Fragment {

    private String email, usuario;
    private FirebaseFirestore db;
    private RecyclerView rv;
    private TextView taskSize;
    private List<CategoriasCuentas> cuentas;
    Boolean check;

    public CuentasFrag() {}

    public static CuentasFrag newInstance(String email, String usuario) {
        CuentasFrag fragment = new CuentasFrag();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cuentas = new ArrayList<>();
        email = getArguments().getString("email");
        usuario = getArguments().getString("usuario");
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cuentas, container, false);
        rv = rootView.findViewById(R.id.ctaRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        checkRellenar();
        CatCtaAdapter adapter = new CatCtaAdapter(cuentas);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        taskSize = rootView.findViewById(R.id.taskSize);
        return rootView;
    }

    private void checkRellenar() {
        Log.wtf("APL usuario.equals(email)", "" + usuario.equals(email));
        if (usuario.equals(email)) {
            rellenarRV(rv);
        }
        else {
            db.collection("users").document(usuario).get().addOnCompleteListener(task -> {
                List<String> usersAccesibles = new ArrayList<>();
                DocumentSnapshot doc = task.getResult();
                usersAccesibles.addAll(Arrays.asList(
                        doc.get("hijos").toString()
                                .replace("[", "")
                                .replace("]", "")
                                .split(", ")));
                usersAccesibles.removeIf(d -> d.equals(""));
                Log.wtf("APL usersAccesibles.contains(email)", "" + usersAccesibles.contains(email));
                if (usersAccesibles.contains(email)) {
                    rellenarRV(rv);
//                    mensOverBudget(rv);
                }
                else {
                    db.collection("users").document(email).get().addOnCompleteListener(task2 -> {
                        Map<String, Boolean> mapa = (Map<String, Boolean>) task2.getResult().get("visibilidad");
                        boolean valor = mapa.get("cuentas");
                        Log.wtf("APL mapa.get(cuentas)", "" + mapa.get("cuentas") + "; valor = " + valor);
                        if (valor)
                            rellenarRV(rv);
                        else
                            Toast.makeText(getContext(), "No tienes acceso a los datos", Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }

    private void rellenarRV(@NonNull RecyclerView rv) {
        DocumentReference usuario = db.collection("users").document(email);
        cuentas = new ArrayList<>();
        usuario.collection("cuentas").orderBy("nombre", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.wtf("APL TAMAÑO TASK", "" + task.getResult().size());
                        if (task.getResult().size() == 0)
                            taskSize.setText("Aún no has añadido cuentas");
                        else
                            taskSize.setText("");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.contains("nombre") && document.contains("gastos")) {
//                                    double aux = getTransFechas(task.getResult());
//                                    Date fecha = Date.newBuilder().setDay(1)
//                                            .setMonth(Calendar.getInstance().get(Calendar.MONTH))
//                                            .setYear(Calendar.getInstance().get(Calendar.YEAR))
//                                            .build();
//                                    List<Double> auxL = new ArrayList<>();
//                                    usuario.collection("transacciones")
//                                            .whereEqualTo("cuenta", document.getString("nombre"))
//                                            .whereGreaterThanOrEqualTo("fecha", fecha)
//                                            .get().getResult().getDocuments().forEach(d -> auxL.add(d.getDouble("dinero")));
//                                    double aux = 0;
//                                    for (Double d : auxL)
//                                        aux += d;

//                                double aux = usuario.collection("transacciones")
//                                        .whereEqualTo("cuenta", document.getString("nombre"))
//                                        .whereEqualTo("fecha", fecha)
//                                        .get().getResult().getDocuments().stream().mapToDouble(d -> d.getDouble("dinero")).sum();
                                CategoriasCuentas cta = new CategoriasCuentas(
                                        document.getString("nombre"),
                                        document.getDouble("gastos"),
                                        document.getDouble("gastoMens"),
                                        document.getDouble("budget"));
                                cuentas.add(cta);
                                Log.wtf("APL AÑADIDO", document.getId() + " => " + document.getData());
                            } else
                                Log.wtf("APL SALTADO", document.getId());
                        }
                        rv.getAdapter().notifyDataSetChanged();
                        mensOverBudget(rv);
                    } else {
                        Log.wtf("APL TASK FALLADO", "Error getting documents.", task.getException());
                    }
                });
    }

    private void mensOverBudget(@NonNull RecyclerView rv) {
        StringBuilder sb = new StringBuilder();
        sb.append("Se ha sobrepasado el límite en las siguientes cuentas: \n");
        int size = sb.length();
        cuentas.forEach(c -> {
            if (c.getGastoMens() > c.getBudget() && c.getBudget() != 0.0) {
                Log.wtf("APL mensOverBudget true", c.getNombre());
                sb.append("- " + c.getNombre() + " (" + (c.getGastoMens() - c.getBudget()) + ")\n");
            }
        });
        if (sb.length() > size) {
            Log.wtf("APL length>size true", "length = " + sb.length() + "; size = " + size);
            AlertDialog.Builder constructor = new AlertDialog.Builder(getContext());
            constructor.setTitle("Budget superado")
                    .setMessage(sb)
                    .setIcon(R.mipmap.ic_launcher)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            constructor.create().show();
        }
    }

    public double getTransFechas(List<QueryDocumentSnapshot> documentos) {
        List<Double> auxL = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documentos) {
            Date fecha = Date.newBuilder().setDay(1)
                    .setMonth(Calendar.getInstance().get(Calendar.MONTH))
                    .setYear(Calendar.getInstance().get(Calendar.YEAR))
                    .build();

            db.collection("users").document(email).collection("transacciones")
                    .whereEqualTo("cuenta", doc.getString("nombre"))
                    .whereGreaterThanOrEqualTo("fecha", fecha)
                    .get().getResult().getDocuments().forEach(d -> auxL.add(d.getDouble("dinero")));
        }
        double aux = 0;
        for (Double d : auxL)
            aux += d;
        return aux;
    }
}