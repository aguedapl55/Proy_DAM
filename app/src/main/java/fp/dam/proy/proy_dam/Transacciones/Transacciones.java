package fp.dam.proy.proy_dam.Transacciones;

import com.google.firebase.Timestamp;

public class Transacciones {
    Double dinero;
    String comentario, categoria, cuenta;
    Timestamp fecha;

    public Transacciones(Double dinero, Timestamp fecha, String categoria, String cuenta, String comentario) {
        this.dinero = dinero;
        this.fecha = fecha;
        this.categoria = categoria;
        this.cuenta = cuenta;
        this.comentario = comentario;
    }

    public Double getDinero() {
        return dinero;
    }

    public String getComentario() {
        return comentario;
    }

    /*
    public String getFechaFormated() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha.toDate());
    }
     */

    public Timestamp getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setDinero(Double dinero) {
        this.dinero = dinero;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

}
