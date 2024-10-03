package Modelos;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Random;
import Modelos.Cuenta;

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
    

    public Transaccion(String tipo, int monto){
        this.porcentajeComision = 2.00;
        this.tipo = tipo;
        this.fecha = LocalDateTime.now();

    }

    public int realizarRetiro(int montoRetiro, int saldo){
        if(validarRetiro(montoRetiro, saldo)){
            if(this.comision == true){
                return (int) (saldo - montoDepositoComision);
            } else{
                return saldo - montoRetiro;
            }
        } else{
            return saldo;
        }
    }

    private boolean validarRetiro(int montoRetiro, int saldo){
        if(validarAplicacionDeComision()){
            montoRetiroComision = montoRetiro * (porcentajeComision/100);
            if(montoRetiroComision > saldo){
                return false;
            }else{
                return true;
            }
        }else{
            if(montoRetiro > saldo){
                return false;
            }else{
                return true;
            }
        }
    }


    private boolean validarAplicacionDeComision(){
        if(cantidadTransacciones <= cantidadTransaccionesSinComision){
            comision = false;
            return false;
        } else{
            comision = true;
            return true;
        }
    }

    public int realizarDeposito(int montoDeposito, int saldo){
        return saldo -= montoDeposito;
    }

    private String generarPalabraAleatoria(int longitudPalabraAleatoria){
        String letras = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder palabra = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < longitudPalabraAleatoria; i++) {
            int indice = random.nextInt(letras.length());  
            palabra.append(letras.charAt(indice));         
        }
        
        return palabra.toString();
    }
    
}   
