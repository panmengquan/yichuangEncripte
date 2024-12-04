package example.utils.signature.service;

import example.utils.Assert;
import example.utils.signature.ISignature;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * @author QingBinGuo
 * @date 2023/1/9 10:14
 */
public abstract class AbstractSignature implements ISignature {

    private String DEFAULT_CHARSET = "UTF-8";

    @Override
    public String sign(String content, String charset, String privateKey) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(content), () -> "待签名内容不可为空");
            Assert.isTrue(StringUtils.isNotBlank(privateKey), () -> "私钥[privateKey]不可为空");
            if (StringUtils.isEmpty(charset)) {
                charset = DEFAULT_CHARSET;
            }
            return doSign(content, charset, privateKey);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("content:{0},charset{1},签名错误,{2}", content, charset, e.getMessage()), e);
        }
    }

    @Override
    public boolean verify(String content, String charset, String publicKey, String sign) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(content), () -> "待验签内容不可为空");
            Assert.isTrue(StringUtils.isNotBlank(publicKey), () -> "公钥不可为空");
            Assert.isTrue(StringUtils.isNotBlank(sign), () -> "签名串不可为空");
            if (StringUtils.isEmpty(charset)) {
                charset = DEFAULT_CHARSET;
            }
            return doVerify(content, charset, publicKey, sign);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("content:{0},charset{1},验签错误,{2}", content, charset, e.getMessage()), e);
        }
    }

    protected abstract String doSign(String content, String charset, String privateKey) throws Exception;

    protected abstract boolean doVerify(String content, String charset, String publicKey, String sign) throws Exception;
}
