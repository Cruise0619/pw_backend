import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

/**
 * Builds a PKCS12 keystore from a PEM certificate chain and a PEM private key.
 *
 * Usage: MakeKeystore <cert.pem> <key.pem> <out.p12> <storepass>
 *
 * Only the JDK is required. Handles PKCS#1 ("RSA PRIVATE KEY") and
 * PKCS#8 ("PRIVATE KEY") private keys and multi-certificate chains.
 */
public class MakeKeystore {

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Usage: MakeKeystore <cert.pem> <key.pem> <out.p12> <storepass>");
            System.exit(1);
        }
        String certPath = args[0];
        String keyPath = args[1];
        String outPath = args[2];
        char[] password = args[3].toCharArray();

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        List<X509Certificate> chain = new ArrayList<>();
        try (InputStream in = new FileInputStream(certPath)) {
            Collection<? extends Certificate> certs = cf.generateCertificates(in);
            for (Certificate cert : certs) {
                chain.add((X509Certificate) cert);
            }
        }
        if (chain.isEmpty()) {
            System.err.println("No certificates found in " + certPath);
            System.exit(2);
        }
        X509Certificate[] certChain = chain.toArray(new X509Certificate[0]);

        String pem = new String(Files.readAllBytes(Paths.get(keyPath)), StandardCharsets.US_ASCII);
        PrivateKey privateKey = parsePrivateKey(pem);

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(null, null);
        ks.setKeyEntry("pwbackend", privateKey, password, certChain);

        Path out = Paths.get(outPath);
        if (out.getParent() != null) {
            Files.createDirectories(out.getParent());
        }
        try (OutputStream os = new FileOutputStream(out.toFile())) {
            ks.store(os, password);
        }
        System.out.println("Keystore written to " + outPath + " (" + certChain.length + " certificate(s))");
    }

    private static PrivateKey parsePrivateKey(String pem) throws Exception {
        String key;
        boolean pkcs8;
        if (pem.contains("-----BEGIN RSA PRIVATE KEY-----")) {
            key = pem.substring(
                    pem.indexOf("-----BEGIN RSA PRIVATE KEY-----") + "-----BEGIN RSA PRIVATE KEY-----".length(),
                    pem.indexOf("-----END RSA PRIVATE KEY-----"));
            pkcs8 = false;
        } else if (pem.contains("-----BEGIN PRIVATE KEY-----")) {
            key = pem.substring(
                    pem.indexOf("-----BEGIN PRIVATE KEY-----") + "-----BEGIN PRIVATE KEY-----".length(),
                    pem.indexOf("-----END PRIVATE KEY-----"));
            pkcs8 = true;
        } else {
            throw new IllegalArgumentException("Unsupported private key format in key file");
        }
        byte[] der = Base64.getMimeDecoder().decode(key.trim());
        if (!pkcs8) {
            der = wrapPkcs1ToPkcs8(der);
        }
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(der));
    }

    /** Wraps a PKCS#1 RSAPrivateKey DER into a PKCS#8 PrivateKeyInfo structure. */
    private static byte[] wrapPkcs1ToPkcs8(byte[] pkcs1) throws Exception {
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        content.write(new byte[] { 0x02, 0x01, 0x00 }); // version INTEGER 0
        content.write(new byte[] { // rsaEncryption AlgorithmIdentifier
                (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86,
                (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00 });
        content.write(derElement(0x04, pkcs1)); // OCTET STRING (private key)
        return derElement(0x30, content.toByteArray()); // outer SEQUENCE
    }

    private static byte[] derElement(int tag, byte[] body) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(tag);
        writeLength(out, body.length);
        out.write(body);
        return out.toByteArray();
    }

    private static void writeLength(ByteArrayOutputStream out, int length) {
        if (length < 0x80) {
            out.write(length);
        } else {
            int numBytes = 0;
            for (int temp = length; temp > 0; numBytes++) {
                temp >>>= 8;
            }
            out.write(0x80 | numBytes);
            for (int i = numBytes - 1; i >= 0; i--) {
                out.write((length >>> (8 * i)) & 0xFF);
            }
        }
    }
}
