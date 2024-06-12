package fp.dam.proy.proy_dam.Transacciones;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class TransaccionesAdapter extends RecyclerView.Adapter<TransaccionesAdapter.ViewHolder> {

    List<Transacciones> modeList;

    public TransaccionesAdapter(List<Transacciones> modelist) {
        this.modeList = modelist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(fp.dam.proy.proy_dam.R.layout.rvrow_transaccion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(modeList.get(position));
    }

    @Override
    public int getItemCount() {
        return modeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dinero, categoria, cuenta, comentario;
        private String id, email;
        FirebaseFirestore db;

        public ViewHolder(View v) {
            super (v);
            dinero = v.findViewById(R.id.transRV_TxtDinero);
            categoria = v.findViewById(R.id.transRV_TxtCategoria);
            cuenta = v.findViewById(R.id.transRV_TxtCuenta);
            comentario = v.findViewById(R.id.transRV_TxtComentario);
            db = FirebaseFirestore.getInstance();

            v.setOnLongClickListener(onLCList -> {
                AlertDialog.Builder constructor = new AlertDialog.Builder(onLCList.getContext());
                constructor.setTitle("¿Eliminar transaccion?")
                        .setMessage(new StringBuilder().append("¿Desea eliminar la siguiente transacción?:")
                                .append("\n- dinero: ").append(dinero.getText().toString())
                                .append("\n- categoría: ").append(categoria.getText().toString())
                                .append("\n- cuenta: ").append(cuenta.getText().toString())
                                .append("\n- comentario: ").append(comentario.getText().toString().isEmpty() ? "N/A" : comentario.getText().toString()))
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("users").document(email).collection("transacciones").get().addOnCompleteListener(task -> {
                                    for (DocumentSnapshot doc : task.getResult()) {
                                        if (doc.getId().equals(id)) {
                                            db.collection("users").document(email).collection("transacciones").document(doc.getId()).delete().addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful())
                                                    Toast.makeText(onLCList.getContext(), "Se ha eliminado la transacción", Toast.LENGTH_SHORT);
                                            });
                                        }
                                        Log.wtf("APL TransAdpt doc.getID.equals(id)", "getID = " + doc.getId() + "; id = " + id + "; equivalencia = " + doc.getId().equals(id));
                                    }
                                });
                                Toast.makeText(onLCList.getContext(), "ELIMINAR", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(onLCList.getContext(), "Se ha cancelado la acción", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();
                return true;
            });
        }

        void bind(Transacciones transacciones) {
            dinero.setText(transacciones.getDinero().toString());
            categoria.setText(transacciones.getCategoria());
            cuenta.setText(transacciones.getCuenta());
            comentario.setText(transacciones.getComentario());
            this.id = transacciones.getId();
            this.email = transacciones.getEmail();
        }
    }
}
