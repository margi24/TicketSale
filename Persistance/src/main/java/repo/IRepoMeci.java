package repo;

import domain.Meci;

public interface IRepoMeci extends ICrudRepository<Integer, Meci> {

    Iterable<Meci> findAllMeciDisponibil();
}
