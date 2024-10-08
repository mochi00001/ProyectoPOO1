package modelos;

import java.time.LocalDateTime;
import java.util.Random;
import servicios.MensajeSMS;

public class Transaccion {
    private static int cantidadTransacciones;
    private double porcentajeComision;
    private String tipo;
    private LocalDateTime fecha;
    private boolean comision;
    private int monto;
    private double montoRetiroComision;
    private double montoDepositoComision;
    private String palabraValidacion;
    private int intentosValidacion;
    private int cantidadTransaccionesSinComision;
    private int longitudPalabraAleatoria;
    private String codigoCuenta; // Atributo para el código de cuenta

    public Transaccion(String tipo, double monto, String codigoCuenta) {
        this.porcentajeComision = 2.00;
        this.tipo = tipo;
        this.monto = monto;
        this.codigoCuenta = codigoCuenta; // Asigna el código de cuenta
        this.fecha = LocalDateTime.now();
        longitudPalabraAleatoria = 7;
    }

    public double realizarRetiro(double montoRetiro, double saldo) {
        if (validarRetiro(montoRetiro, saldo)) {
            if (this.comision) {
                montoRetiroComision = montoRetiro * (1 + porcentajeComision / 100);
                return saldo - montoRetiroComision;
            } else {
                return saldo - montoRetiro;
            }
        } else {
            return saldo; // No se puede realizar el retiro, se devuelve el saldo actual.
        }
    }

    private boolean validarRetiro(double montoRetiro, double saldo) {
        if (validarAplicacionDeComision()) {
            montoRetiroComision = montoRetiro * (porcentajeComision / 100);
            return montoRetiroComision <= saldo;
        } else {
            return montoRetiro <= saldo;
        }
    }

    private boolean validarAplicacionDeComision() {
        if (cantidadTransacciones <= cantidadTransaccionesSinComision) {
            comision = false;
            return false;
        } else {
            comision = true;
            return true;
        }
    }

    public int realizarDeposito(int montoDeposito, int saldo) {
        return saldo -= montoDeposito;
    }

    public String generarPalabraAleatoria() {
        String letras = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder palabra = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < longitudPalabraAleatoria; i++) {
            int indice = random.nextInt(letras.length());
            palabra.append(letras.charAt(indice));
        }
        return palabra.toString();
    }

    // Getters
    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public boolean isComision() {
        return comision;
    }

    public double getMontoRetiroComision() {
        return montoRetiroComision;
    }

    public double getMontoDepositoComision() {
        return montoDepositoComision;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

}
