package fp.dam.proy.proy_dam.AdapterClass;

import org.json.JSONException;
import org.json.JSONObject;

public class Transacciones {
    JSONObject json;
    Integer dinero;
    String producto, lugar, fecha;
    //Date fecha;

    public Transacciones(JSONObject json) {
        this.json = json;
        try {
            this.dinero = json.getInt("PRECIO");
            this.producto = json.getString("PRODUCTO");
            this.lugar = json.getString("LUGAR");
            this.fecha = json.getString("FECHA");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getDinero() {
        return dinero;
    }

    public String getProducto() {
        return producto;
    }

    public String getLugar() {
        return lugar;
    }

    public String getFecha() {
        return fecha;
    }
}
