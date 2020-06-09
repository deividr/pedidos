package br.com.labuonapasta.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia {

    public static String criptografar(String senha) {
        try {
            String senhaSalt = senha.concat("Random$SaltValue12@$@4&#%^$*");

            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = algorithm.digest(senhaSalt.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
