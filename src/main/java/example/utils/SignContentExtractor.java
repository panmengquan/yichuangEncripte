package example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author QingBinGuo
 * @date 2023/2/16 10:07
 */
public class SignContentExtractor {

    //把请求报文
    public static <T> String getSignContent(T obj) {
        Map<String, String> sortedParams = (new ObjectMapper()).convertValue(obj, new TypeReference<Map<String, String>>() {
        });
        sortedParams.remove("sign");

        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (String key : keys) {
            String value = sortedParams.get(key);
            if (StringUtils.isNoneBlank(key, value)) {
                content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
                index++;
            }
        }
        return content.toString();
    }
}
