package cn.xanderye.util;

import java.io.*;
import java.security.interfaces.RSAPrivateKey;

/**
 * @author XanderYe
 * @description:
 * @date 2022/1/1 21:27
 */
public class DNFUtil {

    public static String createToken(long uid) throws Exception {
        String hex = String.format("%08x0101010101010101010101010101010101010101010101010101010101010101559145100" +
                "10403030101", uid);
        byte[] bytes = CodecUtil.hexStringToByteArray(hex);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("privatekey.key");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
           while ((line = br.readLine()) != null) {
               if (line.charAt(0) != '-') {
                   sb.append(line);
               }
           }
        }
        RSAPrivateKey rsaPrivateKey = RSAUtil.loadPrivateKeyByStr(sb.toString());
        byte[] tokenBytes = RSAUtil.encrypt(bytes, rsaPrivateKey);
        return CodecUtil.base64Encode(tokenBytes).replaceAll("\\s", "");
    }
}
