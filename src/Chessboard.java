import java.util.List;

public class Chessboard {
    private int numberOfMove=0;
    private final Piece[][] board;
    private final List<Piece> captured;
    private String turn = "white";
    //constructors
    public Chessboard(Piece[][] board, List<Piece> captured){
        this.board=board;
        this.captured=captured;
    }
    public Chessboard copy(){
        Piece[][] copy=new Piece[8][8];
        for(int i=0; i<8; ++i)
            for(int j=0; j<8; ++j)
                if(board[i][j]!=null)
                    copy[i][j]=board[i][j].copy();
        Chessboard Copy=new Chessboard(copy, this.captured);
        Copy.turn=turn;
        Copy.numberOfMove=numberOfMove;
        return Copy;
    }
    //getters
    public Piece[][] getBoard(){
        return board;
    }
    public List<Piece> getCaptured(){
        return captured;
    }
    //body
    public void makeMove(int row, int col, int targetRow, int targetCol){
        numberOfMove++;
        if(board[row-1][col-1] instanceof Pawn){
            ((Pawn) board[row-1][col-1]).setFirstMoveNumber(numberOfMove);
        }
        if(board[row-1][col-1] instanceof Pawn &&
                ((Pawn) board[row-1][col-1]).isEnPassant(board, row, col, targetRow, targetCol)){
            doEnPassant(row, col, targetRow, targetCol);
        }
        else if(board[row-1][col-1] instanceof King &&
                ((King) board[row-1][col-1]).isCastling(board, row, col, targetRow, targetCol)){
                doCastling(row, col, targetRow, targetCol);
        }
        else{
            if(board[targetRow-1][targetCol-1]!=null)
                capture(row, col, targetRow, targetCol);
            else move(row, col, targetRow, targetCol);
        }
        switchTurn();
    }
    public void move(int row, int col, int targetRow, int targetCol){
        wasMoved(row, col, targetRow, targetCol);
    }
    public void capture(int row, int col, int targetRow, int targetCol){
        wasCaptured(board[targetRow-1][targetCol-1], targetRow, targetCol);
        wasMoved(row, col, targetRow, targetCol);
    }
    public void doEnPassant(int row, int col, int targetRow, int targetCol){
        wasMoved(row, col, targetRow, targetCol);
        wasCaptured(board[row-1][targetCol-1], row, targetCol);
    }
    public void doCastling(int row, int col, int targetRow, int targetCol){
        if(targetCol<col){
            wasMoved(row, col, row, col-2);
            wasMoved(row, 1, row, col-1);
        }
        else{
            wasMoved(row, col, row, col+2);
            wasMoved(row, 8, row, col+1);
        }
    }
    
    public boolean isLegalMove(int row, int col, int targetRow, int targetCol){
        if(board[row-1][col-1]==null ||
                !isMove(row, col, targetRow, targetCol) ||
                !board[row-1][col-1].isLegalMove(board, row, col, targetRow, targetCol))
            return false;
        if(board[row-1][col-1] instanceof Pawn &&
                ((Pawn) board[row-1][col-1]).isEnPassant(board, row, col, targetRow, targetCol) &&
                !(((Pawn) board[row-1][targetCol-1]).firstMoveNumber()==numberOfMove))
            return false;
        if(board[row-1][col-1] instanceof King &&
                ((King) board[row-1][col-1]).isCastling(board, row, col, targetRow, targetCol)){
            if(targetCol>col && (!isCheckAfrerMove(row, col, row, col+1) || isCheck(this, turn))){
                return false;
            }
            else if(targetCol<col && (!isCheckAfrerMove(row, col, row, col-1)) || isCheck(this, turn)){
                return false;
            }
        }
        return !isCheckAfrerMove(row, col, targetRow, targetCol);
    }
    public boolean isLegalMoveWithoutCheck(int row, int col, int targetRow, int targetCol){
        return board[row-1][col-1]!=null &&
                isMove(row, col, targetRow, targetCol) &&
                board[row-1][col-1].isLegalMove(board, row, col, targetRow, targetCol);
    }
    public boolean wrongTurn(int row, int col){
        return !board[row-1][col-1].color().equals(turn);
    }
    public boolean isMove(int row, int col, int targetRow, int targetCol){
        return targetRow!=row || targetCol!=col;
    }
    public void wasCaptured(Piece piece, int row, int col){
        captured.add(piece);
        board[row-1][col-1]=null;
    }
    public void wasMoved(int row, int col, int targetRow, int targetCol){
        board[row-1][col-1].increaseMoveCounter();
        board[targetRow-1][targetCol-1]=board[row-1][col-1];
        board[row-1][col-1]=null;
    }
    public void putPiece(int row, int col, Piece piece){
        board[row-1][col-1]=piece;
    }
    public void switchTurn(){
        turn= turn.equals("white") ? "black" : "white";
    }
    public boolean isCheckAfrerMove(int row, int col, int targetRow, int targetCol){
        Chessboard copy=this.copy();
        copy.makeMove(row, col, targetRow, targetCol);
        return isCheck(copy, copy().turn);
    }
    public boolean isCheck(Chessboard copy, String turn){
        int rowKing=0, colKing=0;
        for(int i=0; i<8; ++i) {
            for(int j=0; j<8; ++j)
                if(copy.getBoard()[i][j] instanceof King
                        && copy.getBoard()[i][j].color().equals(turn)) {
                    rowKing=i+1;
                    colKing=j+1;
                    break;
                }
        }

        for(int i=0; i<8; ++i) {
            for(int j=0; j<8; ++j)
                if(copy.getBoard()[i][j]!=null &&
                        !copy.getBoard()[i][j].color().equals(turn)
                        && copy.isLegalMoveWithoutCheck(i+1, j+1, rowKing, colKing)) {
                    return true;
                }
        }
        return false;
    }
    public boolean isCheckMate(){
        if(!isCheck(this, turn)) return false;

        for(int i=0; i<8; ++i) {
            for(int j=0; j<8; ++j){
                if(board[i][j]!=null &&
                        board[i][j].color().equals(turn)){
                    for(int row=0; row<8; ++row){
                        for(int col=0; col<8; ++col){
                            if(isLegalMoveWithoutCheck(i+1, j+1, row+1, col+1)){
                                if(!isCheckAfrerMove(i+1, j+1, row+1, col+1))
                                    return false;

                            }

                        }
                    }

                }


            }
        }

        return true;
    }

    public String getTurn(){
        return turn;
    }

}
