public class Rook extends Piece {
    public Rook(String color){
        super(color, "♜");
    }
    @Override
    public Piece copy() {
        return new Rook(this.color());
    }
    @Override
    public boolean isLegalMove(Piece[][] chessboard, int row, int col, int targetRow, int targetCol) {
        return super.isLegalMove(chessboard, row, col, targetRow, targetCol) &&
                isLegalLineMove(chessboard, row, col, targetRow, targetCol);
    }

}
