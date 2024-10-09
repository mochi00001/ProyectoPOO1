package modelos;

import java.time.LocalDate;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Transaccion {
    private static int cantidadTransacciones;
    private double porcentajeComision; // Porcentaje de comisión
    private String tipo; // Tipo de transacción (depósito o retiro)
    private LocalDate fecha; // Fecha de la transacción
    private double monto; // Monto de la transacción
    private double montoComision; // Monto de la comisión total
    private String codigoCuenta; // Atributo para el código de cuenta

    public Transaccion(String tipo, double monto, String codigoCuenta) {
        this.porcentajeComision = 2.00;
        this.tipo = tipo;
        this.monto = monto;
        this.codigoCuenta = codigoCuenta; // Asigna el código de cuenta
        this.fecha = LocalDate.now();
    }

    // Método para realizar un retiro y calcular el saldo
    public double realizarRetiro(double montoRetiro, double saldo) {
        double comisionAplicable = calcularComision(montoRetiro);
        
        // Validar si se puede realizar el retiro
        if (montoRetiro + comisionAplicable <= saldo) {
            return saldo - (montoRetiro + comisionAplicable);
        } else {
            System.out.println("Error: Saldo insuficiente para realizar el retiro.");
            return saldo; // No se puede realizar el retiro, se devuelve el saldo actual.
        }
    }

    // Método para verificar si hay una comisión
    public boolean isComision() {
        return montoComision > 0; // Retorna verdadero si hay una comisión calculada
    }

    // Método para calcular la comisión en función del número de transacciones
    private double calcularComision(double montoTransaccion) {
        cantidadTransacciones++; // Aumenta el contador de transacciones
        
        if (cantidadTransacciones > 5) { // Se aplican comisiones a partir de la 6ta transacción
            montoComision = (porcentajeComision / 100) * montoTransaccion;
            return montoComision; // Retorna la comisión calculada
        }
        return 0; // No hay comisión para las primeras 5 transacciones
    }

    // Getters
    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public double getMontoComision() {
        return montoComision; // Devolvemos el monto de la comisión
    }

    public double getMontoRetiroComision() {
        // Aquí puedes retornar la comisión si el tipo es retiro, de lo contrario, retorna 0
        return tipo.equals("retiro") ? montoComision : 0;
    }

    public double getMontoDepositoComision() {
        // Aquí puedes retornar la comisión si el tipo es depósito, de lo contrario, retorna 0
        return tipo.equals("depósito") ? montoComision : 0;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    // Setters
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha; // Establece la fecha de la transacción
    }

    public void setMontoComision(double montoComision) {
        this.montoComision = montoComision; // Establece el monto de la comisión
    }

    @Override
    public String toString() {
        return "Cuenta: " + codigoCuenta + ", Monto: " + monto + ", Fecha: " + fecha;
    }

}
