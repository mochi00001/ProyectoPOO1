package vistas;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import controladores.ClienteControlador;
import controladores.CuentaControlador;
import controladores.TransaccionesControlador;
import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.TipoDeCambioBCCR;
import validacion.Formato;

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
            System.out.println("4. Realizar Retiro en Dólares");
            System.out.println("5. Realizar Transferencia entre Cuentas Propias");
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
                case 4:
                    realizarRetiroDolares();
                    break;
                case 5:
                    realizarTransferencia();
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
            System.out.println("3. Consultar Tipo de Cambio de Venta");
            System.out.println("4. Consultar Saldo Actual");
            System.out.println("5. Consultar Saldo Actual (en dólares)");
            System.out.println("6. Consultar Estado de Cuenta");
            System.out.println("7. Consultar Estado de Cuenta (en dólares)");
            System.out.println("8. Consultar Estatus de una Cuenta");
            System.out.println("9. Consultar Números de Cuenta y Saldos de un Cliente");
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
                    consultarTipoCambioVenta();
                    break;
                case 4:
                    consultarSaldoActual();
                    break;
                case 5:
                    consultarSaldoDolares();
                    break;
                case 6:
                    consultarEstadoDeCuenta();
                    break;
                case 7:
                    consultarEstadoDeCuentaEnDolares();
                    break;
                case 8:
                    consultarEstatusDeCuenta();
                    break;
                case 9:
                    consultarCuentasDeCliente();
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

    private double leerDouble(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.print("Por favor, ingrese un número: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    private String leerString(String mensaje) {
        System.out.print(mensaje);
        return scanner.next();
    }

    // Métodos para las operaciones del menú de gestión de clientes (simplificados,
    // la lógica real estará en el
    // controlador)
    private void crearCliente() {
        // Solicitar nombre completo
        String nombre = leerString("Ingrese el nombre completo del cliente: ");

        // Solicitar identificación
        int identificacion = leerEntero("Ingrese la identificación del cliente: ");

        // Solicitar y validar número telefónico
        String numTelefono;
        while (true) {
            numTelefono = leerString("Ingrese el número telefónico del cliente (debe tener 8 dígitos): ");
            if (numTelefono.length() == 8 && numTelefono.matches("\\d+")) { // Validar longitud y que sean solo dígitos
                break;
            } else {
                System.out.println("Número telefónico inválido. Debe contener exactamente 8 dígitos.");
            }
        }

        // Solicitar y validar correo electrónico
        String correoElectronico;
        while (true) {
            correoElectronico = leerString("Ingrese el correo electrónico del cliente: ");
            if (Formato.validarFormatoCorreo(correoElectronico)) {
                break;
            } else {
                System.out.println("Formato de correo electrónico inválido. Ingrese un correo válido.");
            }
        }

        // Crear el cliente
        boolean resultado = clienteControlador.crearCliente(nombre, identificacion, numTelefono, correoElectronico);

        // Confirmar el resultado
        if (resultado) {
            System.out.println("Se ha creado un nuevo cliente en el sistema, los datos del cliente son:");
            System.out.println("Nombre completo: " + nombre);
            System.out.println("Identificación: " + identificacion);
            System.out.println("Número de teléfono: " + numTelefono);
            System.out.println("Dirección de correo electrónico: " + correoElectronico);
        } else {
            System.out.println("Error: Ya existe un cliente con esa identificación.");
        }
    }

    private void cambiarTelefonoCliente() {
        // Solicitar identificación del cliente
        int identificacion = leerEntero("Ingrese la identificación del cliente: ");

        // Buscar el cliente por identificación
        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);

        // Validar si el cliente existe
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            // Mostrar los datos actuales del cliente
            System.out.println("Datos del cliente:");
            System.out.println("Nombre completo: " + cliente.getNombre());
            System.out.println("Identificación: " + cliente.getIdentificacion());
            System.out.println("Número de teléfono actual: " + cliente.getNumTelefono());
            System.out.println("Correo electrónico: " + cliente.getCorreoElectronico());

            // Solicitar el nuevo número de teléfono
            String nuevoTelefono;
            while (true) {
                nuevoTelefono = leerString("Ingrese el nuevo número telefónico (debe tener 8 dígitos): ");
                if (nuevoTelefono.length() == 8 && nuevoTelefono.matches("\\d+")) { // Validar longitud y que sean solo
                                                                                    // dígitos
                    break;
                } else {
                    System.out.println("Número telefónico inválido. Debe contener exactamente 8 dígitos.");
                }
            }

            // Actualizar el número de teléfono del cliente
            boolean resultado = clienteControlador.actualizarTelefono(identificacion, nuevoTelefono);

            // Confirmar el resultado
            if (resultado) {
                System.out.println(
                        "Estimado usuario: " + cliente.getNombre() + ", usted ha cambiado el número de teléfono " +
                                cliente.getNumTelefono() + " por el nuevo número " + nuevoTelefono + ".");
            } else {
                System.out.println("Error: No se pudo actualizar el número de teléfono.");
            }

        } else {
            System.out.println("Error: No se encontró un cliente con esa identificación.");
        }
    }

    private void cambiarCorreoCliente() {
        // Solicitar identificación del cliente
        int identificacion = leerEntero("Ingrese la identificación del cliente: ");

        // Buscar el cliente por identificación
        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);

        // Validar si el cliente existe
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            // Mostrar los datos actuales del cliente
            System.out.println("Datos del cliente:");
            System.out.println("Nombre completo: " + cliente.getNombre());
            System.out.println("Identificación: " + cliente.getIdentificacion());
            System.out.println("Número de teléfono: " + cliente.getNumTelefono());
            System.out.println("Correo electrónico actual: " + cliente.getCorreoElectronico());

            // Solicitar el nuevo correo electrónico
            String nuevoCorreo;
            while (true) {
                nuevoCorreo = leerString("Ingrese el nuevo correo electrónico: ");
                if (Formato.validarCorreo(nuevoCorreo)) { // Validar formato de correo electrónico
                    break;
                } else {
                    System.out.println("Formato de correo electrónico inválido. Intente nuevamente.");
                }
            }

            // Actualizar el correo electrónico del cliente
            boolean resultado = clienteControlador.actualizarCorreo(identificacion, nuevoCorreo);

            // Confirmar el resultado
            if (resultado) {
                System.out.println(
                        "Estimado usuario: " + cliente.getNombre() + ", usted ha cambiado la dirección de correo " +
                                cliente.getCorreoElectronico() + " por " + nuevoCorreo + ".");
            } else {
                System.out.println("Error: No se pudo actualizar la dirección de correo.");
            }

        } else {
            System.out.println("Error: No se encontró un cliente con esa identificación.");
        }
    }

    // Métodos para las operaciones del menú de gestión de cuentas (simplificados,
    // la lógica real estará en el
    // controlador)
    private void crearCuenta() {
        // Solicitar la identificación del cliente
        int identificacion = leerEntero("Ingrese la identificación del cliente: ");

        // Buscar al cliente por identificación
        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);

        // Validar si el cliente existe
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            // Mostrar los datos actuales del cliente
            System.out.println("Datos del cliente:");
            System.out.println("Nombre completo: " + cliente.getNombre());
            System.out.println("Identificación: " + cliente.getIdentificacion());
            System.out.println("Número de teléfono: " + cliente.getNumTelefono());
            System.out.println("Correo electrónico: " + cliente.getCorreoElectronico());

            // Solicitar el PIN de la cuenta
            String pin;
            while (true) {
                pin = leerString("Ingrese el PIN (6 caracteres, al menos una letra mayúscula y un número): ");
                if (Formato.validarFormatoPin(pin)) { // Validar el formato del PIN
                    break;
                } else {
                    System.out.println("El formato del PIN no es válido. Intente nuevamente.");
                }
            }

            // Solicitar el monto del depósito inicial
            double saldoInicial;
            while (true) {
                saldoInicial = leerDouble("Ingrese el monto del depósito inicial: ");
                if (saldoInicial > 0 && saldoInicial == Math.floor(saldoInicial)) { // Validar que sea un número sin
                                                                                    // decimales
                    break;
                } else {
                    System.out.println("Monto inválido. El monto debe ser un número sin decimales.");
                }
            }

            // Crear la cuenta
            String numeroCuenta = cuentasControlador.crearCuenta(saldoInicial, pin, cliente);

            // Confirmar la creación de la cuenta
            if (numeroCuenta != null) {
                System.out.println("Se ha creado una nueva cuenta en el sistema.");
                System.out.println("Número de cuenta: " + numeroCuenta);
                System.out.println("Estatus de la cuenta: Activa");
                System.out.println("Saldo actual: " + String.format("%.2f", saldoInicial));
                System.out.println("Nombre del dueño de la cuenta: " + cliente.getNombre());
            } else {
                System.out.println("Error al crear la cuenta. Intente nuevamente.");
            }

        } else {
            System.out.println("Error: No se encontró un cliente con esa identificación.");
        }
    }

    private void cambiarPin() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        String pinActual = leerString("Ingrese el PIN actual: ");
        String nuevoPin = leerString(
                "Ingrese el nuevo PIN (debe tener 6 caracteres, al menos una letra mayúscula y un número): ");

        // Buscar la cuenta por número de cuenta
        Optional<Cuenta> cuentaOpt = cuentasControlador.obtenerCuentaPorNumero(numeroCuenta);

        // Verificar si la cuenta fue encontrada
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            // Verificar si el PIN actual es correcto
            if (cuentasControlador.autenticarCuenta(numeroCuenta, pinActual)) {

                // Intentar cambiar el PIN
                boolean resultado = cuentasControlador.cambiarPinCuenta(cuenta, nuevoPin);

                // Validar si el cambio fue exitoso
                if (resultado) {
                    System.out.println("Estimado usuario: " + cuenta.getMiCliente().getNombre() +
                            ", le informamos que se ha cambiado satisfactoriamente el PIN de su cuenta "
                            + cuenta.getCodigo() + ".");
                } else {
                    System.out.println("Error: No se pudo cambiar el PIN. Verifique el formato del nuevo PIN.");
                }
            } else {
                System.out.println("Error: El PIN actual es incorrecto.");
            }
        } else {
            System.out.println("Error: No se encontró la cuenta con el número proporcionado.");
        }
    }

    private void eliminarCuenta() {
        // Solicitar el número de cuenta
        String numeroCuenta = leerString("Ingrese el número de cuenta que desea eliminar: ");

        // Buscar la cuenta por número
        Optional<Cuenta> cuentaOpt = cuentasControlador.obtenerCuentaPorNumero(numeroCuenta);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            // Solicitar el PIN de la cuenta
            String pinIngresado = leerString("Ingrese el PIN de la cuenta: ");

            // Verificar si el PIN es correcto
            if (cuentasControlador.autenticarCuenta(numeroCuenta, pinIngresado)) {

                // Mostrar el saldo actual y confirmar la eliminación
                System.out.println("Estimado usuario: " + cuenta.getMiCliente().getNombre());
                System.out.println("Está a un paso de eliminar su cuenta " + cuenta.getCodigo()
                        + " cuyo saldo actual es de " + String.format("%.2f", (double) cuenta.getSaldo()));

                String confirmacion = leerString("¿Está seguro que desea eliminar esta cuenta? (sí/no): ");

                if (confirmacion.equalsIgnoreCase("sí")) {
                    // Si la cuenta tiene saldo, informar al usuario que debe tomar el dinero
                    if (cuenta.getSaldo() > 0) {
                        System.out.println("Por favor, retire el saldo restante de: "
                                + String.format("%.2f", (double) cuenta.getSaldo())
                                + " que ha sido dispuesto en el dispensador de dinero.");
                    }

                    // Eliminar la cuenta
                    boolean resultado = cuentasControlador.eliminarCuenta(numeroCuenta, pinIngresado);

                    if (resultado) {
                        System.out.println("La cuenta " + cuenta.getCodigo() + " ha sido eliminada exitosamente.");
                    } else {
                        System.out.println("Error: No se pudo eliminar la cuenta. Verifique los datos.");
                    }
                } else {
                    System.out.println("La cuenta no ha sido eliminada.");
                }
            } else {
                System.out.println("El PIN ingresado es incorrecto.");
            }

        } else {
            System.out.println("Error: No se encontró una cuenta con ese número.");
        }
    }

    // Métodos para las operaciones del menú de operaciones financieras
    private void realizarDepositoColones() {
        // Solicitar el número de cuenta
        String numeroCuenta = leerString("Ingrese el número de cuenta donde desea realizar el depósito: ");

        // Verificar si la cuenta existe
        Optional<Cuenta> cuentaOpt = cuentasControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            // Solicitar el monto del depósito
            double montoDeposito = leerDouble("Ingrese el monto del depósito (sin decimales): ");

            // Verificar que el monto sea un número entero y positivo
            if (montoDeposito <= 0 || montoDeposito % 1 != 0) {
                System.out.println("Error: El monto debe ser un número entero positivo.");
                return;
            }

            // Realizar el depósito en colones
            boolean resultado = transaccionesControlador.realizarDepositoColones(numeroCuenta, montoDeposito);

            // Confirmar la operación
            if (resultado) {
                System.out.println("Estimado usuario: " + cuenta.getMiCliente().getNombre()
                        + ", se han depositado correctamente " + String.format("%.2f", montoDeposito)
                        + " colones en su cuenta " + cuenta.getCodigo() + ".");
            } else {
                System.out.println("Error: No se pudo realizar el depósito. Verifique los datos.");
            }

        } else {
            System.out.println("Error: La cuenta con el número ingresado no está registrada en el sistema.");
        }
    }

    private void realizarDepositoDolares() {
        // Solicitar el número de cuenta
        String numeroCuenta = leerString("Ingrese el número de cuenta donde desea realizar el depósito: ");

        // Verificar si la cuenta existe
        Optional<Cuenta> cuentaOpt = cuentasControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            // Solicitar el monto del depósito en dólares
            double montoDolares = leerDouble("Ingrese el monto del depósito en dólares (sin decimales): ");

            // Verificar que el monto sea un número entero y positivo
            if (montoDolares <= 0 || montoDolares % 1 != 0) {
                System.out.println("Error: El monto debe ser un número entero positivo.");
                return;
            }

            // Obtener el tipo de cambio de compra actual
            // TipoDeCambioBCCR.obtenerTipoCambioHoy();
            double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra();

            // Validar que el tipo de cambio sea mayor a 0
            if (tipoCambioCompra <= 0) {
                System.out.println("Error al obtener el tipo de cambio de compra.");
                return;
            }

            // Realizar el depósito en dólares
            boolean resultado = transaccionesControlador.realizarDepositoDolares(numeroCuenta, montoDolares,
                    tipoCambioCompra);

            // Confirmar la operación
            if (resultado) {
                System.out.println("Estimado usuario: " + cuenta.getMiCliente().getNombre() +
                        ", se han recibido correctamente " + String.format("%.2f", montoDolares) + " dólares.");
                System.out.println("[Según el BCCR, el tipo de cambio de compra del dólar de hoy es: " +
                        String.format("%.2f", tipoCambioCompra) + "]");
                double montoColones = montoDolares * tipoCambioCompra;
                System.out.println("[El monto equivalente en colones es " + String.format("%.2f", montoColones) + "]");
            } else {
                System.out.println("Error: No se pudo realizar el depósito. Verifique los datos.");
            }

        } else {
            System.out.println("Error: La cuenta con el número ingresado no está registrada en el sistema.");
        }
    }

    private void realizarRetiro() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        if (!cuentasControlador.verificarCuenta(numeroCuenta)) {
            System.out.println("Cuenta no registrada.");
            return;
        }

        String pin = leerString("Ingrese el PIN actual: ");
        if (!cuentasControlador.autenticarCuenta(numeroCuenta, pin)) {
            System.out.println("PIN incorrecto.");
            return;
        }

        // Enviar mensaje de verificación
        String codigoVerificacion = cuentasControlador.enviarCodigoVerificacion(numeroCuenta);
        if (codigoVerificacion == null) {
            System.out.println("Error al enviar el código de verificación.");
            return;
        }

        String codigoIngresado = leerString(
                "Estimado usuario, se ha enviado una palabra por mensaje de texto. Ingrese la palabra enviada: ");
        if (!cuentasControlador.validarCodigoVerificacion(numeroCuenta, codigoIngresado)) {
            System.out.println("Código de verificación incorrecto.");
            return;
        }

        // Solicitar el monto del retiro
        double monto = leerDouble("Ingrese el monto del retiro (XXXXX.00): ");
        if (monto <= 0) {
            System.out.println("El monto ingresado no es válido.");
            return;
        }

        // Validar si existen fondos suficientes
        if (!cuentasControlador.verificarFondos(numeroCuenta, monto)) {
            System.out.println("Fondos insuficientes.");
            return;
        }

        // Realizar el retiro
        boolean exito = cuentasControlador.realizarRetiro(numeroCuenta, monto, false);
        if (exito) {
            String nombreCliente = cuentasControlador.obtenerNombreCliente(numeroCuenta);
            System.out.println("Estimado usuario: " + nombreCliente + ", el monto de este retiro de su cuenta "
                    + numeroCuenta + " es " + String.format("%.2f", monto)
                    + " colones. Por favor tome el dinero dispensado.");
        } else {
            System.out.println("Error al realizar el retiro.");
        }
    }

    private void realizarRetiroDolares() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");

        // Verificar que la cuenta exista
        if (!cuentasControlador.verificarCuenta(numeroCuenta)) {
            System.out.println("La cuenta no está registrada.");
            return;
        }

        String pin = leerString("Ingrese el PIN actual: ");

        // Autenticar PIN
        if (!cuentasControlador.autenticarCuenta(numeroCuenta, pin)) {
            System.out.println("El PIN es incorrecto.");
            return;
        }

        // Enviar código de verificación
        String codigoVerificacion = cuentasControlador.enviarCodigoVerificacion(numeroCuenta);
        if (codigoVerificacion == null) {
            System.out.println("No se pudo enviar el código de verificación.");
            return;
        }

        System.out.println("Estimado usuario: " + cuentasControlador.obtenerNombreCliente(numeroCuenta)
                + ", se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada.");

        // Ingresar la palabra enviada por SMS
        String palabraIngresada = leerString("Ingrese la palabra enviada por mensaje de texto: ");
        if (!cuentasControlador.validarCodigoVerificacion(numeroCuenta, palabraIngresada)) {
            System.out.println("La palabra ingresada es incorrecta.");
            return;
        }

        // Solicitar el monto en dólares
        double montoDolares = leerDouble("Ingrese el monto que desea retirar en dólares: ");

        // Verificar que el monto sea válido (sin decimales)
        if (montoDolares <= 0 || montoDolares != Math.floor(montoDolares)) {
            System.out.println("El monto ingresado debe ser un número positivo sin decimales.");
            return;
        }

        // Validar si la cuenta tiene fondos suficientes
        if (!cuentasControlador.verificarFondos(numeroCuenta, montoDolares)) {
            System.out.println("Fondos insuficientes para realizar el retiro.");
            return;
        }

        // Realizar el retiro en colones, tomando en cuenta la conversión
        if (cuentasControlador.realizarRetiro(numeroCuenta, montoDolares, true)) {
            double tipoCambioVenta = TipoDeCambioBCCR.obtenerTipoCambioVenta();
            double montoColones = montoDolares * tipoCambioVenta;

            System.out.println("Estimado usuario: " + cuentasControlador.obtenerNombreCliente(numeroCuenta)
                    + ", el monto de este retiro de su cuenta " + numeroCuenta + " es de " + montoDolares
                    + " dólares. Por favor tome el dinero dispensado.");
            System.out.println(
                    "[Según el BCCR, el tipo de cambio de venta del dólar de hoy es: " + tipoCambioVenta + "]");
            System.out.println(
                    "[El monto equivalente de su retiro es " + String.format("%.2f", montoColones) + " colones]");
        } else {
            System.out.println("No se pudo realizar el retiro.");
        }
    }

    private void realizarTransferencia() {
        // Solicitar número de cuenta origen
        String numeroCuentaOrigen = leerString("Ingrese el número de cuenta origen: ");

        // Verificar si la cuenta origen existe
        if (!cuentasControlador.verificarCuenta(numeroCuentaOrigen)) {
            System.out.println("Error: La cuenta origen no está registrada.");
            return;
        }

        // Solicitar PIN
        String pin = leerString("Ingrese el PIN de la cuenta origen: ");

        // Verificar si el PIN es correcto
        if (!cuentasControlador.autenticarCuenta(numeroCuentaOrigen, pin)) {
            System.out.println("Error: El PIN es incorrecto.");
            return;
        }

        // Enviar código de verificación al número de teléfono registrado
        String codigoVerificacion = cuentasControlador.enviarCodigoVerificacion(numeroCuentaOrigen);
        if (codigoVerificacion == null) {
            System.out.println("Error: No se pudo enviar el código de verificación.");
            return;
        }

        // Informar al cliente sobre el envío del código de verificación
        System.out.println("Estimado usuario: " + cuentasControlador.obtenerNombreCliente(numeroCuentaOrigen)
                + ", se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada.");

        // Solicitar palabra de verificación
        String palabraIngresada = leerString("Ingrese la palabra enviada por mensaje de texto: ");
        if (!cuentasControlador.validarCodigoVerificacion(numeroCuentaOrigen, palabraIngresada)) {
            System.out.println("Error: La palabra de verificación es incorrecta.");
            return;
        }

        // Solicitar el monto de la transferencia
        double montoTransferencia = leerDouble("Ingrese el monto a transferir (sin decimales): ");
        if (montoTransferencia <= 0 || montoTransferencia != Math.floor(montoTransferencia)) {
            System.out.println("Error: El monto ingresado debe ser positivo y sin decimales.");
            return;
        }

        // Solicitar número de cuenta destino
        String numeroCuentaDestino = leerString("Ingrese el número de cuenta destino: ");

        // Verificar si la cuenta destino existe y pertenece al mismo dueño
        if (!cuentasControlador.verificarCuentaMismoDueno(numeroCuentaOrigen, numeroCuentaDestino)) {
            System.out.println("Error: La cuenta destino no está registrada o no es del mismo dueño.");
            return;
        }

        // Realizar la transferencia
        if (cuentasControlador.realizarTransferencia(numeroCuentaOrigen, pin, palabraIngresada, numeroCuentaDestino,
                montoTransferencia)) {
            System.out.println("Estimado usuario: " + cuentasControlador.obtenerNombreCliente(numeroCuentaOrigen)
                    + ", la transferencia de fondos se ejecutó satisfactoriamente.");
            System.out.println("El monto retirado de la cuenta origen " + numeroCuentaOrigen
                    + " y depositado en la cuenta destino " + numeroCuentaDestino + " es de "
                    + montoTransferencia + " colones.");
        } else {
            System.out.println("Error: No se pudo realizar la transferencia.");
        }
    }

    // Métodos para las operaciones del menú de consultas y reportes

    public void consultarTransacciones() {
        Scanner scanner = new Scanner(System.in);

        // Solicitar número de cuenta
        System.out.print("Ingrese el número de cuenta: ");
        String numeroCuenta = scanner.nextLine();

        // Verificar si la cuenta existe
        if (!cuentasControlador.verificarCuenta(numeroCuenta)) {
            System.out.println("Error: La cuenta no existe.");
            return;
        }

        // Solicitar el PIN
        System.out.print("Ingrese su PIN: ");
        String pin = scanner.nextLine();

        // Enviar la palabra de verificación por SMS
        String codigoVerificacion = cuentasControlador.enviarCodigoVerificacion(numeroCuenta);
        if (codigoVerificacion == null) {
            System.out.println("Error: No se pudo enviar la palabra de verificación.");
            return;
        }

        // Solicitar la palabra de verificación
        System.out.print("Ingrese la palabra de verificación que recibió por mensaje: ");
        String palabraIngresada = scanner.nextLine();

        // Consultar transacciones
        List<Transaccion> transacciones = transaccionesControlador.consultarTransacciones(numeroCuenta, pin,
                palabraIngresada);
        if (transacciones == null) {
            System.out.println("Error: No se pudieron consultar las transacciones.");
            return;
        }

        // Mostrar las transacciones
        System.out.println("Estimado usuario: " + cuentasControlador.obtenerNombreCliente(numeroCuenta));
        System.out.println("El detalle de todas las transacciones realizadas en su cuenta es:");
        for (Transaccion transaccion : transacciones) {
            System.out.println("Tipo de transacción: " + transaccion.getTipo());
            System.out.println("Monto: " + transaccion.getMonto());
            System.out.println("Fecha: " + transaccion.getFecha());
            System.out.println("Comisión aplicada: " + (transaccion.isComision() ? "Sí" : "No"));
            System.out.println("-----------------------------------------");
        }
    }

    private void consultarTipoCambioCompra() {
        // Llamar al método para obtener el tipo de cambio
        TipoDeCambioBCCR.obtenerTipoCambioHoy();

        double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra();

        if (tipoCambioCompra > 0) {
            System.out.println("El tipo de cambio de compra del dólar es: " + String.format("%.2f", tipoCambioCompra)
                    + " colones.");
        } else {
            System.out.println("No se pudo obtener el tipo de cambio de compra en este momento.");
        }
    }

    private void consultarTipoCambioVenta() {
        // Llamar al método para obtener el tipo de cambio
        TipoDeCambioBCCR.obtenerTipoCambioHoy();

        double tipoCambioVenta = TipoDeCambioBCCR.obtenerTipoCambioVenta();

        if (tipoCambioVenta > 0) {
            System.out.println(
                    "El tipo de cambio de venta del dólar es: " + String.format("%.2f", tipoCambioVenta) + " colones.");
        } else {
            System.out.println("No se pudo obtener el tipo de cambio de venta en este momento.");
        }
    }

    private void consultarSaldoActual() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        String pin = leerString("Ingrese el PIN actual: ");

        double saldo = cuentasControlador.consultarSaldo(numeroCuenta, pin);
        if (saldo >= 0) {
            System.out.println("Estimado usuario: " + cuentasControlador.obtenerNombreCliente(numeroCuenta) +
                    ", el saldo actual de su cuenta " + numeroCuenta + " es de " +
                    String.format("%.2f", saldo) + " colones.");
        } else {
            System.out.println("No se pudo consultar el saldo.");
        }
    }

    private void consultarSaldoDolares() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        String pin = leerString("Ingrese el PIN actual: ");

        double saldo = cuentasControlador.consultarSaldo(numeroCuenta, pin);
        if (saldo >= 0) {
            double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra(); // Obtener el tipo de cambio
            double saldoDolares = saldo / tipoCambioCompra; // Convertir a dólares

            System.out.println("Estimado usuario: " + cuentasControlador.obtenerNombreCliente(numeroCuenta) +
                    ", el saldo actual de su cuenta " + numeroCuenta + " es de " +
                    String.format("%.2f", saldoDolares) + " dólares.");
            System.out.println("Para esta conversión se utilizó el tipo de cambio de compra del dólar del día: " +
                    String.format("%.2f", tipoCambioCompra));
        } else {
            System.out.println("No se pudo consultar el saldo.");
        }
    }

    private void consultarEstadoDeCuenta() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        String pin = leerString("Ingrese el PIN actual: ");

        if (cuentasControlador.autenticarCuenta(numeroCuenta, pin)) {
            System.out.println("Estado de cuenta de la cuenta: " + numeroCuenta);
            // Agrega aquí la lógica que muestre detalles relevantes como saldo, etc.
        } else {
            System.out.println("PIN incorrecto o cuenta no encontrada.");
        }
    }

    private void consultarEstadoDeCuentaEnDolares() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");
        String pin = leerString("Ingrese el PIN actual: ");

        if (cuentasControlador.autenticarCuenta(numeroCuenta, pin)) {
            System.out.println("Estado de cuenta en dólares de la cuenta: " + numeroCuenta);
            // Similar al estado de cuenta en colones, pero convertido a dólares.
        } else {
            System.out.println("PIN incorrecto o cuenta no encontrada.");
        }
    }

    private void consultarEstatusDeCuenta() {
        String numeroCuenta = leerString("Ingrese el número de cuenta: ");

        Optional<Cuenta> cuentaOpt = cuentasControlador.obtenerCuentaPorNumero(numeroCuenta);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            System.out.println("La cuenta número " + cuenta.getCodigo() + " a nombre de "
                    + cuenta.getMiCliente().getNombre() + " tiene estatus de " + cuenta.getEstatus() + ".");
        } else {
            System.out.println("La cuenta no fue encontrada.");
        }
    }

    private void consultarCuentasDeCliente() {
        int identificacionCliente = leerEntero("Ingrese la identificación del cliente: ");

        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacionCliente);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            System.out.println("Cuentas pertenecientes al cliente " + cliente.getNombre() + ":");
            for (Cuenta cuenta : cliente.getMisCuentas()) {
                System.out.println("Número de cuenta: " + cuenta.getCodigo() + ", Saldo: "
                        + String.format("%.2f", cuenta.getSaldo()));
            }
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }

}