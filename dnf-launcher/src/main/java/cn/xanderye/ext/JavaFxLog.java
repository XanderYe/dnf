package cn.xanderye.ext;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

/**
 * Created on 2020/9/2.
 *
 * @author XanderYe
 */
@Slf4j
public class JavaFxLog {

    private static TextArea logArea = null;

    /**
     * 日志方法首先需要在Controller的initialize中执行
     * @param textArea
     * @return void
     * @author XanderYe
     * @date 2020/9/4
     */
    public static void initLog(TextArea textArea) {
        logArea = textArea;
    }

    /**
     * 打印日志
     * @param pattern
     * @param args
     * @return void
     * @author XanderYe
     * @date 2020/9/4
     */
    public static void log(String pattern, String...args) {
        String msg = MessageFormatter.arrayFormat(pattern, args).getMessage();
        log.info(msg);
        msg = msg + "\r\n";
        if (logArea != null) {
            String finalMsg = msg;
            Platform.runLater(() -> logArea.appendText(finalMsg));
        }
    }
}
