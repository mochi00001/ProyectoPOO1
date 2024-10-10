package servicios;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import modelos.Cuenta;

import java.security.SecureRandom;

public class MensajeSMS {
    // Credenciales de Twilio
    private static final String ACCOUNT_SID = "AC260096de90652894c1ed20f7835f57a6";
    private static final String AUTH_TOKEN = "5b0ca26f04a27eaadcbf8b23a18034a3";

    // Código generado
    private String codigoGenerado;

    // Inicializar Twilio con el constructor
    public MensajeSMS() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * Método para enviar un SMS con un código personalizado al número
     * proporcionado.
     *
     * @param numeroDestino El número de teléfono del destinatario en formato
     *                      internacional (ej. +506XXXXXXX).
     * @param codigo        El código personalizado que deseas enviar.
     * @return true si el mensaje fue enviado correctamente, false en caso de error.
     */
    public boolean enviarMensajeVerificacion(String numeroDestino, String codigo) {
        try {
            // Crear el mensaje de verificación utilizando Twilio API (no Verify)
            Message message = Message.creator(
                    new PhoneNumber(numeroDestino), // Número del destinatario
                    new PhoneNumber("+19252478716"), // Número de Twilio
                    "Tu código de verificación es: " + codigo // Mensaje con el código
            ).create();

            // Guardar el código generado para futura verificación
            this.codigoGenerado = codigo;

            // Imprimir SID del mensaje enviado para propósitos de depuración
            System.out.println("Mensaje enviado: SID: " + message.getSid());
            return true;
        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al enviar mensaje de verificación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para verificar el código que ingresa el usuario.
     *
     * @param codigoIngresado El código ingresado por el usuario.
     * @return true si el código es correcto, false si es incorrecto.
     */
    public boolean verificarCodigo(String codigoIngresado) {
        return codigoGenerado != null && codigoGenerado.equals(codigoIngresado);
    }

    /**
     * Método para generar una palabra de verificación aleatoria.
     *
     * @return Una palabra de verificación como String.
     */
    public String generarPalabraVerificacion() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Caracteres permitidos
        StringBuilder palabra = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 8; i++) { // Longitud de la palabra de verificación
            int indice = random.nextInt(caracteres.length());
            palabra.append(caracteres.charAt(indice));
        }

        return palabra.toString();
    }

    // Método de prueba
    public static void main(String[] args) {
        MensajeSMS mensajeSMS = new MensajeSMS();

        // Generar la palabra de verificación
        String palabraVerificacion = mensajeSMS.generarPalabraVerificacion();
        System.out.println("Palabra de verificación generada: " + palabraVerificacion);

        // Generar un código aleatorio (suponiendo que tienes un método en Cuenta para esto)
        String codigoPersonalizado = Cuenta.generarCodigoAleatorio(); // Asegúrate de que este método esté definido

        // Enviar el mensaje de verificación con el código generado
        boolean resultadoEnvio = mensajeSMS.enviarMensajeVerificacion("+50689655235", codigoPersonalizado);

        if (resultadoEnvio) {
            System.out.println("Mensaje enviado con éxito.");

            // Aquí puedes simular que el usuario ingresa un código para validarlo
            String codigoUsuario = "123456"; // Código ingresado por el usuario

            // Verificar si el código ingresado es correcto
            if (mensajeSMS.verificarCodigo(codigoUsuario)) {
                System.out.println("Código verificado correctamente. Transacción permitida.");
            } else {
                System.out.println("El código ingresado es incorrecto. Transacción no permitida.");
            }
        } else {
            System.out.println("Error al enviar el mensaje.");
        }
    }
}
