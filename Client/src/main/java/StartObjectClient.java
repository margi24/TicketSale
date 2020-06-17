
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;

public class StartObjectClient extends Application {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    private static IServer server;

    public static void main(String[] args){

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IServer server=(IServer)factory.getBean("service");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassLoader.getSystemResource("login.fxml"));
        Parent root;

        root = loader.load();

        ControllerLogin loginViewController = loader.getController();
        loginViewController.setService(server);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Authentication");
        primaryStage.show();
    }
}
