# ing-sw-2018-zorzenon-pasini-piovani

##Group members
* Pasini Alessandra (Matricola: 843807, Codice persona: 10488785) 
* Piovani Davidde (Matricola: 846034, Codice persona: 10528265) 
* Zorzenon Luca (Matricola: 845560, Codice persona: 10525863)

##Test Coverage: 31.8%

##Uml image

##Functions
* Complete rules
* CLI
* GUI
* RMI
* Socket
* Advance functionality: Multiple games
* Other:
    * Predisposition for Singleplayer Game
    * Predisposition for Persistence
    * CLI support for multilanguages
    * GUI predisposition for multilanguages

##Limitations
* Singleplayer not working yet
* Won/Lost/Left games are saved in db and players can see their stats. There is also a partial implementation of the ranking functionality: at the end of every game each player gets points based on game result. A player can see his ranking value but not other players scores. <br>
We thought at a ranking mode where there are different tables based on players number, that's why you're able to choose how many players you want to play with.
* GUI strings need to be translated, that's why there is only a predisposition for multilanguages.
* CLI has problems displaying colors on windows console