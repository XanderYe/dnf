package cn.xanderye.ext;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;

import java.util.function.Function;

/**
 * @author XanderYe
 * @description: 展示对象转换，列表、下拉支持存放对象
 * @date 2021/10/28 10:28
 */
public class Converter {

    /**
     * 列表设置展示字段
     * @param listView listView对象
     * @param function lambda表达式
     * @return void
     * @author XanderYe
     * @date 2021/10/13
     */
    public static <T> void listViewConverter(ListView<T> listView, Function<T, Object> function) {
        listView.setCellFactory(param -> new ListCell<T>(){
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || function.apply(item) == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(function.apply(item)));
                }
            }
        });
    }

    /**
     * 下拉设置展示字段
     * @param comboBox comboBox对象
     * @param function lambda表达式
     * @return void
     * @author XanderYe
     * @date 2021/10/13
     */
    public static <T> void comboBoxConverter(ComboBox<T> comboBox, Function<T, Object> function) {
        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T object) {
                if (object != null) {
                    return String.valueOf(function.apply(object));
                }
                return null;
            }
            @Override
            public T fromString(String string) {
                for (T item : comboBox.getItems()) {
                    if (item != null && string.equals(function.apply(item))) {
                        return item;
                    }
                }
                return null;
            }
        });
    }

    /**
     * 字符串设置下拉默认值对象
     * @param comboBox
     * @param function
     * @param value
     * @return void
     * @author XanderYe
     * @date 2021/11/23
     */
    public static <T> void setComboValue(ComboBox<T> comboBox, Function<T, Object> function, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        ObservableList<T> observableList = comboBox.getItems();
        for (T t : observableList) {
            if (value.equals(function.apply(t))) {
                comboBox.setValue(t);
                break;
            }
        }
    }
}
