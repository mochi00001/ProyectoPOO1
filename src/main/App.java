package main;

import java.util.List;

// import vistas.GUI; // Descomentar si se va a usar la GUI
import controladores.BancoControlador;
import vistas.CLI;

/**
 * Clase principal que inicializa el sistema bancario. Puede cambiarse entre CLI
 * o GUI
 * simplemente comentando/descomentando la línea correspondiente.
 */

public class App {
    public static void main(String[] args) {
        BancoControlador controlador = new BancoControlador();

        // Inicializa la aplicación con CLI
        iniciarCLI(controlador);

        // Inicializa la aplicación con GUI (Descomentar para usar GUI)
        // iniciarGUI(controlador);
    }

    /**
     * Método para inicializar la aplicación con la interfaz de línea de comandos
     * (CLI).
     */
    private static void iniciarCLI(BancoControlador controlador) {
        CLI cli = new CLI(controlador);
        cli.iniciar();
    }

    /**
     * Método para inicializar la aplicación con la interfaz gráfica de usuario
     * (GUI).
     */
    private static void iniciarGUI(BancoControlador controlador) {
        // Aquí iría el código para inicializar la interfaz gráfica (GUI)
        // GUI gui = new GUI(controlador);
        // gui.iniciar();
    }

    public static void main(String[] args) {
        // Cargar las cuentas desde el archivo XML al iniciar la aplicación
        List<Cuenta> cuentas = persistenciaDatos.cargarCuentasDesdeXML("data/cuentas.xml");

        // Crear el controlador y pasarle las cuentas
        BancoControlador bancoControlador = new BancoControlador(cuentas);

        // Iniciar la vista CLI con el controlador
        CLI cli = new CLI(bancoControlador);
        cli.iniciar();

        // Guardar las cuentas al salir de la aplicación
        persistenciaDatos.guardarCuentasEnXML(bancoControlador.obtenerCuentas(), "data/cuentas.xml");
    }
}
