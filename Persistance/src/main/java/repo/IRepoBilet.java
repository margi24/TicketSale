package repo;

import domain.Bilet;

public interface IRepoBilet extends ICrudRepository<Integer, Bilet> {
    Integer findAvailableId();
}
