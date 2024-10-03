package modelos;

import java.sql.Date;
import java.util.ArrayList;

public class Cuenta {
    private String codigo;
    private static int cantidadCuentas;
    private Date fechaCreacion;
    private String estatus;
    private int saldo;
    private String pin;
    private double sumaComisiones;
    private double sumaComisionesRetiros;
    private double sumaComisionesDepositos;
    private int sumaRetiros;
    private int sumaDepositos;
    private String monedaOficial;
    private String monedaExtrangera;
    private String pinEncriptado;
    private int usosPin;
    private Cliente miCliente;
    private ArrayList<Transaccion> transacciones;

<<<<<<< HEAD
    public Cuenta(int saldo, String pin, Cliente cliente){
        codigo = "cta-" + cantidadCuentas;
        cantidadCuentas++;
        this.saldo = saldo;
        this.pin = pin;
        miCliente = cliente;
        transacciones = new ArrayList<>();
        estatus = "Activa";
        usosPin = 0;
=======
    public Cuenta(int saldo, String pin) {
        this.saldo = saldo;
        this.pin = pin;
        // this.miCliente = cliente;
        this.transacciones = new ArrayList<>();
        this.estatus = "Activa";
>>>>>>> 3d8900791684dc235b3944d0989966f17be46c85
    }

    public void agregarTransaccion(String tipo, int monto) {
        if (validarEstatus()) {
            Transaccion nuevaTransaccion = new Transaccion(tipo, monto);
            transacciones.add(nuevaTransaccion);
            if (tipo.equals("Retiro")) {
                nuevaTransaccion.realizarRetiro(monto, this.saldo);
            } else {
                if (tipo.equals("Depósito")) {
                    nuevaTransaccion.realizarDeposito(monto, this.saldo);
                }
            }
        }
<<<<<<< HEAD
    }  

    public void cambiarPin(String pinNuevo){
        if(validarCambioPin(pin, pinNuevo)){
            pin = pinNuevo;
        }
    }

    private boolean validarCambioPin(String pinActual, String pinNuevo){
            if(!pinActual.equals(pinNuevo)){
                return true;
            }else{
                return false;
            }
        }

    private boolean validarPin(String pin){
        return this.pin.equals(pin);
    }

    private String encriptarPin(String pin){
       ////////
        return "encriptado_" + pin;
    }

    private void bloquearCuenta(){
        if(estatus.equals("Activa")){
            estatus = "Inactiva";
        }
    }

    private boolean validarEstatus(){
        if(this.estatus.equals("Activa")){
            return true;
        } return false;
    }
 
    private boolean validarIngreso(String pinIngresado){
        while(usosPin <= 3){
            if(pinIngresado.compareTo(pin) == 0){
                usosPin = 0;
                return true;
            }
            usosPin++;
        }
        bloquearCuenta();
        return false;
=======
    }

    /*
     * public void cambiarPin(String pinNuevo){
     * if(validarCambioPin(pin, pinNuevo)){
     * pin = pinNuevo;
     * }
     * }
     * 
     * private boolean validarCambioPin(String pinActual, String pinNuevo){
     * if(pinActual.equals(pinNuevo)){
     * return false;
     * }else{
     * return true;
     * }
     * }
     * 
     * private boolean validarPin(String pin){
     * 
     * }
     * 
     * private String encriptarPin(String pin){
     * 
     * }
     * 
     * private void bloquearCuenta(){
     * if(estatus.equals("Activa")){
     * estatus = "Inactiva";
     * }
     * }
     */
    private boolean validarEstatus() {
        if (this.estatus.equals("Activa")) {
            return true;
        }
        return false;
    }

    public void testAgregarTransaccion() {
        Cuenta cuenta = new Cuenta(50, "stp123");
        cuenta.agregarTransaccion("Depósito", 100);
        System.out.println("Se ejecutó la transacción");
>>>>>>> 3d8900791684dc235b3944d0989966f17be46c85
    }
}