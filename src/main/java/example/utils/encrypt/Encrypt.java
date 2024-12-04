package example.utils.encrypt;

/**
 * @author QingBinGuo
 * @date 2023/2/16 17:49
 */
public interface Encrypt {
    String encrypt(String content, String aesKey, String charset) throws Exception;

    String decrypt(String content, String key, String charset) throws Exception;
}
