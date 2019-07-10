package main.java.Blockchain;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;

public class Transaction implements TransactionInterface {
    private final String hash;
    private final Input input;
    private final Output output;

    public Transaction(String hash, Input input, Output output) {
        this.hash = hash;
        this.input = input;
        this.output = output;
    }


    @Override
    public byte[] addArgument(byte[] arg) {
        return new byte[0];
    }

    @Override
    public byte[] addArgument(String arg) {
        return new byte[0];
    }

    @Override
    public boolean checkSignature() {
        return false;
    }

    public static KeyPair generateKeypair(byte[] seed) {
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom(seed));
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            return null;
        }

    }

    public byte[] generateSignature(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initSign(privateKey);
        String str = this.hash + this.input.prevHash + this.output.pubKeyHash + this.output.value;
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        ecdsa.update(strByte);
        return ecdsa.sign();
    }

    public boolean verifySignature(PublicKey publicKey, byte[] sig) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        ecdsaVerify.initVerify(publicKey);
        String str = this.hash + this.input.prevHash + this.output.pubKeyHash + this.output.value;
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        ecdsaVerify.update(strByte);
        return ecdsaVerify.verify(sig);
    }

    public static KeyPair getKeyPairFromByte(byte[] privateKeyBytes, byte[]publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return new KeyPair(publicKey,privateKey);
    }


    private class Input {
        final String prevHash;
        //final long index;   //vout
        final String scriptSig;

        private Input(String prevHash, String scriptSig) {
            this.prevHash = prevHash;
            //      this.index = index;
            this.scriptSig = scriptSig;
        }
    }

    private class Output {
        final boolean value;
        final String pubKeyHash;

        private Output(boolean value, String pubKeyHash) {
            this.value = value;
            this.pubKeyHash = pubKeyHash;
        }
    }

}
