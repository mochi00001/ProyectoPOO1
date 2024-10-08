package main;

import java.util.List;

import controladores.ClienteControlador;
import controladores.CuentaControlador;
import controladores.TransaccionesControlador;
import vistas.CLI;
// import vistas.GUI; // Descomentar si se va a usar la GUI

/**
 * Clase principal que inicializa el sistema bancario. Puede cambiarse entre CLI
 * o GUI
 * simplemente comentando/descomentando la línea correspondiente.
 */

public class App {
    public static void main(String[] args) {
        CuentaControlador cuentaControlador = new CuentaControlador();
        ClienteControlador clienteControlador = new ClienteControlador();
        TransaccionesControlador transaccionesControlador = new TransaccionesControlador();

        // Inicializa la aplicación con CLI
        iniciarCLI(cuentaControlador, clienteControlador, transaccionesControlador);

        // Inicializa la aplicación con GUI (Descomentar para usar GUI)
        // iniciarGUI(controlador);
    }

    /**
     * Método para inicializar la aplicación con la interfaz de línea de comandos
     * (CLI).
     */
    private static void iniciarCLI(CuentaControlador cuentaControlador, ClienteControlador clienteControlador,
            TransaccionesControlador transaccionesControlador) {
        CLI cli = new CLI(clienteControlador, cuentaControlador, transaccionesControlador);
        cli.iniciar();
    }

    /**
     * Método para inicializar la aplicación con la interfaz gráfica de usuario
     * (GUI).
     */
    private static void iniciarGUI(CuentaControlador cuentaControlador, ClienteControlador clienteControlador,
            TransaccionesControlador transaccionesControlador) {
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
