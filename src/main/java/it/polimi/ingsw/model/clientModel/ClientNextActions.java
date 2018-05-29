package it.polimi.ingsw.model.clientModel;

import java.io.Serializable;

public enum ClientNextActions implements Serializable {
    PICKDICE,                       //Diventa PLACEDICE
    PICKDICEFROMOTHERUSER,          //Eliminare
    PICKPOSITION,                   //Eliminare
    PICKCOLOR,                      //Eliminare
    PICKNUMBER,
    MOVEFINISHED,
    MENU,
    MENU_ONLY_PICKDICE,
    MENU_ONLY_TOOLCARD,
    TURNFINISHED,
    PICKDICE_SINGLEPLAYER_CARD;
    //Aggiungere PLACEDICE FOR TOOLCARD
    //Aggiungere STOPTTOLCARD
}
