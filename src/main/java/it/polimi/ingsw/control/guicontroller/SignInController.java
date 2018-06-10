package it.polimi.ingsw.control.guicontroller;

import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.model.clientModel.ClientModel;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindGameForUserInDatabaseException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.usersdb.PlayerInGame;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;



public class SignInController {

    private NetworkClient networkClient;
    private ClientModel clientModel;

    @FXML private TextField signInUsername;

    @FXML private PasswordField signInPassword;

    @FXML private Button signInButton;

    @FXML private Button backButton;

    @FXML private Label signInErrorLabel;


    public void initialize( ) {
        networkClient = NetworkClient.getInstance();
        clientModel = ClientModel.getInstance();

        signInButton.setOnAction(event-> {

                String username = signInUsername.getText();
                String password = signInPassword.getText();

                if (!password.equals("") || !username.equals(""))
                    loginUser(username, password, event);
                else signInErrorLabel.setVisible(true);
        });

        signInUsername.setOnMouseClicked(event -> signInErrorLabel.setVisible(false));

        backButton.setOnAction(event -> changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/StartingScene.fxml"));
    }


    private void changeSceneHandle(ActionEvent event, String path) {
        AnchorPane nextNode = new AnchorPane();
        try {
            nextNode = FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(nextNode);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    private void loginUser(String username, String password, ActionEvent event) {
        Thread signIn = new Thread(()->{
            try {
                networkClient.login(username, password);
            } catch (CannotLoginUserException e) {
                Platform.runLater(()->{
                    signInErrorLabel.setVisible(true);
                    signInUsername.clear();
                    signInPassword.clear();
                });
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //TODO
            }
            Platform.runLater(()->{
                try {
                    int numPlayers = networkClient.findAlreadyStartedGame(clientModel.getUserToken());
                    switch (numPlayers){
                        case 1:
                            break;
                        case 2:
                            changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/TwoPlayersGameScene.fxml");
                            break;
                        case 3:
                            changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/ThreePlayersGameScene.fxml");
                            break;
                        case 4:
                            changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/FourPlayersGameScene.fxml");
                            break;
                    }
                } catch (CannotFindGameForUserInDatabaseException e) {
                    changeSceneHandle(event, "/it/polimi/ingsw/view/gui/guiview/SetNewGameScene.fxml");
                }


            });
        });
        signIn.start();
    }
}
