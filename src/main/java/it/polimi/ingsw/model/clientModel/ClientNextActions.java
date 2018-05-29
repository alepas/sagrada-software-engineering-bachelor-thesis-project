package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public enum ClientNextActions implements Serializable {
    PICKDICE, PICKDICEFROMOTHERUSER, PICKPOSITION, PICKCOLOR, PICKNUMBER, MOVEFINISHED, MENU,MENU_ONLY_PICKDICE, MENU_ONLY_TOOLCARD, TURNFINISHED, PICKDICE_SINGLEPLAYER_CARD;
}
