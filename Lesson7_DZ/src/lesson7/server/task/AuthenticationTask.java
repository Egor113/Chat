package lesson7.server.task;

import lesson7.server.ClientHandler;
import lesson7.server.Server;
import lesson7.server.api.AuthService;
import lesson7.server.model.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AuthenticationTask extends Thread {
    private Socket socket;
    private Server server;
    private AuthService authService;

    public AuthenticationTask(Socket socket, Server server, AuthService authService) {
        this.socket = socket;
        this.server = server;
        this.authService = authService;
    }

    @Override
    public void run() {
        String line;
        DataInputStream in;
        DataOutputStream out;

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true){
                line = in.readUTF();
                if (line.startsWith("/auth")){
                    String[] creds = line.split(" ");
                    Client client = authService.authenticate(creds[1], creds[2]);
                    if (client != null){
                        out.writeUTF("/auth_success");
                        server.subscribe(new ClientHandler(server, socket, client));
                        break;
                    }else {
                        out.writeUTF("/auth_failed");
                    }
                }
                else if(line.startsWith("/signUp")){
                    String[] creds = line.split(" ");
                    if (authService.signUp(creds[1], creds[2])){
                        out.writeUTF("/signUp_success");
                        break;
                    }
                    else {
                        out.writeUTF("/signUp_failed");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
