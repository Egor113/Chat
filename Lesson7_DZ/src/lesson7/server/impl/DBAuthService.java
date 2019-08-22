package lesson7.server.impl;

import lesson7.server.Server;
import lesson7.server.api.AuthService;
import lesson7.server.model.Client;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class DBAuthService implements AuthService {
    private Set<Client> users = new LinkedHashSet<>();

    public DBAuthService() {

    }

    @Override
    public Client authenticate(String login, String pass) {
        Statement stmt = null;
        try {
            stmt = Server.conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
                if (rs.getString("name").equals(login) && rs.getString("password").equals(pass)){
                    return new Client(rs.getString("name"),rs.getString("password"),rs.getString("nick"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean signUp(String login, String pass) {
        Statement stmt = null;
        try {
            stmt = Server.conn.createStatement();
            String request = "insert into users(name,password,nick) values('" + login + "','" + pass + "','" + login + "')";
            stmt.execute(request);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
