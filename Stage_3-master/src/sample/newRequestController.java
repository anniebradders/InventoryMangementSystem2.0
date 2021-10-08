package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static sample.Main.userDetails;


public class newRequestController {

    Stage dialogStage = new Stage();


    @FXML
    private Button home;
    @FXML
    private Button newRequest;
    @FXML
    private Button logout;
    @FXML
    private MenuItem hardware;
    @FXML
    private MenuItem software;
    @FXML
    private MenuItem comm;
    @FXML
    private MenuItem general;
    @FXML
    private MenuItem security;
    @FXML
    private MenuItem other;
    @FXML
    private TextArea details;
    @FXML
    private TextField lol;
    @FXML
    private Button newReq;

    public String type;

    public String hardwareOnAction(){
        type = "Hardware";
        return type;
    }

    public String softwareOnAction(){
        type = "Software";
        return type;
    }

    public String commOnAction(){
        type = "Communications";
        return type;
    }

    public String generalOnAction(){
        type = "General";
        return type;
    }

    public String secOnAction(){
        type = "Security";
        return type;
    }

    public String otherOnAction(){
        type = "Other";
        return type;
    }

    public void newProduct(String[] userDetails) throws IOException {
        //sets up the database connection
        Connection conn = databaseConnection.connect();
        String loc = lol.getText();
        try {
            //the sql string to add a new product
            String sql = "INSERT INTO requests(TYPE,ISSUE,EMPLOYEE_ID,LOCATION,STATUS,SEVERITY) VALUES(?,?,?,?,?,?)";
            //prepares the sql statement by linking it to the database
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //if the values are the correct data types than they are added to the database
            pstmt.setString(1,type);
            pstmt.setString(2, details.getText());
            pstmt.setInt(3, Integer.parseInt(userDetails[0]));
            pstmt.setString(4, loc);
            pstmt.setString(5, "Needs Approval");
            pstmt.setString(6, severity);

            pstmt.executeUpdate();

        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                //if the database connection isn't closed than it is closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            homeOnAction();
        }
    }

    private final String[] choice = {"homeEmployee.fxml","newRequest.fxml","login.fxml"};
    private final String[] menu = {"Home","New Request","Logout"};

    public void homeOnAction() throws IOException {
        windowStage._Choice(home, choice[0], menu[0]);
    }

    public void newRequestOnAction() throws IOException {
        windowStage._Choice(home, choice[1], menu[1]);
    }

    public void logoutOnAction() throws IOException {
        windowStage._Choice(home, choice[2], menu[2]);
        Main.setUserDetails(null);
    }

    public void newReqOnAction() throws IOException {
        newProduct(Main.getUserDetails());
    }

    public String severity = "Medium";

    public String criticalOnAction(ActionEvent actionEvent) {
        severity = "Critical";
        return severity;
    }

    public String highOnAction(ActionEvent actionEvent) {
        severity = "High";
        return severity;
    }

    public String medOnAction(ActionEvent actionEvent) {
        severity = "Medium";
        return severity;
    }

    public String lowOnAction(ActionEvent actionEvent) {
        severity = "Low";
        return severity;
    }
}
