package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.clientModel.ClientCell;
import it.polimi.ingsw.model.clientModel.ClientColor;
import it.polimi.ingsw.model.clientModel.ClientPosition;
import it.polimi.ingsw.model.clientModel.ClientWpc;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.wpc.Cell;
import it.polimi.ingsw.model.wpc.Position;
import it.polimi.ingsw.model.wpc.WPC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CliRender {
    private final int cellHeight = 3;
    private final int wpcHeight = (cellHeight+1)*WpcConstants.ROWS_NUMBER + 1;
    private final int wpcLenght = 8*WpcConstants.COLS_NUMBER+1;


    //Colors
    // Reset
    private final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    private final String BLACK = "\033[0;30m";   // BLACK
    private final String RED = "\033[0;31m";     // RED
    private final String GREEN = "\033[0;32m";   // GREEN
    private final String YELLOW = "\033[0;33m";  // YELLOW
    private final String BLUE = "\033[0;34m";    // BLUE
    private final String PURPLE = "\033[0;35m";  // PURPLE
    private final String CYAN = "\033[0;36m";    // CYAN
    private final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    private final String BLACK_BOLD = "\033[1;30m";  // BLACK
    private final String RED_BOLD = "\033[1;31m";    // RED
    private final String GREEN_BOLD = "\033[1;32m";  // GREEN
    private final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    private final String BLUE_BOLD = "\033[1;34m";   // BLUE
    private final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    private final String CYAN_BOLD = "\033[1;36m";   // CYAN
    private final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Background
    private final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    private final String RED_BACKGROUND = "\033[41m";    // RED
    private final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    private final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    private final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    private final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    private final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    private final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    private final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    private final String RED_BRIGHT = "\033[0;91m";    // RED
    private final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    private final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    private final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    private final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    private final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    private final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    private final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    private final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    private final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    private final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    private final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    private final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    private final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    private final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    private final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    private final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    private final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    private final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    private final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    private final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    private final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    private final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

    private final String WPC_BORDER_COLOR = BLACK;
    private final String NULL_COLOR_CELL = BLACK_BRIGHT;


    //Dices
    private final String wpcLine = WPC_BORDER_COLOR + "+-------+-------+-------+-------+-------+";
    private final String colSeparator = WPC_BORDER_COLOR + "|";

    private final String[] emptyDice = {"       ",
                                        "       ",
                                        "       "};


    private final String[] dice1 = {"       ",
                                    "   O   ",
                                    "       "};

    private final String[] dice2 = {" O     ",
                                    "       ",
                                    "     O "};

    private final String[] dice3 = {" O     ",
                                    "   O   ",
                                    "     O "};

    private final String[] dice4 = {" O   O ",
                                    "       ",
                                    " O   O "};

    private final String[] dice5 = {" O   O ",
                                    "   O   ",
                                    " O   O "};

    private final String[] dice6 = {" O   O ",
                                    " O   O ",
                                    " O   O "};

    private final String[] cellColorRestricted =   {"XXXXXXX",
                                                    "XXXXXXX",
                                                    "XXXXXXX"};
//    private final String[] cellColorRestricted =   {"       ",
//                                                    "       ",
//                                                    "       "};

    private final String[][] dices = {emptyDice, dice1, dice2, dice3, dice4, dice5, dice6};


    public CliRender() { }

    //Restiusce la stringa che rappresenta la wpc su cli
    public String renderWpc(ClientWpc wpc){
        StringBuilder wpcRendered = new StringBuilder();
        String[] stringWpc = convertWpcToString(wpc);

        wpcRendered.append("ID:" + wpc.getWpcID() + "\tFavours: " + wpc.getFavours() + "\n");
        for (String row : stringWpc){
            wpcRendered.append(row + "\n");
        }

        return wpcRendered.append(RESET + "\n").toString();
    }

    //Restituisce la stringa che rappresenta le wpc passate su cli, distanziate di distance carattateri
    public String renderWpcs(ClientWpc[] wpcs, int distance){
        StringBuilder wpcsRendered = new StringBuilder();
        String[][] stringWpcs = new String[wpcs.length][];

        String wpcSpacing = calculateSpace(distance, null);
        String titleSpacing;
        String title;

        for(int i = 0; i < wpcs.length; i++){
            stringWpcs[i] = convertWpcToString(wpcs[i]);
            title = "ID: " + wpcs[i].getWpcID() + "    Favours: " + wpcs[i].getFavours();
            titleSpacing = calculateSpace(distance, title);
            wpcsRendered.append(title + titleSpacing);
        }
        wpcsRendered.append("\n");

        for(int row = 0; row < wpcHeight; row++){
            for(int numWpc = 0; numWpc < wpcs.length; numWpc++){
                wpcsRendered.append(stringWpcs[numWpc][row]);
                wpcsRendered.append(wpcSpacing);
            }
            wpcsRendered.append("\n");
        }

        return wpcsRendered.append(RESET + "\n").toString();
    }

    private String calculateSpace(int distance, String title){
        StringBuilder spacing = new StringBuilder();

        int stringSpace;
        if (title != null) stringSpace = wpcLenght - title.length() + distance;
        else stringSpace = distance;

        for(int i = 0; i < stringSpace; i++){
            spacing.append(" ");
        }

        return spacing.toString();
    }

    //Converte la wpc in un array di stringhe dove ogni elemento dell'array rappresenta una riga
    //della wpc convertita in stringa
    private String[] convertWpcToString(ClientWpc wpc){
        String[] stringWpc = new String[wpcHeight];
        StringBuilder str;
        int i = 0;

        stringWpc[i++] = wpcLine;

        ArrayList<ClientCell> allCells = wpc.getSchema();

        for(int row = 0; row < WpcConstants.ROWS_NUMBER; row++){
            ClientCell[] rowCells = getRowCells(allCells, row);
            String[][] stringsCells = convertCellsToString(rowCells);

            for(int cellRow = 0; cellRow < cellHeight; cellRow++){
                str = new StringBuilder();
                for(String[] cell : stringsCells){
                    str.append(colSeparator);
                    str.append(cell[cellRow]);
                }
                str.append(colSeparator);
                stringWpc[i++] = str.toString();
            }

            stringWpc[i++] = wpcLine;
        }

        return stringWpc;
    }

    //Restituisce le celle nella wpc della riga passata ordinate
    private ClientCell[] getRowCells(ArrayList<ClientCell> allCells, int row){
        HashMap<Integer, ClientCell> rowCellsByCol = new HashMap<>();

        for(ClientCell cell : allCells){
            ClientPosition pos = cell.getCellPosition();
            if (pos.getRow() == row) rowCellsByCol.put(pos.getColumn(), cell);
        }

        int colNumber = WpcConstants.COLS_NUMBER;
        ClientCell[] rowCells = new ClientCell[colNumber];

        for(int i = 0; i < colNumber; i++){
            rowCells[i] = rowCellsByCol.get(i);
        }

        return rowCells;
    }

    //Converte le celle passate in stringhe
    private String[][] convertCellsToString(ClientCell[] rowCells) {
        int colNumber = WpcConstants.COLS_NUMBER;

        String[][] cell = new String[colNumber][];
        for(int i = 0; i < colNumber; i++){
            cell[i] = convertCellToString(rowCells[i]);
        }
        return cell;
    }

    //Converte la cella passata in un array di stringhe, dove ogni elemento rappresenta una riga
    //della cella in formato stringa
    private String[] convertCellToString(ClientCell rowCell) {
        String color;
        ClientColor cellColor = cellColor(rowCell);

        int num;
        if (rowCell.getCellDice() != null) num = rowCell.getCellDice().getDiceNumber();
        else if (rowCell.getCellNumber() != 0) num = rowCell.getCellNumber();
        else num = 0;

        if (cellColor != null) {
            switch (cellColor(rowCell)) {
                case GREEN:
                    if (num == 0) color = GREEN;
                    else color = GREEN_BACKGROUND + WHITE_BRIGHT;
                    break;
                case RED:
                    if (num == 0) color = RED;
                    else color = RED_BACKGROUND_BRIGHT + WHITE_BRIGHT;
                    break;
                case YELLOW:
                    if (num == 0) color = YELLOW;
                    else color = YELLOW_BACKGROUND_BRIGHT + WHITE_BRIGHT;
                    break;
                case BLUE:
                    if (num == 0) color = BLUE;
                    else color = BLUE_BACKGROUND_BRIGHT + WHITE_BRIGHT;
                    break;
                case VIOLET:
                    if (num == 0) color = PURPLE;
                    else color = PURPLE_BACKGROUND + WHITE_BRIGHT;
                    break;
                default:
                    color = NULL_COLOR_CELL;
                    break;
            }
        } else { color = NULL_COLOR_CELL; }
        
        return renderCell(num, color);
    }

    private ClientColor cellColor(ClientCell rowCell) {
        if (rowCell.getCellDice() != null){
            return rowCell.getCellDice().getDiceColor();
        } else {
            return rowCell.getCellColor();
        }
    }

    //Genera la stringa che rappresenta una cella con numero e colori passati
    private String[] renderCell(int num, String color) {
        String[] diceRows;

        if (num == 0 && !color.equals(NULL_COLOR_CELL)) diceRows = Arrays.copyOf(cellColorRestricted, cellColorRestricted.length);
        else diceRows= Arrays.copyOf(dices[num], cellHeight);
        
        for(int row = 0; row < diceRows.length; row++){
            diceRows[row] = color + diceRows[row] + RESET;
        }
        
        return diceRows;
    }
}
