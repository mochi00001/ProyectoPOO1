package controladores;

import java.util.HashMap;

import modelos.Cuenta;
import modelos.Transaccion;

public class TransaccionesControlador {

    // Mapa para almacenar las cuentas
    private HashMap<String, Cuenta> cuentas;

    public TransaccionesControlador() {
        this.cuentas = new HashMap<String, Cuenta>();
    }

    // Método para realizar un depósito en colones
    public boolean realizarDeposito(String numeroCuenta, int monto) {
        Cuenta cuenta = cuentas.get(numeroCuenta);
        if (cuenta != null) {
            Transaccion transaccion = new Transaccion("Depósito", monto);
            cuenta.setSaldo(cuenta.getSaldo() + monto);
            cuenta.getTransacciones().add(transaccion);
            return true;
        }
        return false; // Cuenta no encontrada
    }

    // Método para realizar un depósito en dólares, convierte a colones usando el
    // tipo de cambio
    public boolean realizarDepositoDolares(String numeroCuenta, int montoDolares, double tipoCambioCompra) {
        Cuenta cuenta = cuentas.get(numeroCuenta);
        if (cuenta != null) {
            int montoColones = (int) (montoDolares * tipoCambioCompra);
            Transaccion transaccion = new Transaccion("Depósito (Dólares a Colones)", montoColones);
            cuenta.setSaldo(cuenta.getSaldo() + montoColones);
            cuenta.getTransacciones().add(transaccion);
            return true;
        }
        return false; // Cuenta no encontrada
    }

    // Método para realizar un retiro
    public boolean realizarRetiro(String numeroCuenta, String pin, int montoRetiro) {
        Cuenta cuenta = cuentas.get(numeroCuenta);
        if (cuenta != null && cuenta.validarIngreso(pin)) {
            Transaccion transaccion = new Transaccion("Retiro", montoRetiro);
            int nuevoSaldo = transaccion.realizarRetiro(montoRetiro, cuenta.getSaldo());
            if (nuevoSaldo != cuenta.getSaldo()) { // Si el saldo cambia, significa que el retiro fue exitoso
                cuenta.setSaldo(nuevoSaldo);
                cuenta.getTransacciones().add(transaccion);
                return true;
            }
        }
        return false; // Cuenta no encontrada o PIN incorrecto
    }
}
