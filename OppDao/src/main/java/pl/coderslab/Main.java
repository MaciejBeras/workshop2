package pl.coderslab;

import java.sql.SQLException;
import java.util.Arrays;
import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDAO;

public class Main {


  public static void main(String[] args) throws SQLException {

    User user = new User();

    user.setUserName("Witold");
    user.setEmail("witek@gmail.com");
    user.setPassword("taczka1996");

    UserDAO userDAO = new UserDAO();

//    System.out.println(userDAO.read(0));
//
//    User test = userDAO.read(1);
//    test.setUserName("Maciej");
//    test.setEmail("maciej@gmail.com");
//    test.setPassword("grabie2024");
//    userDAO.update(test);

    user.setUserName("Luiza");
    user.setEmail("luiza@gmail.com");
    user.setPassword("motyka2007");

//    userDAO.create(user);
//    System.out.println(userDAO.read(5));
    System.out.println(Arrays.toString(userDAO.findAllUsers()));
//    userDAO.delete(4);

  }


}
