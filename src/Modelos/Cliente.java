package modelos;

import java.util.ArrayList;

public class Cliente {
    private String nombre;
    private int identificacion;
    private int numTelefono;
    private String correoElectronico;
    private ArrayList<String> identificaciones;
    private ArrayList<Cuenta> misCuentas;

    public Cliente(String nombre, int identificacion, int numTelefono, String correoElectronico) {
        identificaciones = new ArrayList<>();
        if (validarIdentificacion(identificacion)) {
            this.identificacion = identificacion;
        }
        this.nombre = nombre;
        this.numTelefono = numTelefono;
        this.correoElectronico = correoElectronico;
        this.identificacion = identificacion;
        identificaciones.add(String.valueOf(identificacion));
    }

    private boolean validarIdentificacion(int identificacion) {
        if (identificaciones.contains(String.valueOf(identificacion))) {
            return false;
        } else {
            return true;
        }
    }

<<<<<<< HEAD
    public void crearCuenta(int saldo, String pin, Cliente cliente){
        Cuenta cuenta = new Cuenta(saldo, pin, cliente);
=======
    public void crearCuenta(int saldo, String pin) {
        Cuenta cuenta = new Cuenta(saldo, pin);
>>>>>>> 3d8900791684dc235b3944d0989966f17be46c85
        misCuentas.add(cuenta);
    }
}
