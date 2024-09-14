public abstract class Piece implements Logic {
    private final String color;
    private int moveCounter=0;
    private final String figure;

    public Piece(String color, String figure){
        this.color=color;
        this.figure=figure;
    }
    public abstract Piece copy();

    public String color(){
        return color;
    }
    public String getFigure(){
        return figure;
    }

    public int moveCounter(){
        return moveCounter;
    }

    public void increaseMoveCounter(){
        moveCounter++;
    }
    public boolean canCapture(Piece target){
        return !color.equals(target.color);
    }

    @Override
    public boolean isLegalMove(Piece[][] board, int row, int col, int targetRow, int targetCol) {
        if(board[targetRow-1][targetCol-1]!=null)
            return canCapture(board[targetRow-1][targetCol-1]);
        return true;
    }

    public boolean isLegalLineMove(Piece[][] board, int row, int col, int targetRow, int targetCol){
        if(!isLineMove(row, col, targetRow, targetCol)) return false;
        if(targetRow!=row) {
            for(int i=Math.min(row, targetRow)+1; i<Math.max(row, targetRow); ++i) {
                if(board[i-1][col-1]!=null) return false;
            }
        }
        else{
            for(int j=Math.min(col, targetCol)+1; j<Math.max(col, targetCol); ++j){
                if(board[row-1][j-1]!=null) return false;
            }
        }
        return true;
    }
    public boolean isLineMove(int row, int col, int targetRow, int targetCol){
        return row==targetRow || col==targetCol;
    }
    public boolean isLegalDiagonalMove(Piece[][] board,int row, int col, int targetRow, int targetCol){
        if(!isDiagonalMove(row, col, targetRow, targetCol)) return false;
        int drow=targetRow>row ? 1 : -1;
        int dcol=targetCol>col ? 1 : -1;
        int j=col+dcol;
        for(int i=row+drow; i!=targetRow; i+=drow){
            if(board[i-1][j-1]!=null) return false;
            j+=dcol;
        }
        return true;
    }
    public boolean isDiagonalMove(int row, int col,int targetRow, int targetCol){
        return Math.abs(row-targetRow)==Math.abs(col-targetCol);
    }

    @Override
    public String toString() {
        final String black= "\u001B[30m";
        return color.equals("white") ? figure : black+figure;
    }
}