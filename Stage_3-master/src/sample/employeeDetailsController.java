package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class employeeDetailsController implements Initializable {

    public static boolean menuBoolean = true;

    @FXML
    private Label employeeEmail;
    @FXML
    private Button close;
    @FXML
    private Label employeeNum;
    @FXML
    private Button home;
    @FXML
    private Button viewAll;
    @FXML
    private Button logout;

    private ResultSet res;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayDetails();
    }

    private int employee_id;

    public void displayDetails(){
        Connection conn = databaseConnection.connect();
        try{
            String sql = "SELECT EMPLOYEE_ID FROM requests WHERE REQUEST_ID = ?";
            PreparedStatement found = conn.prepareStatement(sql);
            //sets the value of ? in the sql statement to the value of the variable searching
            found.setInt(1, Main.getRequestID());
            //assigns the ability to execute the sql statement to the varuable res
            res = found.executeQuery();

            employee_id = res.getInt("EMPLOYEE_ID");

            getDetails();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                //if the database connection is not closed, it is closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void getDetails(){
        Connection conn = databaseConnection.connect();
        try{
            String sql = "SELECT EMAIL, PHONE_NUM FROM users WHERE EMPLOYEE_ID = ?";
            PreparedStatement found = conn.prepareStatement(sql);
            //sets the value of ? in the sql statement to the value of the variable searching
            found.setInt(1, employee_id);
            //assigns the ability to execute the sql statement to the varuable res
            res = found.executeQuery();

            String email = res.getString("EMAIL");
            String phone = res.getString("PHONE_NUM");

            email = Main.decrypt(email);
            phone = Main.decrypt(phone);

            employeeEmail.setText(email);
            employeeNum.setText("0" + phone);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                //if the database connection is not closed, it is closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    private final String[] choice = {"adminHome.fxml","viewAllAdmin.fxml","login.fxml","adminHome.fxml", "adminHome.fxml", "approveRequest.fxml"};
    private final String[] menu = {"View All","Log in","Home","Admin Home","Home"};

    public void homeOnAction() throws IOException {
        windowStage._Choice(home, choice[0], menu[0]);
    }

    public void viewAllOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(home, choice[1], menu[1]);
    }

    public void logoutOnAction() throws IOException {
        windowStage._Choice(home, choice[2], menu[2]);
        Main.setUserDetails(null);
    }

    public void closeOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(home, choice[3], menu[3]);
    }

    public void activeRequestsOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(home, choice[4], menu[4]);
    }
}
