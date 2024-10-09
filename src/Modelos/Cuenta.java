package modelos;

import servicios.MensajeSMS;
import servicios.CorreoElectronico;
import validacion.Formato;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Base64;
import java.util.Random;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Cuenta {
    private String codigo;
    private static int cantidadCuentas;
    private LocalDate fechaCreacion;
    private String estatus;
    private double saldo;
    private String pin;
    private Cliente miCliente;
    private List<Transaccion> transacciones; 

    private int intentosValidacion = 0;
    private int usosPin = 0;
    private String pinEncriptado;
    private double sumaRetiros = 0; //Verificar uso
    public static int cantidadTransacciones = 0;


    public Cuenta(double saldo, String codigo, String pin, Cliente cliente) {
        this.codigo = "cta-" + cantidadCuentas++;
        this.saldo = saldo;
        this.pin = pin;
        this.miCliente = cliente;
        this.transacciones = new ArrayList<>();
        this.estatus = "Activa";
        this.fechaCreacion = LocalDate.now();
    }

    // Funcionalidades
    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public ArrayList<Transaccion> obtenerTransaccionesPorCodigo(String codigoCuenta) {
        ArrayList<Transaccion> transaccionesPorCodigo = new ArrayList<>();
        for (Transaccion transaccion : transacciones) {
            if (transaccion.getCodigoCuenta().equals(codigoCuenta)) {
                transaccionesPorCodigo.add(transaccion);
            }
        }
        return transaccionesPorCodigo;
    }
    

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getSaldoFormateado() {
        return String.format("%.2f", saldo);
    }


    public String getEstatus() {
        return estatus;
    }

    public Cliente getMiCliente() {
        return miCliente;
    }

    public String getPin() {
        return this.pin; // Asegúrate de que 'pin' esté correctamente definido en la clase
    }

    public void setPin(String nuevoPin) {
        this.pin = nuevoPin; // Asigna el nuevo PIN
        //this.pinEncriptado = encriptarPin(nuevoPin); // Encripta el nuevo PIN
    }
    
    

    // Funcionalidades
    public void realizarDeposito(double cantidad) {
        if(validarEstatus()){
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad a depositar debe ser positiva.");
            }
            this.saldo += cantidad;
            cantidadTransacciones++; // Aumentar el saldo con la cantidad depositada
            // Puedes agregar un mensaje para depuración
        }
    }
    

    public void agregarRetiro(int pMonto, String pCodigo) {
        String tipo = "Retiro";
        if (validarEstatus()) {
            // Asegúrate de pasar el código de cuenta al crear la transacción
            MensajeSMS mensajeCodigo = new MensajeSMS();
            boolean mensaje = mensajeCodigo.enviarMensajeVerificacion(miCliente.getNumTelefono(),
                    generarCodigoAleatorio());
            if (mensaje) {
                while (intentosValidacion <= 3) {
                    if (mensajeCodigo.verificarCodigo(pCodigo)) {
                        Transaccion nuevaTransaccion = new Transaccion(tipo, pMonto, this.codigo);
                        transacciones.add(nuevaTransaccion);
                        nuevaTransaccion.realizarRetiro(pMonto, saldo);
                        sumaRetiros += pMonto;
                        cantidadTransacciones++;
                        break;
                    } else {
                        intentosValidacion += 1;
                    }
                }
                bloquearCuenta();
            }
        }
    }
    

    private boolean validarCambioPin(String pinActual, String pinNuevo) {
        return !pinActual.equals(pinNuevo);
    }

    private boolean validarPin(String pin) {
        return this.pin.equals(pin);
    }

    private void bloquearCuenta() {
        if (estatus.equals("Activa")) {
            estatus = "Inactiva";
            CorreoElectronico.enviarCorreo(miCliente.getCorreoElectronico(),
                    "Su cuenta se encuentra inactiva debido a que se intentó autenticar más de tres veces");
        }
    }

    private boolean validarEstatus() {
        if (this.estatus.equals("Activa")) {
            return true;
        }
        return false;
    }

    public boolean validarIngreso(String pinIngresado) {
        try {
            String pinEncriptadoIngresado = encriptarPin(pinIngresado); // Encriptar el PIN ingresado
    
            if (pinEncriptadoIngresado.equals(pinEncriptado)) { // Comparar con el PIN encriptado
                usosPin = 0; // Reiniciar el contador si el PIN es correcto
                return true;
            } else {
                usosPin++;
                if (usosPin >= 3) {
                    bloquearCuenta(); // Bloquear cuenta si se alcanzan 3 intentos fallidos
                }
                return false; // PIN incorrecto
            }
        } catch (Exception e) {
            System.err.println("Error al validar el PIN: " + e.getMessage());
            return false; // Manejo de excepción
        }
    }
    
    
    

    public String getPinEncriptado() {
        return pinEncriptado;
    }

    public void setPinEncriptado(String pinEncriptado) {
        this.pinEncriptado = pinEncriptado;
    }

    // Llave de cifrado (debe ser de 16 bytes para AES-128)
    private static final String CLAVE_SECRETA = "clave12345678901"; // Asegúrate de que tenga 16 bytes.

    public String encriptarPin(String pin) throws Exception {
        // Validar el formato del PIN antes de encriptarlo
        if (!Formato.validarFormatoPin(pin)) {
            throw new IllegalArgumentException("El PIN no cumple con el formato requerido.");
        }

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE_SECRETA.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encriptado = cipher.doFinal(pin.getBytes());
        return Base64.getEncoder().encodeToString(encriptado);
    }

    public String desencriptarPin(String pinEncriptado) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE_SECRETA.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodificado = Base64.getDecoder().decode(pinEncriptado);
        byte[] desencriptado = cipher.doFinal(decodificado);
        return new String(desencriptado);
    }
    

    // Ejemplo de cómo usar el método de encriptación cuando se establece un nuevo
    // PIN
    public void cambiarPin(String pinNuevo) {
        try {
            if (validarCambioPin(this.pin, pinNuevo)) {
                this.pinEncriptado = encriptarPin(pinNuevo);
                System.out.println("PIN cambiado y encriptado correctamente.");
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar el PIN: " + e.getMessage());
        }
    }

    public static String generarCodigoAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder(6);
        Random random = new Random();

        // Generar código de 6 caracteres
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(caracteres.length()); // Seleccionar un índice aleatorio
            codigo.append(caracteres.charAt(index)); // Añadir el carácter al código
        }

        return codigo.toString();
    }

    @Override
    public String toString() {
        String identificacionStr = (miCliente != null) ? String.valueOf(miCliente.getIdentificacion()) : "Sin Cliente";
        return "Cuenta{" +
                "numeroCuenta='" + codigo + '\'' +
                ", estatus='" + estatus + '\'' +
                ", saldo=" + saldo +
                ", identificacion='" + identificacionStr + '\'' +
                '}';
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cuenta)) return false;
        Cuenta otraCuenta = (Cuenta) obj;
        return this.codigo.equals(otraCuenta.codigo); // O cualquier otro identificador único
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo); // Asegúrate de que 'codigo' es único para cada cuenta
    }
}

