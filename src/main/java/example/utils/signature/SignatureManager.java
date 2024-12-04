package example.utils.signature;

import example.utils.signature.service.impl.RSA2Signature;
import example.utils.signature.service.impl.RSASignature;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author QingBinGuo
 * @date 2023/1/9 10:13
 */
public class SignatureManager {
    private static final Map<String, ISignature> SIGNATURE_MAP = new ConcurrentHashMap<>();

    static {
        RSASignature rsaSignature = new RSASignature();
        RSA2Signature rsa2Signature = new RSA2Signature();
        SIGNATURE_MAP.put(rsaSignature.getAsymmetricType(), rsaSignature);
        SIGNATURE_MAP.put(rsa2Signature.getAsymmetricType(), rsa2Signature);
    }

    private static boolean isAssignableFrom(Class<?> source, Class<?> clazz) {
        if (Object.class.equals(source)) {
            return false;
        }
        if (clazz.isAssignableFrom(source)) {
            return true;
        }
        List<Class<?>> classList = new ArrayList<>();
        classList.addAll(Arrays.asList(source.getInterfaces()));
        classList.addAll(Arrays.asList(source.getSuperclass().getInterfaces()));
        classList.add(source.getSuperclass());
        for (Class<?> cl : classList) {
            if (isAssignableFrom(cl, clazz)) {
                return true;
            }
        }
        return false;
    }

    public static ISignature getInstance(String signType) {
        ISignature signature = SIGNATURE_MAP.get(signType);
        if (signature == null) {
            throw new RuntimeException(MessageFormat.format("不支持的签名类型,signType:{0}", signType));
        }
        return signature;
    }
}
