package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageHospitalController {

    public AnchorPane root;
    public TextField txtSearch;
    public JFXListView<String> lstHospitals;
    public JFXComboBox<String> cmbDistricts;
    public JFXButton btnHome;
    public JFXButton btnNewHospital;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtCity;
    public JFXTextField txtCapacity;
    public JFXTextField txtDirector;
    public JFXTextField txtDirectorContactNo;
    public JFXTextField txtHospitalContactNo1;
    public JFXTextField txtHospitalContactNo2;
    public JFXTextField txtHospitalFaxNo;
    public JFXTextField txtHospitalEmail;

    public void initialize() {

        loadAllHospitals();

        String districtsText = " Colombo\n" +
                " Gampaha\n" +
                " Kalutara\n" +
                " Kandy\n" +
                " Matale\n" +
                " Nuwara Eliya\n" +
                " Galle\n" +
                " Matara\n" +
                " Hambantota\n" +
                " Jaffna\n" +
                " Mannar\n" +
                " Vauniya\n" +
                " Mullativue\n" +
                " Ampara\n" +
                " Trincomalee\n" +
                " Batticaloa\n" +
                " Kilinochchi\n" +
                " Kurunegala\n" +
                " Puttalam\n" +
                " Anuradhapura\n" +
                " Polonnaruwa\n" +
                " Badulla\n" +
                " Moneragala\n" +
                " Ratnapura\n" +
                " Kegalle";
        String[] districts = districtsText.split("\n");
        ObservableList<String> olDistricts = FXCollections.observableArrayList(Arrays.asList(districts));
        cmbDistricts.setItems(olDistricts);


        lstHospitals.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(lstHospitals.getSelectionModel().getSelectedItems().isEmpty()){
                    return;
                }

                String name = lstHospitals.getSelectionModel().getSelectedItem().toString();

                Connection connection = DBConnection.getInstance().getConnection();
                try {
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM hospital WHERE name =?");
                    pstm.setObject(1,name);

                    ResultSet rst = pstm.executeQuery();
                    if(rst.next()){
                        txtId.setText(rst.getString(1));
                        txtName.setText(rst.getString(2));
                        txtCity.setText(rst.getString(3));
                        cmbDistricts.getSelectionModel().select(rst.getString(4));
                        txtCapacity.setText(rst.getString(5));
                        txtDirector.setText(rst.getString(6));
                        txtDirectorContactNo.setText(rst.getString(7));
                        txtHospitalContactNo1.setText(rst.getString(8));
                        txtHospitalContactNo2.setText(rst.getString(9));
                        txtHospitalFaxNo.setText(rst.getString(10));
                        txtHospitalEmail.setText(rst.getString(11));

                        btnSave.setText("Update");
                        btnSave.setDisable(false);
                        btnDelete.setDisable(false);
                        txtName.requestFocus();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }


    private void loadAllHospitals() {

        try {
            ObservableList hospitals = lstHospitals.getItems();
            hospitals.clear();
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT name FROM hospital");
            while (rst.next()){
                hospitals.add(rst.getString(1));
            }
            lstHospitals.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void txtSearchOnKeyReleased(javafx.scene.input.KeyEvent keyEvent) {

        String hname = txtSearch.getText();
        ObservableList items = lstHospitals.getItems();
        items.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement stm = connection.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT name FROM hospital WHERE name LIKE '%" + hname + "%'");
            while(resultSet.next()){
                items.add(resultSet.getString(1));
            }
            lstHospitals.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void btnHome_OnAction(ActionEvent actionEvent) throws IOException {

        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (cmbDistricts.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }


    public void btnNewHospital_OnAction(ActionEvent event) {

        txtId.clear();
        txtName.clear();
        txtCity.clear();
        cmbDistricts.getSelectionModel().clearSelection();
        txtCapacity.clear();
        txtDirector.clear();
        txtDirectorContactNo.clear();
        txtHospitalContactNo1.clear();
        txtHospitalContactNo2.clear();
        txtHospitalFaxNo.clear();
        txtHospitalEmail.clear();

        txtId.setDisable(false);
        txtName.setDisable(false);
        txtCity.setDisable(false);
        cmbDistricts.setDisable(false);
        txtCapacity.setDisable(false);
        txtDirector.setDisable(false);
        txtDirectorContactNo.setDisable(false);
        txtHospitalContactNo1.setDisable(false);
        txtHospitalContactNo2.setDisable(false);
        txtHospitalFaxNo.setDisable(false);
        txtHospitalEmail.setDisable(false);

        txtName.requestFocus();

        btnSave.setDisable(false);

        // Generate a new hospital id

        int maxId = 0;
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT hospitalID FROM hospital ORDER BY hospitalID DESC LIMIT 1");
            if (rst.next()) {
                maxId = Integer.parseInt(rst.getString(1).replace("H", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "H00" + maxId;
        } else if (maxId < 100) {
            id = "H0" + maxId;
        } else {
            id = "H" + maxId;
        }
        txtId.setText(id);
    }


    public void btnSave_OnAction(ActionEvent event) {

        if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtCity.getText().isEmpty() ||
                txtCapacity.getText().isEmpty() || txtDirector.getText().isEmpty() ||
                txtDirectorContactNo.getText().isEmpty() || txtHospitalContactNo1.getText().isEmpty() ||
                txtHospitalContactNo2.getText().isEmpty() || txtHospitalFaxNo.getText().isEmpty() ||
                txtHospitalEmail.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR,"Please Make Sure To Fill All The Fields").show();
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("Save")){

            String regex = "\\d{3}[-]\\d{7}";
            Pattern pattern = Pattern.compile(regex);
            String dCon = txtDirectorContactNo.getText();
            Matcher matcher = pattern.matcher(dCon);

            if (!matcher.matches()){
                new Alert(Alert.AlertType.ERROR,"You are entered Director Contact Number is wrong, Please re enter!").show();
                return;
            }

            if (!Pattern.matches("\\d{3}[-]\\d{7}", txtHospitalContactNo1.getText())){
                new Alert(Alert.AlertType.ERROR,"You are entered Hospital Contact Number 1 is wrong, Please re enter!").show();
                return;
            }

            if (!Pattern.matches("\\d{3}[-]\\d{7}", txtHospitalContactNo2.getText())){
                new Alert(Alert.AlertType.ERROR,"You are entered Hospital Contact Number 2 is wrong, Please re enter!").show();
                return;
            }

            if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", txtHospitalEmail.getText())){
                new Alert(Alert.AlertType.ERROR,"You are entered email address is wrong, Please re enter!").show();
                return;
            }

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO hospital VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                pstm.setString(1, txtId.getText());
                pstm.setString(2, txtName.getText());
                pstm.setString(3, txtCity.getText());
                pstm.setString(4, cmbDistricts.getValue());
                pstm.setString(5, txtCapacity.getText());
                pstm.setString(6, txtDirector.getText());
                pstm.setString(7, txtDirectorContactNo.getText());
                pstm.setString(8, txtHospitalContactNo1.getText());
                pstm.setString(9, txtHospitalContactNo2.getText());
                pstm.setString(10, txtHospitalFaxNo.getText());
                pstm.setString(11, txtHospitalEmail.getText());

                int affectedRows = pstm.executeUpdate();

                if (affectedRows <= 0){
                    new Alert(Alert.AlertType.ERROR,"Record cannot be added!", ButtonType.OK).show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION,"Record added successfully", ButtonType.OK).show();
                    lstHospitals.getItems().clear();

                    loadAllHospitals();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        else {
            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET name=?, city=?, district=?, capacity=?, director=?, directorContactNo=?, hospitalContactNo1=?, hospitalContactNo2=?, faxNo=?, email=? WHERE hospitalID=?");
                pstm.setString(1, txtName.getText());
                pstm.setString(2, txtCity.getText());
                pstm.setString(3, cmbDistricts.getValue());
                pstm.setString(4, txtCapacity.getText());
                pstm.setString(5, txtDirector.getText());
                pstm.setString(6, txtDirectorContactNo.getText());
                pstm.setString(7, txtHospitalContactNo1.getText());
                pstm.setString(8, txtHospitalContactNo2.getText());
                pstm.setString(9, txtHospitalFaxNo.getText());
                pstm.setString(10, txtHospitalEmail.getText());
                pstm.setString(11, txtId.getText());

                int affectedRows = pstm.executeUpdate();

                if (affectedRows <= 0){
                    new Alert(Alert.AlertType.ERROR,"Record cannot be updated!", ButtonType.OK).show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION,"Record updated successfully", ButtonType.OK).show();
                    lstHospitals.getItems().clear();

                    loadAllHospitals();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        reset();
    }


    public void reset(){

        btnDelete.setDisable(true);

        btnSave.setText("Save");
        txtId.setText("");
        txtName.setText("");
        txtCity.setText("");
        cmbDistricts.setValue(null);
        txtCapacity.setText("");
        txtDirector.setText("");
        txtDirectorContactNo.setText("");
        txtHospitalContactNo1.setText("");
        txtHospitalContactNo2.setText("");
        txtHospitalFaxNo.setText("");
        txtHospitalEmail.setText("");

        txtId.setPromptText("Hospital ID");
        txtName.setPromptText("Hospital Name");
        txtCity.setPromptText("City");
        txtCapacity.setPromptText("Capacity");
        txtDirector.setPromptText("Director");
        txtDirectorContactNo.setPromptText("Director Contact No");
        txtHospitalContactNo1.setPromptText("Hospital Contact No1");
        txtHospitalContactNo2.setPromptText("Hospital Contact No2");
        txtHospitalFaxNo.setPromptText("Hospital Fax No");
        txtHospitalEmail.setPromptText("Hospital Email");

    }


    public void btnDelete_OnAction(ActionEvent event) throws SQLException {

        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM hospital WHERE hospitalID = ?");
        pstm.setString(1, txtId.getText());

            Optional<ButtonType> alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this Hospital?", ButtonType.YES, ButtonType.NO).showAndWait();
            if (alert.get().equals(ButtonType.YES)) {
                try {
                    pstm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                clearAll();
                btnSave.setText("Save");
                btnDelete.setDisable(true);
                btnSave.setDisable(true);
                btnNewHospital.requestFocus();
                loadAllHospitals();
            }
        }


    private void clearAll() {

        txtId.clear();
        txtName.clear();
        txtCity.clear();
        cmbDistricts.getSelectionModel().clearSelection();
        txtCapacity.clear();
        txtDirector.clear();
        txtDirectorContactNo.clear();
        txtHospitalContactNo1.clear();
        txtHospitalContactNo2.clear();
        txtHospitalFaxNo.clear();
        txtHospitalEmail.clear();
        txtSearch.clear();
    }
}

