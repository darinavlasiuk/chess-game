public class King extends Piece {
    public King(String color){
        super(color, "♚");
    }
    @Override
    public Piece copy() {
        return new King(this.color());
    }

    @Override
    public boolean isLegalMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        return (super.isLegalMove(board, row, col, targetRow, targetCol) &&
                (Math.abs(row-targetRow)<=1 && Math.abs(col-targetCol)<=1)) ||
                isCastling(board, row, col, targetRow, targetCol);
    }
    public boolean isCastling(Piece[][] board, int row, int col, int targetRow, int targetCol){
        if(targetCol<col){
            return super.isLegalLineMove(board, row, col, targetRow, 1) &&
                    moveCounter()==0 &&
                    board[targetRow-1][0].moveCounter()==0 &&
                    (board[targetRow-1][targetCol-1] instanceof Rook ||
                            col-targetCol==2);

        }
        return super.isLegalLineMove(board, row, col, targetRow, 8) &&
                moveCounter()==0 &&
                board[targetRow-1][7].moveCounter()==0 &&
                (board[targetRow-1][targetCol-1] instanceof Rook ||
                        targetCol-col==2);
    }

}

