package repo;

import domain.User;

public interface IRepoUser extends ICrudRepository<Integer, User> {
    User findUserName(String name);
}
