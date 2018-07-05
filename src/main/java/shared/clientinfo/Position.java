package shared.clientinfo;

import java.io.Serializable;

/**
 * Class that contains the postion of cells
 */
public class Position implements Serializable {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row ;
        this.column = column;
    }


    public int getRow( ){
        return row;
    }

    public int getColumn( ){
        return column;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position){
            Position pos = (Position) obj;
            return pos.getRow() == this.getRow() && pos.getColumn() == this.getColumn();
        }
        return false;
    }
}
