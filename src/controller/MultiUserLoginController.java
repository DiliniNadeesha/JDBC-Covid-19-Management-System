package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MultiUserLoginController {
    public AnchorPane root;
    public JFXTextField txtUsername;
    public JFXPasswordField txtPassword;
    public JFXButton btnLogin;

    public void btnLogin_OnAction(ActionEvent event) {

        try {
            PreparedStatement pstm= DBConnection.getInstance().getConnection().prepareStatement("SELECT username,password,userRole FROM user WHERE username=? AND password=?");
            pstm.setString(1,txtUsername.getText());
            pstm.setString(2,txtPassword.getText());
            ResultSet rst = pstm.executeQuery();
            if (rst.next()){
                String username = rst.getString(1);
                String password = rst.getString(2);
                String role = rst.getString(3);

                System.out.println(username+password+role);

                login(role);
            }
            else {
                new Alert(Alert.AlertType.ERROR,"Invalid Username or Password").show();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void login(String role) throws IOException {

        Parent root = null;

        FXMLLoader fxmlLoader = null;

        switch (role) {
            case "Admin":
                URL url=this.getClass().getResource("/view/Dashboard.fxml");
                Parent parent = FXMLLoader.load(url);
                Scene scene =new Scene(parent);
                Stage stage = (Stage) this.root.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Admin");
                stage.setFullScreen(false);
                break;

            case "Hospital-IT":
                URL url2 =this.getClass().getResource("/view/ManageHospital.fxml");
                Parent parent2 = FXMLLoader.load(url2);
                Scene scene2 =new Scene(parent2);
                Stage stage2 = (Stage) this.root.getScene().getWindow();
                stage2.setScene(scene2);
                stage2.setTitle("Hospital-IT");
                stage2.setFullScreen(false);
                break;

            case "QuarantineCenter-IT":
                URL url3 = this.getClass().getResource("/view/ManageQuarantineCenters.fxml");
                Parent parent3 = FXMLLoader.load(url3);
                Scene scene3 = new Scene(parent3);
                Stage stage3 = (Stage) this.root.getScene().getWindow();
                stage3.setScene(scene3);
                stage3.setTitle("QuarantineCenter-IT");
                stage3.setFullScreen(false);
                break;

            case "PSTF":
                URL url4 = this.getClass().getResource("/view/Dashboard.fxml");
                Parent parent4 = FXMLLoader.load(url4);
                Scene scene4 = new Scene(parent4);
                Stage stage4 = (Stage) this.root.getScene().getWindow();
                stage4.setScene(scene4);
                stage4.setTitle("PSTF");
                stage4.setFullScreen(false);
                break;
        }
    }
}
