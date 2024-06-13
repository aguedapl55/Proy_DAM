package fp.dam.proy.proy_dam.CategoriasCuentas;

import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import fp.dam.proy.proy_dam.R;

public class CatCtaAdapter extends RecyclerView.Adapter<CatCtaAdapter.ViewHolder> {

    List<CategoriasCuentas> modeList;
    String coleccion;

    public CatCtaAdapter(List<CategoriasCuentas> modeList, String coleccion) {
        this.modeList = modeList;
        this.coleccion = coleccion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvrow_catcta, parent, false);
        return new ViewHolder(v, coleccion);
    }

    @Override
    public void onBindViewHolder(@NonNull CatCtaAdapter.ViewHolder holder, int position) {
        holder.bind(modeList.get(position));
    }

    @Override
    public int getItemCount() {
        return modeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombre, dinero;
        private double gastos, budget;
        private String id, email;
        private boolean isCategoria;
        FirebaseFirestore db;

        public ViewHolder(View v, String coleccion) {
            super(v);
            nombre = v.findViewById(R.id.ccRV_nombre);
            dinero = v.findViewById(R.id.ccRV_dinero);
            db = FirebaseFirestore.getInstance();

            v.setOnLongClickListener(onLCList -> {
                AlertDialog.Builder constructor = new AlertDialog.Builder(onLCList.getContext());
                constructor.setTitle("¿Eliminar elemento?")
                        .setMessage(new StringBuilder().append("¿Desea eliminar el siguiente elemento?:")
                                .append("\n- nombre: ").append(nombre.getText().toString())
                                .append("\n- gastos totales: ").append(gastos)
                                .append("\n- gastos mensuales: ").append(dinero.getText().toString())
                                .append("\n- budget: ").append(budget))
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            db.collection("users").document(email).collection(coleccion.toString()).get().addOnCompleteListener(task -> {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    if (doc.getId().equals(id)) {
                                        db.collection("users").document(email).collection(coleccion.toString()).document(doc.getId()).delete();
                                    }
                                    Log.wtf("APL CatCtaAdpt doc.getID.equals(id)", "getID = " + doc.getId() + "; id = " + id + "; equivalencia = " + doc.getId().equals(id));
                                }
                                Toast.makeText(onLCList.getContext(), "Se ha eliminado el elemento", Toast.LENGTH_SHORT);
                            });
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> Toast.makeText(onLCList.getContext(), "Se ha cancelado la acción", Toast.LENGTH_SHORT).show())
                        .create().show();
                return true;
            });
        }

        void bind(CategoriasCuentas catctas) {
            nombre.setText(catctas.getNombre());
            if (catctas.getBudget() == 0) {
                dinero.setText(String.valueOf(catctas.getGastos()));
            } else {
                String s = catctas.getGastoMens() + " / " + catctas.getBudget();
                dinero.setText(s);
                if (catctas.getGastoMens() > catctas.getBudget())
                    dinero.setTextColor(Color.parseColor("#FF2222"));
            }
            this.id = catctas.getId();
            this.email = catctas.getEmail();
            this.gastos = catctas.getGastos();
            this.budget = catctas.getBudget();
            this.isCategoria = catctas.isCategoria();
        }
    }
}
