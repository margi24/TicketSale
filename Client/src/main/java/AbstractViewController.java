import javafx.scene.control.Alert;

public abstract class AbstractViewController {
    void showErrorMessage(String msg){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(msg);
        message.showAndWait();
    }

    static void showMessage(Alert.AlertType type, String header, String msg){
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(msg);
        message.showAndWait();
    }
}
