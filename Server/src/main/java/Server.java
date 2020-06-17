import domain.Bilet;
import domain.Meci;
import domain.User;
import repo.*;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IServer {
    private IRepoMeci repoMeci;
    private IRepoUser repoUser;
    private IRepoBilet repoBilet;
    private Map<String, IObserver> loggedClients;

    public Server(RepoMeci repoMeci, RepoBilet repoBilet, RepoUser repoUser){
        this.repoBilet = repoBilet;
        this.repoMeci = repoMeci;
        this.repoUser = repoUser;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User pers, IObserver client) throws ServerEx {
        User user = repoUser.findUserName(pers.getNume());
        if (user !=null ){
            if(loggedClients.containsKey(user.getNume()))
                throw new ServerEx("User is already logged in");
            loggedClients.put(user.getNume(), client);
        }
        else{
            throw new ServerEx("Authentication error");
        }
    }

    @Override
    public synchronized void logout(User pers) throws ServerEx {
        loggedClients.remove(pers.getNume());
    }

    @Override
    public synchronized Iterable<Meci> getAllMeciuri() {
        return repoMeci.findAll();
    }

    @Override
    public synchronized  Iterable<Meci> getAllMeciuriDisponibile() {
        return repoMeci.findAllMeciDisponibil();
    }

    @Override
    public synchronized Iterable<Bilet> getAllBilete() {
        return repoBilet.findAll();
    }

    @Override
    public Iterable<User> getAllUser() {
        return repoUser.findAll();
    }

    @Override
    public synchronized Integer findMeciId() {
        return repoBilet.findAvailableId();
    }

    @Override
    public synchronized int sizeUser() {
        return repoUser.size();
    }

    @Override
    public synchronized void addBilet(Bilet b) throws ServerEx {
        repoBilet.save(b);
        notify(repoMeci.findOne(b.getIdMeci()));

    }

    @Override
    public synchronized void updateMeci(Integer id, Meci m) throws ServerEx {
        repoMeci.update(id,m);
        notify(m);
    }

    private final int defaultThreadsNo = 5;
    private synchronized void notify(Meci meci) throws ServerEx {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(String s: loggedClients.keySet()){
            String name = String.valueOf(loggedClients.entrySet());
            IObserver user = loggedClients.get(s);
            executor.execute(() -> {
                try {
                    user.update(meci);
                    System.out.println("Notify " + name);
                } catch (ServerEx ex) {
                    System.out.println("Error notifying friend " + ex);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }

}
