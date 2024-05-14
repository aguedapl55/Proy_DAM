package fp.dam.proy.proy_dam.CategoriasCuentas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fp.dam.proy.proy_dam.R;

public class CatCtaAdapter extends RecyclerView.Adapter<CatCtaAdapter.ViewHolder> {

    List<CategoriasCuentas> modeList;

    public CatCtaAdapter(List<CategoriasCuentas> modeList) {
        this.modeList = modeList;
    }

    @NonNull
    @Override
    public CatCtaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvrow_catcta, parent, false);
        return new CatCtaAdapter.ViewHolder(v);
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
        private final ImageView icon;

        public ViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.ccRV_nombre);
            icon = v.findViewById(R.id.ccRV_icon);
            dinero = v.findViewById(R.id.ccRV_dinero);
        }

        void bind(CategoriasCuentas catctas) {
            nombre.setText(catctas.getNombre());
            icon.setImageResource(icon.getResources().getIdentifier(catctas.getIcon(), "drawable", icon.getContext().getPackageName()));
            if (catctas.getBudget() == 0) {
                dinero.setText(String.valueOf(catctas.getGastos()));
            } else {
                String s = catctas.getGastos() + " / " + catctas.getBudget();
                dinero.setText(s);
            }
        }
    }
}
