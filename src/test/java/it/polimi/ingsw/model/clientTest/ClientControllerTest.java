package it.polimi.ingsw.model.clientTest;

import it.polimi.ingsw.control.ClientController;
import it.polimi.ingsw.control.network.NetworkClient;
import it.polimi.ingsw.control.network.commands.Request;
import it.polimi.ingsw.control.network.commands.Response;
import it.polimi.ingsw.control.network.commands.requests.CreateUserRequest;
import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
import it.polimi.ingsw.model.clientModel.ClientContext;
import it.polimi.ingsw.view.AbstractView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientControllerTest {
    private ClientController clientController;
    private NetworkClient client;
    private ClientContext clientContext;
    private AbstractView view;
    private CreateUserResponse createUserResponse;

    @Before
    public void before() throws Exception{
        client = mock(NetworkClient.class);
        clientContext = ClientContext.get();
        view = mock(AbstractView.class);

        clientController = ClientController.getInstance(client);
    }

//    @Test
//    public void createUserTest() throws Exception {
//        String user = "user1";
//        String pass = "123";
//        String token = "token";
//        createUserResponse = mock(CreateUserResponse.class);
//
//        when(createUserResponse.userToken).thenReturn(token);
//        when(createUserResponse.username).thenReturn(user);
//        when(createUserResponse.error).thenReturn(null);
//        when(client.request(new CreateUserRequest(user, pass))).thenReturn(createUserResponse);
//
//        String clientContextUser = clientController.createUser(user, pass);
//
//        Assert.assertEquals(user, clientContextUser);
//        Assert.assertEquals(user, clientContext.getUsername());
//        Assert.assertEquals(token, clientContext.getUserToken());
//
//    }
}
