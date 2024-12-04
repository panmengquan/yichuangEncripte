package example.utils.signature.service.impl;

import example.utils.signature.service.AbstractSignature;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author QingBinGuo
 * @date 2023/1/9 10:15
 */
public class RSASignature extends AbstractSignature {

    private static final String SIGN_TYPE_RSA = "RSA";

    private static final String SIGN_ALGORITHMS_SHA1WithRSA = "SHA1WithRSA";

    public String getSignAlgorithm() {
        return SIGN_ALGORITHMS_SHA1WithRSA;
    }

    @Override
    public String getAsymmetricType() {
        return SIGN_TYPE_RSA;
    }

    @Override
    public String doSign(String content, String charset, String privateKey) throws Exception {
        PrivateKey priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));

        Signature signature = Signature.getInstance(getSignAlgorithm());

        signature.initSign(priKey);

        if (StringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }

        byte[] signed = signature.sign();

        return new String(Base64.encode(signed));
    }

    @Override
    public boolean doVerify(String content, String charset, String publicKey, String sign) throws Exception {
        PublicKey pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));

        Signature signature = Signature.getInstance(getSignAlgorithm());

        signature.initVerify(pubKey);

        if (StringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }

        return signature.verify(Base64.decode(sign.getBytes()));
    }

    public PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || StringUtils.isEmpty(algorithm)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = IOUtils.toByteArray(ins);

        encodedKey = Base64.decode(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        IOUtils.copy(new InputStreamReader(ins), writer);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decode(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }
}
