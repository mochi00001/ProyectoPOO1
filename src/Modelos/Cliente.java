package modelos;

import validacion.Formato;

import java.util.List;
import java.util.ArrayList;


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

    //Funcionalidades
    public List<Cuenta> getMisCuentas() {
        return misCuentas;
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

    // Getters y Setters
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
        if (!Formato.validarTelefono(numTelefono)) {
            throw new IllegalArgumentException("Número de teléfono inválido.");
        }
        this.numTelefono = numTelefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    // Sobreescritura del método
    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", identificacion=" + identificacion +
                ", numTelefono='" + numTelefono + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", misCuentas=" + misCuentas.size() + " cuentas" +
                '}';
    }
}
