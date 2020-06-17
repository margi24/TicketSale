package repo;

import domain.Bilet;
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

public class RepoBilet implements IRepoBilet {


    JdbcUtils jdbcUtils;
    public static final Logger logger= LogManager.getLogger();
    private RepoMeci repoMeciuri;

    public RepoBilet(Properties prop, RepoMeci repoMeciuri) {
        this.repoMeciuri = repoMeciuri;
        jdbcUtils=new JdbcUtils(prop);
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con=jdbcUtils.getConnection();
        try{
            PreparedStatement preparedStatement= con.prepareStatement("SELECT count (*) as [SIZE] from Bilet");
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
    public void save(Bilet entity) {
        logger.traceEntry("",entity);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Bilet values (?,?,?)")){
            preStmt.setInt(1,entity.getID());
            preStmt.setInt(2,entity.getIdMeci());
            preStmt.setInt(3,entity.getNrBilete());
            preStmt.executeUpdate();
            Meci meciuri=repoMeciuri.findOne(entity.getIdMeci());
            repoMeciuri.update(meciuri.getID(),new Meci(meciuri.getID(),meciuri.getNume(),meciuri.getPret(),
                    String.valueOf(Integer.valueOf(meciuri.getNrBileteDisponibile())-entity.getNrBilete())));

        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Bilet entity) {

    }

    @Override
    public Bilet findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Bilet> findAll() {
        logger.traceEntry();
        List<Bilet> vanzareList=new ArrayList<>();
        Connection con= jdbcUtils.getConnection();
        try{
            PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Bilet");

            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int Mid = resultSet.getInt("Mid");
                int nrBilete = resultSet.getInt("nrBilete");
                vanzareList.add(new Bilet(id, Mid, nrBilete));
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return vanzareList;
    }

    @Override
    public Integer findAvailableId() {
          int idMax = -1;
            for (Bilet b: this.findAll()) {
                if (b.getID() > idMax) {
                    idMax = b.getID();
                }
            }

            return idMax + 1;

    }
}
