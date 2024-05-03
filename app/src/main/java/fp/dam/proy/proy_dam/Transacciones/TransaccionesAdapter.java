package fp.dam.proy.proy_dam.Transacciones;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        public ViewHolder(View v) {
            super (v);
            dinero = v.findViewById(R.id.transRV_TxtDinero);
            categoria = v.findViewById(R.id.transRV_TxtCategoria);
            cuenta = v.findViewById(R.id.transRV_TxtCuenta);
            comentario = v.findViewById(R.id.transRV_TxtComentario);
        }

        void bind(Transacciones transacciones) {
            dinero.setText(transacciones.getDinero().toString());
            categoria.setText(transacciones.getCategoria());
            cuenta.setText(transacciones.getCuenta());
            comentario.setText(transacciones.getComentario());
        }
    }
}
