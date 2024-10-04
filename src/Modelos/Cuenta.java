package modelos;

import servicios.CorreoElectronico;
import servicios.MensajeSMS;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


public class Cuenta {
    private String codigo;
    private static int cantidadCuentas;
    private LocalDate fechaCreacion;
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


    public Cuenta(int saldo, String pin, Cliente cliente){
        codigo = "cta-" + cantidadCuentas;
        cantidadCuentas++;
        this.saldo = saldo;
        this.pin = pin;
        miCliente = cliente;
        transacciones = new ArrayList<>();
        estatus = "Activa";
        usosPin = 0;
        fechaCreacion = LocalDate.now();
        
    }

    //Getters y Setters
    public String getCodigo(){
        return codigo;
    }

    public int getSaldo(){
        return saldo;
    }

    public String getEstatus(){
        return estatus;
    }

    //Funcionalidades
    public boolean agregarRetiro(int pMonto, String pCodigo){
        String  tipo = "Retiro";
        if (validarEstatus()){
            Transaccion nuevaTransaccion = new Transaccion(tipo, pMonto);
            transacciones.add(nuevaTransaccion);
            MensajeSMS mensajeCodigo = new MensajeSMS();
            boolean mensaje = mensajeCodigo.enviarMensajeVerificacion(miCliente.getNumTelefono(), generarCodigoAleatorio());
            if(mensaje){
                if(mensajeCodigo.verificarCodigo(pCodigo)){
                    nuevaTransaccion.realizarRetiro(pMonto, saldo);
                    sumaRetiros += pMonto;
                    return true;
                }
                else{
                    return false;
                }    
            }else{
                return false;
            }   
        }
        else{
            return false;
        }
    }

    public boolean agregarDeposito(int pMonto){
        String  tipo = "Depósito";
        if (validarEstatus()){
            Transaccion nuevaTransaccion = new Transaccion(tipo, pMonto);
            nuevaTransaccion.realizarDeposito(pMonto, saldo);
            sumaDepositos += pMonto;
            return true;
        }else{
            return false;
        }
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
        return "encriptado_" + pin;
    }

    private void bloquearCuenta(){
        if(estatus.equals("Activa")){
            estatus = "Inactiva";
            CorreoElectronico.enviarCorreo(miCliente.getCorreoElectronico(), "Su cuenta se encuentra inactiva debido a que se intentó autenticar más de tres veces");
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
    }

    private String generarCodigoAleatorio(){
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder(6);
        Random random = new Random();

        // Generar código de 6 caracteres
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length());  // Seleccionar un índice aleatorio
            codigo.append(caracteres.charAt(index));  // Añadir el carácter al código
        }

        return codigo.toString();
    }
}


