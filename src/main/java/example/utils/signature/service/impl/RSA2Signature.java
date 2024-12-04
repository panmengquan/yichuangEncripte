package example.utils.signature.service.impl;

/**
 * @author QingBinGuo
 * @date 2023/1/9 10:15
 */
public class RSA2Signature extends RSASignature {

    private static final String SIGN_TYPE_RSA = "RSA2";

    private static final String SIGN_ALGORITHMS_SHA256WithRSA = "SHA256WithRSA";

    @Override
    public String getSignAlgorithm() {
        return SIGN_ALGORITHMS_SHA256WithRSA;
    }

    @Override
    public String getAsymmetricType() {
        return SIGN_TYPE_RSA;
    }
}
