package main;

import controladores.ClienteControlador;
import controladores.CuentaControlador;
import controladores.TransaccionesControlador;
import vistas.CLI;
import vistas.GUI;

import java.util.List;

import modelos.Cliente;
import modelos.Cuenta;
import servicios.PersistenciaDatos;
// import vistas.GUI; // Descomentar si se va a usar la GUI

/**
 * Clase principal que inicializa el sistema bancario. Puede cambiarse entre CLI
 * o GUI
 * simplemente comentando/descomentando la línea correspondiente.
 */

public class App {

    public static void main(String[] args) {
        ClienteControlador clienteControlador = new ClienteControlador();
        CuentaControlador cuentaControlador = new CuentaControlador(clienteControlador);
        TransaccionesControlador transaccionesControlador = new TransaccionesControlador(cuentaControlador);

        // Inicializa la aplicación con CLI
        //iniciarCLI(cuentaControlador, clienteControlador, transaccionesControlador);

        // Inicializa la aplicación con GUI (Descomentar para usar GUI)
        iniciarGUI(cuentaControlador, clienteControlador, transaccionesControlador);
    }
    
    /**
     * Método para inicializar la aplicación con la interfaz gráfica de usuario
     * (GUI).
     */
    private static void iniciarGUI(CuentaControlador cuentaControlador, ClienteControlador clienteControlador,
            TransaccionesControlador transaccionesControlador) {
        GUI gui = new GUI(cuentaControlador, clienteControlador, transaccionesControlador);
        gui.iniciar();
    } 
}