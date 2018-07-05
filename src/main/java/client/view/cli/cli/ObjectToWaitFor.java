package client.view.cli.cli;

import client.controller.CliController;

public enum ObjectToWaitFor {
    PLAYERS,
    GAME,
    PRIVATE_OBJS,
    WPCS,
    TOOLCARDS,
    POC,
    TURN;

    public boolean isArrived(CliController controller){
        switch (this){
            case PLAYERS:
                return controller.areAllPlayersInGame();
            case GAME:
                return controller.isGameStarted();
            case PRIVATE_OBJS:
                return controller.arePrivateObjectivesArrived();
            case WPCS:
                return controller.areWpcsArrived();
            case TOOLCARDS:
                return controller.areToolcardsArrived();
            case POC:
                return controller.arePocsArrived();
            case TURN:
                return controller.isActive();
        }
        return false;
    }
}
