package modelos;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private int identificacion;
    private String numTelefono;
    private String correoElectronico;
    private ArrayList<Cuenta> misCuentas;

    public Cliente(String nombre, int identificacion, String numTelefono, String correoElectronico) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.numTelefono = numTelefono;
        this.correoElectronico = correoElectronico;
        this.misCuentas = new ArrayList<>();
    }

    public Cliente(int identificacion) {
        this.identificacion = identificacion;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public List<Cuenta> getMisCuentas() {
        return misCuentas; // Devuelve como List
    }

    private boolean validarIdentificacion(int pIdentificacion) {
        if (identificacion.equals(String.valueOf(pIdentificacion))) {
            return false;
        } else {
            return true;
        }
    }

    public void crearCuenta(int saldo, String pin, Cliente cliente) {
        Cuenta cuenta = new Cuenta(saldo, pin, cliente);
        misCuentas.add(cuenta);
    }

    public void agregarCuenta(Cuenta cuenta) {
        misCuentas.add(cuenta);
    }

    // Método para listar las cuentas asociadas a este cliente
    public void mostrarCuentas() {
        System.out.println("Cuentas asociadas al cliente " + nombre + " (ID: " + identificacion + "):");
        for (Cuenta cuenta : misCuentas) {
            System.out.println("Cuenta: " + cuenta.getCodigo() + ", Saldo: " + cuenta.getSaldo());
        }
    }

}
