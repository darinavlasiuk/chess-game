import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class VisualManager {
    private final Scanner sc=new Scanner(System.in);
    private Piece[][] board;

    public void setBoard(Piece[][] board){
        this.board=board;
    }

    public void printChessboard(){
        System.setOut(
                new PrintStream(
                        new FileOutputStream(FileDescriptor.out),
                        true,
                        StandardCharsets.UTF_8
                )
        );
        printSpace();
        final String reset= "\u001B[0m";
        final String yellow="\u001B[43m";
        final String green="\u001B[42m";
        printLetterLane();
        for(int i=0; i<8; ++i){
            System.out.print(i+1+" ");
            for(int j=0; j<8; ++j){
                String background=(i+j)%2==0 ? green : yellow;
                if(board[i][j]!=null){
                    System.out.print(background+"\u2004"+ board[i][j]+"\u2004"+reset);
                }
                else System.out.print(background+"\u2004\u2006 \u2006\u2004"+reset);
            }
            System.out.print(" "+(i+1));
            printCommands(i);
        }
        printLetterLane();
    }
    public void printLetterLane(){
        System.out.print("  ");
        for(int i=0; i<8; ++i) System.out.print("\u2004\u2006"+(char)('a'+i)+"\u2006\u2004");
        System.out.println();
    }
    public void printSpace(){
        System.out.println("\n\n\n");
    }
    public String getSource(){
        System.out.print("Source: ");
        return sc.nextLine();
    }
    public String getTarget(){
        System.out.print("Target: ");
        return sc.nextLine();
    }
    public void choosePiece(){
        System.out.println("\nPawn reached the opposite side of the board.\n"+
                "Please type a number to choose the piece to replace Pawn:\n"+
                "1.♛  2.♞  3.♜  4.♝");
    }
    public int getPiece(){
        return sc.nextInt();
    }
    public void wrongCoordinate(){
        System.out.println("Incorrect coordinate! \n" +
                "Please type coordinate like b3 or F5 in the range of the chessboard.");
    }
    public void fileDoesNotExist(){
        System.out.println("File with such name does not exist!");
    }
    public void fileDoesAlreadyExist(){
        System.out.println("File with such name does already exist! Cannot save game in there.");
    }
    public void illegalMove(){
        System.out.println("Illegal move! Try one more time.");
    }

    public void printResults(String turn){
        turn= turn.equals("white") ? "Black" : "White";
        System.out.print(turn+" won!");
    }
    public void draw(){
        System.out.println("Game is finished with a draw.\n"+
                "Goodbye!");
    }
    public String getCommand(){
        System.out.print("\nInput: ");
        return sc.nextLine();
    }
    public void noSuchCommand(){
        System.out.println("There is no such command. Try one more time.");
    }
    public String getInputPath(){
        System.out.println("Print a name of binary file in src/ from which you want to load game:");
        return sc.nextLine();
    }
    public String getOutputPath(){
        System.out.println("Print a name of binary file in src/ where you want to save game:");
        return sc.nextLine();
    }
    public void printCommands(int i){
        switch(i){
            case 0 -> System.out.println("        Commands:");
            case 1 -> System.out.println("        - enter 's' or 'save' to save game");
            case 2 -> System.out.println("        - enter 'l' or 'load' to load game from file");
            case 3 -> System.out.println("        - enter 'n' or 'new' to start a new game");
            case 4 -> System.out.println("        - enter 'd' or 'draw' to finish game with a draw");
            default -> System.out.println();
        }
    }
    public void printDefaultCommands(){
        System.out.print(
                "\nCommands:\n"+
                "- enter 'l' or 'load' to load game from file\n"+
                "- enter 'n' or 'new' to start a new game\n"+
                "- enter 'e' or 'exit' to exit game\n");
    }
    public void gameIsSaved(){
        System.out.println("Current game state is saved.");
    }

}
