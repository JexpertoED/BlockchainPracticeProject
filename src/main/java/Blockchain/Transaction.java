package main.java.Blockchain;

import sun.security.provider.SHA;

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

    Transaction(byte[] prevHash, byte[] password, byte[] pin, boolean value, byte[] addressPubKeyHash) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException {
        this.hash = new byte[0];
        KeyPair keyPair = generateKeypair(getSeed(password, pin));

        ByteArrayOutputStream sctiptSig = new ByteArrayOutputStream();
        try {
            assert keyPair != null;
            sctiptSig.write(keyPair.getPublic().getEncoded());
            sctiptSig.write(generateSignature(keyPair.getPrivate()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.input = new Input(prevHash, sctiptSig.toByteArray());
        this.output = new Output(true, addressPubKeyHash);
    }


    private static KeyPair generateKeypair(byte[] seed) {
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

    private static byte[] getSeed(byte[] password, byte[] pin) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(SHA256.sha256(pin));
        byteArrayOutputStream.write(password);

        return digest.digest((byteArrayOutputStream.toByteArray()));
    }

    private byte[] generateSignature(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initSign(privateKey);
        //String str = this.hash + this.input.prevHash + this.output.pubKeyHash + this.output.value;
        String str = getTransactionString();
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        ecdsa.update(strByte);
        return ecdsa.sign();
    }

    private boolean verifySignature(PublicKey publicKey, byte[] sig) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        ecdsaVerify.initVerify(publicKey);
        //String str = this.hash + this.input.prevHash + this.output.pubKeyHash + this.output.value;
        String str = getTransactionString();
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        ecdsaVerify.update(strByte);
        return ecdsaVerify.verify(sig);
    }

    public boolean verifySignature(byte[] pubSig) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        return verifySignature(getPublicKeyFromByte(Arrays.copyOf(pubSig, 91)), Arrays.copyOf(pubSig, pubSig.length - 91));
    }

    private static KeyPair getKeyPairFromByte(byte[] privateKeyBytes, byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey privateKey = getPrivateKeyFromByte(privateKeyBytes);
        PublicKey publicKey = getPublicKeyFromByte(publicKeyBytes);
        return new KeyPair(publicKey, privateKey);
    }

    private static PrivateKey getPrivateKeyFromByte(byte[] privateKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(privateKeySpec);
    }

    private static PublicKey getPublicKeyFromByte(byte[] publicKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
        try {
            outputStream.write(this.hash);
        } catch (Exception e) {
            //System.out.println("Exceptrionnn!1");
        }
        try {
            outputStream.write(this.input.prevHash);
        } catch (Exception e) {
            //System.out.println("Exceptrionnn!2");
        }
        try {
            outputStream.write(this.input.scriptSig);
        } catch (Exception e) {
            // System.out.println("Exceptrionnn!3");
        }
        try {
            outputStream.write(this.output.pubKeyHash);
            outputStream.write(String.valueOf(this.output.value).getBytes());
        } catch (Exception e) {
            // System.out.println("Exceptrionnn!4");
        }

        return outputStream.toByteArray();
    }

    String getTransactionString() {
        return Blockchain.bytesToHex(getTransactionBytes());
    }

    public String getTransactionHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return Blockchain.bytesToHex(digest.digest(getTransactionBytes()));
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

}
