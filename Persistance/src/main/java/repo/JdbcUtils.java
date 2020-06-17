package repo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils {
    private Properties jdbcProps;

    public JdbcUtils(Properties props){
        jdbcProps=props;
    }
    private static final Logger logger= LogManager.getLogger();
    private Connection instance=null;
    private Connection getNewConnection(){
        logger.traceEntry();
        String url ="jdbc:sqlite:D:\\an2\\mpp\\mpp2\\src\\main\\java\\db\\basket.db";
        //String driver = "com.mysql.jdbc.Driver";
        logger.info("trying to connect to the database ... {}", url);
        Connection conn = null;
        try{
            Class.forName("org.sqlite.JDBC");
            //logger.info("Loader driver ... {}", driver);
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException exception) {
            logger.error(exception);
            System.out.println("Error get connection" + exception);
        }

        return conn;
    }

    public Connection getConnection(){
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return instance;
    }
}
