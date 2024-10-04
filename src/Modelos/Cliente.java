package modelos;

import java.util.ArrayList;
import java.util.Iterator;

public class Cliente {
    private String nombre;
    private int identificacion;
    private String numTelefono;
    private String correoElectronico;
    private ArrayList<String> identificaciones;
    private ArrayList<Cuenta> misCuentas;

    public Cliente(String nombre, int identificacion, String numTelefono, String correoElectronico) {
        identificaciones = new ArrayList<>();
        if (validarIdentificacion(identificacion)) {
            this.identificacion = identificacion;
        }
        this.nombre = nombre;
        this.numTelefono = numTelefono;
        this.correoElectronico = correoElectronico;
        this.identificacion = identificacion;
        identificaciones.add(String.valueOf(identificacion));
    }

    //Getters y setters

    public int getIdentificacion(){
        return identificacion;
    }

    public String getCorreoElectronico(){
        return correoElectronico;
    }

    public String getNumTelefono(){
        return numTelefono;
    }


    private boolean validarIdentificacion(int identificacion) {
        if (identificaciones.contains(String.valueOf(identificacion))) {
            return false;
        } else {
            return true;
        }
    }

    public void crearCuenta(int saldo, String pin, Cliente cliente){
        Cuenta cuenta = new Cuenta(saldo, pin, cliente);
        misCuentas.add(cuenta);
    }

    public void eliminarCuenta(String codigo){
        Iterator<Cuenta> iterator = misCuentas.iterator();

        while (iterator.hasNext()) {
            Cuenta cuenta = iterator.next();
            if (cuenta.getCodigo().equals(codigo)) {
                iterator.remove(); // Elimina la cuenta de la lista
                break;
            }
        }
    }
}
