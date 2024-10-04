package servicios;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.type.PhoneNumber;

public class MensajeSMS {
    // Credenciales de Twilio
    private static final String ACCOUNT_SID = "AC260096de90652894c1ed20f7835f57a6";
    private static final String AUTH_TOKEN = "db7d2aba53f29872c651f47d8768d794";
    private static final String SERVICE_SID = "VA3960d0adfca52a29537f382a7496e79a"; // Servicio de verificación de Twilio

    // Código generado
    private String codigoGenerado;

    // Inicializar Twilio con el constructor
    public MensajeSMS() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * Método para enviar un SMS con un código personalizado al número proporcionado.
     *
     * @param numeroDestino El número de teléfono del destinatario en formato internacional (ej. +506XXXXXXX).
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

    // Método de prueba
    public static void main(String[] args) {
        MensajeSMS mensajeSMS = new MensajeSMS();

        // Definir el código personalizado que deseas enviar
        String codigoPersonalizado = "123456"; // El código que desees

        // Enviar el mensaje de verificación con el código personalizado
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
