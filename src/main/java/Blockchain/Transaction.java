package main.java.Blockchain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;

public class Transaction implements TransactionInterface {
    private final byte[] hash;
    private final Input input;
    private final Output output;

    Transaction(byte[] prevHash, byte[] password, byte[] personData, boolean value, byte[] addressPubKeyHash) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException {
        KeyPair keyPair = generateKeypair(getSeed(personData, password));
        ByteArrayOutputStream sctiptSig = new ByteArrayOutputStream();
        try {
            assert keyPair != null;
            sctiptSig.write(keyPair.getPublic().getEncoded());
            sctiptSig.write(generateSignature(keyPair.getPrivate(),prevHash, addressPubKeyHash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.input = new Input(prevHash, sctiptSig.toByteArray());
        this.output = new Output(true, addressPubKeyHash);
        this.hash = generateHash();
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

//    private static byte[] getSeed(String password, String pin) throws NoSuchAlgorithmException {
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        return digest.digest((password + SHA256.sha256(pin)).getBytes());
//    }

    public static byte[] getSeed(byte[] personData, byte[] password) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(SHA256.sha256(personData));
        byteArrayOutputStream.write(SHA256.sha256(password));

        return digest.digest((byteArrayOutputStream.toByteArray()));
    }

    private byte[] generateSignature(PrivateKey privateKey, byte[] prevHash, byte[] addressPubKeyHash) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initSign(privateKey);
        //String str = this.hash + this.input.prevHash + this.output.pubKeyHash + this.output.value;
        String str = getTransactionHash(prevHash, addressPubKeyHash);
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        ecdsa.update(strByte);
        return ecdsa.sign();
    }

    public boolean verifySignature(PublicKey publicKey, byte[] sig) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        ecdsaVerify.initVerify(publicKey);
        //String str = this.hash + this.input.prevHash + this.output.pubKeyHash + this.output.value;
        String str = getTransactionHash();
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        ecdsaVerify.update(strByte);
        return ecdsaVerify.verify(sig);
    }

    public boolean verifySignature() throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        return verifySignature(getPublicKeyFromByte(getPubKeyFromScriptSig(this.input.scriptSig)), getSignatureFromScriptSig(this.input.scriptSig));
    }

    private static KeyPair getKeyPairFromByte(byte[] privateKeyBytes, byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey privateKey = getPrivateKeyFromByte(privateKeyBytes);
        PublicKey publicKey = getPublicKeyFromByte(publicKeyBytes);
        return new KeyPair(publicKey, privateKey);
    }

    public static PrivateKey getPrivateKeyFromByte(byte[] privateKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    public static PublicKey getPublicKeyFromByte(byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    public boolean validateTransaction(Transaction transaction) {

        return false;
    }

//    private byte[] getTransactionBytes() {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            outputStream.write(this.hash);
//            try {
//                outputStream.write(this.input.prevHash);
//                try {
//                    outputStream.write(this.input.scriptSig);
//                    try {
//                        outputStream.write(this.output.pubKeyHash);
//                        outputStream.write(String.valueOf(this.output.value).getBytes());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        //System.out.println("Null 1");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    //System.out.println("Null 2");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                //System.out.println("Null 3");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            //System.out.println("Null 4");
//
//        }
//        return outputStream.toByteArray();
//    }

    private byte[] getTransactionBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            outputStream.write(this.hash);
//        } catch (Exception e) {
//            //System.out.println("Exceptrionnn!1");
//        }
        try {
            outputStream.write(this.input.prevHash);
        } catch (Exception e) {
            System.out.println("Exceptrionnn!2");
        }
//        try {
//            outputStream.write(this.input.scriptSig);
//        } catch (Exception e) {
//            System.out.println("Exceptrionnn!3");
//        }
        try {
            outputStream.write(this.output.pubKeyHash);
        } catch (Exception e) {
            // System.out.println("Exceptrionnn!4");
        }

        return outputStream.toByteArray();
    }

    private byte[] getTransactionBytesForMercle() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(this.hash);
        } catch (Exception e) {
            //System.out.println("Exceptrionnn!1");
        }
        try {
            outputStream.write(this.input.prevHash);
        } catch (Exception e) {
           // System.out.println("Exceptrionnn!2");
        }
        try {
            outputStream.write(this.input.scriptSig);
        } catch (Exception e) {
            //System.out.println("Exceptrionnn!3");
        }
        try {
            outputStream.write(this.output.pubKeyHash);
        } catch (Exception e) {
            // System.out.println("Exceptrionnn!4");
        }

        return outputStream.toByteArray();
    }

    private byte[] getTransactionBytes(byte[] prevHash, byte[] addressPubKeyHash) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(prevHash);
        } catch (Exception e) {
            //System.out.println("Exceptrionnn!2");
        }
        try {
            outputStream.write(addressPubKeyHash);
        } catch (Exception e) {
             //System.out.println("Exceptrionnn!4");
        }

        return outputStream.toByteArray();
    }


    String getTransactionString() {
        return Blockchain.bytesToHex(getTransactionBytes());
    }

    String getTransactionStringForMercle() {
        return Blockchain.bytesToHex(getTransactionBytesForMercle());
    }

    public String getTransactionHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return Blockchain.bytesToHex(digest.digest(getTransactionBytes()));
    }

    public String getTransactionHash(byte[] prevHash, byte[] addressPubKeyHash) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return Blockchain.bytesToHex(digest.digest(getTransactionBytes(prevHash,addressPubKeyHash)));
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


    static class Input {
        final byte[] prevHash;
        //final long index;   //vout
        final byte[] scriptSig;

        Input(byte[] prevHash, byte[] scriptSig) {
            this.prevHash = prevHash;
            //      this.index = index;
            this.scriptSig = scriptSig;
        }
    }

    static class Output {
        final boolean value;
        final byte[] pubKeyHash;

        Output(boolean value, byte[] pubKeyHash) {
            this.value = value;
            this.pubKeyHash = pubKeyHash;
        }
    }

    public byte[] getHash() {
        return hash;
    }

    byte[] generateHash() {
        try {
            return SHA256.sha256(Blockchain.hexStringToByteArray(Blockchain.bytesToHex(this.input.prevHash) + Blockchain.bytesToHex(this.input.scriptSig) + Blockchain.bytesToHex(this.output.pubKeyHash)));
        } catch (Exception e) {
        }
        return new byte[0];
    }

    public Input getInput() {
        return input;
    }

    public Output getOutput() {
        return output;
    }

    public static byte[] getPubKeyFromScriptSig(byte[] scriptSig) {
        return Arrays.copyOf(scriptSig, 91);
    }

    public static byte[] getSignatureFromScriptSig(byte[] scriptSig) {
        int sigLength = scriptSig.length - 91;
        byte[] sig = new byte[sigLength];
        System.arraycopy(scriptSig, 91, sig, 0, sigLength);
        return sig;
    }
}
