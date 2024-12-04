package example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletException;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.aliyun.fc.runtime.HttpRequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import example.domain.ApiRequest;
import example.utils.encrypt.EncryptManager;

/**
 * Hello world!
 *
 */
public class App implements HttpRequestHandler, FunctionInitializer {

    public void initialize(Context context) throws IOException {
        //TODO
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws IOException, ServletException {
        String requestPath = (String) request.getAttribute("FC_REQUEST_PATH");
        String requestURI = (String) request.getAttribute("FC_REQUEST_URI");
        String requestClientIP = (String) request.getAttribute("FC_REQUEST_CLIENT_IP");

        String requestParms = "";
        SimpleResponse simpleResponse = new SimpleResponse();

        try {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                requestParms = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                if (requestParms.isEmpty()) return;

                simpleResponse.setCode(200);
                simpleResponse.setMessage("请求成功");

                ObjectMapper objectMapper = new ObjectMapper();

                JSONObject responseObject = new JSONObject();
                EncryptQuestParams paramsBean = objectMapper.readValue(requestParms, EncryptQuestParams.class);
                if (paramsBean.getType().equals("加密")){
                    ApiRequest signRequest = CommonUtils.getMySign(paramsBean.getData());
                    responseObject.put("sign",signRequest.getSign());
                    responseObject.put("biz_content",signRequest.getBizContent());
                }else {
                    responseObject.put("data",CommonUtils.decryptParam(paramsBean.getData()));
                }
                simpleResponse.setData(responseObject);
                OutputStream out = response.getOutputStream();
                out.write((new Gson().toJson(simpleResponse)).getBytes());
                out.flush();
                out.close();

            }

        } catch (Exception e) {
            simpleResponse.setMessage(e.toString());
            OutputStream out = response.getOutputStream();
            out.write((new Gson().toJson(simpleResponse)).getBytes());
            out.flush();
            out.close();
        }
    }

}
