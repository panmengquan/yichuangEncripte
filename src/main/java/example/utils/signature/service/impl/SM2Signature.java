package example.utils.signature.service.impl;

import example.utils.signature.service.AbstractSignature;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.MessageFormat;

/**
 * @author QingBinGuo
 * @date 2023/1/9 10:15
 */
public class SM2Signature extends AbstractSignature {

    public static final String SIGN_TYPE_SM2 = "SM2";

    public static final String SIGN_ALGORITHMS_SM2 = "SM3withSM2";

    /**
     * SM2算法默认用户ID，目前开放平台不会使用非默认用户ID
     */
    public String DEFAULT_USER_ID = "1234567812345678";

    private BouncyCastleProvider provider;

    {
        provider = new BouncyCastleProvider();
        Security.addProvider(provider);
    }

    @Override
    public String getAsymmetricType() {
        return SIGN_TYPE_SM2;
    }

    @Override
    protected String doSign(String content, String charset, String privateKey) throws Exception {
        byte[] privateKeyByte = Base64.decode(privateKey);
        PrivateKey sm2PrivateKey = parsePKCS8PrivateKey(privateKeyByte);
        byte[] message = content.getBytes(charset);
        byte[] signature = sm2Sign(message, sm2PrivateKey, null);

        //将签名结果转换为Base64
        return Base64.toBase64String(signature);
    }

    @Override
    protected boolean doVerify(String content, String charset, String publicKey, String sign) throws Exception {
        // 使用SM2公钥验签
        byte[] signature = Base64.decode(sign);
        byte[] message = content.getBytes(charset);
        byte[] publicKeyByte = Base64.decode(publicKey);
        PublicKey sm2PublicKey = parseX509PublicKey(publicKeyByte);
        return sm2Verify(signature, message, sm2PublicKey, null);
    }

    private byte[] sm2Sign(byte[] message, PrivateKey sm2PrivateKey, String sm2UserId) {
        try {
            String userId = DEFAULT_USER_ID;
            if (!StringUtils.isEmpty(sm2UserId)) {
                userId = sm2UserId;
            }
            Signature sm2SignEngine = Signature.getInstance(SIGN_ALGORITHMS_SM2);
            sm2SignEngine.setParameter(new SM2ParameterSpec(
                    Strings.toByteArray(userId)));
            sm2SignEngine.initSign(sm2PrivateKey);
            sm2SignEngine.update(message);
            return sm2SignEngine.sign();
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("签名错误,{0}", e.getMessage()), e);
        }
    }

    private boolean sm2Verify(byte[] signature, byte[] message, PublicKey publicKey, String sm2UserId) {
        try {
            String userId = DEFAULT_USER_ID;
            if (!StringUtils.isEmpty(sm2UserId)) {
                userId = sm2UserId;
            }
            Signature sm2SignEngine = Signature.getInstance(SIGN_ALGORITHMS_SM2);
            sm2SignEngine.setParameter(new SM2ParameterSpec(Strings.toByteArray(userId)));
            sm2SignEngine.initVerify(publicKey);
            sm2SignEngine.update(message);
            return sm2SignEngine.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("验签错误,{0}", e.getMessage()), e);
        }
    }

    private PublicKey parseX509PublicKey(byte[] x509PublicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(x509PublicKey);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(MessageFormat.format("签名异常,not available algorithm,{0}", e.getMessage()), e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(MessageFormat.format("签名异常,invalid key specifications,{0}", e.getMessage()), e);
        }
    }

    private PrivateKey parsePKCS8PrivateKey(byte[] pkcs8PrivateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8PrivateKey);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(MessageFormat.format("签名异常,not available algorithm,{0}", e.getMessage()), e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(MessageFormat.format("签名异常,invalid key specifications,{0}", e.getMessage()), e);
        }
    }
}
