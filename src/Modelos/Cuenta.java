package modelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import servicios.CorreoElectronico;
import servicios.MensajeSMS;

public class Cuenta {
    private String codigo;
    private static int cantidadCuentas;
    private LocalDate fechaCreacion;
    private String estatus;
    private int saldo;
    private String pin;
    private double sumaComisiones;
    private double sumaComisionesRetiros;
    private double sumaComisionesDepositos;
    private int sumaRetiros;
    private int sumaDepositos;
    private String monedaOficial;
    private String monedaExtrangera;
    private String pinEncriptado;
    private int usosPin;
    private Cliente miCliente;
    private ArrayList<Transaccion> transacciones;
    private int intentosValidacion;

    public Cuenta(int saldo, String pin, Cliente cliente) {
        codigo = "cta-" + cantidadCuentas;
        cantidadCuentas++;
        this.saldo = saldo;
        this.pin = pin;
        miCliente = cliente;
        transacciones = new ArrayList<>();
        estatus = "Activa";
        usosPin = 0;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getEstatus() {
        return estatus;
    }

    // Funcionalidades
    public void agregarRetiro(int pMonto, String pCodigo) {
        String tipo = "Retiro";
        if (validarEstatus()) {
            Transaccion nuevaTransaccion = new Transaccion(tipo, pMonto);
            transacciones.add(nuevaTransaccion);
            MensajeSMS mensajeCodigo = new MensajeSMS();
            boolean mensaje = mensajeCodigo.enviarMensajeVerificacion(miCliente.getNumTelefono(),
                    generarCodigoAleatorio());
            if (mensaje) {
                while (intentosValidacion <= 3) {
                    if (mensajeCodigo.verificarCodigo(pCodigo)) {
                        nuevaTransaccion.realizarRetiro(pMonto, saldo);
                        sumaRetiros += pMonto;
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
        if (!pinActual.equals(pinNuevo)) {
            return true;
        } else {
            return false;
        }
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

    private boolean validarIngreso(String pinIngresado) {
        while (usosPin <= 3) {
            if (pinIngresado.compareTo(pin) == 0) {
                usosPin = 0;
                return true;
            }
            usosPin++;
        }
        bloquearCuenta();
        return false;
    }

    public String getPinEncriptado() {
        return pinEncriptado;
    }

    public void setPinEncriptado(String pinEncriptado) {
        this.pinEncriptado = pinEncriptado;
    }

    // Llave de cifrado (debe ser de 16 bytes para AES-128)
    private static final String CLAVE_SECRETA = "clave123456789012";

    public String encriptarPin(String pin) throws Exception {
        // Validar el formato del PIN antes de encriptarlo
        if (!validarFormatoPin(pin)) {
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

    private boolean validarFormatoPin(String pin) {
        // Expresión regular para validar el PIN
        String regex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(pin).matches();
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

}
