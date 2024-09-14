import java.util.ArrayList;
import java.util.List;


public class GameManager {
    private Chessboard chessboard;
    boolean gameIsOver=false;
    //constructors
    public GameManager(){
        startNewGame();
    }
    public GameManager(Piece[][] board, List<Piece> captured){
        continueGame(board, captured);
    }
    //getters
    public Piece[][] getBoard(){
        return chessboard.getBoard();
    }
    public Chessboard getChessboard(){
        return chessboard;
    }
    //body
    public void continueGame(Piece[][] board, List<Piece> captured){
        chessboard =new Chessboard(board, captured);
    }
    public void startNewGame(){
        chessboard =new Chessboard(setNewBoard(), new ArrayList<>());
    }
    public Piece[][] setNewBoard(){
        Piece[][] board=new Piece[8][8];
        int row=1, col=1;
        String color="white";
        board[row-1][col++-1]=new Rook(color);
        board[row-1][col++-1]=new Knight(color);
        board[row-1][col++-1]=new Bishop(color);
        board[row-1][col++-1]=new Queen(color);
        board[row-1][col++-1]=new King(color);
        board[row-1][col++-1]=new Bishop(color);
        board[row-1][col++-1]=new Knight(color);
        board[row-1][col-1]=new Rook(color);

        row=2;
        for(col=1; col<=8; col++)
            board[row-1][col-1]=new Pawn(color);

        color="black";
        row=7;
        for(col=1; col<=8; col++)
            board[row-1][col-1]=new Pawn(color);
        row=8; col=1;
        board[row-1][col++-1]=new Rook(color);
        board[row-1][col++-1]=new Knight(color);
        board[row-1][col++-1]=new Bishop(color);
        board[row-1][col++-1]=new Queen(color);
        board[row-1][col++-1]=new King(color);
        board[row-1][col++-1]=new Bishop(color);
        board[row-1][col++-1]=new Knight(color);
        board[row-1][col-1]=new Rook(color);

        return board;
    }
    public boolean pawnReachedEnd(String target){
        int row=convert(target.charAt(1)),
                col=convert(target.charAt(0));
        return getBoard()[row-1][col-1] instanceof Pawn &&
                (row==1 || row==8);
    }
    public void castPawn(String target, int piece){
        int row=convert(target.charAt(1)),
                col=convert(target.charAt(0));
        String color=getBoard()[row-1][col-1].color();
        getChessboard().wasCaptured(getBoard()[row-1][col-1], row, col);

        switch(piece){
            case 1 -> getChessboard().putPiece(row, col, new Queen(color));
            case 2 -> getChessboard().putPiece(row, col, new Knight(color));
            case 3 -> getChessboard().putPiece(row, col, new Rook(color));
            case 4 -> getChessboard().putPiece(row, col, new Bishop(color));
        };
    }
    public void makeMove(String source, String target){
        int row=convert(source.charAt(1)),
            col=convert(source.charAt(0)),
            targetRow=convert(target.charAt(1)),
            targetCol=convert(target.charAt(0));

        chessboard.makeMove(row, col, targetRow, targetCol);
    }
    public boolean isLegalMove(String source, String target){
        return chessboard.isLegalMove(convert(source.charAt(1)),
                convert(source.charAt(0)),
                convert(target.charAt(1)),
                convert(target.charAt(0)));
    }
    public boolean isIllegalCoordinate(String coordinate){
        return coordinate.length()!=2 ||
                isNotInRange(convert(coordinate.charAt(0))) ||
                isNotInRange(convert(coordinate.charAt(1)));
    }
    public int convert(char c){
        if(Character.isDigit(c)) return c-'0';
        return (int) c-'a'+1;
    }
    public boolean isNotInRange(int coordinate){
        return coordinate<1 || coordinate>8;
    }
    public boolean wrongTurn(String source){
        return chessboard.wrongTurn(convert(source.charAt(1)), convert(source.charAt(0)));
    }
    public boolean isEmptySource(String source){
        return chessboard.getBoard()[convert(source.charAt(1))-1][convert(source.charAt(0))-1]==null;
    }

    public boolean gameIsOver(){
        gameIsOver=isCheckMate();
        return gameIsOver;
    }
    public boolean isCheckMate(){
        return chessboard.isCheckMate();
    }
}
