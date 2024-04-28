package fp.dam.proy.proy_dam.AdapterClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fp.dam.proy.proy_dam.Class.Transacciones;
import fp.dam.proy.proy_dam.R;

public class TransaccionesAdapter extends RecyclerView.Adapter<TransaccionesAdapter.ViewHolder> {

    List<Transacciones> modeList;

    public TransaccionesAdapter(List<Transacciones> lista) {
        modeList = lista;
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
        /*
        Transacciones tr = modeList.get(position);
        holder.fecha.setText(tr.getFecha().toDate().toString());
        holder.lugar.setText(tr.getLugar());
        holder.producto.setText(tr.getProducto());
        holder.precio.setText(tr.getDinero().toString());
         */
    }

    @Override
    public int getItemCount() {
        return modeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fecha;
        private final TextView lugar;
        private final TextView producto;
        private final TextView precio;

        public ViewHolder(View v) {
            super (v);
            fecha = v.findViewById(R.id.transRV_TxtFecha);
            lugar = v.findViewById(R.id.transRV_TxtLugar);
            producto = v.findViewById(R.id.transRV_TxtProd);
            precio = v.findViewById(R.id.transRV_TxtDinero);
        }

        void bind(Transacciones transacciones) {
            precio.setText(transacciones.getDinero().toString());
            producto.setText(transacciones.getProducto());
            fecha.setText(transacciones.getFecha());
            lugar.setText(transacciones.getLugar());
        }
    }
}
