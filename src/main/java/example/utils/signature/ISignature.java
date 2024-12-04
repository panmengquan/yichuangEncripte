package example.utils.signature;

/**
 * @author QingBinGuo
 * @date 2023/1/9 10:14
 */
public interface ISignature {

    /**
     * 非对称类型
     * @return
     */
    String getAsymmetricType();

    /**
     * 计算指定内容的签名
     * @param content 待签名的原文
     * @param charset 待签名的原文的字符集编码
     * @param privateKey 私钥字符串
     * @return 签名字符串
     */
    String sign(String content, String charset, String privateKey) throws Exception;

    /**
     * 验证指定内容的签名是否正确
     * @param content 待校验的原文
     * @param charset 待校验的原文的字符集编码
     * @param publicKey 公钥字符串
     * @param sign 签名字符串
     * @return true：验证通过；false：验证不通过
     */
    boolean verify(String content, String charset, String publicKey, String sign) throws Exception;
}