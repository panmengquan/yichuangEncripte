package example.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author QingBinGuo
 * @date 2023/2/6 11:04
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private String code;

    private String message;

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

    /**
     * 响应业务参数
     */

    @JsonProperty("data")
    private String response;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
