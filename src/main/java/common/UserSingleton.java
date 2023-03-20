package common;

import classess.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserSingleton {
    private static UserSingleton object;
    private final HashMap<Integer, User> map = new HashMap<>();

    private UserSingleton() { }

    public static UserSingleton getInstance() {
        if (object == null) {
            object = new UserSingleton();
        }

        return object;
    }

    public User getUserById(int userId) throws SQLException {

//    	System.out.println("userId "+userId);
        if (map.containsKey(userId)) {
            return map.get(userId);
        } else {
            PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from users where userId=?");
            psmt.setInt(1, userId);
            ResultSet rs=psmt.executeQuery();

            if (rs.next()) {
                User user = User.fromResultSet(rs);
                map.put(userId, user);
  
                return user;
            }

        }

        return null;
    }

    public User getByEmail(String email) throws SQLException {

        for (User user: map.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from users where email=?");
        psmt.setString(1, email);
        ResultSet rs=psmt.executeQuery();

        if (rs.next()) {
            User user = User.fromResultSet(rs);
            map.put(user.getUserId(), user);

            return user;
        }

		return null;
	}
}
