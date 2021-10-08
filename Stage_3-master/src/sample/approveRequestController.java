package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class approveRequestController implements Initializable {
    public Button home;
    public Button viewAll;
    public Button logout;
    public Button approveButton;
    @FXML
    private TableView<Request> tableview;
    @FXML
    private TableColumn<Request,Integer> severityColumn;
    @FXML
    private TableColumn<Request,String> typeColumn;
    @FXML
    private TableColumn<Request,String> issueColumn;
    @FXML
    private TableColumn<Request,String> locationColumn;
    @FXML
    private TableColumn<Request,String> statusColumn;

    Stage dialogStage = new Stage();
    Scene scene;

    private int id;
    private String type;
    private String issue;
    private int employee_id;
    private String location;
    private String status;
    private String severity;

    public static boolean menuBoolean = true;

    public ResultSet res;

    private ObservableList<Request> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert tableview != null;
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
        tableview.setEditable(true);
        issueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        try {
            displayAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayAll() {
        //makes data a new empty observable list that is backed by an arraylist.
        data = FXCollections.observableArrayList();
        //creates a connection to the database
        Connection conn = databaseConnection.connect();
        try {
            String sql  = "SELECT * FROM requests WHERE STATUS = 'Needs Approval'";
            //assigns the ability to create an sql statement to the variable stmt
            Statement stmt = conn.createStatement();
            //assigns the ability to execute the sql statement (get the result set) to the variable res
            res = stmt.executeQuery(sql);

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

    public void homeOnAction(ActionEvent actionEvent) throws IOException {
        menuBoolean = true;
        Stage stage = (Stage) home.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("approveRequest.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1280, 800));
        dialogStage.show();
    }

    public void viewAllOnAction() throws IOException {
        menuBoolean = true;
        Stage stage = (Stage) viewAll.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("viewAllAdmin.fxml"));
        dialogStage.setTitle("View All");
        dialogStage.setScene(new Scene(root, 1280, 800));
        dialogStage.show();
    }

    public void logoutOnAction() throws IOException {
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        dialogStage.setTitle("Log in");
        dialogStage.setScene(new Scene(root, 1280, 800));
        dialogStage.show();
    }

    public void approveOnAction(ActionEvent actionEvent) {
        ObservableList<Request> allUser,SingleUser;
        //assigns all the data in the table to the observale list allUser
        allUser=tableview.getItems();
        //assigns the data from the row selected to the observable list SingleUser
        SingleUser=tableview.getSelectionModel().getSelectedItems();
        //removes the vale of singleUser from the observable list allUser
        aprroveRequest();
        SingleUser.forEach(allUser::remove);
        //runs the method removeUser to remove them from the database
    }

    public void aprroveRequest(){
        Connection conn = databaseConnection.connect();
        try {
            Request request = tableview.getSelectionModel().getSelectedItem();
            int requestID = request.getID();
            String sql = "UPDATE requests SET STATUS = 'Active' WHERE REQUEST_ID = ?";
            //links the sql statement to the database
            PreparedStatement ps = conn.prepareStatement(sql);
            //sets the ? to the id of the row selected
            ps.setInt(1, requestID);
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

    public void onEditChanged(TableColumn.CellEditEvent<Request, String> issueStringCellEditEvent) {
        //connection to the database is established
        Connection conn = databaseConnection.connect();
        try {
            //gets the details from the row selected
            Request request = tableview.getSelectionModel().getSelectedItem();
            //assigns the value id from user class based on the row selected
            int id = request.getID();
            //assigns the new value entered into the username column to the string newUser
            String newIssue = request.setIssue(issueStringCellEditEvent.getNewValue());
            System.out.println(newIssue);
            //creates the sql statement
            String sql = "UPDATE requests SET ISSUE = ? WHERE REQUEST_ID = ?";
            //links the sql statement and the database connection
            PreparedStatement ps = conn.prepareStatement(sql);
            //assigns the value of the first ? to the data the user has just inputted
            ps.setString(1, newIssue);
            //assigns the value of the second ? to the id of the row
            ps.setInt(2, id);
            //executes the sql statement
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

    public void activeRequestsOnAction(ActionEvent actionEvent) throws IOException {
        menuBoolean = true;
        Stage stage = (Stage) home.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("adminHome.fxml"));
        dialogStage.setTitle("Home");
        dialogStage.setScene(new Scene(root, 1280, 800));
        dialogStage.show();
    }
}
