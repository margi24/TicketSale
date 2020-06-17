import domain.Meci;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver  extends Remote {
    void update(Meci m) throws ServerEx, RemoteException;

}
