package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Controller {

    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Label numAttempt;

    private static int attempt = 0;
    private static String dataUsername = null;
    private static String dataPassword = null;
    private static String dataRole = null;
    private static int dataEmployee_ID;

    Stage dialogStage = new Stage();

    @FXML
    public void loginOnAction() {
        if (!username.getText().isBlank() && !password.getText().isBlank()) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Incorrect Username and/or Password");
        }
    }

    public void validateLogin() {
        Connection conn = databaseConnection.connect();
        Parent root;
        try {
            String user = username.getText();
            String pass = password.getText();
            String sql = "SELECT EMPLOYEE_ID, USERNAME, PASSWORD, ROLE FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean success = false;
            while (rs.next()) {
                dataEmployee_ID = rs.getInt("EMPLOYEE_ID");
                dataUsername = rs.getString("USERNAME");
                dataPassword = rs.getString("PASSWORD");
                dataRole = rs.getString("ROLE");
                dataUsername = Main.decrypt(dataUsername);
                dataPassword = Main.decrypt(dataPassword);
                if (dataUsername.equals(user) && dataPassword.equals(pass)) {
                    Main.setUserDetails(new String[]{String.valueOf(dataEmployee_ID), user, pass, dataRole});
                    success = true;
                    Stage stage = (Stage) login.getScene().getWindow();
                    stage.close();
                    if (dataRole.equals("Admin")) {
                        root = FXMLLoader.load(getClass().getResource("approveRequest.fxml"));
                    } else {
                        root = FXMLLoader.load(getClass().getResource("homeEmployee.fxml"));
                    }
                    dialogStage.setTitle("Home");
                    dialogStage.setScene(new Scene(root, 1280, 800));
                    dialogStage.show();
                }
                if (success) {
                    break;
                }
            }
            if (!success) {
                attempt += 1;
                if (attempt == 3) {
                    System.exit(0); //WANT TO GET RID OF
                } else {
                    loginMessageLabel.setText("Username and/or Password Incorrect");
                    numAttempt.setText((3 - attempt) + " more attempt(s)");
                }
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void homeOnAction() { }

    public void logoutOnAction() { }


}


