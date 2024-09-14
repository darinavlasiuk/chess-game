import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private File file;
    private Piece[][] board;
    private List<Piece> captured;
    //getters

    public Piece[][] getBoard() {
        return board;
    }
    public List<Piece> getCaptured(){
        return captured;
    }
    public boolean fileExist(String path){
        file=new File("src/"+path+".bin");
        return file.exists() && file.canRead() && !file.isDirectory();
    }
    public void readFile(String path){
        file=new File("src/"+path+".bin");
        board=new Piece[8][8];
        captured=new ArrayList<>();
        try (
                FileInputStream fis = new FileInputStream(file)
        ){
            while(fis.available()>0){
                byte[] b=new byte[2];
                fis.read(b);
                fromBinary(b);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
    public void saveGame(Chessboard chessboard, String path){
        file=new File("src/"+path+".bin");
        board=chessboard.getBoard();
        captured=chessboard.getCaptured();
        try (
                FileOutputStream fis = new FileOutputStream(file)
        ){
            for(int row=1; row<=8; ++row){
                for(int col=1; col<=8; ++col){
                    if(board[row-1][col-1]!=null)
                        fis.write(toBinary(board[row-1][col-1], row, col));
                }
            }
            for(Piece piece: captured) fis.write(toBinary(piece, 0, 0));

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public byte[] toBinary(Piece piece, int row, int col){
        int data=convertDataToBinary(piece, row, col);
        byte[] b=new byte[2];
        b[1]=(byte) data;
        b[0]=(byte) (data>>8);
        return b;
    }
    public void fromBinary(byte[] b){
        int data=(b[0] & 0xFF)<<8 | (b[1] & 0xFF);
        byte type=(byte)(data & 0x7);
        byte horPos=(byte) ((data>>3) & 0xF);
        byte verPos=(byte) ((data>>7) & 0xF);
        byte color=(byte) ((data>>11) & 0x1);

        if(horPos!=0)
        board[verPos-1][horPos-1]=convertToPieceFromBinary(type, color);
        else captured.add(convertToPieceFromBinary(type, color));
    }
    public byte convertTypeToBinary(String type){
        return switch(type){
            case "♚" -> 1;
            case "♛" -> 2;
            case "♜" -> 3;
            case "♝" -> 4;
            case "♞" -> 5;
            default -> 0;
        };
    }
    public Piece convertToPieceFromBinary(byte type, byte colorInByte){
        String color=colorInByte==0 ? "white" : "black";
        return switch(type){
            case 1 -> new King(color);
            case 2 -> new Queen(color);
            case 3 -> new Rook(color);
            case 4 -> new Bishop(color);
            case 5 -> new Knight(color);
            default -> new Pawn(color);
        };
    }
    public int convertDataToBinary(Piece piece, int row, int col){
        byte type=convertTypeToBinary(piece.getFigure());
        byte verPos=(byte) row;
        byte horPos=(byte) col;
        byte color=piece.color().equals("white") ? (byte)0 : (byte)1;
        return type|(horPos<<3)|(verPos<<7)|(color<<11);

    }

}
