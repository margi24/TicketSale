package repo;

import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoUser implements IRepoUser {


    JdbcUtils jdbcUtils;
    public static final Logger logger= LogManager.getLogger();

    public RepoUser(Properties prop) {
        this.jdbcUtils = new JdbcUtils(prop);
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con=jdbcUtils.getConnection();
        try{
            PreparedStatement preparedStatement= con.prepareStatement("SELECT count (*) as [SIZE] from User");
            try(ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void save(User entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, User entity) {

    }

    @Override
    public User findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        List<User> vanzareList=new ArrayList<>();
        Connection con= jdbcUtils.getConnection();
        try{
            PreparedStatement preparedStatement=con.prepareStatement("SELECT * from User");

            ResultSet resultSet=preparedStatement.executeQuery();
            int id= resultSet.getInt("id");
            String nume= resultSet.getString("username");
            String par= resultSet.getString("parola");
            vanzareList.add(new User(id,nume,par));
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return vanzareList;
    }

    @Override
    public User findUserName(String name) {
        logger.traceEntry("finding user with name {} ",name);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from User where username=?")){
            preStmt.setString(1,name);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("username");
                    String par = result.getString("parola");
                    User meciuri=new User(id,nume,par);
                    logger.traceExit(meciuri);
                    return meciuri;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No user found with nae {}", name);
        return null;

    }
}

