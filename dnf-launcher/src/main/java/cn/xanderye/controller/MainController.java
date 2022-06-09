package cn.xanderye.controller;

import cn.xanderye.control.NumberTextField;
import cn.xanderye.ext.Dialog;
import cn.xanderye.util.DNFUtil;
import cn.xanderye.util.PropertyUtil;
import cn.xanderye.util.SystemUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author XanderYe
 * @date 2020/2/6
 */
@Slf4j
public class MainController implements Initializable {

    @FXML
    private NumberTextField uidText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PropertyUtil.init();
        String uidStr = PropertyUtil.get("uid");
        if (StringUtils.isNotEmpty(uidStr)) {
            uidText.setText(uidStr);
        }
    }

    public void start() {
        String uidStr = uidText.getText();
        try {
            long uid = Long.parseLong(uidStr);
            if (uid < 18000000) {
                throw new NumberFormatException();
            }
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    String token = DNFUtil.createToken(uid);
                    System.out.println(token);
                    SystemUtil.execStrAsync(s -> {}, "DNF.exe", token);
                    PropertyUtil.save("uid", uidStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage(), e);
                    Dialog.errorDialog("错误", "请检查DNF.exe路径");
                }
            });
            executorService.shutdown();
        } catch (NumberFormatException e) {
            Dialog.errorDialog("错误", "请输入正确的uid");
        }
    }
}
