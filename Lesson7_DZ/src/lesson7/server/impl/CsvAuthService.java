package lesson7.server.impl;

import lesson7.server.api.AuthService;
import lesson7.server.model.Client;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

public class CsvAuthService implements AuthService {
    private Set<Client> users = new LinkedHashSet<>();
    private final String PATH = "./data/db.csv";


    public CsvAuthService() {
        File file = Paths.get(PATH).toFile();
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String user = reader.readLine();
            while (user != null) {
                String[] creds = user.split(";");
                users.add(new Client(creds[0], creds[1], creds[2]));
                user = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client authenticate(String login, String pass) {
        return users
                .stream()
                .filter(user -> user.getLogin().equals(login) && user.getPass().equals(pass))
                .findFirst().orElse(null);
    }

    @Override
    public boolean signUp(String login, String pass) {
        return false;
    }

}
