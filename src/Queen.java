public class Queen extends Piece {
    public Queen(String color){
        super(color, "♛");
    }
    @Override
    public Piece copy() {
        return new Queen(this.color());
    }
    @Override
    public boolean isLegalMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        return super.isLegalMove(board, row, col, targetRow, targetCol) &&
                (isLegalLineMove(board, row, col, targetRow, targetCol) ||
                isLegalDiagonalMove(board, row, col, targetRow, targetCol));
    }


}
