//package it.polimi.ingsw.model.clientTest;
//
//import it.polimi.ingsw.control.ClientController;
//import it.polimi.ingsw.control.network.NetworkClient;
//import it.polimi.ingsw.control.network.commands.responses.CreateUserResponse;
//import it.polimi.ingsw.model.clientModel.ClientModel;
//import it.polimi.ingsw.view.AbstractView;
//import org.junit.Before;
//
//import static org.mockito.Mockito.mock;
//
//public class ClientControllerTest {
//    private ClientController clientController;
//    private NetworkClient client;
//    private ClientModel clientModel;
//    private AbstractView view;
//    private CreateUserResponse createUserResponse;
//
//    @Before
//    public void before() throws Exception{
//        client = mock(NetworkClient.class);
//        clientModel = ClientModel.getInstance();
//        view = mock(AbstractView.class);
//
//        clientController =  ClientController.getInstance();
//    }
//
////    @Test
////    public void createUserTest() throws Exception {
////        String user = "user1";
////        String pass = "123";
////        String token = "token";
////        createUserResponse = mock(CreateUserResponse.class);
////
////        when(createUserResponse.userToken).thenReturn(token);
////        when(createUserResponse.username).thenReturn(user);
////        when(createUserResponse.exception).thenReturn(null);
////        when(client.request(new CreateUserRequest(user, pass))).thenReturn(createUserResponse);
////
////        String clientContextUser = clientController.createUser(user, pass);
////
////        Assert.assertEquals(user, clientContextUser);
////        Assert.assertEquals(user, clientModel.getUsername());
////        Assert.assertEquals(token, clientModel.getUserToken());
////
////    }
//}
