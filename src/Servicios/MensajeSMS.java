package servicios;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;

public class MensajeSMS {

    // Credenciales de Twilio
    private static final String ACCOUNT_SID = "AC260096de90652894c1ed20f7835f57a6";
    private static final String AUTH_TOKEN = "f8da62f7d70e99967391dc5a487bf96c";
    private static final String SERVICE_SID = "VA3960d0adfca52a29537f382a7496e79a"; // Servicio de verificación de
                                                                                    // Twilio

    // Inicializar Twilio con el constructor
    public MensajeSMS() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * Método para enviar un SMS de verificación al número proporcionado.
     *
     * @param numeroDestino El número de teléfono del destinatario en formato
     *                      internacional (ej. +506XXXXXXX).
     * @return true si el mensaje fue enviado correctamente, false en caso de error.
     */
    public boolean enviarMensajeVerificacion(String numeroDestino) {
        try {
            // Crear la verificación utilizando Twilio Verify
            Verification verification = Verification.creator(
                    SERVICE_SID, // SID del servicio de verificación
                    numeroDestino, // Número al que se envía la verificación
                    "sms" // Método de envío: SMS
            ).create();

            // Imprimir SID del mensaje enviado para propósitos de depuración
            System.out.println("Mensaje de verificación enviado: SID: " + verification.getSid());
            return true;
        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al enviar mensaje de verificación: " + e.getMessage());
            return false;
        }
    }

    // Método de prueba
    public static void main(String[] args) {
        MensajeSMS mensajeSMS = new MensajeSMS();
        boolean resultado = mensajeSMS.enviarMensajeVerificacion("+50689655235");
        if (resultado) {
            System.out.println("Mensaje enviado con éxito.");
        } else {
            System.out.println("Error al enviar el mensaje.");
        }
    }
}
