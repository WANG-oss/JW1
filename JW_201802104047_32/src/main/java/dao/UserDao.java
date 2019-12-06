package dao;

import domain.Teacher;
import domain.User;
import util.JdbcHelper;

import java.sql.*;
import java.util.*;
import java.util.Date;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;

	public Collection<User> findAll()throws SQLException {
			users = new TreeSet<>();
			//获得连接对象
			Connection connection = JdbcHelper.getConn();
			Statement statement = connection.createStatement();
			//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
			ResultSet resultSet = statement.executeQuery("select * from user");
			//若结果集仍然有下一条记录，则执行循环体
			while (resultSet.next()){
				//创建User对象，根据遍历结果中的id,description,no,remarks值
				User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),new Date(),TeacherDao.getInstance().find(2));
				//向users集合中添加User对象
				users.add(user);
			}
			//关闭资源
			JdbcHelper.close(resultSet,statement,connection);
			return users;
		}
	
	public User find(Integer id)throws SQLException{
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "SELECT * FROM user WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,username,password值为参数，User
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),new Date(),TeacherDao.getInstance().find(2));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
		}

	public boolean changePassword(User user,Integer id)throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateUser_sql = " update user set password=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateUser_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,user.getPassword());
		preparedStatement.setInt(2,id);
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public User findByUsername(String username) throws SQLException {
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String selectUser_sql = "SELECT * FROM user WHERE username=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(selectUser_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,username);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks，school值为参数，创建Department对象
		//若结果集中没有记录，则本方法返回null
		while (resultSet.next()){
			Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
			 user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),new Date(),teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
	}
	public User login(String username,String password) throws SQLException{
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String finndByUser_sql = "SELECT * FROM user WHERE username=? and password =?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(finndByUser_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,username);
		preparedStatement.setString(2,password);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks，school值为参数，创建Department对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
			user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("passworc"),new Date(),teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
	}

}
