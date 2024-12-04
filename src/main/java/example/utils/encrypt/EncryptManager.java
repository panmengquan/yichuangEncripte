package example.utils.encrypt;


import example.utils.encrypt.impl.AesEncrypt;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2023/2/16 17:52
 */
public class EncryptManager {

    private static Map<String, Encrypt> encryptManager = new HashMap<String, Encrypt>();

    static {
        encryptManager.put("AES", new AesEncrypt());
    }

    private EncryptManager() {
    }

    /**
     * 加密
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String encryptContent(String content, String encryptType, String encryptKey, String charset) throws Exception {

        Encrypt encrypt = encryptManager.get(encryptType);
        if (encrypt == null) {
            throw new RuntimeException("不存在算法:" + encryptType);
        }

        return encrypt.encrypt(content, encryptKey, charset);
    }

    /**
     * 解密
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String decryptContent(String content, String encryptType, String encryptKey, String charset) throws Exception {
        Encrypt encrypt = encryptManager.get(encryptType);
        if (encrypt == null) {
            throw new RuntimeException("不存在算法:" + encryptType);
        }

        return encrypt.decrypt(content, encryptKey, charset);
    }
}
