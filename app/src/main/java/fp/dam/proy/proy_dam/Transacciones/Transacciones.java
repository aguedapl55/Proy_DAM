package fp.dam.proy.proy_dam.Transacciones;

import com.google.firebase.Timestamp;

public class Transacciones {
    Double dinero;
    String comentario, categoria, cuenta;
    String id, email;
    Timestamp fecha;

    public Transacciones() {}

    public Transacciones(String email, String id, Double dinero, Timestamp fecha, String categoria, String cuenta, String comentario) {
        this.email = email;
        this.id = id;
        this.dinero = dinero;
        this.fecha = fecha;
        this.categoria = categoria;
        this.cuenta = cuenta;
        this.comentario = comentario;
    }

//    public Transacciones(Double dinero, Timestamp fecha, String categoria, String cuenta, String comentario) {
//        this.dinero = dinero;
//        this.fecha = fecha;
//        this.categoria = categoria;
//        this.cuenta = cuenta;
//        this.comentario = comentario;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getDinero() {
        return dinero;
    }

    public String getComentario() {
        return comentario;
    }

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
