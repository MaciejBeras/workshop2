package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

public class UserDAO {

  private static final String CREATE_USER_QUERY =
      "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

  private static final String READ_USER_QUERY =
      "SELECT * FROM users where id = ?";
  private static final String UPDATE_USER_QUERY =
      "UPDATE users SET username = ?, email = ?, password = ? where id = ?";

  private static final String DELETE_USER_QUERY =
      "DELETE FROM users where id = ?";

  private static final String FIND_ALL_USERS_QUERY =
      "SELECT * FROM users";

  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }


  public User create(User user) {
    try (Connection conn = DbUtil.getConnection()) {
      PreparedStatement statement =
          conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, user.getUserName());
      statement.setString(2, user.getEmail());
      statement.setString(3, hashPassword(user.getPassword()));
      statement.executeUpdate();
      //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
      ResultSet resultSet = statement.getGeneratedKeys();
      if (resultSet.next()) {
        user.setId(resultSet.getInt(1));
      }
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public User read(int userId) {
    try (Connection conn = DbUtil.getConnection()) {
      PreparedStatement statement =
          conn.prepareStatement(READ_USER_QUERY);
      statement.setInt(1, userId);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void update(User user) {
    try (Connection conn = DbUtil.getConnection()) {
      PreparedStatement statement =
          conn.prepareStatement(UPDATE_USER_QUERY);
      statement.setString(1, user.getUserName());
      statement.setString(2, user.getEmail());
      statement.setString(3, hashPassword(user.getPassword()));
      statement.setInt(4, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void delete(int userId) {
    try (Connection conn = DbUtil.getConnection()) {
      PreparedStatement statement =
          conn.prepareStatement(DELETE_USER_QUERY);
      statement.setInt(1,userId);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

//  public void findAllUsers() {
//    try (Connection conn = DbUtil.getConnection()) {
//      PreparedStatement statement =
//          conn.prepareStatement(FIND_ALL_USERS_QUERY);
//      ResultSet resultSet = statement.executeQuery();
//      User user = new User();
//      while (resultSet.next()) {
//          System.out.print(resultSet.getInt("id") + " ");
//          System.out.print(resultSet.getString("username") + " ");
//          System.out.print(resultSet.getString("email") + " ");
//          System.out.println(resultSet.getString("password"));
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }

    public User [] findAllUsers() {
      User[] usersArray = new User[0];
    try (Connection conn = DbUtil.getConnection()) {
      PreparedStatement statement =
          conn.prepareStatement(FIND_ALL_USERS_QUERY);
      ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          User user = new User();
          user.setId(resultSet.getInt("id"));
          user.setUserName(resultSet.getString("username"));
          user.setEmail(resultSet.getString("email"));
          user.setPassword(resultSet.getString("password"));
          usersArray = addToArray(user, usersArray);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return usersArray;
  }


  private User[] addToArray(User u, User[] users) {
    // Tworzymy kopię tablicy powiększoną o 1.
    User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
    // Dodajemy obiekt na ostatniej pozycji.
    tmpUsers[users.length] = u;
    // Zwracamy nową tablicę.
    return tmpUsers;

  }
}

