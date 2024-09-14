public class Game {
    private FileManager fileManager;
    private VisualManager visual;
    private GameManager manager;
    public Game(){
        setGame();
    }
    public void setGame(){
        fileManager=new FileManager();
        visual=new VisualManager();
        chooseCommand();
    }
    public void chooseCommand(){
        visual.printDefaultCommands();
        String command=visual.getCommand();
        while(!isDefaultCommand(command)){
            visual.noSuchCommand();
            command=visual.getCommand();
        }
        executeCommand(command);
    }
    public void startNewGame(){
        manager=new GameManager();
        visual.setBoard(manager.getBoard());
        manageGame();
    }
    public void continueGame(){
        manager=new GameManager(fileManager.getBoard(), fileManager.getCaptured());
        visual.setBoard(manager.getBoard());
        manageGame();
    }
    public void manageGame(){
        visual.printChessboard();
        while(!gameIsOver()) {
            makeMove();
            visual.printChessboard();
        }
        stopGame();
    }
    public boolean gameIsOver(){
        return manager.gameIsOver();
    }
    public void makeMove(){
        String source=getSource(), target=getTarget();
        if(!isLegalMove(source, target)){
            visual.illegalMove();
            makeMove();
        }
        else{
            manager.makeMove(source, target);
            checkIfPawnReachedEnd(target);
        }
    }
    public void checkIfPawnReachedEnd(String target){
        if(manager.pawnReachedEnd(target)){
            visual.choosePiece();
            int choice=visual.getPiece();
            while(!isCorrectPieceChoice(choice))
                choice=visual.getPiece();
            manager.castPawn(target, choice);
        }
    }
    public boolean isCorrectPieceChoice(int choice){
        return choice>=1 && choice<=4;
    }
    public String getSource(){
        String source=visual.getSource();
        if(isCommand(source)) executeCommandMidGame(source);
        if(manager.isIllegalCoordinate(source) ||
                manager.isEmptySource(source) ||
                manager.wrongTurn(source)){
            visual.wrongCoordinate();
            return getSource();
        }
        else return source;
    }
    public String getTarget(){
        String target=visual.getTarget();
        if(isCommand(target)) executeCommandMidGame(target);
        if(manager.isIllegalCoordinate(target)){
            visual.wrongCoordinate();
            return getTarget();
        }
        else return target;
    }
    public boolean isLegalMove(String source, String target){
        return manager.isLegalMove(source, target);
    }
    public boolean isCommand(String s){
        return switch(s){
            case "s", "save",
                "d", "draw",
                "l", "load",
                "n", "new" -> true;
            default -> false;
        };
    }
    public boolean isDefaultCommand(String s){
        return switch(s){
            case "e", "exit",
                "l", "load",
                "n", "new" -> true;
            default -> false;
        };
    }
    public void executeCommandMidGame(String command){
        switch(command){
            case "s", "save" -> saveGame();
            case "d", "draw" -> draw();
            case "l", "load" -> {
                readFileMidGame();
            }
            case "n", "new" -> startNewGame();
        }
    }
    public void executeCommand(String command){
        switch(command){
            case "e", "exit" -> stop();
            case "l", "load" -> {
                readFile();
                continueGame();
            }
            case "n", "new" -> startNewGame();
        }
    }
    public void readFile(){
        String inputPath=visual.getInputPath();
        if(!fileManager.fileExist(inputPath)){
            visual.fileDoesNotExist();
            chooseCommand();
        }
        else fileManager.readFile(inputPath);
    }
    public void readFileMidGame(){
        String inputPath=visual.getInputPath();
        if(!fileManager.fileExist(inputPath)){
            visual.fileDoesNotExist();
            manageGame();
        }
        else{
            fileManager.readFile(inputPath);
            continueGame();
        }
    }
    public void saveGame(){
        String outputPath=visual.getOutputPath();
        if(fileManager.fileExist(outputPath)){
            visual.fileDoesAlreadyExist();
        }
        else{
            fileManager.saveGame(manager.getChessboard(), outputPath);
            visual.gameIsSaved();
        }
        manageGame();
    }
    public void draw(){
        visual.draw();
        stop();
    }
    public void stopGame(){
        visual.printResults(manager.getChessboard().getTurn());
        stop();
    }
    public void stop(){
        System.exit(0);
    }
}
