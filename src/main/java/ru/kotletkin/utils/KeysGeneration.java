package ru.kotletkin.utils;

import lombok.Getter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Getter
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

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPair keyPair = createKeyPair();

        System.out.println("Private key: " + Arrays.toString(keyPair.getPublic().getEncoded()));
        System.out.println("Public key: " + keyPair.getPrivate());
    }
}
