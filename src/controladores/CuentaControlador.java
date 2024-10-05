package controladores;

import java.util.HashMap;

import modelos.Cliente;
import modelos.Cuenta;

public class CuentaControlador {

    // Mapa para almacenar cuentas (número de cuenta como clave y objeto Cuenta como
    // valor)
    private HashMap<String, Cuenta> cuentas;

    public CuentaControlador() {
        this.cuentas = new HashMap<>();
    }

    // Método para autenticar cuenta con número de cuenta y PIN
    public boolean autenticarCuenta(String numeroCuenta, String pin) {
        Cuenta cuenta = cuentas.get(numeroCuenta);
        if (cuenta != null) {
            return cuenta.validarIngreso(pin);
        }
        return false;
    }

    // Método para crear una cuenta y añadirla al sistema
    public boolean crearCuenta(int saldoInicial, String pin, Cliente cliente) {
        try {
            if (!validarFormatoPin(pin)) {
                throw new IllegalArgumentException("El PIN no cumple con el formato requerido.");
            }
            String numeroCuenta = "cta-" + cuentas.size(); // Generar número de cuenta único
            Cuenta nuevaCuenta = new Cuenta(saldoInicial, pin, cliente);
            nuevaCuenta.setPinEncriptado(nuevaCuenta.encriptarPin(pin));
            cuentas.put(numeroCuenta, nuevaCuenta);
            return true;
        } catch (Exception e) {
            System.err.println("Error al crear la cuenta: " + e.getMessage());
            return false;
        }
    }

    // Método para cambiar el PIN de una cuenta
    public boolean cambiarPinCuenta(String numeroCuenta, String pinActual, String nuevoPin) {
        Cuenta cuenta = cuentas.get(numeroCuenta);
        if (cuenta != null && cuenta.validarIngreso(pinActual)) {
            cuenta.cambiarPin(nuevoPin);
            return true;
        }
        return false; // Error en autenticación o cuenta no encontrada
    }

    // Método para eliminar una cuenta
    public boolean eliminarCuenta(String numeroCuenta, String pin) {
        Cuenta cuenta = cuentas.get(numeroCuenta);
        if (cuenta != null && cuenta.validarIngreso(pin)) {
            cuentas.remove(numeroCuenta);
            return true;
        }
        return false; // No se puede eliminar si la autenticación falla o la cuenta no existe
    }

    // Método auxiliar para validar el formato del PIN
    private boolean validarFormatoPin(String pin) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6}$";
        return pin.matches(regex);
    }
}
