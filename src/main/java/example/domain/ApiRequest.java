package example.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author QingBinGuo
 * @date 2023/2/3 9:59
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRequest {

    /**
     * 商户号
     */
    @JsonProperty("merchant_no")
    private String merchantNo;

    /**
     * 业务参数
     */
    @JsonProperty("biz_content")
    private String bizContent;


    /**
     * 时间戳
     */
    @JsonProperty("timestamp")
    private String timestamp;

    /**
     * 随机数
     */
    @JsonProperty("nonce")
    private String nonce;

    /**
     * 加密方式
     */

    @JsonProperty("encrypt_type")
    private String encryptType;

    /**
     * 签名算法类型
     */
    @JsonProperty("sign_type")
    private String signType;

    /**
     * 签名串
     */
    @JsonProperty("sign")
    private String sign;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
