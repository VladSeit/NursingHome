package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class PasswordEncryption {
    public static String encryptPassword(String password) {
        try {
            // Instanziieren eines MessageDigest-Objekts mithilfe des SHA-256-Algorithmus
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Konvertieren eines Kennworts in ein Bytearray
            byte[] passwordBytes = password.getBytes();

            // Einen Passwort-Hash berechnen
            byte[] hashedBytes = md.digest(passwordBytes);

            // Konvertieren eines Hash in eine Hexadezimalformatzeichenfolge
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }


}
