package fp.dam.proy.proy_dam.CategoriasCuentas;

public class CategoriasCuentas {
    public String nombre
//    , icon
    ;
    //int icon;
    public double gastos, budget, gastoMens;

    public CategoriasCuentas () {}

    public CategoriasCuentas(String nombre, double gastos, double gastoMens, double budget) {
//    public CategoriasCuentas(String nombre, String icon, double gastos, double budget) {
        this.nombre = nombre;
//        this.icon = icon;
        this.gastos = gastos;
        this.gastoMens = gastos;
        this.budget = budget;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

//    public String getIcon() {
//        return icon;
//    }

//    public void setIcon(String icon) {
//        this.icon = icon;
//    }

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
}
