package controladores;

import modelos.Cuenta;
import servicios.persistenciaDatos;

import java.util.ArrayList;
import java.util.List;

public class BancoControlador {
    public boolean autenticarCuenta(String numeroCuenta, String pin) {
        if (numeroCuenta.equals("123456") && pin.equals("1234")) {
            return true;
        }
        return false;
    }

    private List<Cuenta> cuentas;

    // Constructor que carga las cuentas desde XML al iniciar la aplicación
    public BancoControlador(String rutaArchivoXML) {
        this.cuentas = persistenciaDatos.cargarCuentasDesdeXML(rutaArchivoXML);
    }

    public BancoControlador(List<Cuenta> cuentasIniciales) {
        // Almacenar la lista de cuentas en el controlador
        this.cuentas = new ArrayList<>(cuentasIniciales);
    }

    public List<Cuenta> obtenerCuentas() {
        return cuentas;
    }

    // Aquí irán los métodos para gestionar cuentas como crear, eliminar, cambiar PIN, etc.
    // Ejemplo de creación de cuenta:
    public void crearCuenta(String numeroCuenta, double saldoInicial, String pin) {
        Cuenta nuevaCuenta = new Cuenta(numeroCuenta, saldoInicial, pin);
        cuentas.add(nuevaCuenta);
    }

    // Otros métodos para manejar operaciones con las cuentas
}

