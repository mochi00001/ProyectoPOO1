package servicios;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

public class CorreoElectronico {

    public static void enviarCorreo(String destinatario, String mensaje) {
        // Información de autenticación del remitente
        String correoRemitente = "proyectopoofsl@gmail.com";
        String contrasenaRemitente = "jupk byzm obxi rbsu";

        // Configuración de propiedades para la conexión SMTP de Gmail
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.setProperty("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        propiedades.setProperty("mail.smtp.port", "587");
        propiedades.setProperty("mail.smtp.user", correoRemitente);
        propiedades.setProperty("mail.smtp.auth", "true");
        propiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        // Crear una sesión de correo
        Session session = Session.getDefaultInstance(propiedades);

        try {
            // Crea un mensaje de correo
            Message mensajeCorreo = new MimeMessage(session);
            mensajeCorreo.setFrom(new InternetAddress(correoRemitente));
            mensajeCorreo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensajeCorreo.setSubject("Correo confirmación");

            // Crea la parte del texto del mensaje
            // BodyPart cuerpoMensaje = new MimeBodyPart();
            // cuerpoMensaje.setText(mensaje);

            // Crea la parte del texto del mensaje
            MimeBodyPart cuerpoMensaje = new MimeBodyPart();
            cuerpoMensaje.setText(mensaje);

            // Crea un contenedor de mensaje y añade la parte del cuerpo
            Multipart contenido = new MimeMultipart();
            contenido.addBodyPart(cuerpoMensaje);

            // Establecer el contenido del mensaje
            mensajeCorreo.setContent(contenido);

            // Enviar el mensaje
            Transport t = session.getTransport("smtp");
            t.connect(correoRemitente, contrasenaRemitente);
            t.sendMessage(mensajeCorreo, mensajeCorreo.getAllRecipients());
            t.close();
            JOptionPane.showMessageDialog(null, "Mensaje enviado a: " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Destinatario del correo
        String destinatario = "proyectopoofsl@gmail.com";

        // Mensaje del correo
        String mensaje = "Este es un mensaje de prueba.";

        // Enviar el correo
        enviarCorreo(destinatario, mensaje);
    }

}