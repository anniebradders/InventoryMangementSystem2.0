package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class windowStage {

    private static Stage dialogStage = new Stage();

    public static void OnAction(String choiceClass, String title) throws IOException {
        Parent root = FXMLLoader.load(windowStage.class.getResource(choiceClass));
        dialogStage.setTitle(title);
        dialogStage.setScene(new Scene(root, 1280, 800));
        dialogStage.show();
    }
    public static void _Choice(Button button, String opt1, String opt2) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        OnAction(opt1, opt2);
    }

    public static void _DisplayEmployeeID(Button button, TableView<Request> tableview, String opt1, String opt2) {
        try {
            Main.setRequestID(tableview.getSelectionModel().getSelectedItem().getID());
            _Choice(button, opt1, opt2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select a request before viewing employee details!\n\n");
        }
    }
}
