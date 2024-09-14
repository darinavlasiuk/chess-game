public class Knight extends Piece {
    public Knight(String color){
        super(color, "♞");
    }
    @Override
    public Piece copy() {
        return new Knight(this.color());
    }
    @Override
    public boolean isLegalMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        return super.isLegalMove(board, row, col, targetRow, targetCol) &&
                Math.abs(row-targetRow)+Math.abs(col-targetCol)==3 &&
                (Math.abs(row-targetRow)!=0 && Math.abs(col-targetCol)!=0);
    }

}
