package fp.dam.proy.proy_dam.CategoriasCuentas;

public class CategoriasCuentas {
    String nombre;
    String id, email;
    double gastos, budget, gastoMens;
    boolean isCategoria;

    public CategoriasCuentas () {}

    public CategoriasCuentas(String email, String id, String nombre, double gastos, double gastoMens, double budget, boolean isCategoria) {
        this.email = email;
        this.id = id;
        this.nombre = nombre;
        this.gastos = gastos;
        this.gastoMens = gastos;
        this.budget = budget;
        this.isCategoria = isCategoria;
    }

//    public CategoriasCuentas(String nombre, double gastos, double gastoMens, double budget) {
//        this.nombre = nombre;
//        this.gastos = gastos;
//        this.gastoMens = gastos;
//        this.budget = budget;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getGastos() {
        return gastos;
    }

    public void setGastos(double gastos) {
        this.gastos = gastos;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getGastoMens() {
        return gastoMens;
    }

    public void setGastoMens(double gastoMens) {
        this.gastoMens = gastoMens;
    }

    public boolean isCategoria() {
        return isCategoria;
    }

    public void setCategoria(boolean categoria) {
        isCategoria = categoria;
    }
}
