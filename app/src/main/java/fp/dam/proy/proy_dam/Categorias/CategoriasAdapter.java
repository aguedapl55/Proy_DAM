package fp.dam.proy.proy_dam.Categorias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fp.dam.proy.proy_dam.R;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    List<Categorias> modeList;

    public CategoriasAdapter(List<Categorias> modeList) {
        this.modeList = modeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvrow_categorias, parent, false);
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
        private final TextView nombre, dinero;
        private final ImageView icon;

        public ViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.catRV_nombre);
            icon = v.findViewById(R.id.catRV_icon);
            dinero = v.findViewById(R.id.catRV_dinero);
        }

        void bind(Categorias categorias) {
            nombre.setText(categorias.getNombre());
            icon.setImageResource(categorias.getIcon());
            dinero.setText(String.valueOf(categorias.getDinero()));
        }
    }
}
