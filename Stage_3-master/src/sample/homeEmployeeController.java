package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class homeEmployeeController implements Initializable {

    Stage dialogStage = new Stage();

    @FXML
    private Button home;
    @FXML
    private Button logout;
    @FXML
    private Button search;
    @FXML
    private TextField searchBar;
    @FXML
    private Button newRequest;


    private String sql  = "SELECT * FROM requests WHERE EMPLOYEE_ID = ?";
    //initialises string searching
    private static String searching = null;
    //initialises string boolean
    private static boolean menuBoolean = true;
    private ResultSet res;


    @FXML
    private TableView<Request> tableview;
    @FXML
    private TableColumn<Request,String> severityColumn;
    @FXML
    private TableColumn<Request, String> typeColumn;
    @FXML
    private TableColumn<Request, String> issueColumn;
    @FXML
    private TableColumn<Request, String> locationColumn;
    @FXML
    private TableColumn<Request, String> statusColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert tableview != null;
        //the column labelled Username is set the value of username within the user class
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        //the column labelled Role is set the value of role within the user class
        issueColumn.setCellValueFactory(
                new PropertyValueFactory<>("issue"));
        severityColumn.setCellValueFactory(
                new PropertyValueFactory<>("severity"));
        locationColumn.setCellValueFactory(
                new PropertyValueFactory<>("location"));
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        try {
            displayAll(Main.getUserDetails());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchOnAction(){
        tableview.getItems().clear();
        menuBoolean = false;
        //if the textfield searchProduct is not blank
        if(!searchBar.getText().isBlank()) {
            //gets the text from the search bar
            searching = searchBar.getText();
            //creates a new sql statement
            sql = "SELECT * FROM requests WHERE REQUEST_ID = ? AND EMPLOYEE_ID = ?";
            //runs the method viewProducts
            displayAll(Main.getUserDetails());
        }
    }

    public void displayAll(String[] userDetails) {
        //makes data a new empty observable list that is backed by an arraylist.
        ObservableList<Request> data = FXCollections.observableArrayList();
        //creates a connection to the database
        Connection conn = databaseConnection.connect();
        try {
            if(menuBoolean) {
                //assigns the ability to create an sql statement to the variable stmt
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, userDetails[0]);
                //assigns the ability to execute the sql statement (get the result set) to the variable res
                res = stmt.executeQuery();
                //if b is equal to false the products are ordered in a certain way depending on the sql statement
            }if(!menuBoolean){
                //assigns the ability to parepare an sql statement to the variable found
                PreparedStatement found = conn.prepareStatement(sql);
                //sets the value of ? in the sql statement to the value of the variable searching
                found.setString(1, searching);
                found.setString(2, userDetails[0]);
                //assigns the ability to execute the sql statement to the varuable res
                res = found.executeQuery();
            }
            while (res.next()) {
                //user is added to the observable list data
                data.add(new Request(res.getInt("REQUEST_ID"), res.getString("TYPE"),
                        res.getString("ISSUE"), res.getInt("EMPLOYEE_ID"),
                        res.getString("LOCATION"), res.getString("STATUS"), res.getString("SEVERITY")));

            }
            //sets the item within the correct row and column in the database

            tableview.setItems(data);
        } catch (
                SQLException e) {
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

    public void activeOnAction(){
        menuBoolean = true;
        sql = "SELECT * FROM requests WHERE STATUS = 'Active' AND EMPLOYEE_ID = ?";
        displayAll(Main.getUserDetails());
    }

    public void deactiveOnAction(){
        menuBoolean = true;
        sql = "SELECT * FROM requests WHERE STATUS = 'Solved' AND EMPLOYEE_ID = ?";
        displayAll(Main.getUserDetails());
    }
    private final String[] optionFile = {"HomeEmployee.fxml","newRequest.fxml", "login.fxml", "approveRequest.fxml"};
    private final String[] setTitle = {"Home", "New Request", "Login in", "Home"};

    public void homeOnAction() throws IOException {
        windowStage._Choice(home, optionFile[0], setTitle[0]);
    }
    public void newRequestOnAction() throws IOException {
        windowStage._Choice(newRequest, optionFile[1], setTitle[1]);
    }
    public void logoutOnAction() throws IOException {
        windowStage._Choice(logout, optionFile[2], setTitle[2]);
        Main.setUserDetails(null);
    }

    public void approveRequestOnAction() throws IOException {
        windowStage._Choice(newRequest, optionFile[3], setTitle[3]);
    }
}
