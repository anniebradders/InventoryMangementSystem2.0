package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    public static String[] userDetails;
    private static int requestID;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }
    public static String[] getUserDetails() {
        return userDetails;
    }

    public static void setUserDetails(String[] userDetails) {
        Main.userDetails = userDetails;
    }

    public static int getRequestID() { return requestID;
    }

    public static void setRequestID(int newRequestID) {
        requestID = newRequestID;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String decrypt(String password) {
        int length = password.length();
        int counter = 0;
        int previous = 0;
        String[] breakPoints = {};
        String breakChar;
        int[] intArray = {};
        String decrypted = "";
        for (int i = 0; i < length; i++) {
            breakChar = password.substring(i, i + 1);

            if (checkInt(breakChar) == -9) {

                breakPoints = Arrays.copyOf(breakPoints, breakPoints.length + 1);
                if (previous == 0) {
                    breakPoints[counter] = password.substring(previous, i);
                } else {
                    breakPoints[counter] = password.substring(previous + 1, i);
                }
                counter += 1;
                previous = i;
            }


        }
        for (int i2 = 0; i2 < breakPoints.length; i2++) {
            intArray = Arrays.copyOf(intArray, intArray.length + 1);

            if (i2 == breakPoints.length - 1) {
                intArray[i2] = checkInt(breakPoints[0]);
            } else {
                intArray[i2] = checkInt(breakPoints[i2 + 1]);
            }

        }
        char convertedChar;
        for (int i3 = 0; i3 < intArray.length; i3++) {
            intArray[i3] = intArray[i3] - i3 - 4;
            convertedChar = (char) intArray[i3];
            decrypted = decrypted + convertedChar;
        }
        return decrypted;
    }

    private static Integer checkInt(String stringConvert) {
        try {
            return Integer.parseInt(stringConvert);
        } catch (NumberFormatException e) {
            return(-9);
        }
    }
}
