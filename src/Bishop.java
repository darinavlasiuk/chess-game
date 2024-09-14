public class Bishop extends Piece {
    public Bishop(String color){
        super(color, "♝");
    }
    @Override
    public Piece copy() {
        return new Bishop(this.color());
    }
    @Override
    public boolean isLegalMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        return super.isLegalMove(board, row, col, targetRow, targetCol) &&
                isLegalDiagonalMove(board, row, col, targetRow, targetCol);
    }

}
