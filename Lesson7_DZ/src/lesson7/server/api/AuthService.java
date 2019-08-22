package lesson7.server.api;

import lesson7.server.model.Client;

public interface AuthService {
    Client authenticate(String login, String pass);
    boolean signUp(String login, String pass);
}
