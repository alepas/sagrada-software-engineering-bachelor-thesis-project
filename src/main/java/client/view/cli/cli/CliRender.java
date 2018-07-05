package client.view.cli.cli;

import client.constants.CliConstants;
import shared.clientInfo.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static client.constants.CliRenderConstants.*;


class CliRender {
    private int wpcRows = 0;
    private int wpcCols = 0;
    private int wpcHeight = 0;
    private int wpcLenght = 0;
    private int wpcDistance = CliConstants.WPC_SPACING;
    private int strNum = CliConstants.COUNTER_START_NUMBER;

    private final String WPC_BORDER_COLOR = BLACK;
    private final String NULL_COLOR_CELL = BLACK_BRIGHT;


    //Dices
    private final String WPC_COLUMNS_NUMBERS = "       " + strNum + "       " + (strNum+1) + "       " + (strNum+2) +
                                              "       " + (strNum+3) + "       " + (strNum+4) + "    ";
    private final String wpcLine = WPC_BORDER_COLOR + "   +-------+-------+-------+-------+-------+";
    private final String colSeparator = WPC_BORDER_COLOR + "|";

    private final String[] emptyDice = {"       ",
                                        "       ",
                                        "       "};


    private final String[] dice1 = {"       ",
                                    "   o   ",
                                    "       "};

    private final String[] dice2 = {" o     ",
                                    "       ",
                                    "     o "};

    private final String[] dice3 = {" o     ",
                                    "   o   ",
                                    "     o "};

    private final String[] dice4 = {" o   o ",
                                    "       ",
                                    " o   o "};

    private final String[] dice5 = {" o   o ",
                                    "   o   ",
                                    " o   o "};

    private final String[] dice6 = {" o   o ",
                                    " o   o ",
                                    " o   o "};

    private final String[] diceWithNumber1 = {  "       ",
                                                "   o   ",
                                                "       "};

    private final String[] diceWithNumber2 = {  " o     ",
                                                "       ",
                                                "     o "};

    private final String[] diceWithNumber3 = {  " o     ",
                                                "   o   ",
                                                "     o "};

    private final String[] diceWithNumber4 = {  " o   o ",
                                                "       ",
                                                " o   o "};

    private final String[] diceWithNumber5 = {  " o   o ",
                                                "   o   ",
                                                " o   o "};
    
    private final String[] diceWithNumber6 = {  " o   o ",
                                                " o   o ",
                                                " o   o "};

    private final String[] cellColorRestricted =   {"///////",
                                                    "///////",
                                                    "///////"};

    private final String[][] dices = {emptyDice, dice1, dice2, dice3, dice4, dice5, dice6};
    private final String[][] dicesWithNumber = {emptyDice, diceWithNumber1, diceWithNumber2, diceWithNumber3, diceWithNumber4, diceWithNumber5, diceWithNumber6};


    public CliRender() { }

    public String renderDice(ClientDice dice){
        StringBuilder diceRendered = new StringBuilder();
        String[] stringDice = renderCell(dice.getDiceNumber(), getStringColor(dice.getDiceColor(), dice.getDiceNumber()));

        for(String str : stringDice){
            diceRendered.append(str + "\n");
        }
        diceRendered.append(CliConstants.ID + dice.getDiceID() + "\n");

        return diceRendered.toString();
    }

    public String renderDices(ArrayList<ClientDice> dices){
        StringBuilder diceRendered = new StringBuilder();
        String[][] stringDices = new String[dices.size()][CELL_HEIGHT +1];

        String diceSpacing = calculateSpace(DICE_DISTANCE, null, DICE_LENGHT);
        String titleSpacing;

        for(int i = 0; i < dices.size(); i++){
            String[] temp = renderCell(dices.get(i).getDiceNumber(), getStringColor(dices.get(i).getDiceColor(), dices.get(i).getDiceNumber()));
            for(int j = 0; j < temp.length; j++){
                stringDices[i][j] = temp[j];
            }
            stringDices[i][CELL_HEIGHT] = CliConstants.ID + dices.get(i).getDiceID();
        }


        for(int diceRow = 0; diceRow < CELL_HEIGHT +1; diceRow++){
            for(int dice = 0; dice < dices.size(); dice++){
                if (diceRow != CELL_HEIGHT) diceRendered.append(stringDices[dice][diceRow] + diceSpacing);
                else {
                    diceRendered.append(stringDices[dice][diceRow] + calculateSpace(DICE_DISTANCE, stringDices[dice][diceRow], DICE_LENGHT));
                }
            }
            diceRendered.append("\n");
        }

        return diceRendered.toString();
    }

    private void getWpcDimension(ClientWpc wpc) {
        if (wpcHeight == 0) {
            int rows = 0;
            int cols = 0;
            for (ClientCell cell : wpc.getSchema()) {
                if (cell.getCellPosition().getRow() + 1 > rows) rows = cell.getCellPosition().getRow() + 1;
                if (cell.getCellPosition().getColumn() + 1 > cols) cols = cell.getCellPosition().getColumn() + 1;
            }
            wpcRows = rows;
            wpcCols = cols;
            wpcHeight = (CELL_HEIGHT + 1) * rows + 3;
            wpcLenght = (DICE_LENGHT + 1) * cols + 1;
        }
    }

    //Restiusce la stringa che rappresenta la wpc su cli
    public String renderWpc(ClientWpc wpc, boolean withID){
        getWpcDimension(wpc);
        StringBuilder wpcRendered = new StringBuilder();
        String[] stringWpc = convertWpcToString(wpc);

        if (withID) wpcRendered.append(CliConstants.ID + wpc.getWpcID() + FAVOURS + wpc.getFavours() + "\n");
        for (String row : stringWpc){
            wpcRendered.append(row + "\n");
        }

        return wpcRendered.append(RESET).toString();
    }

    //Restituisce la stringa che rappresenta le wpc passate su cli, distanziate di distance carattateri
    public String renderWpcs(ClientWpc[] wpcs, int distance){
        getWpcDimension(wpcs[0]);
        StringBuilder wpcsRendered = new StringBuilder();
        String[][] stringWpcs = new String[wpcs.length][];

        String wpcSpacing = calculateSpace(distance, null, wpcLenght);
        String titleSpacing;
        String title;

        for(int i = 0; i < wpcs.length; i++){
            stringWpcs[i] = convertWpcToString(wpcs[i]);
            title = CliConstants.ID + wpcs[i].getWpcID() + FAVOURS + wpcs[i].getFavours();
            titleSpacing = calculateSpace(distance, title, wpcLenght+3);
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

    private String calculateSpace(int distance, String title, int width){
        StringBuilder spacing = new StringBuilder();

        int stringSpace;
        if (title != null) stringSpace = width - title.length() + distance;
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

        stringWpc[i++] = CliConstants.VOID_STRING;
        stringWpc[i++] = WPC_COLUMNS_NUMBERS;
        stringWpc[i++] = wpcLine;

        ArrayList<ClientCell> allCells = wpc.getSchema();

        for(int row = 0; row < wpcRows; row++){
            ClientCell[] rowCells = getRowCells(allCells, row);
            String[][] stringsCells = convertCellsToString(rowCells);

            for(int cellRow = 0; cellRow < CELL_HEIGHT; cellRow++){
                str = new StringBuilder();
                str.append(cellRow == 1 ? (row+strNum) + "  " : "   ");
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
            Position pos = cell.getCellPosition();
            if (pos.getRow() == row) rowCellsByCol.put(pos.getColumn(), cell);
        }

        ClientCell[] rowCells = new ClientCell[wpcCols];

        for(int i = 0; i < wpcCols; i++){
            rowCells[i] = rowCellsByCol.get(i);
        }

        return rowCells;
    }

    //Converte le celle passate in stringhe
    private String[][] convertCellsToString(ClientCell[] rowCells) {
        String[][] cell = new String[wpcCols][];
        for(int i = 0; i < wpcCols; i++){
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

        color = getStringColor(cellColor, num);
        
        return renderCell(num, color);
    }

    private ClientColor cellColor(ClientCell rowCell) {
        if (rowCell.getCellDice() != null){
            return rowCell.getCellDice().getDiceColor();
        } else {
            return rowCell.getCellColor();
        }
    }
    
    private String getStringColor(ClientColor color, int num){
        if (color != null) {
            switch (color) {
                case GREEN:
                    if (num == 0) return GREEN;
                    else return GREEN_BACKGROUND + BLACK;
                case RED:
                    if (num == 0) return RED;
                    else return RED_BACKGROUND + BLACK;
                case YELLOW:
                    if (num == 0) return YELLOW;
                    else return YELLOW_BACKGROUND + BLACK;
                case BLUE:
                    if (num == 0) return BLUE;
                    else return BLUE_BACKGROUND + BLACK;
                case VIOLET:
                    if (num == 0) return PURPLE;
                    else return PURPLE_BACKGROUND + BLACK;
                default:
                    return NULL_COLOR_CELL;
            }
        } else { return NULL_COLOR_CELL; }
    }

    //Genera la stringa che rappresenta una cella con numero e colori passati
    private String[] renderCell(int num, String color) {
        String[] diceRows;

        if (!color.equals(NULL_COLOR_CELL)) {
            if (num == 0) diceRows = Arrays.copyOf(cellColorRestricted, cellColorRestricted.length);
            else diceRows = Arrays.copyOf(dicesWithNumber[num], CELL_HEIGHT);
        }
        else diceRows = Arrays.copyOf(dices[num], CELL_HEIGHT);
        
        for(int row = 0; row < diceRows.length; row++){
            diceRows[row] = color + diceRows[row] + RESET;
        }
        
        return diceRows;
    }
}
