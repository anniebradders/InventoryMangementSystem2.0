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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class adminHomeController implements Initializable {

    public Button viewEmployee;


    @FXML
    private Button home;
    @FXML
    private Button viewAll;
    @FXML
    private Button logout;
    @FXML
    private Button search;
    @FXML
    private TextField searchBar;

    @FXML
    private TableView<Request> tableview;

    @FXML
    private TableColumn<Request, String> employee_idColumn;
    @FXML
    private TableColumn<Request, String> typeColumn;
    @FXML
    private TableColumn<Request, String> issueColumn;
    @FXML
    private TableColumn<Request, String> locationColumn;
    @FXML
    private TableColumn<Request, String> statusColumn;
    @FXML
    private TableColumn<Request,String> severityColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert tableview != null;
        //the column labelled Username is set the value of username within the user class
        //the column labelled Password is set the value of password within the user class
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
            displayAll();
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
            sql = "SELECT * FROM requests WHERE REQUEST_ID = ?";
            //runs the method viewProducts
            displayAll();
        }
    }

    private String sql  = "SELECT * FROM requests WHERE STATUS = 'Active'";
    //initialises string searching
    private static String searching = null;
    //initialises string boolean
    private static boolean menuBoolean = true;
    private ResultSet res;

    public void displayAll() {
        //makes data a new empty observable list that is backed by an arraylist.
        ObservableList<Request> data = FXCollections.observableArrayList();
        //creates a connection to the database
        Connection conn = databaseConnection.connect();
        try {
            if(menuBoolean) {
                //assigns the ability to create an sql statement to the variable stmt
                Statement stmt = conn.createStatement();
                //assigns the ability to execute the sql statement (get the result set) to the variable res
                res = stmt.executeQuery(sql);
                //if b is equal to false the products are ordered in a certain way depending on the sql statement
            }if(!menuBoolean){
                //assigns the ability to prepare an sql statement to the variable found
                PreparedStatement found = conn.prepareStatement(sql);
                //sets the value of ? in the sql statement to the value of the variable searching
                found.setString(1, searching);
                //assigns the ability to execute the sql statement to the varuable res
                res = found.executeQuery();
            }
            while (res.next()) {
                //a new user is created based off thr class user
                data.addAll(new Request(res.getInt("REQUEST_ID"), res.getString("TYPE"),
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

    private static final String[] optionFile = {"approveRequest.fxml", "viewAllAdmin.fxml", "login.fxml", "employeeDetails.fxml", "adminHome.fxml"};
    private static final String[] setTitle = {"Home", "View All", "Login in", "Employee details", "View Active Requests", };

    public void homeOnAction() throws IOException  {
        menuBoolean = true;
        windowStage._Choice(home, optionFile[0], setTitle[0]);
    }
    public void viewAllOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(viewAll, optionFile[1], setTitle[1]);
    }
    public void logoutOnAction() throws IOException {
        windowStage._Choice(logout, optionFile[2], setTitle[2]);
    }
    public void viewEmployeeOnAction() {
        displayEmployeeID();
    }
    public void displayEmployeeID() {
        menuBoolean = true;
        windowStage._DisplayEmployeeID(home, tableview, optionFile[3], setTitle[3]);
    }
    public void activeRequestsOnAction(){
        menuBoolean = true;
        windowStage._DisplayEmployeeID(home, tableview, optionFile[4], setTitle[4]);
    }

    public void solveIssue(String statusInsert){
        Connection conn = databaseConnection.connect();
        try {
            String sql = "UPDATE requests SET STATUS = '" + statusInsert + "' WHERE REQUEST_ID = ?";
            //links the sql statement to the database
            PreparedStatement ps = conn.prepareStatement(sql);
            //sets the ? to the id of the row selected
            ps.setInt(1, Main.getRequestID());
            //executes the sql statement
            ps.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                //if the database connection isn't closed, its closed here
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private final String[] choiceStatus = {"Solved", "Ongoing"};
    public void solvedOnAction() {
        solvedOrOnGoingOnAction(choiceStatus[0]);
    }
    public void onGoingOnAction(){ solvedOrOnGoingOnAction(choiceStatus[1]); }

    public void solvedOrOnGoingOnAction(String option){
        ObservableList<Request> allRequest, SingleRequest;
        //assigns all the data in the table to the observale list allUser
        allRequest = tableview.getItems();
        //assigns the data from the row selected to the observable list SingleUser
        SingleRequest = tableview.getSelectionModel().getSelectedItems();
        //removes the vale of singleUser from the observable list allUser
        solveIssue(option);
        SingleRequest.forEach(allRequest::remove);
    }

}
