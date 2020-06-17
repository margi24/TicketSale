package repo;

import domain.Meci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoMeci implements IRepoMeci {

    private JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();

    public RepoMeci(Properties properties) {
        logger.info("Initializare repoMeci");
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con=jdbcUtils.getConnection();
        try{
            PreparedStatement preparedStatement= con.prepareStatement("SELECT count * as [SIZE] from Meciuri");
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
    public void save(Meci entity) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Meci entity) {
        logger.traceEntry("updating meci with id{} with {}" ,integer,entity);
        Connection con=jdbcUtils.getConnection();
        PreparedStatement preStmt=null;
        try{
            preStmt=con.prepareStatement("update Meciuri set nume=?,pret=?,bileteDisponibile=? where id=? ");
            preStmt.setInt(4,entity.getID());
            preStmt.setString(1,entity.getNume());
            preStmt.setString(2,entity.getPret());
            if(entity.getNrBileteDisponibile().equals("0")){
                preStmt.setString(3,"SOLD OUT");}
            else{
                preStmt.setString(3,entity.getNrBileteDisponibile());
            }
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(preStmt!=null)
                try {
                    preStmt.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        logger.traceExit();
    }

    @Override
    public Meci findOne(Integer integer) {
        logger.traceEntry("finding meci with id {} ",integer);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Meciuri where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String pret = result.getString("pret");
                    String nrbilete = result.getString("bileteDisponibile");
                    Meci meciuri=new Meci(id,nume,pret,nrbilete);
                    logger.traceExit(meciuri);
                    return meciuri;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No music found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Meci> findAll() {
        logger.traceEntry();
        List<Meci> meciuriList=new ArrayList<>();
        Connection con= jdbcUtils.getConnection();
        ResultSet resultSet=null;
        PreparedStatement preparedStatement=null;
        try{
            preparedStatement=con.prepareStatement("SELECT * from Meciuri");
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                int id= resultSet.getInt("id");
                String nume= resultSet.getString("nume");
                String pret= resultSet.getString("pret");
                String nrBilete= resultSet.getString("bileteDisponibile");
                meciuriList.add(new Meci(id,nume,pret,nrBilete));
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        finally {
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null)
                try {
                    preparedStatement.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit(meciuriList);
        return meciuriList;
    }
    @Override
    public Iterable<Meci> findAllMeciDisponibil() {
        logger.traceEntry();
        List<Meci> meciuriList=new ArrayList<>();
        Connection con= jdbcUtils.getConnection();
        try{
            PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Meciuri where bileteDisponibile>0 and bileteDisponibile not like 'SOLD OUT' order by bileteDisponibile desc ");
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id= resultSet.getInt("id");
                String nume= resultSet.getString("nume");
                String pret= resultSet.getString("pret");
                String bilDisp= resultSet.getString("bileteDisponibile");
                meciuriList.add(new Meci(id,nume,pret,bilDisp));
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return meciuriList;
    }
}

