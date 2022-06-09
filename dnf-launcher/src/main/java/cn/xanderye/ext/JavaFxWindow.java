package cn.xanderye.ext;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * @author XanderYe
 * @description: 窗口操作
 * @date 2022/3/12 20:39
 */
public class JavaFxWindow {

    /**
     * 初始化系统托盘
     * @param url
     * @param toolTips
     * @param menuItems
     * @param mouseListener
     * @return void
     * @author XanderYe
     * @date 2022/3/12
     */
    public static void initTray(URL url, String toolTips, MenuItem[] menuItems, MouseListener mouseListener) {
        if (!SystemTray.isSupported()) {
            throw new RuntimeException("System does not supported tray.");
        }
        // 关闭最后一个窗口不结束程序
        Platform.setImplicitExit(false);
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);
        SystemTray tray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();
        for (MenuItem menuItem : menuItems) {
            popup.add(menuItem);
        }
        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip(toolTips);
        if (mouseListener != null) {
            trayIcon.addMouseListener(mouseListener);
        }
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示窗口
     * @param stage
     * @return void
     * @author XanderYe
     * @date 2022/3/12
     */
    public static void showStage(Stage stage) {
        Platform.runLater(() -> {
            if (stage.isIconified()) {
                stage.setIconified(false);
            }
            if (!stage.isShowing()) {
                stage.show();
            }
            stage.toFront();
        });
    }
}
