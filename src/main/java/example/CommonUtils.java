package example;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.domain.ApiRequest;
import example.domain.ApiResponse;
import example.domain.MerchantConfig;
import example.utils.SignContentExtractor;
import example.utils.encrypt.EncryptManager;
import example.utils.signature.ISignature;
import example.utils.signature.SignatureManager;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;


public class CommonUtils {
    private static final MerchantConfig MERCHANT_CONFIG;
    public static  String ENCRYPT_TYPE = "AES";

    private static  String SIGN_TYPE = "RSA";

    public static final String CHARSET = "UTF-8";

    private static final String XCY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2crs8ZhVIw9FI4JPcGrbV+xPZN6+lacJmKMhgzBdNlvNFYaLrozeQkS8BA4xdyCIBykV5DLHxbIRvHUmbrRkfnvI0BwgHQ0opAj+vx3QoZBlSpnwG+wKSNo0t6DQX9mYlOLL/dPVuyfEJGevEgJAKGnxd9ge2FfIN9Ub8S8J2/UUk+50q6utpuEt43wjEPxI1jvlxCbjVi7Hng3ZATJoBwbgUC+WUG/hm1fH+pBpbl133zl5WTlqPRkfwXX9B8DC2+1Y+VXA/bu4z5KdqWLt9dfCJ3zDXW/TOerlO6uu3kWaihQjaaIb02bmcA15pnFR9bOhWLN94lUNT6lzRTrqVwIDAQAB";

    static {
        MERCHANT_CONFIG = new MerchantConfig();
        MERCHANT_CONFIG.setMerchantNo("10000716");
        MERCHANT_CONFIG.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2crs8ZhVIw9FI4JPcGrbV+xPZN6+lacJmKMhgzBdNlvNFYaLrozeQkS8BA4xdyCIBykV5DLHxbIRvHUmbrRkfnvI0BwgHQ0opAj+vx3QoZBlSpnwG+wKSNo0t6DQX9mYlOLL/dPVuyfEJGevEgJAKGnxd9ge2FfIN9Ub8S8J2/UUk+50q6utpuEt43wjEPxI1jvlxCbjVi7Hng3ZATJoBwbgUC+WUG/hm1fH+pBpbl133zl5WTlqPRkfwXX9B8DC2+1Y+VXA/bu4z5KdqWLt9dfCJ3zDXW/TOerlO6uu3kWaihQjaaIb02bmcA15pnFR9bOhWLN94lUNT6lzRTrqVwIDAQAB");
        MERCHANT_CONFIG.setPrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCNdLoKCVG6Jakv5qPV8Afbbz/k0hjT1tw0+8S827wXRUzHyx+xQeKTZdO949QDvPoA5q3cC7Csi3GSa2DpdoJv56rBz+Bn83q4TidHhaI8oAueg+6UbdmsBNevKAOkP4JCDJtGUGJsfzaVLMIJ7khz4aiW8W5c8nfdLEnP7EJyBmETfrsBRm6vFn05mAeWGGKqU+VnlcLzbWgyOeIIZ5ds3HH07KawId9XLDGx9OZs2w0pLj6MfWiGI2qvfIVLIqOiyabkHNjk6Y1GjZfSsvAWZM+9O5VgyIznRe+pwDxQgWGWg2ST138JX/LJKLZAUYfH2NwEwf29MbYkwWSYrM4bAgMBAAECggEAbSI7Tfb2qMjZiO1GRY4UYcWo49777KP97Jq5pINa0BFVNWePIfCU3+dVv1EAh4nKz9spE3WpbtV+X9qcQEKAS9nOMg0xMsH0UjFpuJJvIprPnBJphPZjU4XA6Y/Tj2q4EkErosntxYs4D0siwQqnt0/IA/xsiyU+ONMsd0/ty43bQa97VFs6ipKME3MM7cWGsgOqLwR46h1SosOJf9zPY0ZkLqYxWrKz9XyvFagiaSD8zo35idwpYqqoGpfSXFnQDdHzpB5FSJ95kKjwfAxC4A5nZ5QsDin61XHdi2gE2la2H86K/lc6aLReC13XTPlmileS4/pDS95xm8qK686CAQKBgQDeBy9KVdCu1aQMNt/UMMhLj8/qiiOxiE2vtLHRLJOOMRjPlXemngTM+jegJ133/ksEI7powwdNExRAQVKdCJt+EBp56Q1uG8CF+SJPbdP0tlAVxpYLTOBcsBw0/4zSaI9kz24b5IqePHxoxlKHJrGrdH1+O2YXTK8IF+CIvapCgQKBgQCjGYqfXMbV2C+Vyfd6NDgEmGHVXUPaPOgk9inxEMbI4ZJIMaeHp+O+fKrTp0qHbAYluKGdd0n1/Q8CjA57EYGV38uNT4sciUfNEOmfEbNvRyoBFwB+nPwkWbrxl8ssaG2vvsQjhv86DYfKx4xmuBXk1Tl/h34RzyvcU2V1Ij2KmwKBgCLyCIxvo0/RSmetv177apsSAIyOv31y0Z9lFlOw62yG57vMb6+m0WSdwWGQsKaWvKHjKbTRdXW5GgffACy9LRQTXsdvWfDJp2yuwKq1w5WRmFtFDVqr9+g+w8HHsxdu45rfVxpGx1aOeJpZFKY2/eHZjOLwYGosZKQmde2vxHuBAoGAKmEnoPMVg1lSPASntmw9JgyGV0+NovZyh7AjA82NKYNbZEDqvBgG2GdCaA6vEfMiwchwzh7B7BYeQVYtKRNKbawiEwzZI8gUDRfY7IlH66E9K91TNpMJ/VQHfGEp3NchsSnLg0O/q9D6ONmTBNnObpBtCVMmkfAYAH72/PGxtgMCgYBB3/+vDfZv/6nrkoHq00KPf4tbv3eGiIbbmdnSa+ylKjNevHnkFsAO+pZn4bPGGO7SZLjywVXrGUAU17w2TaYhI1VDvgyefJ1gWy1QIhbYjw5y9tLVJBcm60IB8vL6IYDnl812YrPF/mD9fIY8ie3JWXQIBHqC6EzMwkTOOWgFYw==");
        MERCHANT_CONFIG.setAesSecretKey("F2C0DBD944CAB21D435E5388658259DE");

    }
    public static ApiRequest getMySign(JSONObject bizParamsMap) throws Exception{
        String secretKey = Base64.toBase64String(MERCHANT_CONFIG.getAesSecretKey().getBytes());
        // 业务参数加密得到密文
        String ciphertext = EncryptManager.encryptContent(new ObjectMapper().writeValueAsString(bizParamsMap), ENCRYPT_TYPE, secretKey, CHARSET);

        // 构造请求参数
        ApiRequest request = new ApiRequest();
        request.setMerchantNo((String) bizParamsMap.get("merchant_no"));
        request.setBizContent(ciphertext);
//        request.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        request.setTimestamp((String) bizParamsMap.get("timestamp"));
        request.setNonce((String) bizParamsMap.get("nonce"));
        request.setEncryptType((String) bizParamsMap.get("encrypt_type"));
        request.setSignType((String) bizParamsMap.get("sign_type"));
        SIGN_TYPE = (String) bizParamsMap.get("sign_type");
        ENCRYPT_TYPE = (String) bizParamsMap.get("encrypt_type");

        // 根据签名类型获取签名实例
        ISignature signature = SignatureManager.getInstance(SIGN_TYPE);
        // 拼接请求字符串
        String requestJson = SignContentExtractor.getSignContent(request);
        // 使用商户的私钥对请求参数进行签名
        String sign = signature.sign(requestJson, CHARSET, MERCHANT_CONFIG.getPrivateKey());

        // 设置签名串
        request.setSign(sign);
        System.out.println("myTag.request="+new ObjectMapper().writeValueAsString(request));

        return request;
    }

    public static String getCommonSecriteKeyString(){
        return Base64.toBase64String(MERCHANT_CONFIG.getAesSecretKey().getBytes());
    }
    public static String decryptParam(JSONObject bizParamsMap){
        // 构建mock响应数据
        String cleartext = null;
        try {
            ApiResponse response = new ApiResponse();
            response.setCode((String) bizParamsMap.get("code"));
            response.setMessage((String) bizParamsMap.get("message"));
            response.setEncryptType((String) bizParamsMap.get("encrypt_type"));
            response.setResponse((String) bizParamsMap.get("data"));
            response.setSignType((String) bizParamsMap.get("sign_type"));
            response.setSign((String) bizParamsMap.get("sign"));

            // 根据签名类型获取签名实例
            ISignature signature = SignatureManager.getInstance(SIGN_TYPE);
            // 使用小创云平台的公钥对响应参数进行验签
            boolean verify = signature.verify(SignContentExtractor.getSignContent(response), CHARSET, XCY_PUBLIC_KEY, response.getSign());
            assert verify;

            if (StringUtils.isBlank(response.getResponse())) {
                System.out.println(new ObjectMapper().writeValueAsString(response));
                return "";
            }
            cleartext = EncryptManager.decryptContent(response.getResponse(), ENCRYPT_TYPE, CommonUtils.getCommonSecriteKeyString(), CHARSET);

            response.setResponse(cleartext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cleartext;

    }
}
