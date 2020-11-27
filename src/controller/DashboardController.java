package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public AnchorPane dashboard;
    public Label lblMenu;
    public Label lblDescription;
    public ImageView imgGlobal;
    public ImageView imgHospital;
    public ImageView imgQuarantineCenter;
    public ImageView imgUser;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), dashboard);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void playMouseExitAnimation(MouseEvent event) {

        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }

    public void playMouseEnterAnimation(MouseEvent event) {

        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgGlobal":
                    lblMenu.setText("Manage Global COVID-19 Data");
                    lblDescription.setText("Click to add, edit, delete, search or view Global COVID-19 Data");
                    break;
                case "imgHospital":
                    lblMenu.setText("Manage Hospitals");
                    lblDescription.setText("Click to add, edit, delete, search or view Hospitals");
                    break;
                case "imgQuarantineCenter":
                    lblMenu.setText("Manage Quarantine Centers");
                    lblDescription.setText("Click to add, edit, delete, search or view Quarantine Centers");
                    break;
                case "imgUser":
                    lblMenu.setText("Manage Users");
                    lblDescription.setText("Click to add, edit, delete, search or view Users");
                    break;
            }
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);

        }
    }

    public void navigate(MouseEvent event) throws IOException {

        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            FXMLLoader fxmlLoader = null;
            switch(icon.getId()){

                case "imgGlobal":
                    root = FXMLLoader.load(this.getClass().getResource("/view/GlobalCOVID.fxml"));
                    break;

                case "imgHospital":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageHospital.fxml"));
                    break;

                case "imgQuarantineCenter":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageQuarantineCenters.fxml"));
                    break;

                case "imgUser":
                    Stage stage = (Stage) dashboard.getScene().getWindow();
                    String title = stage.getTitle();
                    if(!title.equalsIgnoreCase("Admin")){
                        new Alert(Alert.AlertType.INFORMATION,"Sorry,You don't have the Admin Privileges due to the security purposes!").show();
                        return;
                    }

                    fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/ManageUsers.fxml"));
                    root = fxmlLoader.load();
                    break;
            }

            if (root != null){
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.dashboard.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    }
