package lesson7;

import java.sql.*;

public class MainDB {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/chat");
            stmt = conn.createStatement();

            stmt.execute("insert into users(name,password,nick) values('egor',12345,'egor')");
            ResultSet rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " : " + rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
