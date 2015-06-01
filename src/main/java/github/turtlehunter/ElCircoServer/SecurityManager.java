package github.turtlehunter.ElCircoServer;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;

public class SecurityManager {
    public HashMap<String, PublicKey> clientKeys = new HashMap<String, PublicKey>();
    public PublicKey serverPublic;
    private PrivateKey serverPrivate;

    public SecurityManager() {
        if(new File(Main.instance.references.getReference("publickey")).exists()) {
            serverPublic = readPublicKey();
            serverPrivate = readPrivateKey(); //TODO generate new pair often
        } else {
            generateKey();
        }
    }

    public void generateKey() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(2048, new SecureRandom());

            KeyPair keyPair = keyGen.genKeyPair();
            Signature signature = Signature.getInstance("SHA1withRSA", "BC");

            KeyFactory fact = KeyFactory.getInstance("RSA");

            RSAPublicKeySpec pub = fact.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
            saveToFile(Main.instance.references.getReference("publickey"), pub.getModulus(), pub.getPublicExponent());

            RSAPrivateKeySpec priv = fact.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
            saveToFile(Main.instance.references.getReference("privatekey"), priv.getModulus(), priv.getPrivateExponent());

        } catch (NoSuchAlgorithmException e) {
            Main.instance.loggingManager.write("Error generating key, no RSA avaliable.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Main.instance.loggingManager.write("Error generating key, Invalid Key Spec.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            Main.instance.loggingManager.write("Error generating key, No Such Provider.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        }
    }

    private static void saveToFile(String fileName, BigInteger mod, BigInteger exp) {
        ObjectOutputStream oout = null;
        try {
            oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            oout.writeObject(mod);
            oout.writeObject(exp);
            oout.close();
        } catch (IOException e) {
            Main.instance.loggingManager.write("Error saving key, IOException.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        }

    }

    private static PublicKey readPublicKey() {
        InputStream in = null;
        try {
            in = new FileInputStream(Main.instance.references.getReference("publickey"));
            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            oin.close();
            return pubKey;
        } catch (FileNotFoundException e) {
            Main.instance.loggingManager.write("Error saving key, File not found.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Main.instance.loggingManager.write("Error saving key, Class Not found.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Main.instance.loggingManager.write("Error saving key, No such algorithm.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Main.instance.loggingManager.write("Error saving key, Invalid Key Spec.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (IOException e) {
            Main.instance.loggingManager.write("Error saving key, IOException.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        }
        return null;
    }

    private static PrivateKey readPrivateKey() {
        InputStream in = null;
        try {
            in = new FileInputStream(Main.instance.references.getReference("privatekey"));
            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey pubKey = fact.generatePrivate(keySpec);
            oin.close();
            return pubKey;
        } catch (FileNotFoundException e) {
            Main.instance.loggingManager.write("Error saving key, File not found.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Main.instance.loggingManager.write("Error saving key, Class Not found.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Main.instance.loggingManager.write("Error saving key, No such algorithm.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Main.instance.loggingManager.write("Error saving key, Invalid Key Spec.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        } catch (IOException e) {
            Main.instance.loggingManager.write("Error saving key, IOException.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            Main.instance.loggingManager.write("Error saving key, Exception.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        }
        return cipherText;
    }

    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception e) {
            Main.instance.loggingManager.write("Error saving key, Exception.");
            Main.instance.loggingManager.write("---Log Start---" + Main.instance.loggingManager.crash(e) + "---Log End---");
            e.printStackTrace();
        }

        return new String(dectyptedText);
    }

}
