package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageQuarantineCentersController {

    public AnchorPane root;
    public TextField txtSearch;
    public JFXListView<String> lstCenters;
    public JFXComboBox<String> cmbDistricts;
    public JFXButton btnHome;
    public JFXButton btnNewCenter;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtCity;
    public JFXTextField txtHead;
    public JFXTextField txtHeadContactNo;
    public JFXTextField txtQuarantineCenterContactNo1;
    public JFXTextField txtQuarantineCenterContactNo2;
    public JFXTextField txtCapacity;


    public void initialize() {

        loadAllQuarantineCenters();

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


        lstCenters.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(lstCenters.getSelectionModel().getSelectedItems().isEmpty()){
                    return;
                }

                String name = lstCenters.getSelectionModel().getSelectedItem().toString();

                Connection connection = DBConnection.getInstance().getConnection();
                try {
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM quarantinecenter WHERE name =?");
                    pstm.setObject(1,name);

                    ResultSet rst = pstm.executeQuery();
                    if(rst.next()){
                        txtId.setText(rst.getString(1));
                        txtName.setText(rst.getString(2));
                        txtCity.setText(rst.getString(3));
                        cmbDistricts.getSelectionModel().select(rst.getString(4));
                        txtHead.setText(rst.getString(5));
                        txtHeadContactNo.setText(rst.getString(6));
                        txtQuarantineCenterContactNo1.setText(rst.getString(7));
                        txtQuarantineCenterContactNo2.setText(rst.getString(8));
                        txtCapacity.setText(rst.getString(9));

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

    private void loadAllQuarantineCenters() {

        try {
            ObservableList centers = lstCenters.getItems();
            centers.clear();
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT name FROM quarantinecenter");
            while (rst.next()){
                centers.add(rst.getString(1));
            }
            lstCenters.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void txtSearchOnKeyReleased(KeyEvent keyEvent) {

        String cname = txtSearch.getText();
        ObservableList items = lstCenters.getItems();
        items.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement stm = connection.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT name FROM quarantinecenter WHERE name LIKE '%" + cname + "%'");
            while(resultSet.next()){
                items.add(resultSet.getString(1));
            }
            lstCenters.refresh();
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

    public void btnNewCenter_OnAction(ActionEvent event) {

        txtId.clear();
        txtName.clear();
        txtCity.clear();
        cmbDistricts.getSelectionModel().clearSelection();
        txtHead.clear();
        txtHeadContactNo.clear();
        txtQuarantineCenterContactNo1.clear();
        txtQuarantineCenterContactNo2.clear();
        txtCapacity.clear();

        txtId.setDisable(false);
        txtName.setDisable(false);
        txtCity.setDisable(false);
        cmbDistricts.setDisable(false);
        txtHead.setDisable(false);
        txtHeadContactNo.setDisable(false);
        txtQuarantineCenterContactNo1.setDisable(false);
        txtQuarantineCenterContactNo2.setDisable(false);
        txtCapacity.setDisable(false);

        txtName.requestFocus();

        btnSave.setDisable(false);

        // Generate a new id

        int maxId = 0;
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT quarantineCenterID FROM quarantinecenter ORDER BY quarantineCenterID DESC LIMIT 1");
            if (rst.next()) {
                maxId = Integer.parseInt(rst.getString(1).replace("C", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10) {
            id = "C00" + maxId;
        } else if (maxId < 100) {
            id = "C0" + maxId;
        } else {
            id = "C" + maxId;
        }
        txtId.setText(id);
    }

    public void btnSave_OnAction(ActionEvent event) {

        if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtCity.getText().isEmpty() ||
                txtHead.getText().isEmpty() || txtHeadContactNo.getText().isEmpty() ||
                txtQuarantineCenterContactNo1.getText().isEmpty() || txtQuarantineCenterContactNo2.getText().isEmpty() ||
                txtCapacity.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR,"Please Make Sure To Fill All The Fields").show();
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("Save")){

            String regex = "\\d{3}[-]\\d{7}";
            Pattern pattern = Pattern.compile(regex);
            String dCon = txtHeadContactNo.getText();
            Matcher matcher = pattern.matcher(dCon);

            if (!matcher.matches()){
                new Alert(Alert.AlertType.ERROR,"You are entered Head Contact Number is wrong, Please re enter!").show();
                return;
            }

            if (!Pattern.matches("\\d{3}[-]\\d{7}", txtQuarantineCenterContactNo1.getText())){
                new Alert(Alert.AlertType.ERROR,"You are entered QuarantineCenter Contact Number 1 is wrong, Please re enter!").show();
                return;
            }

            if (!Pattern.matches("\\d{3}[-]\\d{7}", txtQuarantineCenterContactNo2.getText())){
                new Alert(Alert.AlertType.ERROR,"You are entered QuarantineCenter Contact Number 2 is wrong, Please re enter!").show();
                return;
            }

            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO quarantinecenter VALUES (?,?,?,?,?,?,?,?,?)");
                pstm.setString(1, txtId.getText());
                pstm.setString(2, txtName.getText());
                pstm.setString(3, txtCity.getText());
                pstm.setString(4, cmbDistricts.getValue());
                pstm.setString(5, txtHead.getText());
                pstm.setString(6, txtHeadContactNo.getText());
                pstm.setString(7, txtQuarantineCenterContactNo1.getText());
                pstm.setString(8, txtQuarantineCenterContactNo2.getText());
                pstm.setString(9, txtCapacity.getText());

                int affectedRows = pstm.executeUpdate();

                if (affectedRows <= 0){
                    new Alert(Alert.AlertType.ERROR,"Record cannot be added!", ButtonType.OK).show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION,"Record added successfully", ButtonType.OK).show();
                    lstCenters.getItems().clear();

                    loadAllQuarantineCenters();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        else {
            try {
                PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantinecenter SET name=?, city=?, district=?, head=?, headContactNo=?, centerContactNo1=?, centerContactNo2=?, capacity=? WHERE quarantineCenterID=?");
                pstm.setString(1, txtName.getText());
                pstm.setString(2, txtCity.getText());
                pstm.setString(3, cmbDistricts.getValue());
                pstm.setString(4, txtHead.getText());
                pstm.setString(5, txtHeadContactNo.getText());
                pstm.setString(6, txtQuarantineCenterContactNo1.getText());
                pstm.setString(7, txtQuarantineCenterContactNo2.getText());
                pstm.setString(8, txtCapacity.getText());
                pstm.setString(9, txtId.getText());

                int affectedRows = pstm.executeUpdate();

                if (affectedRows <= 0){
                    new Alert(Alert.AlertType.ERROR,"Record cannot be updated!", ButtonType.OK).show();
                }
                else {
                    new Alert(Alert.AlertType.INFORMATION,"Record updated successfully", ButtonType.OK).show();
                    lstCenters.getItems().clear();

                    loadAllQuarantineCenters();
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
        txtHead.setText("");
        txtHeadContactNo.setText("");
        txtQuarantineCenterContactNo1.setText("");
        txtQuarantineCenterContactNo2.setText("");
        txtCapacity.setText("");

        txtId.setPromptText("Quarantine Center ID");
        txtName.setPromptText("Quarantine Center Name");
        txtCity.setPromptText("City");
        txtHead.setPromptText("Head");
        txtHeadContactNo.setPromptText("Head Contact No");
        txtQuarantineCenterContactNo1.setPromptText("Quarantine Center Contact No1");
        txtQuarantineCenterContactNo2.setPromptText("Quarantine Center Contact No2");
        txtCapacity.setPromptText("Capacity");

    }

    public void btnDelete_OnAction(ActionEvent event) throws SQLException {

        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM quarantinecenter WHERE quarantineCenterID = ?");
        pstm.setString(1, txtId.getText());

        Optional<ButtonType> alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this QuarantineCenter?", ButtonType.YES, ButtonType.NO).showAndWait();
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
            btnNewCenter.requestFocus();
            loadAllQuarantineCenters();
        }
    }


    private void clearAll() {

        txtId.clear();
        txtName.clear();
        txtCity.clear();
        cmbDistricts.getSelectionModel().clearSelection();
        txtHead.clear();
        txtHeadContactNo.clear();
        txtQuarantineCenterContactNo1.clear();
        txtQuarantineCenterContactNo2.clear();
        txtCapacity.clear();
        txtSearch.clear();
    }
}

