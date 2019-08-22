package lesson7.fxClient;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller{

    //chat pane elements
    public AnchorPane chatPane;
    public TextField messageField;
    public Button sendButton;
    public TextArea textArea;

    //login pane elements
    public GridPane loginPane;
    public TextField loginField;
    public PasswordField passField;
    public Button loginBtn;
    public Button signUpBtn;

    public GridPane signUpPane;
    public TextField signUpField;
    public PasswordField passSignUpField;
    public Button CreateUserBtn;



    private final String HOST = "localhost";
    private final int PORT = 8090;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        out.writeUTF(messageField.getText() + "\n");
        messageField.clear();
        messageField.requestFocus();
    }

    public void tryAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()){
            initSocket();
        }
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initSocket(){
        try {
            socket = new Socket(HOST, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread tr = new Thread(() -> {
                try {
                    String message;
                    while (true){
                        message = in.readUTF();
                        if (message.startsWith("/auth_success")){
                            authenticate(true);
                            break;
                        }
                        if (message.startsWith("/auth_failed")){
//                            alert();
                            System.out.println("auth failed: " + message);
                        }
                        if (message.startsWith("/signUp_success")){
                            showSignInPage(true);
                        }
                        if (message.startsWith("/signUp_failed")){
                            System.out.println("signUp failed: " + message);
                        }
                    }

                    while (true) {
                        message = in.readUTF();
                        System.out.print("from server:" + message);
                        textArea.appendText(message);
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
                }
            });
            tr.setDaemon(true);
            tr.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSignInPage(boolean signUpSuccess) {
        signUpPane.setVisible(!signUpSuccess);
        signUpPane.setManaged(!signUpSuccess);

        loginPane.setVisible(signUpSuccess);
        loginPane.setManaged(signUpSuccess);
    }

    private void authenticate(boolean auth) {
        loginPane.setVisible(!auth);
        loginPane.setManaged(!auth);

        chatPane.setVisible(auth);
        chatPane.setManaged(auth);
    }

    public void openSignUpForm(ActionEvent actionEvent) {
        loginPane.setVisible(false);
        loginPane.setManaged(false);

        signUpPane.setVisible(true);
        signUpPane.setManaged(true);
    }

    public void createUser(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()){
            initSocket();
        }
        try {
            out.writeUTF("/signUp " + signUpField.getText() + " " + passSignUpField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void alert(){
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Auth error");
//        alert.setContentText("Неправильный логин или пароль");
//        alert.showAndWait();
//    }
}
