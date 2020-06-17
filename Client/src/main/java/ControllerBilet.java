import domain.Bilet;
import domain.Meci;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerBilet extends UnicastRemoteObject implements IObserver, Initializable {

    @FXML
    private TableView<Meci> tabel;

    @FXML
    private TableColumn<Object, Integer> colId;

    @FXML
    private TableColumn<Object, String> colMeci;

    @FXML
    private TableColumn<Object, String> colPret;

    @FXML
    private TableColumn<Object, String> colBilete;

    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnBilet;

    @FXML
    private Button btnVerif;

    @FXML
    private TextField txtCauta;
    @FXML
    private TextField txtIdMeci;

    @FXML
    private TextField textNrBilete;
    IServer service;
    private ControllerBilet user;
    private ObservableList<Meci> meciObservableList = FXCollections.observableList(new ArrayList<>());

    public ControllerBilet() throws RemoteException {
    }

    public void setService(IServer service, ControllerBilet user) {

        this.service = service;
        this.user = user;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colMeci.setCellValueFactory(new PropertyValueFactory<>("Nume"));
        colPret.setCellValueFactory(new PropertyValueFactory<>("Pret"));
        colBilete.setCellValueFactory(new PropertyValueFactory<>("NrBileteDisponibile"));
    }

    private void loadTableSt() {

        tabel.setItems(meciObservableList);
        colBilete.setCellFactory(new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
            @Override
            public TableCell<Object, String> call(TableColumn<Object, String> param) {
                return new TableCell<Object, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.equals("SOLD OUT"))
                                this.setTextFill(Color.RED);
                            setText(item);

                        }
                    }
                };
            }
        });
    }


    public synchronized void clickVanzare(ActionEvent event) throws ServerEx {
        int idMeci = Integer.parseInt(txtIdMeci.getText());
        int nrBilete = Integer.parseInt(textNrBilete.getText());
        Meci m=null;
        int ok=0;
        try {
            List<Meci> meciuri = new ArrayList<>();
            for (Meci aux : service.getAllMeciuri()) {
                if (aux.getID() == idMeci) {
                    if (Integer.parseInt(aux.getNrBileteDisponibile()) < nrBilete) {
                        showErrorMessage("prea multe bilete");
                    } else {
                        service.addBilet(new Bilet(service.findMeciId(), idMeci, nrBilete));
                     }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void clickDisponibile(ActionEvent event) throws ServerEx {

        List<Meci> meci = (List<Meci>) this.service.getAllMeciuriDisponibile();
        List<Meci> meciuri = new ArrayList<>();
        for (Meci aux : meci) {
            meciuri.add(new Meci(aux.getID(), aux.getNume(), aux.getPret(), aux.getNrBileteDisponibile()));
        }
        meciObservableList.setAll(StreamSupport.stream(meciuri.spliterator(), false)
                .collect(Collectors.toList()));
        this.loadTableSt();
    }


    public void clickAll(ActionEvent event) {
        try {
            List<Meci> meci = (List<Meci>) this.service.getAllMeciuri();
            List<Meci> meciuri = new ArrayList<>();
            for (Meci aux : meci) {
                meciuri.add(new Meci(aux.getID(), aux.getNume(), aux.getPret(), aux.getNrBileteDisponibile()));
            }
            meciObservableList.setAll(StreamSupport.stream(meciuri.spliterator(), false)
                    .collect(Collectors.toList()));
            this.loadTableSt();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public synchronized void update(Meci meci) throws ServerEx {
        int i = 0;
        int gas = 0;
        Meci meciGas = null;
        for (Meci meciDTO : meciObservableList
        ) {
            if (meciDTO.getID().equals(meci.getID())) {
                gas = i;
                meciGas = meciDTO;
            }
            i++;
        }
        Meci meci2 = new Meci(meciGas.getID(), meciGas.getNume(), meciGas.getPret(), meci.getNrBileteDisponibile());
        meciObservableList.set(gas, meci2);
        this.tabel.setItems(meciObservableList);
    }

    public void clickLogOut(ActionEvent event) {
        try {
            btnLogOut.getScene().getWindow().hide();
            Parent root;
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("login.fxml"));

            root = loader.load();

            ControllerLogin controller = loader.getController();
            controller.setService(service);
            Scene scene = new Scene(root, 313, 449);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void showErrorMessage(String msg){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(msg);
        message.showAndWait();
    }

}

