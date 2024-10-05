package vistas;

import java.util.Scanner;

import controladores.ClienteControlador;
import controladores.CuentaControlador;
import controladores.TransaccionesControlador;
import modelos.Cliente;

/**
 * Clase que representa la vista en el patrón MVC, encargada de interactuar
 * con el usuario a través de una interfaz de línea de comandos (CLI).
 */
public class CLI {
    private Scanner scanner;
    private boolean autenticado = false;
    private String cuentaAutenticada;
    private ClienteControlador clienteControlador;
    private CuentaControlador cuentasControlador;
    private TransaccionesControlador transaccionesControlador;

    public CLI(ClienteControlador clienteControlador, CuentaControlador cuentaControlador,
            TransaccionesControlador transaccionesControlador) {
        this.clienteControlador = clienteControlador;
        this.cuentasControlador = cuentaControlador;
        this.transaccionesControlador = transaccionesControlador;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {

        System.out.println("--- Bienvenido al Sistema Bancario ---");
        iniciarSesion(); // Verificamos el inicio de sesión

        boolean ejecutar = true;
        while (ejecutar) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    mostrarMenuGestionClientes();
                    break;
                case 2:
                    mostrarMenuGestionCuentas();
                    break;
                case 3:
                    mostrarMenuOperacionesFinancieras();
                    break;
                case 4:
                    mostrarMenuConsultasReportes();
                    break;
                case 5:
                    System.out.println("Configuración del sistema no disponible.");
                    break;
                case 6:
                    ejecutar = false;
                    System.out.println("Gracias por usar el sistema.");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }

    // Muestra el menú de inicio de sesión
    private void iniciarSesion() {
        while (!autenticado) {
            System.out.println("\n--- Inicio de Sesión ---");
            String numeroCuenta = leerString("Ingrese su cuenta de cuenta: ");
            String pin = leerString("Ingrese su PIN: ");

            if (cuentasControlador.autenticarCuenta(numeroCuenta, pin)) {
                System.out.println("Autenticación exitosa.");
                cuentaAutenticada = numeroCuenta;
                autenticado = true;
            } else {
                System.out.println("Error: Número de cuenta o PIN incorrecto.");
            }
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Gestión de Clientes");
        System.out.println("2. Gestión de Cuentas");
        System.out.println("3. Operaciones Financieras");
        System.out.println("4. Consultas y Reportes");
        System.out.println("5. Configuración del Sistema");
        System.out.println("6. Salir");
    }

    private void mostrarMenuGestionClientes() {
        boolean ejecutar = true;
        while (ejecutar) {
            System.out.println("\n--- Gestión de Clientes ---");
            System.out.println("1. Crear Cliente");
            System.out.println("2. Cambiar Número de Teléfono");
            System.out.println("3. Cambiar Dirección de Correo");
            System.out.println("0. Volver al Menú Principal");

            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    crearCliente();
                    break;
                case 2:
                    cambiarTelefonoCliente();
                    break;
                case 3:
                    cambiarCorreoCliente();
                    break;
                case 0:
                    ejecutar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void mostrarMenuGestionCuentas() {
        boolean ejecutar = true;
        while (ejecutar) {
            System.out.println("\n--- Gestión de Cuentas ---");
            System.out.println("1. Crear Cuenta");
            System.out.println("2. Cambiar PIN");
            System.out.println("3. Eliminar Cuenta");
            System.out.println("0. Volver al Menú Principal");

            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    crearCuenta();
                    break;
                case 2:
                    cambiarPin();
                    break;
                case 3:
                    eliminarCuenta();
                    break;
                case 0:
                    ejecutar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void mostrarMenuOperacionesFinancieras() {
        boolean ejecutar = true;
        while (ejecutar) {
            System.out.println("\n--- Operaciones Financieras ---");
            System.out.println("1. Realizar Depósito en Colones");
            System.out.println("2. Realizar Depósito con Cambio de Moneda");
            System.out.println("3. Realizar Retiro en Colones");
            System.out.println("0. Volver al Menú Principal");

            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    realizarDepositoColones();
                    break;
                case 2:
                    realizarDepositoDolares();
                    break;
                case 3:
                    realizarRetiro();
                    break;
                case 0:
                    ejecutar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void mostrarMenuConsultasReportes() {
        boolean ejecutar = true;
        while (ejecutar) {
            System.out.println("\n--- Consultas y Reportes ---");
            System.out.println("1. Consultar Transacciones");
            System.out.println("2. Consultar Tipo de Cambio de Compra");
            System.out.println("3. Consultar Saldo Actual");
            System.out.println("0. Volver al Menú Principal");

            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    consultarTransacciones();
                    break;
                case 2:
                    consultarTipoCambioCompra();
                    break;
                case 3:
                    consultarSaldoActual();
                    break;
                case 0:
                    ejecutar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor, ingrese un número: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private String leerString(String mensaje) {
        System.out.print(mensaje);
        return scanner.next();
    }

    // Métodos para las operaciones del menú de gestión de clientes (simplificados,
    // la lógica real estará en el
    // controlador)
    private void crearCliente() {
        String nombre = leerString("Ingrese nombre del cliente: ");
        int identificacion = leerEntero("Ingrese identificación: ");
        String telefono = leerString("Ingrese teléfono: ");
        String correo = leerString("Ingrese correo: ");

        boolean resultado = clienteControlador.crearCliente(nombre, identificacion, telefono, correo);
        if (resultado) {
            System.out.println("Cliente creado exitosamente.");
        } else {
            System.out.println("Error: Ya existe un cliente con la identificación proporcionada.");
        }
    }

    private void cambiarTelefonoCliente() {
        int identificacion = leerEntero("Ingrese la identificación del cliente: ");
        String nuevoTelefono = leerString("Ingrese el nuevo número de teléfono: ");

        boolean resultado = clienteControlador.actualizarTelefono(identificacion, nuevoTelefono);
        if (resultado) {
            System.out.println("Número de teléfono actualizado exitosamente.");
        } else {
            System.out.println("Error: No se encontró un cliente con la identificación proporcionada.");
        }
    }

    private void cambiarCorreoCliente() {
        int identificacion = leerEntero("Ingrese la identificación del cliente: ");
        String nuevoCorreo = leerString("Ingrese el nuevo correo electrónico: ");

        boolean resultado = clienteControlador.actualizarCorreo(identificacion, nuevoCorreo);
        if (resultado) {
            System.out.println("Correo electrónico actualizado exitosamente.");
        } else {
            System.out.println("Error: No se encontró un cliente con la identificación proporcionada.");
        }
    }

    // Métodos para las operaciones del menú de gestión de cuentas (simplificados,
    // la lógica real estará en el
    // controlador)
    private void crearCuenta() {
        int saldoInicial = leerEntero("Ingrese saldo inicial: ");
        String pin = leerString("Ingrese PIN (debe tener 6 caracteres, al menos una letra mayúscula y un número): ");
        System.out.print("Ingrese identificación del cliente para asignar la cuenta: ");
        int identificacion = leerEntero("");

        Cliente cliente = clienteControlador.buscarClientePorIdentificacion(identificacion);

        if (cliente != null) {
            boolean resultado = cuentasControlador.crearCuenta(saldoInicial, pin, cliente);
            if (resultado) {
                System.out.println("Cuenta creada exitosamente.");
            } else {
                System.out.println("Error al crear la cuenta. Verifique los datos.");
            }
        } else {
            System.out.println("Cliente no encontrado. Verifique la identificación proporcionada.");
        }
    }

    private void cambiarPin() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        String pinActual = leerString("Ingrese el PIN actual: ");
        String nuevoPin = leerString(
                "Ingrese el nuevo PIN (debe tener 6 caracteres, al menos una letra mayúscula y un número): ");

        boolean resultado = cuentasControlador.cambiarPinCuenta(numeroCuenta, pinActual, nuevoPin);
        if (resultado) {
            System.out.println("PIN actualizado exitosamente.");
        } else {
            System.out.println("Error: No se pudo cambiar el PIN. Verifique el PIN actual y la cuenta ingresada.");
        }
    }

    private void eliminarCuenta() {
        String numeroCuenta = leerString("Ingrese el número de cuenta a eliminar: ");
        String pin = leerString("Ingrese el PIN de la cuenta: ");

        boolean resultado = cuentasControlador.eliminarCuenta(numeroCuenta, pin);
        if (resultado) {
            System.out.println("Cuenta eliminada exitosamente.");
        } else {
            System.out.println("Error: No se pudo eliminar la cuenta. Verifique el número de cuenta y el PIN.");
        }
    }

    // Métodos para las operaciones del menú de operaciones financieras
    private void realizarDepositoColones() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        int monto = leerEntero("Ingrese el monto a depositar en colones: ");

        boolean resultado = transaccionesControlador.realizarDeposito(numeroCuenta, monto);
        if (resultado) {
            System.out.println("Depósito realizado exitosamente.");
        } else {
            System.out.println("Error: No se encontró la cuenta o el monto ingresado no es válido.");
        }
    }

    private void realizarDepositoDolares() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        int montoDolares = leerEntero("Ingrese el monto a depositar en dólares: ");

        // Obtener el tipo de cambio de compra (esto podría venir de otro servicio o
        // clase)
        double tipoCambioCompra = 533.0; // controlador.obtenerTipoCambioCompra();

        // Convertir dólares a colones y realizar el depósito
        boolean resultado = transaccionesControlador.realizarDepositoDolares(numeroCuenta, montoDolares,
                tipoCambioCompra);
        if (resultado) {
            System.out.println("Depósito realizado exitosamente. (Monto en colones: "
                    + (int) (montoDolares * tipoCambioCompra) + ")");
        } else {
            System.out.println("Error: No se encontró la cuenta o el monto ingresado no es válido.");
        }
    }

    private void realizarRetiro() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        String pin = leerString("Ingrese el PIN de la cuenta: ");
        int montoRetiro = leerEntero("Ingrese el monto a retirar: ");

        boolean resultado = transaccionesControlador.realizarRetiro(numeroCuenta, pin, montoRetiro);
        if (resultado) {
            System.out.println("Retiro realizado exitosamente. Monto retirado: " + montoRetiro);
        } else {
            System.out.println(
                    "Error: No se pudo realizar el retiro. Verifique el PIN, el saldo disponible, o la cuenta ingresada.");
        }
    }
    // Métodos para las operaciones del menú de consultas y reportes

    private void consultarTransacciones() {
        String numeroCuenta = leerString("Ingrese el número de cuenta para consultar las transacciones: ");

        String transacciones = "No hay transacciones en este momento."; // controlador.consultarTransaccionesCuenta(numeroCuenta);
        if (transacciones != null && !transacciones.isEmpty()) {
            System.out.println("--- Transacciones de la Cuenta ---");
            System.out.println(transacciones);
        } else {
            System.out.println("Error: No se encontraron transacciones o la cuenta no existe.");
        }
    }

    private void consultarTipoCambioCompra() {
        double tipoCambioCompra = 533.0; // controlador.obtenerTipoCambioCompra();
        System.out.println("El tipo de cambio de compra actual es: " + tipoCambioCompra + " colones por dólar.");
    }

    private void consultarSaldoActual() {
        String numeroCuenta = leerString("Ingrese el número de cuenta para consultar el saldo: ");
        String pin = leerString("Ingrese el PIN de la cuenta: ");

        int saldo = 0; // controlador.consultarSaldo(numeroCuenta, pin);
        if (saldo >= 0) {
            System.out.println("El saldo actual de la cuenta es: " + saldo + " colones.");
        } else {
            System.out.println("Error: No se pudo acceder a la cuenta. Verifique el número de cuenta y el PIN.");
        }
    }

}