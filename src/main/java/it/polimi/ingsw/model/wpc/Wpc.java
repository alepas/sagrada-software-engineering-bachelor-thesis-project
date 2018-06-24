package it.polimi.ingsw.model.wpc;

import it.polimi.ingsw.model.clientModel.ClientCell;
import it.polimi.ingsw.model.clientModel.ClientDice;
import it.polimi.ingsw.model.clientModel.ClientWpc;
import it.polimi.ingsw.model.clientModel.Position;
import it.polimi.ingsw.model.constants.WpcConstants;
import it.polimi.ingsw.model.dicebag.Color;
import it.polimi.ingsw.model.dicebag.Dice;

import java.util.ArrayList;

import static it.polimi.ingsw.model.constants.WpcConstants.COLS_NUMBER;
import static it.polimi.ingsw.model.constants.WpcConstants.ROWS_NUMBER;


public class Wpc {
    private String id;
    private int favours;
    ArrayList<Cell> schema = new ArrayList<>();
    private boolean firstDicePutted = false;
    private boolean onlyFirstDice = false;


    /**
     * creates the new object wpc
     *
     * @param id      is the id of the new object schema
     * @param favours is the number of favours of the schema
     * @param schema  is the Arraylist composed by cell's objects
     */
    Wpc(String id, int favours, ArrayList<Cell> schema) {
        this.id = id;
        this.favours = favours;
        for (Cell cell : schema)
            this.schema.add(new Cell(cell));
    }


    /**
     * @return the new object with is a copy of this.
     */
    public Wpc copyWpc() {
        return new Wpc(id, favours, schema);
    }


    /**
     * @return an object equivalent to this but for the client model
     */
    public ClientWpc getClientWpc() {
        ArrayList<ClientCell> cells = new ArrayList<>();
        for (Cell cell : schema) {
            Dice dice = cell.getDice();
            ClientDice clientDice;
            if (dice != null) clientDice = dice.getClientDice();
            else clientDice = null;

            cells.add(new ClientCell(clientDice, Color.getClientColor(cell.getColor()),
                    cell.getNumber(), cell.getCellPosition()));
        }
        return new ClientWpc(id, favours, cells);
    }

    public int getFavours() {
        return favours;
    }

    public ArrayList<Cell> getSchema() {
        return schema;
    }

    public String getId() {
        return id;
    }


    /**
     * @param dice is the object the player want to add to a schema's cell
     * @return true if there is at least a cell where it is possible to add the dice
     */
    public boolean isDicePlaceable(Dice dice) {
        Position position;
        for (Cell cell : schema) {
            if (cell.getDice() == null) {
                position = cell.getCellPosition();
                if (!firstDicePutted) {
                    if (checkFirstTurnRestriction(cell) && checkCellRestriction(cell, dice)) return true;
                } else {
                    ArrayList<Cell> orthoCells = getOrthogonallyAdjacentCells(position);
                    ArrayList<Cell> diagCells = getDiagonallyAdjacentCells(position);
                    if (checkCellRestriction(cell, dice)
                            && checkAdjacentDiceRestriction(orthoCells, dice)
                            && isThereAtLeastADiceNear(orthoCells, diagCells)) return true;
                }
            }
        }
        return false;
    }


    /**
     * this method is called when the current player turn ends before he/she can terminate to use a ToolCard, this method
     * try to place the selected dice in the first acceptable position.
     *
     * @param dice is the chosen dice selected by the player while he/she was using a ToolCard
     * @return true if there is a cell where it is possibe to add the dice, false if not
     */
    public boolean autoAddDice(Dice dice) {
        Position tempPos;
        for (Cell cell : schema) {

            if (cell != null && cell.getDice() == null) {
                tempPos = cell.getCellPosition();
                if (!firstDicePutted) {
                    if (checkFirstTurnRestriction(cell) && checkCellRestriction(cell, dice)) {
                        cell.setDice(dice);
                        firstDicePutted = true;
                        onlyFirstDice = true;
                        return true;
                    }
                } else {
                    ArrayList<Cell> orthoCells = getOrthogonallyAdjacentCells(tempPos);
                    ArrayList<Cell> diagCells = getDiagonallyAdjacentCells(tempPos);

                    if (checkCellRestriction(cell, dice)
                            && checkAdjacentDiceRestriction(orthoCells, dice)
                            && isThereAtLeastADiceNear(orthoCells, diagCells)) {
                        cell.setDice(dice);
                        onlyFirstDice = false;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Controls if it is possible to add the dice in the position selected by the player.
     *
     * @param dice chosen by the player
     * @param pos  is the position where the player wants to add the dice
     * @return true if the dice has been set correctly in the chosen position, false if it was not possible to add the dice
     * in the chosen position
     */
    public boolean addDiceWithAllRestrictions(Dice dice, Position pos) {
        Cell cell = getCellFromPosition(pos);
        if (cell == null)
            return false;
        if (cell.getDice() != null)
            return false;
        if (!firstDicePutted) {
            if (checkFirstTurnRestriction(cell) && checkCellRestriction(cell, dice)) {
                cell.setDice(dice);
                firstDicePutted = true;
                onlyFirstDice = true;
                return true;
            }
        } else {
            ArrayList<Cell> orthoCells = getOrthogonallyAdjacentCells(pos);
            ArrayList<Cell> diagCells = getDiagonallyAdjacentCells(pos);

            if (checkCellRestriction(cell, dice)
                    && checkAdjacentDiceRestriction(orthoCells, dice)
                    && isThereAtLeastADiceNear(orthoCells, diagCells)) {
                cell.setDice(dice);
                onlyFirstDice = false;
                return true;
            }
        }
        return false;
    }


    /**
     * Checks, by the given parameters, if it is possible to add the dice in the chosen position
     *
     * @param dice             is the dice chosen by the player
     * @param pos              is the position where the player would like to add the dice
     * @param ColorRestr       if it's true it means that the color restrictions must be considered, if it's false it is
     *                         possible to add a dice on a cell with a color that is different from his or close to a dice
     *                         with the same color.
     * @param ValueRestr       if it's true it means that the number restrictions must be considered, if it's false it is
     *                         possible to add a dice on a cell with a number that is different from his or close to a dice
     *                         with the same number.
     * @param PlacementRestr   if it's true it means the placement must fallow all the placement rules
     * @param atLeastADiceNear if it's true it means that there must be at least a dice on of the cells close to the
     *                         chosen one
     * @param noDicesNear      if it's true it means that there must not be dices in the cells close to the chosen one
     * @return true iff it is possible to add the dice in the chosen position, false if it is not possible.
     */
    public boolean addDicePersonalizedRestrictions(Dice dice, Position pos, boolean ColorRestr, boolean ValueRestr, boolean PlacementRestr, boolean atLeastADiceNear, boolean noDicesNear) {
        Cell cell = getCellFromPosition(pos);

        assert cell != null;
        if (!firstDicePutted && !checkFirstTurnRestriction(cell)) return false;

        if (ColorRestr && !checkOnlyColorCellRestriction(cell, dice)) return false;

        if (ValueRestr && !checkOnlyNumberCellRestriction(cell, dice)) return false;

        ArrayList<Cell> orthoCells = getOrthogonallyAdjacentCells(pos);
        ArrayList<Cell> diagCells = getDiagonallyAdjacentCells(pos);

        if (PlacementRestr && !checkAdjacentDiceRestriction(orthoCells, dice)) return false;

        if (atLeastADiceNear && firstDicePutted && !isThereAtLeastADiceNear(orthoCells, diagCells)) return false;

        if (noDicesNear && isThereAtLeastADiceNear(orthoCells, diagCells)) return false;

        cell.setDice(dice);
        if (onlyFirstDice)
            onlyFirstDice = false;
        if (!firstDicePutted) {
            firstDicePutted = true;
            onlyFirstDice = true;
        }
        return true;
    }


    /**
     * Removes the dice from the chosen position, if the dice was the only one inside the schema sets to false the
     * boolean related to the rules which wants the first dice been added in particular areas
     *
     * @param position is the position where is the dice the player wants to remove
     * @return the removed dice
     */
    public Dice removeDice(Position position) {
        Cell cell = getCellFromPosition(position);
        assert cell != null;
        if (onlyFirstDice) {
            firstDicePutted = false;
            onlyFirstDice = false;
        }
        return cell.removeDice();
    }


    // public Dice getDiceFromPosition(Position pos){ return Objects.requireNonNull(getCellFromPosition(pos)).getDice(); }

    /**
     * Creates a new object dice and position with the dice related to the id and his position.
     *
     * @param diceId is the ID of the dice selected
     * @return the new object dice and position
     */
    public DiceAndPosition getDiceAndPosition(int diceId) {
        Dice dice;
        for (Cell cell : schema) {
            if ((dice = cell.getDice()) != null)
                if (dice.getId() == diceId)
                    return new DiceAndPosition(cell.getDice(), cell.getCellPosition());
        }
        return null;
    }

   /* public Position getPositionFromDice(int diceID){
        for(Cell cell : schema) {
            if (cell.getDice().getId() == diceID)
                return cell.getCellPosition();
        }
        return null;
    }*/


    /**
     * @param pos is the position selected
     * @return the cell associated to the given postion
     */
    Cell getCellFromPosition(Position pos) {
        if (pos.getColumn() >= COLS_NUMBER)
            return null;
        if (pos.getRow() >= ROWS_NUMBER)
            return null;
        return schema.get(pos.getRow() * WpcConstants.COLS_NUMBER + pos.getColumn());
    }


    /*private void checkCellExistence(Cell cell) throws NotExistingCellException {
        //controllo se la cella esiste
        for (Cell schemaCell : schema){
            if (schemaCell.equals(cell)) return;
        }

        throw new NotExistingCellException(cell);
    }*/


    /**
     * @param cell is the cell chosen by the player
     * @return true if the cell is a border one
     */
    boolean checkFirstTurnRestriction(Cell cell) {
        int row = cell.getCellPosition().getRow();
        int column = cell.getCellPosition().getColumn();
        return row == 0 || column == 0 || row == ROWS_NUMBER - 1 || column == COLS_NUMBER - 1;
    }


    /**
     * Checks is the placement respect the vale and color restrictions.
     *
     * @param cell is the cell where the user wnats to place the dice
     * @param dice is the dice chosen by the user
     * @return true iff it is possible to add the dice in the chosen cell
     */
    boolean checkCellRestriction(Cell cell, Dice dice) {
        if (cell.getDice() == null) {
            if (cell.getNumber() == 0 && cell.getColor() != null)
                return (cell.getColor().equals(dice.getDiceColor()));
            else if (cell.getColor() == null && cell.getNumber() == 0)
                return true;
            else
                return (cell.getNumber() == dice.getDiceNumber());
        } else
            return false;
    }


    /**
     * Checks if a dice can be add to the chosen cell without caring of the color restriction
     *
     * @param cell is the cell chosen by the user
     * @param dice is the dice chosen by the user
     * @return true if it is possible to add the dice in the cell
     */
    boolean checkOnlyNumberCellRestriction(Cell cell, Dice dice) {
        if (cell.getDice() != null)
            return false;
        if (cell.getNumber() == 0)
            return true;
        return cell.getNumber() == dice.getDiceNumber();
    }


    /**
     * Checks if a dice can be add to the chosen cell without caring of the number restriction
     *
     * @param cell is the cell chosen by the user
     * @param dice is the dice chosen by the user
     * @return true if it is possible to add the dice in the cell
     */
    boolean checkOnlyColorCellRestriction(Cell cell, Dice dice) {
        if (cell.getDice() != null)
            return false;
        if (cell.getColor() == null)
            return true;

        return cell.getColor().equals(dice.getDiceColor());
    }


    /**
     * Checks if there's at least a dice in an adjacent cell.
     *
     * @param orthoCell list of all the cells orthogonal to the chosen one
     * @param dice      is the chosen dice
     * @return false if an adjacent dice has same color of number to the chosen one
     */
    boolean checkAdjacentDiceRestriction(ArrayList<Cell> orthoCell, Dice dice) {
        boolean condition = true;
        for (Cell cell : orthoCell) {
            if (cell.getDice() != null)
                condition &= !checkDiceEquivalence(cell.getDice(), dice);
        }
        return condition;
    }


    /**
     * Checks if the schema's cell is orthogonally adjacent to the cell where the player wants to place a dice.
     *
     * @param cell   is a cell of the schema
     * @param row    is the row of the chosen cell
     * @param column is the column of the chosen cell
     * @return true if the cell is adjacent to the chosen one
     */
    boolean isOrthogonallyAdjacentCell(Cell cell, int row, int column) {
        int adjacentRow = cell.getCellPosition().getRow();
        int adjacentColumn = cell.getCellPosition().getColumn();
        return ((adjacentRow == row && (adjacentColumn == (column + 1) || adjacentColumn == (column - 1)))
                || (adjacentColumn == column && (adjacentRow == row - 1 || adjacentRow == row + 1)));
    }


    /**
     * Checks if the schema's cell is diagonally adjacent to the cell where the player wants to place a dice.
     *
     * @param cell   is a cell of the schema
     * @param row    is the row of the chosen cell
     * @param column is the column of the chosen cell
     * @return true if the cell is adjacent to the chosen one
     */
    boolean isDiagonallyAdjacentCell(Cell cell, int row, int column) {
        int adjacentRow = cell.getCellPosition().getRow();
        int adjacentColumn = cell.getCellPosition().getColumn();
        return (((adjacentRow == row - 1) && ((adjacentColumn == column - 1) || (adjacentColumn == column + 1)))
                || ((adjacentRow == row + 1) && ((adjacentColumn == column - 1) || (adjacentColumn == column + 1))));
    }


    /**
     * Finds all the orthogonal cells to the chosen one
     *
     * @param position is the chosen cell position
     * @return the list of all orthogonal cells to the chosen one
     */
    private ArrayList<Cell> getOrthogonallyAdjacentCells(Position position) {
        ArrayList<Cell> orthoCells = new ArrayList<>();
        for (Cell schemaCell : this.schema) {
            if (isOrthogonallyAdjacentCell(schemaCell, position.getRow(), position.getColumn()))
                orthoCells.add(schemaCell);
        }
        return orthoCells;
    }


    /**
     * Finds all the diagonal cells to the chosen one
     *
     * @param position is the chosen cell position
     * @return the list of all cells diagonal to the chosen one
     */
    private ArrayList<Cell> getDiagonallyAdjacentCells(Position position) {
        ArrayList<Cell> diagCells = new ArrayList<>();
        for (Cell schemaCell : this.schema) {
            if (isDiagonallyAdjacentCell(schemaCell, position.getRow(), position.getColumn()))
                diagCells.add(schemaCell);
        }
        return diagCells;
    }


    /**
     * Checks if there' s at least a dice in an adjacent cell.
     *
     * @param orthoCells list of cells orthogonally adjacent to the chosen cell
     * @param diagCells  list of cells diagonally adjacent to the chosen cell
     * @return true if there is at least a dice in one of those cells
     */
    boolean isThereAtLeastADiceNear(ArrayList<Cell> orthoCells, ArrayList<Cell> diagCells) {
        for (Cell cell : orthoCells) {
            if (cell.getDice() != null)
                return true;
        }
        for (Cell cell : diagCells) {
            if (cell.getDice() != null)
                return true;
        }
        return false;
    }


    /**
     * checks if the two dices are equivalent or not.
     *
     * @param cellDice is the dice in a cell adjacent to the chosen one
     * @param dice     is the dice chosen by the user
     * @return true if the two dices have same number or color or both
     */
    private boolean checkDiceEquivalence(Dice cellDice, Dice dice) {
        return dice.getDiceNumber() == cellDice.getDiceNumber() || dice.getDiceColor().equals(cellDice.getDiceColor());
    }


    /**
     * @param row a row of the schema
     * @return a list composed by all dices and empty spaces in the chosen row
     */
    public ArrayList<Dice> getRowDicesAndEmptySpaces(int row) {
        ArrayList<Dice> rowDices = new ArrayList<>();
        for (Cell cell : schema) {
            if (cell.getCellPosition().getRow() == row) {
                rowDices.add(cell.getDice());
            }
        }

        return rowDices;
    }

    /**
     * @param row a row of the schema
     * @return a list composed by all dices in the chosen row
     */
    public ArrayList<Dice> getRowDices(int row) {
        ArrayList<Dice> rowDices = new ArrayList<>();

        for (Cell cell : schema)
            if (cell.getCellPosition().getRow() == row && cell.getDice() != null)
                rowDices.add(cell.getDice());

        return rowDices;
    }


    /**
     * @param column a column of the schema
     * @return a list composed by all dices in the chosen column
     */
    public ArrayList<Dice> getColDices(int column) {
        ArrayList<Dice> columnDices = new ArrayList<>();

        for (Cell cell : schema) {
            if (cell.getCellPosition().getColumn() == column && cell.getDice() != null)
                columnDices.add(cell.getDice());
        }

        return columnDices;
    }


    /**
     * @return the list of all dices in the schema
     */
    public ArrayList<Dice> getWpcDices() {
        ArrayList<Dice> WPCDices = new ArrayList<>();

        for (Cell cell : schema) {
            if (cell.getDice() != null) WPCDices.add(cell.getDice());
        }

        return WPCDices;
    }


    /**
     * @param shade is the dice number
     * @return how many dices are there in the schema with the chosen number
     */
    public int numDicesOfShade(int shade) {
        int count = 0;

        for (Dice dice : getWpcDices()) {
            if (dice.getDiceNumber() == shade) count++;
        }

        return count;
    }


    /**
     * @param color the dice color
     * @return how many dices are there in the schema with the chosen color
     */
    public int numDicesOfColor(Color color) {
        int count = 0;

        for (Dice dice : getWpcDices()) {
            if (dice.getDiceColor().equals(color)) count++;
        }

        return count;
    }


    /**
     * @return how many dices are there are in the schema
     */
    public int getNumOfDices() {
        int count = 0;
        for (Cell cell : schema) {
            if (cell.getDice() != null) count++;
        }
        return count;
    }


    /**
     * Counts the cells having dice = null.
     *
     * @return an integer which represent the number of cells with dice= null
     */
    public int getNumFreeCells() {
        int count = 0;
        for (Cell cell : schema) if (cell.getDice() == null) count++;
        return count;
    }

}
