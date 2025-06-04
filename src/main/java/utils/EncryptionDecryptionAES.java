package utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionDecryptionAES {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 128;

    // Navigate from the current file to the project root: 3 levels up
    private static final Path dataFile = Paths.get("")
            .toAbsolutePath()              // get full path to working dir
            .resolve("src")               // navigate into src
            .resolve("main")
            .resolve("data")              // into data
            .resolve("secret.key");       // the file
    private static final String KEY_FILE = dataFile.toString(); // adjust path as needed

    private final SecretKey secretKey;

    private EncryptionDecryptionAES(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    // Factory method to get instance
    public static EncryptionDecryptionAES getInstance() {
        SecretKey key;
        try{
            if (Files.exists(Paths.get(KEY_FILE))) {
                key = loadKey(KEY_FILE);
            } else {
                key = generateAndSaveKey(KEY_FILE);
            }
            return new EncryptionDecryptionAES(key);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }


    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        byte[] combined = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();

        return Base64.getEncoder().encodeToString(combined);
    }


    public String decrypt(String encryptedText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);

        ByteBuffer buffer = ByteBuffer.wrap(combined);
        byte[] iv = new byte[16];
        buffer.get(iv);
        byte[] cipherText = new byte[buffer.remaining()];
        buffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decrypted = cipher.doFinal(cipherText);
        return new String(decrypted);
    }

    // Generate and save AES key
    private static SecretKey generateAndSaveKey(String filePath) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        SecretKey key = generator.generateKey();
        byte[] encoded = key.getEncoded();
        Files.write(Paths.get(filePath), encoded);
        return key;
    }

    // Load AES key from file
    private static SecretKey loadKey(String filePath) throws IOException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        return new SecretKeySpec(keyBytes, "AES");
    }
}
