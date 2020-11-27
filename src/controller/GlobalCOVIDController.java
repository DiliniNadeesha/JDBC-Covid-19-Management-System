package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class GlobalCOVIDController {

    public AnchorPane root;
    public JFXDatePicker dtpDate;
    public JFXTextField txtConfirmed;
    public JFXTextField txtRecoverd;
    public JFXTextField txtDeaths;

    public Label lblDate;
    public Label lblConfirmed;
    public Label lblRecoverd;
    public Label lblDeaths;

    public JFXButton btnHome;
    public JFXButton btnUpdate;
    public JFXButton btnReset;

    public void initialize(){

        dtpDate.setValue(LocalDate.now());

        loadAllData();

        btnUpdate.setText("Update");

        dtpDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                LocalDate localDate = newValue;

                try {
                    PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM globalcoviddata WHERE updatedDate=?");
                    pstm.setString(1, String.valueOf(newValue));
                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()){
                        lblDate.setText(newValue.toString());
                        lblConfirmed.setText(rst.getString(2));
                        lblRecoverd.setText(rst.getString(3));
                        lblDeaths.setText(rst.getString(4));

                        txtConfirmed.setText(rst.getString(2));
                        txtRecoverd.setText(rst.getString(3));
                        txtDeaths.setText(rst.getString(4));

                        btnUpdate.setText("Update");
                    }
                    else {
                        btnUpdate.setText("Save");

                        txtConfirmed.setText("");
                        txtRecoverd.setText("");
                        txtDeaths.setText("");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadAllData() {

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM globalcoviddata");
            rst.last();
            Date date = rst.getDate(1);
            String confirmed = rst.getString(2);
            String recovered = rst.getString(3);
            String deaths = rst.getString(4);

            lblDate.setText(date.toString());
            lblConfirmed.setText(confirmed);
            lblRecoverd.setText(recovered);
            lblDeaths.setText(deaths);

            txtConfirmed.setText(rst.getString(2));
            txtRecoverd.setText(rst.getString(3));
            txtDeaths.setText(rst.getString(4));
            dtpDate.valueProperty().setValue(date.toLocalDate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {

        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (dtpDate.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    public void btnUpdate_OnAction(ActionEvent actionEvent) {

        if (txtConfirmed.getText().isEmpty() || txtRecoverd.getText().isEmpty() || txtDeaths.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please make sure to fill all the fields", ButtonType.OK).show();
            return;
        }

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM globalcoviddata WHERE updatedDate=?");
            pstm.setDate(1, Date.valueOf(dtpDate.getValue()));
            ResultSet rst = pstm.executeQuery();
            boolean bool = rst.next();
            if (bool){
                System.out.println("Update");
                PreparedStatement pstm1 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE globalcoviddata SET confirmed=?, recovered=?, death=? WHERE updatedDate=?");
                pstm1.setObject(1, txtConfirmed.getText());
                pstm1.setObject(2, txtRecoverd.getText());
                pstm1.setObject(3, txtDeaths.getText());
                pstm1.setObject(4, Date.valueOf(dtpDate.getValue()));

                if (pstm1.executeUpdate() == 0){
                    new Alert(Alert.AlertType.ERROR,"Failed to update the Record", ButtonType.OK).show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION,"Successfully Updated", ButtonType.OK);
                }
            }

            else {
                System.out.println("Insert");
                PreparedStatement pstm2 = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO globalcoviddata VALUES (?,?,?,?)");
                pstm2.setObject(1, Date.valueOf(dtpDate.getValue()));
                pstm2.setObject(2, txtConfirmed.getText());
                pstm2.setObject(3, txtRecoverd.getText());
                pstm2.setObject(4, txtDeaths.getText());

                if (pstm2.executeUpdate() == 0){
                    new Alert(Alert.AlertType.INFORMATION,"Failed to save the Record", ButtonType.OK).show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION,"Record Added Successfully", ButtonType.OK).show();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadAllData();
    }

    public void btnReset_OnAction(ActionEvent event) {

        txtRecoverd.setText("");
        txtConfirmed.setText("");
        txtDeaths.setText("");

        loadAllData();
    }
}
