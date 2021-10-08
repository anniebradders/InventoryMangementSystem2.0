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
import java.sql.*;
import java.util.ResourceBundle;

public class viewAllAdminController implements Initializable {

    Stage dialogStage = new Stage();

    @FXML
    public Button home;
    @FXML
    public Button viewAll;
    @FXML
    public Button logout;
    @FXML
    public Button search;
    @FXML
    public TextField searchBar;


    private ObservableList<Request> data;


    public static String searching = null;
    //initialises string boolean
    public static boolean menuBoolean = true;
    public ResultSet res;

    @FXML
    private TableView<Request> tableview;
    @FXML
    private TableColumn<Request,String> severityColumn;
    @FXML
    private TableColumn<Request,String> typeColumn;
    @FXML
    private TableColumn<Request,String> issueColumn;
    @FXML
    private TableColumn<Request,String> locationColumn;
    @FXML
    private TableColumn<Request,String> statusColumn;

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
            displayAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sql = "SELECT * FROM requests";

    public void displayAll() {
        //makes data a new empty observable list that is backed by an arraylist.
        data = FXCollections.observableArrayList();
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
                //assigns the ability to parepare an sql statement to the variable found
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
    private final String[] choice = {"adminHome.fxml","viewAllAdmin.fxml","login.fxml", "approveRequest.fxml"};
    private final String[] menu = {"Home","View All","Log in", "Home"};

    public void homeOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(home ,choice[0], menu[0]);
    }
    public void viewAllOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(home ,choice[1], menu[1]);
    }
    public void logoutOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(home ,choice[2], menu[2]);
        Main.setUserDetails(null);
    }

    public void activeRequestsOnAction() throws IOException {
        menuBoolean = true;
        windowStage._Choice(home ,choice[3], menu[3]);
    }

    public void activeOnAction(){
        sql = "SELECT * FROM requests WHERE STATUS = 'Active'";
        displayAll();
    }

    public void deactiveOnAction(){
        sql = "SELECT * FROM requests WHERE STATUS = 'Solved'";
        displayAll();
    }
}
