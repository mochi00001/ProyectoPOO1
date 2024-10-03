package Modelos;

import java.util.ArrayList;
import Modelos.Cuenta;


public class Cliente {
    private String nombre;
    private int identificacion;
    private int numTelefono;
    private String correoElectronico;
    private ArrayList<String> identificaciones;
    private ArrayList<Cuenta> misCuentas;

    public Cliente(String nombre, int identificacion, int numTelefono, String correoElectronico){
        identificaciones = new ArrayList<>();
        if(validarIdentificacion(identificacion)){
            this.identificacion = identificacion;
        }
        this.nombre = nombre;
        this.numTelefono = numTelefono;
        this.correoElectronico = correoElectronico;
        this.identificacion = identificacion;
        identificaciones.add(String.valueOf(identificacion));
    }

    private boolean validarIdentificacion(int identificacion){
        if(identificaciones.contains(String.valueOf(identificacion))){
            return false;
        }else{
            return true;
        }
    }

    public void crearCuenta(int saldo, String pin, Cliente cliente){
        Cuenta cuenta = new Cuenta(saldo, pin, cliente);
        misCuentas.add(cuenta);
    }
}
