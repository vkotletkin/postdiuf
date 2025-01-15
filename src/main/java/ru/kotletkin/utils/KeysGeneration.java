package ru.kotletkin.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeysGeneration {

    private final KeyPair keyPair;

    public static KeyPair createKeyPair() throws NoSuchAlgorithmException {
        return new KeysGeneration().getKeyPair();
    }

    private KeysGeneration() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPair keyPair = createKeyPair();

        System.out.println("Private key: " + keyPair.getPrivate());
        System.out.println("Public key: " + keyPair.getPrivate());
    }
}
