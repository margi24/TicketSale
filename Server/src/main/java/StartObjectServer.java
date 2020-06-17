import domain.Meci;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import repo.RepoBilet;
import repo.RepoMeci;
import repo.RepoUser;
import java.io.IOException;
import java.util.Properties;

public class StartObjectServer {
    private static int defaultPort = 55555;
    public static void main(String[] args) {

        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-server.xml");
        System.out.println("Waiting for clients...");

    }


}
