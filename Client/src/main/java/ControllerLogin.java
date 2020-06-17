
import domain.Bilet;
import domain.Meci;
import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ControllerLogin extends UnicastRemoteObject {
    IServer service;
    @FXML
    private TextField txtUser;
    @FXML
    AnchorPane anchor;
    @FXML
    private PasswordField txtPass;

    @FXML
    private Button btnLogin;

    public ControllerLogin() throws RemoteException {}
    public void setService(IServer service) {
        this.service=service;
    }

    public void clickLogin(ActionEvent event) {
        User user = new User( 1, txtUser.getText(), txtPass.getText());
        try {
            anchor.getScene().getWindow().hide();
            Parent root;
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bilet.fxml"));
            root = loader.load();
            ControllerBilet controller = loader.getController();
            controller.setService(service,controller);
            service.login(user, controller);
            Scene scene = new Scene(root, 722, 390);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {

            showErrorMessage("ceva nu a fost ok");
        }

    }
    void showErrorMessage(String msg){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(msg);
        message.show();
    }
}
