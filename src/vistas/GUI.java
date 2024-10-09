package vistas;

import controladores.CuentaControlador;
import controladores.ClienteControlador;
import controladores.TransaccionesControlador;
import modelos.Cliente;
import modelos.Cuenta;
import modelos.Transaccion;
import servicios.MensajeSMS; 
import servicios.TipoDeCambioBCCR;
import validacion.Formato;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private CuentaControlador cuentaControlador;
    private ClienteControlador clienteControlador;
    private TransaccionesControlador transaccionesControlador;
    private MensajeSMS mensajeSMS;

    private JFrame frame;

    public GUI(CuentaControlador cuentaControlador, ClienteControlador clienteControlador, TransaccionesControlador transaccionesControlador) {
        this.cuentaControlador = cuentaControlador;
        this.clienteControlador = clienteControlador;
        this.transaccionesControlador = transaccionesControlador;
        this.mensajeSMS = new MensajeSMS(); 
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Gestión de Cuentas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(0, 1));
    
        // Botón para Información Cliente
        JButton btnInformacionCliente = new JButton("Información Cliente");
        btnInformacionCliente.addActionListener(e -> mostrarSubmenuInformacionCliente());
        frame.add(btnInformacionCliente);

        // Botón para gestionar cuentas
        JButton btnGestionarCuentas = new JButton("Gestionar Cuentas");
        btnGestionarCuentas.addActionListener(e -> gestionarCuentas());
        frame.add(btnGestionarCuentas);
    
        // Botón para realizar transacciones
        JButton btnRealizarTransaccion = new JButton("Realizar Transacción");
        btnRealizarTransaccion.addActionListener(e -> gestionarTransacciones());
        frame.add(btnRealizarTransaccion);
    
        // Botón para consultar tipo de cambio
        JButton btnConsultarTipoCambio = new JButton("Consultar Tipo de Cambio");
        btnConsultarTipoCambio.addActionListener(e -> consultarTipoCambio());
        frame.add(btnConsultarTipoCambio);

        // Botón para consultar saldo
        JButton btnConsultarSaldo = new JButton("Consultar Saldo");
        btnConsultarSaldo.addActionListener(e -> consultarSaldo());
        frame.add(btnConsultarSaldo);
    
        frame.setVisible(true);
    }

    private void mostrarSubmenuInformacionCliente() {
        JFrame submenuFrame = new JFrame("Información Cliente");
        submenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        submenuFrame.setSize(300, 200);
        submenuFrame.setLayout(new GridLayout(0, 1));
    
        // Opción para crear cliente
        JButton btnCrearCliente = new JButton("Crear Cliente");
        btnCrearCliente.addActionListener(e -> crearCliente());
        submenuFrame.add(btnCrearCliente);
    
        // Opción para cambiar número de teléfono
        JButton btnCambiarTelefono = new JButton("Cambiar Número de Teléfono");
        btnCambiarTelefono.addActionListener(e -> cambiarNumeroTelefono());
        submenuFrame.add(btnCambiarTelefono);
    
        // Opción para cambiar correo electrónico
        JButton btnCambiarCorreo = new JButton("Cambiar Correo Electrónico");
        btnCambiarCorreo.addActionListener(e -> cambiarCorreoElectronico()); // Asegúrate de tener este método implementado
        submenuFrame.add(btnCambiarCorreo);
    
        submenuFrame.setVisible(true);
    }    
    
    private void crearCliente() {
        String nombre = JOptionPane.showInputDialog("Ingrese nombre del cliente:");
        int identificacion;
        try {
            identificacion = Integer.parseInt(JOptionPane.showInputDialog("Ingrese identificación:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error: Identificación debe ser un número.");
            return;
        }

        String telefono = JOptionPane.showInputDialog("Ingrese teléfono:");
        if (!Formato.validarTelefono(telefono)) { // Validación del número telefónico
            JOptionPane.showMessageDialog(frame, "Error: El número de teléfono debe tener 11 dígitos y ser numérico.");
            return;
        }

        String correo = JOptionPane.showInputDialog("Ingrese correo:");
        if (!Formato.validarCorreo(correo)) { // Validación del formato del correo
            JOptionPane.showMessageDialog(frame, "Error: Dirección de correo electrónico no válida.");
            return;
        }

        boolean resultado = clienteControlador.crearCliente(nombre, identificacion, telefono, correo);
        if (resultado) {
            JOptionPane.showMessageDialog(frame, "Cliente creado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(frame, "Error: Ya existe un cliente con la identificación proporcionada.");
        }
    }

    private void cambiarNumeroTelefono() {
        String identificacionStr = JOptionPane.showInputDialog("Ingrese la identificación del cliente:");
        if (identificacionStr == null || identificacionStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Error: Identificación no puede estar vacía.");
            return;
        }
        int identificacion;
    
        try {
            identificacion = Integer.parseInt(identificacionStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Identificación inválida. Debe ser un número entero.");
            return;
        }
    
        // Solicitar nuevo número de teléfono
        String nuevoTelefono = JOptionPane.showInputDialog("Ingrese el nuevo número de teléfono:");

        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);
        if (clienteOpt.isPresent()) {
            // Obtener el cliente del Optional
            Cliente cliente = clienteOpt.get();
    
        // Cambiar el número de teléfono
            boolean exito = clienteControlador.actualizarTelefono(identificacion, nuevoTelefono);
        
            if (exito) {
                JOptionPane.showMessageDialog(frame, "Estimado usuario: " + cliente.getNombre() + 
                ", usted ha cambiado el número de teléfono por el nuevo número " + nuevoTelefono + ".");
            } else {
                JOptionPane.showMessageDialog(frame, "Error al cambiar el número de teléfono. Cliente no encontrado o número inválido.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Cliente no encontrado.");
        }
    }
    
    public void cambiarCorreoElectronico() {
        String identificacionStr = JOptionPane.showInputDialog("Ingrese la identificación del cliente:");
        
        // Verificar que la entrada no sea nula
        if (identificacionStr == null || identificacionStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Error: Identificación no puede estar vacía.");
            return;
        }
    
        int identificacion;
        try {
            identificacion = Integer.parseInt(identificacionStr); // Convertir a entero
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error: La identificación debe ser un número entero.");
            return;
        }
    
        String nuevoCorreo = JOptionPane.showInputDialog("Ingrese correo:");
        if (!Formato.validarCorreo(nuevoCorreo)) { // Validación del formato del correo
            JOptionPane.showMessageDialog(frame, "Error: Dirección de correo electrónico no válida.");
            return;
        }
    
        // Llamar al controlador para obtener el cliente
        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);

    
        if (clienteOpt.isPresent()) {
            // Obtener el cliente del Optional
            Cliente cliente = clienteOpt.get();
    
            // Cambiar el correo
            boolean resultado = clienteControlador.actualizarCorreo(identificacion, nuevoCorreo);
            
            if (resultado) {
                JOptionPane.showMessageDialog(frame, "Estimado usuario: " + cliente.getNombre() +
                    ", usted ha cambiado la dirección de correo por: " + nuevoCorreo);
            } else {
                JOptionPane.showMessageDialog(frame, "Error al cambiar el correo.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Cliente no encontrado.");
        }
    }
    
    private void gestionarCuentas() {
        String[] opciones = {"Crear Cuenta", "Listar Cuentas Cliente", "Eliminar Cuenta", "Cambiar PIN", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(frame, "Seleccione una opción:", "Gestión de Cuentas",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
    
        switch (seleccion) {
            case 0: // Crear Cuenta
                crearCuenta();
                break;
            case 1: // Listar Cuentas Cliente
                listarCuentasCliente();
                break;
            case 2: // Eliminar Cuenta
                eliminarCuenta();
                break;
            case 3: // Cambiar PIN
                cambiarPIN();
                break;
            case 4: // Cancelar
                break;
            default:
                break;
        }
    }

    private void cambiarPIN() {
        // Solicitar número de cuenta
        String numeroCuenta = JOptionPane.showInputDialog("Ingrese el número de cuenta:");
        
        // Verificar que la cuenta existe
        Optional<Cuenta> cuentaOpt = cuentaControlador.obtenerCuentaPorNumero(numeroCuenta); // Método a implementar en CuentaControlador
        if (!cuentaOpt.isPresent()) {
            JOptionPane.showMessageDialog(frame, "Error: No se encontró una cuenta con el número proporcionado.");
            return;
        }
    
        // Solicitar PIN actual
        String pinActual = JOptionPane.showInputDialog("Ingrese su PIN actual:");
        Cuenta cuenta = cuentaOpt.get();
    
        // Verificar si el PIN actual es correcto
        if (!cuenta.getPin().equals(pinActual)) { // Asumiendo que tienes un método para obtener el PIN
            JOptionPane.showMessageDialog(frame, "Error: El PIN actual es incorrecto.");
            return;
        }
    
        // Solicitar nuevo PIN
        String nuevoPin = JOptionPane.showInputDialog("Ingrese su nuevo PIN:");
        
        // Validar el formato del nuevo PIN
        if (!Formato.validarFormatoPin(nuevoPin)) {
            JOptionPane.showMessageDialog(frame, "Error: El nuevo PIN no es válido. Debe tener 4 dígitos.");
            return;
        }
    
        // Cambiar el PIN
        boolean resultado = cuentaControlador.cambiarPIN(cuenta, nuevoPin);
        if (resultado) {
            JOptionPane.showMessageDialog(frame, "Estimado usuario le informamos que se ha cambiado satisfactoriamente el PIN de su cuenta " + numeroCuenta + ".");
        } else {
            JOptionPane.showMessageDialog(frame, "Error: No se pudo cambiar el PIN. Por favor, inténtelo más tarde.");
        }
    }
    
    private void crearCuenta() {
        // Solicitar identificación del cliente
        int idCliente;
        try {
            idCliente = Integer.parseInt(JOptionPane.showInputDialog("Ingrese identificación del cliente:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error: La identificación debe ser un número.");
            return;
        }
    
        // Validar que el cliente existe
        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(idCliente);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            
            // Mostrar datos del cliente
            JOptionPane.showMessageDialog(frame, "Datos del cliente:\n" +
                "Nombre: " + cliente.getNombre() + "\n" +
                "Identificación: " + cliente.getIdentificacion() + "\n" +
                "Teléfono: " + cliente.getNumTelefono() + "\n" +
                "Correo: " + cliente.getCorreoElectronico());
    
            // Solicitar el PIN
            String pinCliente = JOptionPane.showInputDialog("Ingrese el PIN (con al menos una mayúscula y un número) de la cuenta:");
            
            // Validar el formato del PIN
            if (!Formato.validarFormatoPin(pinCliente)) { // Asumiendo que tienes un método de validación de PIN
                JOptionPane.showMessageDialog(frame, "Error: El PIN no es válido. Debe tener 6 dígitos alfanuméricos.");
                return;
            }
    
            // Solicitar el monto del depósito inicial
            double saldo;
            try {
                saldo = Double.parseDouble(JOptionPane.showInputDialog("Ingrese monto del depósito inicial:"));
                if (saldo < 0) {
                    JOptionPane.showMessageDialog(frame, "Error: El monto del depósito debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Error: El monto debe ser un número.");
                return;
            }
    
            // Crear la cuenta
            String codigoCuenta = cuentaControlador.crearCuenta(saldo, pinCliente, cliente);
            if (codigoCuenta != null) { // Verifica que no sea null
                JOptionPane.showMessageDialog(frame, "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son:\n" +
                    "Número de cuenta: " + codigoCuenta + "\n" +  // Mostrar el código de la cuenta
                    "Estatus de la cuenta: Activa\n" +
                    "Saldo actual: " + String.format("%.2f", (double) saldo) + "\n" +
                    "Nombre del dueño de la cuenta: " + cliente.getNombre());
            } else {
                JOptionPane.showMessageDialog(frame, "Error: Ya existe una cuenta con el número proporcionado.");
            }

        } else {
            JOptionPane.showMessageDialog(frame, "Error: No se encontró un cliente con la identificación proporcionada.");
        }
    }
    
    // Método de prueba para consultar cuentas por cliente
    private void listarCuentasCliente() {
        // Solicitar la identificación del cliente
        String idClienteStr = JOptionPane.showInputDialog("Ingrese la identificación del cliente:");
    
        // Validar que la identificación sea un número válido
        int idCliente;
        try {
            idCliente = Integer.parseInt(idClienteStr); // Convertir el valor ingresado a un número
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error: La identificación debe ser un número.");
            return; // Salir si la identificación no es válida
        }
    
        // Llamar al método que consulta las cuentas del cliente
        consultarCuentasCliente(idCliente);
    }
    
    public void consultarCuentasCliente(int identificacion) {
        // Buscar el cliente por identificación usando ClienteControlador
        Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorIdentificacion(identificacion);
    
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get(); // Obtener el cliente
    
            // Crear mensaje con los datos del cliente
            StringBuilder mensaje = new StringBuilder("Cliente encontrado:\n" +
                    "Nombre: " + cliente.getNombre() + "\n" +
                    "Identificación: " + cliente.getIdentificacion() + "\n" +
                    "Teléfono: " + cliente.getNumTelefono() + "\n" +
                    "Correo: " + cliente.getCorreoElectronico() + "\nCuentas:\n");
    
            // Obtener las cuentas del cliente
            List<Cuenta> cuentasDelCliente = cliente.getMisCuentas();
    
            if (cuentasDelCliente.isEmpty()) {
                mensaje.append("No hay cuentas asociadas a este cliente.");
            } else {
                for (Cuenta cuenta : cuentasDelCliente) {
                    mensaje.append("Número de cuenta: ").append(cuenta.getCodigo())
                           .append(", Saldo actual: ").append(String.format("%.2f", cuenta.getSaldo())).append("\n");
                }
            }
    
            // Mostrar el mensaje
            JOptionPane.showMessageDialog(frame, mensaje.toString());
    
        } else {
            // Mostrar error si no se encuentra el cliente
            JOptionPane.showMessageDialog(frame, "Error: No se encontró un cliente con la identificación: " + identificacion);
        }
    }

    

    private void eliminarCuenta() {
        String numeroCuenta = JOptionPane.showInputDialog("Ingrese número de cuenta a eliminar:");
        String pinCliente = JOptionPane.showInputDialog("Ingrese PIN del cliente:");
        boolean resultado = cuentaControlador.eliminarCuenta(numeroCuenta, pinCliente);
        if (resultado) {
            JOptionPane.showMessageDialog(frame, "Cuenta eliminada exitosamente.");
        } else {
            JOptionPane.showMessageDialog(frame, "Error: No se encontró una cuenta con el número proporcionado.");
        }
    }

    private void gestionarTransacciones() {
        String[] opciones = {"Depositar", "Retirar", "Transferir", "Consultar Transacciones", "Cancelar"}; // Agregada opción de Consultar Transacciones
        int seleccion = JOptionPane.showOptionDialog(frame, "Seleccione el tipo de transacción:", "Realizar Transacción",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
    
        switch (seleccion) {
            case 0: // Depositar
                realizarDeposito();
                break;
            case 1: // Retirar
                realizarRetiro();
                break;
            case 2: // Transferir
                realizarTransferencia();
                break;
            case 3: // Consultar Transacciones
                consultarTransacciones(); // Llamada al nuevo método
                break;
            case 4: // Cancelar
                break;
            default:
                break;
        }
    }

    private void realizarDeposito() {
        TipoDeCambioBCCR.obtenerTipoCambioHoy();

        String numeroCuenta = JOptionPane.showInputDialog("Ingrese número de cuenta:");
    
        System.out.println("Número de cuenta ingresado: " + numeroCuenta); // Mensaje de depuración
    
        // Mostrar todas las cuentas registradas
        System.out.println("Cuentas registradas: " + transaccionesControlador.obtenerListaCuentas());
    
        // Verificar que la cuenta existe
        if (!transaccionesControlador.verificarCuenta(numeroCuenta)) {
            JOptionPane.showMessageDialog(frame, "Error: La cuenta no está registrada en el sistema.");
            return;
        }
    
        // Opciones para el tipo de depósito
        String[] opciones = {"Colones", "Dólares"};
        int seleccion = JOptionPane.showOptionDialog(frame,
                "Seleccione el tipo de depósito:",
                "Tipo de Depósito",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]); // Preseleccionar "Colones"
    
        if (seleccion == -1) {
            return; // Si el usuario cierra el diálogo, salir
        }
    
        double monto;
    
        // Dependiendo de la selección, pide el monto
        if (seleccion == 0) { // Colones
            // Solicitar el monto
            String montoStr = JOptionPane.showInputDialog("Ingrese monto a depositar en colones:");
            if (montoStr == null || montoStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Error: El monto no puede estar vacío.");
                return;
            }
    
            try {
                monto = Double.parseDouble(montoStr);
                if (monto < 0) {
                    JOptionPane.showMessageDialog(frame, "Error: El monto debe ser positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Error: El monto debe ser un número.");
                return;
            }
    
            // Llamar al método del controlador
            boolean resultado = transaccionesControlador.realizarDepositoColones(numeroCuenta, monto);
            if (resultado) {
                JOptionPane.showMessageDialog(frame, "Depósito realizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(frame, "Error: No se encontró la cuenta o el monto ingresado no es válido.");
            }
        } else if (seleccion == 1) { // Dólares
            // Solicitar el monto
            String montoStr = JOptionPane.showInputDialog("Ingrese monto a depositar en dólares (sin decimales):");
            if (montoStr == null || montoStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Error: El monto no puede estar vacío.");
                return;
            }
    
            try {
                monto = Double.parseDouble(montoStr);
                if (monto <= 0 || monto % 1 != 0) { // Validar que sea un número entero positivo
                    JOptionPane.showMessageDialog(frame, "Error: El monto debe ser un número entero positivo (sin decimales).");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Error: El monto debe ser un número.");
                return;
            }
    
            // Obtener el tipo de cambio de compra
            double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra();
    
            // Buscar la cuenta correspondiente
            Cuenta cuenta = cuentaControlador.getCuentas().stream()
                                               .filter(c -> c.getCodigo().equals(numeroCuenta))
                                               .findFirst()
                                               .orElse(null);
    
            // Llamar al método del controlador con el tipo de cambio
            boolean resultado = transaccionesControlador.realizarDepositoDolares(numeroCuenta, monto, tipoCambioCompra);
    
            // Mostrar mensaje de confirmación o error
            if (resultado) {
                // Obtener el monto equivalente en colones y la comisión para mostrar en el mensaje
                double montoColones = monto * tipoCambioCompra;
    
                if (cuenta != null) {
                    double comision = transaccionesControlador.calcularComision(cuenta, montoColones);
                    double montoRealDepositado = montoColones - comision;
    
                    // Mensaje de confirmación al usuario
                    String mensaje = String.format("Estimado usuario: %s, se han recibido correctamente %.0f dólares.\n" +
                                    "[Según el BCCR, el tipo de cambio de compra del dólar de hoy %s es: %.2f]\n" +
                                    "[El monto equivalente en colones es %.2f]\n" +
                                    "[El monto real depositado a su cuenta %s es de %.2f colones]\n" +
                                    "[El monto cobrado por concepto de comisión fue de %.2f colones, que fueron rebajados automáticamente de su saldo actual].",
                            obtenerNombreCompleto(numeroCuenta), 
                            monto,
                            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            tipoCambioCompra,
                            montoColones,
                            numeroCuenta,
                            montoRealDepositado,
                            comision);
                    JOptionPane.showMessageDialog(frame, mensaje);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: No se pudo encontrar la cuenta para calcular la comisión.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Error: No se pudo realizar el depósito. Verifique la cuenta y el monto.");
            }
        }
    }
    
    // Método ficticio para obtener el nombre completo del dueño de la cuenta
    private String obtenerNombreCompleto(String numeroCuenta) {
        // Aquí deberías implementar la lógica para obtener el nombre del propietario de la cuenta
        return "Nombre del Dueño"; // Cambiar a la lógica real
    }

    private void realizarRetiro() {
        // Solicitar el número de cuenta al usuario
        String numeroCuenta = JOptionPane.showInputDialog("Ingrese número de cuenta:");
        
        double monto; // Variable para el monto a retirar
        boolean esDolar = false; // Por defecto, asumimos que es en colones.
    
        // Preguntar al usuario si el retiro es en dólares
        int respuesta = JOptionPane.showConfirmDialog(frame, "¿Desea realizar un retiro en dólares?", "Selección de moneda", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            esDolar = true; // Si el usuario selecciona "Sí", se establece que es en dólares.
        }
    
        // Solicitar el monto a retirar y manejar posibles errores
        try {
            monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese monto a retirar:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error: El monto debe ser un número.");
            return; // Salir del método si la entrada es inválida
        }
    
        // Solicitar el PIN del cliente
        String pinCliente = JOptionPane.showInputDialog("Ingrese PIN del cliente:");
    
        // Llamar al método realizarRetiro en el controlador de transacciones
        boolean resultado = transaccionesControlador.realizarRetiro(numeroCuenta, pinCliente, monto, esDolar);
    
        // Mostrar el resultado al usuario
        if (resultado) {
            JOptionPane.showMessageDialog(frame, "Retiro realizado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(frame, "Error: No se pudo realizar el retiro. Verifique el número de cuenta y saldo disponible.");
        }
    }
     
    private void realizarTransferencia() {
        String numeroCuentaOrigen = JOptionPane.showInputDialog("Ingrese número de cuenta origen:");
        String pinCliente = JOptionPane.showInputDialog("Ingrese PIN del cliente:");
    
        Cuenta cuentaOrigen = transaccionesControlador.buscarCuenta(numeroCuentaOrigen);
    
        if (cuentaOrigen != null && cuentaOrigen.validarPin(pinCliente)) {
            // Generar un código de verificación y enviarlo
            String numeroDestino = cuentaOrigen.getPropietario().getNumTelefono(); // Obtener el número de teléfono
            String palabraVerificacion = mensajeSMS.generarPalabraVerificacion();

            // Enviar el mensaje de verificación
            boolean mensajeEnviado = mensajeSMS.enviarMensajeVerificacion(numeroDestino, palabraVerificacion); // Usar la instancia de MensajeSMS
        
            if (mensajeEnviado) {
                String palabraIngresada = JOptionPane.showInputDialog("Ingrese la palabra de verificación enviada:");
                if (palabraIngresada.equals(mensajeSMS.getPalabraGenerada())) { // Verificar con el código enviado
                    // Aquí continúa el flujo de la transferencia
                } else {
                    JOptionPane.showMessageDialog(frame, "Palabra de verificación incorrecta.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Error al enviar el mensaje de verificación.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Error: Número de cuenta o PIN incorrecto.");
        }
        
    }
    
    private void consultarTransacciones() {
        String numeroCuenta = JOptionPane.showInputDialog("Ingrese el número de cuenta para consultar sus transacciones:");
    
        // Verificar que la cuenta existe
        if (!transaccionesControlador.verificarCuenta(numeroCuenta)) {
            JOptionPane.showMessageDialog(frame, "Error: La cuenta no está registrada en el sistema.");
            return;
        }
    
        // Obtener las transacciones asociadas a la cuenta
        List<Transaccion> historial = transaccionesControlador.obtenerTransaccionesPorCuenta(numeroCuenta);
    
        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay transacciones registradas para esta cuenta.");
        } else {
            StringBuilder detalles = new StringBuilder("Transacciones para la cuenta " + numeroCuenta + ":\n\n");
            for (Transaccion transaccion : historial) {
                detalles.append(transaccion.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, detalles.toString());
        }
    }

    private void consultarTipoCambio() {
        String[] opciones = {"Compra", "Venta"};
        int seleccion = JOptionPane.showOptionDialog(frame, "Seleccione el tipo de cambio que desea consultar:",
                "Consultar Tipo de Cambio", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
    
        // Obtener el tipo de cambio desde el BCCR
        TipoDeCambioBCCR.obtenerTipoCambioHoy();
    
        switch (seleccion) {
            case 0: // Compra
                double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra();
                if (tipoCambioCompra > 0) {
                    JOptionPane.showMessageDialog(frame, "Tipo de cambio de compra del dólar: " + tipoCambioCompra,
                            "Tipo de Cambio Compra", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo obtener el tipo de cambio de compra.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
    
            case 1: // Venta
                double tipoCambioVenta = TipoDeCambioBCCR.obtenerTipoCambioVenta();
                if (tipoCambioVenta > 0) {
                    JOptionPane.showMessageDialog(frame, "Tipo de cambio de venta del dólar: " + tipoCambioVenta,
                            "Tipo de Cambio Venta", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No se pudo obtener el tipo de cambio de venta.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
    
            default:
                // No se hizo ninguna selección
                break;
        }
    }

    private void consultarSaldo() {
        String[] opciones = {"Consultar Saldo Actual", "Consultar Saldo en Dólares", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(frame, "Seleccione una opción:", "Consultar Saldo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

        switch (seleccion) {
            case 0: // Consultar Saldo Actual
                consultarSaldoActual();
                break;
            case 1: // Consultar Saldo en Dólares
                consultarSaldoActualDivisa();
                break;
            case 2: // Cancelar
                break;
            default:
                break;
        }
    }

    private void consultarSaldoActual() {
        String numeroCuenta = solicitarNumeroCuenta();
        if (numeroCuenta == null) return;

        String pin = solicitarPIN();
        if (pin == null) return;

        double saldo = transaccionesControlador.consultarSaldo(numeroCuenta, pin);
        mostrarSaldo(numeroCuenta, saldo, "colones");
    }

    private void consultarSaldoActualDivisa() {
        String numeroCuenta = solicitarNumeroCuenta();
        if (numeroCuenta == null) return;

        String pin = solicitarPIN();
        if (pin == null) return;

        double saldo = transaccionesControlador.consultarSaldo(numeroCuenta, pin);
        if (saldo >= 0) {
            try {
                TipoDeCambioBCCR.obtenerTipoCambioHoy(); // Obtener tipo de cambio
                double tipoCambioCompra = TipoDeCambioBCCR.obtenerTipoCambioCompra();
                double saldoDolares = saldo / tipoCambioCompra;

                JOptionPane.showMessageDialog(frame,
                    "Estimado usuario: " + obtenerNombreCompleto(numeroCuenta) + 
                    ", el saldo actual de su cuenta " + numeroCuenta + 
                    " es de " + saldoDolares + " dólares.\n" +
                    "Para esta conversión se utilizó el tipo de cambio de compra del dólar de hoy: " + 
                    tipoCambioCompra + " colones.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "Error al obtener el tipo de cambio: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            mostrarError();
        }
    }

    private String solicitarNumeroCuenta() {
        String numeroCuenta = JOptionPane.showInputDialog(frame, "Ingrese el número de cuenta:");
        return (numeroCuenta == null || numeroCuenta.isEmpty()) ? null : numeroCuenta;
    }

    private String solicitarPIN() {
        String pin = JOptionPane.showInputDialog(frame, "Ingrese el PIN de la cuenta:");
        return (pin == null || pin.isEmpty()) ? null : pin;
    }

    private void mostrarSaldo(String numeroCuenta, double saldo, String divisa) {
        if (saldo >= 0) {
            JOptionPane.showMessageDialog(frame,
                "Estimado usuario: " + obtenerNombreCompleto(numeroCuenta) + 
                ", el saldo actual de su cuenta " + numeroCuenta + 
                " es de " + saldo + " " + divisa + ".");
        } else {
            mostrarError();
        }
    }

    private void mostrarError() {
        JOptionPane.showMessageDialog(frame, 
            "Número de cuenta o PIN incorrecto.", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }


    public void iniciar() {
        frame.setVisible(true);
    }
    
}

