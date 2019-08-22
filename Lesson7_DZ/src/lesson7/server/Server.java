package lesson7.server;

import lesson7.server.impl.CsvAuthService;
import lesson7.server.impl.DBAuthService;
import lesson7.server.model.Client;
import lesson7.server.task.AuthenticationTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    public static Connection conn = null;

    private List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public Server(){
        ServerSocket server = null;
        Socket socket = null;
        try {

            server = new ServerSocket(8090);
            System.out.println("server start");
            connectDB();
            while (true) {
                socket = server.accept();
                System.out.println("client connected");
                new AuthenticationTask(socket, this, new DBAuthService()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void connectDB() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/chat");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void broadCast(String msg){
        for (ClientHandler handler: clients){
            handler.sendMsg(msg);
        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client){
        clients.remove(client);
    }

    public void sendToUser(String line, String nick) {
        for (ClientHandler c: clients
             ) {
            if (line.contains(c.getClient().getNick())){
                String message = line.substring(line.indexOf(c.getClient().getNick()) + c.getClient().getNick().length()+1);
                c.sendMsg(nick + ": " + message);
            }
        }
    }
}
