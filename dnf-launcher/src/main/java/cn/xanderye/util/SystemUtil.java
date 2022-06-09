package cn.xanderye.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created on 2020/11/5.
 *
 * @author XanderYe
 */
@Slf4j
public class SystemUtil {

    /**
     * 空格
     */
    public static final String BREAK = " ";
    /**
     * Tab
     */
    public static final String TAB = "    ";
    /**
     * Windows换行符
     */
    public static final String WINDOWS_LINE_BREAK = "\r\n";
    /**
     * UNIX换行符
     */
    public static final String UNIX_LINE_BREAK = "\r";

    /**
     * 注册表反馈
     */
    public static final String[] REG_RESULT = new String[]{"操作成功完成。"};

    /**
     * Ip正则
     */
    public static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

    /**
     * 本地Ip
     */
    public static final String LOCALHOST_IP = "127.0.0.1";


    /**
     * 调用cmd方法，默认GBK编码
     * @param cmdStr
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static String execStr(String cmdStr) {
        return execStr(getCharset(), cmdStr);
    }

    /**
     * 异步掉用cmd方法，默认GBK编码
     * @param consumer 自由实现日志打印
     * @param cmdStr
     * @return void
     * @author XanderYe
     * @date 2021/4/18
     */
    public static void execStrAsync(Consumer<String> consumer, String cmdStr) {
        execStrAsync(getCharset(), consumer, cmdStr);
    }

    /**
     * 调用cmd方法，默认GBK编码
     * @param cmds
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static String execStr(String...cmds) {
        return execStr(getCharset(), cmds);
    }

    /**
     * 异步调用cmd方法，默认GBK编码
     * @param cmds
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static void execStrAsync(Consumer<String> consumer, String...cmds) {
        execStrAsync(getCharset(), consumer, cmds);
    }


    /**
     * 调用cmd方法，同步返回结果
     * @param charset
     * @param cmds
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static String execStr(Charset charset, String...cmds) {
        if (1 == cmds.length) {
            if (cmds[0] == null || "".equals(cmds[0])) {
                throw new RuntimeException("Empty command !");
            }
            cmds = cmds[0].split(BREAK);
        }
        Process process = null;
        try {
            process = new ProcessBuilder(cmds).redirectErrorStream(true).start();
            InputStream is = process.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is, charset));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = buffer.readLine()) != null) {
                sb.append(line).append(getLineBreak());
            }
            is.close();
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
        return null;
    }

    /**
     * 调用cmd，异步打印结果
     * @param charset
     * @param consumer 自由实现日志打印
     * @param cmds
     * @return void
     * @author XanderYe
     * @date 2021/4/18
     */
    public static void execStrAsync(Charset charset, Consumer<String> consumer, String...cmds) {
        if (1 == cmds.length) {
            if (cmds[0] == null || "".equals(cmds[0])) {
                throw new RuntimeException("Empty command !");
            }
            cmds = cmds[0].split(BREAK);
        }
        try {
            Process process = new ProcessBuilder(cmds).redirectErrorStream(true).start();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    InputStream is = process.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(is, charset));
                    String line;
                    while ((line = buffer.readLine()) != null) {
                        if (consumer != null) {
                            consumer.accept(line);
                        } else {
                            // 默认打印日志
                            log.info(line);
                        }
                    }
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    process.destroy();
                }
            });
            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取系统Ip地址
     * @param
     * @return java.util.List<java.lang.String>
     * @author XanderYe
     * @date 2020/12/18
     */
    public static List<String> getSystemIp() {
        if (isWindows()) {
            return getWindowsIp();
        } else {
            return getLinuxIp();
        }
    }

    /**
     * 获取Windows下的Ip地址
     * @param
     * @return java.util.List<java.lang.String>
     * @author XanderYe
     * @date 2020/12/18
     */
    private static List<String> getWindowsIp() {
        String res = execStr("ipconfig");
        List<String> rowList = Arrays.asList(res.split(getLineBreak()));
        List<String> ipList = new ArrayList<>();
        for (String string : rowList) {
            if(string.contains("IPv4 地址") || string.contains("IPv4 Address")){
                Matcher mc = IP_PATTERN.matcher(string);
                if(mc.find()){
                    ipList.add(mc.group());
                }
            }
        }
        return ipList;
    }

    /**
     * 获取Linux下的Ip地址
     * @param
     * @return java.util.List<java.lang.String>
     * @author XanderYe
     * @date 2020/12/18
     */
    private static List<String> getLinuxIp() {
        String res = execStr("ip addr");
        List<String> rowList = Arrays.asList(res.split(getLineBreak()));
        List<String> ipList = new ArrayList<>();
        for (String string : rowList) {
            if(string.contains("inet")){
                Matcher mc = IP_PATTERN.matcher(string);
                if(mc.find()){
                    String ip = mc.group();
                    if (!LOCALHOST_IP.equals(ip)) {
                        ipList.add(mc.group());
                    }
                }
            }
        }
        return ipList;
    }

    /**
     * 获取CpuId
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2021/1/22
     */
    public static List<String> getCpuId() {
        if (isWindows()) {
            return getWindowsCpuId();
        } else {
            return getLinuxCpuId();
        }
    }

    /**
     * Windows下获取CpuId
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2021/1/22
     */
    private static List<String> getWindowsCpuId() {
        String res = SystemUtil.execStr("wmic", "cpu", "get", "ProcessorId");
        String[] strs = res.split("(\r|\n|\r\n)");
        return Arrays.stream(strs)
                .filter(str -> !str.contains("ProcessorId") && str.length() > 0)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Linux下获取CpuId
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2021/1/22
     */
    private static List<String> getLinuxCpuId() {
        String res = execStr("sh", "-c", "dmidecode -t processor | grep 'ID'");
        String[] strs = res.split("(\r|\n|\r\n)");
        return Arrays.stream(strs)
                .map(str -> res.substring(res.indexOf(":") + 1).replace(" ", "").trim())
                .collect(Collectors.toList());
    }

    /**
     * 增加注册表
     * @param path 注册表路径
     * @param key 键
     * @param type 类型 常用 REG_SZ/REG_DWORD
     * @param value 值
     * @return boolean
     * @author XanderYe
     * @date 2020/11/15
     */
    public static boolean addReg(String path, String key, String type, String value) {
        String command = "reg add \"" + path + "\" /v " + key + " /t " + type + " /d " + value + " /f";
        String res = execStr(command).replaceAll("[\\r|\\n|\\\\s]", "");
        return Arrays.asList(REG_RESULT).contains(res);
    }

    /**
     * 删除注册表
     * @param path 注册表路径
     * @param key 键
     * @return boolean
     * @author XanderYe
     * @date 2020/11/15
     */
    public static boolean deleteReg(String path, String key) {
        String command = "reg delete \"" + path + "\" /v " + key + " /f";
        String res = execStr(command).replaceAll("[\\r|\\n|\\\\s]", "");
        return Arrays.asList(REG_RESULT).contains(res);
    }

    /**
     * 判断系统环境
     * @param
     * @return boolean
     * @author XanderYe
     * @date 2020/11/5
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 获取系统字符编码
     * @param
     * @return java.nio.charset.Charset
     * @author XanderYe
     * @date 2020/11/5
     */
    public static Charset getCharset() {
        return isWindows() ? Charset.forName("GBK") : Charset.defaultCharset();
    }

    /**
     * 获取系统换行符
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static String getLineBreak() {
        return isWindows() ? WINDOWS_LINE_BREAK : UNIX_LINE_BREAK;
    }
}
