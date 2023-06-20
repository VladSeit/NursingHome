package controller;

import datastorage.DAOFactory;
import datastorage.PflegerDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Pfleger;

import java.sql.SQLException;

import static utils.NewMessageWindow.getMessageInTextField;
import static utils.PasswordEncryption.encryptPassword;

public class LoginController {

    @FXML
    public TextField loginTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Button loginButton;
    @FXML
    public Label welcomeLabel;
    @FXML
    public Button logoutButtonInLoginWindow;
    static public boolean isLogged = false;
    static Pfleger currentPfleger;
    PflegerDAO dao;

    /**
     * initialize login window
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        this.dao= DAOFactory.getDAOFactory().createPflegerDAO();
        logoutButtonInLoginWindow.setVisible(false);
        welcomeLabel.setVisible(false);
    }

    /**
     * Methode for LoginWindowView.fxml. Logs a users, if the login and the password were correct
     * @throws SQLException
     */
    @FXML
    public void handleLogin() throws SQLException {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        if(checkLoginData(login,encryptPassword(password))){
            isLogged=true;
            welcomeLabel.setText("Willkommen " + login);
            welcomeLabel.setVisible(true);
            loginTextField.setEditable(false);
            passwordTextField.setEditable(false);
            loginTextField.setText("");
            passwordTextField.setText("");
            loginButton.setDisable(true);
            logoutButtonInLoginWindow.setVisible(true);
            getMessageInTextField("Sie haben angemeldet");
            currentPfleger = dao.getPflegerByLogin(login);
        }
        else {
            getMessageInTextField("Falsche Einlogdaten");
        }
    }

    /**
     * Check, if the login and password from textfields were correct
     * @param login
     * @param password
     * @return
     * @throws SQLException
     */
    public boolean checkLoginData(String login, String password) throws SQLException{
            if (dao.checkPflegerLogin(login)) {
                return dao.checkPflegerPassword(password);
            }
            return false;
    }

    /**
     * Logout the user
     */
    @FXML
    public void handleLogout(){
        isLogged=false;
        welcomeLabel.setVisible(false);
        loginTextField.setEditable(true);
        passwordTextField.setEditable(true);
        loginButton.setDisable(false);
        logoutButtonInLoginWindow.setVisible(false);
        currentPfleger=null;
    }
}
