public class Pawn extends Piece {
    private int firstMoveNumber=0;
    public Pawn(String color){
        super(color, "♟");
    }
    public void setFirstMoveNumber(int num){
        if(moveCounter()==0)
            firstMoveNumber=num;
    }

    public int firstMoveNumber(){
        return firstMoveNumber;
    }

    @Override
    public Piece copy() {
        Pawn copy=new Pawn(this.color());
        copy.firstMoveNumber=firstMoveNumber;
        return copy;
    }

    @Override
    public boolean isLegalMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        return super.isLegalMove(board, row, col, targetRow, targetCol) &&
                isCorrectDirection(row, targetRow) &&
                (isLegalLineMove(board, row, col, targetRow, targetCol) ||
                        isLegalDiagonalMove(board, row, col, targetRow, targetCol) ||
                        isEnPassant(board, row, col, targetRow, targetCol));
    }
    public boolean isCorrectDirection(int row, int targetRow){
        return (color().equals("white") && targetRow-row>0) ||
                (color().equals("black") && row-targetRow>0);
    }
    @Override
    public boolean isLegalLineMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        return super.isLegalLineMove(board, row, col, targetRow, targetCol) &&
                board[targetRow-1][targetCol-1]==null &&
                (Math.abs(targetRow-row)==1 || (Math.abs(targetRow-row)==2 && moveCounter()==0));
    }
    @Override
    public boolean isLegalDiagonalMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        return super.isLegalDiagonalMove(board, row, col, targetRow, targetCol) &&
                (Math.abs(row-targetRow)==1 && Math.abs(col-targetCol)==1) &&
                board[targetRow-1][targetCol-1]!=null;
    }

    public boolean isEnPassant(Piece[][] board, int row, int col, int targetRow, int targetCol){
        return board[row-1][targetCol-1] != null &&
                board[row-1][targetCol-1] instanceof Pawn &&
                board[row-1][targetCol-1].moveCounter() == 1 &&
                super.isLegalDiagonalMove(board, row, col, targetRow, targetCol) &&
                (Math.abs(row-targetRow)==1 && Math.abs(col-targetCol)==1) &&
                board[targetRow-1][targetCol-1] == null &&
                canCapture(board[row-1][targetCol-1]) &&
                (targetRow == 3 || targetRow == 6);
    }

}
