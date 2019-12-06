package service;


import dao.UserDao;
import domain.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();

	public UserService() {
	}

	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> findAll()throws SQLException {
		return userDao.findAll();
	}

	public User find(Integer id)throws SQLException{
		return userDao.find(id);
	}

	public boolean changePassword(User user,Integer id)throws SQLException{
		return userDao.changePassword(user,id);
	}


	public User login(String username, String password)throws SQLException{
		Collection<User> users = this.findAll();
		User desiredUser = null;
		for(User user:users){
			if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
				desiredUser = user;
			}
		}
		return desiredUser;
	}

	public User findByUsername(String username)throws SQLException{
		Collection<User> users = this.findAll();
		User foundUser = null;
		for(User user:users){
			if(username.equals(user.getUsername())) {
				foundUser = user;
			}
		}
		return foundUser;
	}
}
