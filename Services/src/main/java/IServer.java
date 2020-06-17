import domain.Bilet;
import domain.Meci;
import domain.User;

public interface IServer {
    void login(User pers, IObserver client) throws ServerEx;
    void logout(User pers) throws ServerEx;

    Iterable<Meci> getAllMeciuri() ;
    Iterable<Meci> getAllMeciuriDisponibile() throws ServerEx;
    Iterable<Bilet> getAllBilete() ;
    Iterable<User> getAllUser();
    Integer findMeciId() throws ServerEx;
    int sizeUser();
    void addBilet(Bilet b) throws ServerEx;
    void updateMeci(Integer id,Meci m) throws ServerEx;
}
