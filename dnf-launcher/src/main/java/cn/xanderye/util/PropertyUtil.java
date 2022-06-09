package cn.xanderye.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 配置工具类
 *
 * @author XanderYe
 * @date 2020/3/15
 */
public class PropertyUtil {

    private static Properties properties = null;

    private static String filePath = null;

    /**
     * 初始化
     *
     * @return void
     * @author XanderYe
     * @date 2020-03-15
     */
    public static void init() {
        init(null);
    }

    /**
     * 初始化
     *
     * @param url
     * @return void
     * @author XanderYe
     * @date 2020-03-15
     */
    public static void init(String url) {
        filePath = url == null ? System.getProperty("user.dir") + File.separator + "config.properties" : url;
        File file = new File(filePath);
        FileInputStream fis = null;
        InputStreamReader inputStreamReader = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fis = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取值
     *
     * @param key
     * @return java.lang.String
     * @author XanderYe
     * @date 2020-03-15
     */
    public static String get(String key) {
        if (properties == null) {
            return null;
        }
        return properties.getProperty(key);
    }

    /**
     * 重写保存方法，不转义
     *
     * @param key
     * @param value
     * @return void
     * @author XanderYe
     * @date 2020-03-15
     */
    public static synchronized void save(String key, String value) {
        save(key, value, null);
    }

    /**
     * 重写保存方法，不转义
     *
     * @param key
     * @param value
     * @param comment
     * @return void
     * @author XanderYe
     * @date 2020-03-15
     */
    public static synchronized void save(String key, String value, String comment) {
        if (properties != null) {
            if (properties.getProperty(key) == null) {
                append(key, value, comment);
            } else {
                rewrite(key, value, comment);
            }
            properties.setProperty(key, value);
        }
    }

    /**
     * 追加文件
     *
     * @param key
     * @param value
     * @param comment
     * @return void
     * @author XanderYe
     * @date 2020-03-15
     */
    private static void append(String key, String value, String comment) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(new File(filePath), true);
            bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            if (comment != null) {
                bw.write("#" + comment);
                bw.newLine();
            }
            bw.write(key + "=" + value);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重写文件
     *
     * @param key
     * @param value
     * @param comment
     * @return void
     * @author XanderYe
     * @date 2020-03-15
     */
    private static void rewrite(String key, String value, String comment) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(new File(filePath));
            bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            Enumeration<?> enumeration = properties.keys();
            while (enumeration.hasMoreElements()) {
                String k = (String) enumeration.nextElement();
                String val = properties.getProperty(k);
                if (k.equals(key)) {
                    if (comment != null) {
                        bw.write("#" + comment);
                        bw.newLine();
                    }
                    bw.write(key + "=" + value);
                } else {
                    bw.write(k + "=" + val);
                }
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
