import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kobi-PC on 31-May-17.
 */
public class Controller {
    Engine engine = new Engine();
    Board board = new Board();
    List<Board> boards = new ArrayList<Board>();
    List<Integer> numPlays = new ArrayList<>();
    List<String> wordsComposed = new ArrayList<>();
    List<List<String>> playerLists = new ArrayList<>();
    List<Integer> points = new ArrayList<>();
    List<Integer> turnOf = new ArrayList<>();
    private int boardsIndex;
    private int numOfSelect;
    private boolean startTheGame = true;
    private boolean ableToTestWord = false;
    private boolean ableToThrowCube = false;
    private boolean ableToExpose = false;
    private boolean ableToStartGame = false;
    private boolean ableToLoadXml = true;
    private boolean ableToQuit = false;
    private int diff;
    Stage window;
    private String massage = "waiting for the room to be full";
    private boolean afterCube;

    public String currentPlayerName() {
        return engine.getPlayers().get(engine.getTurnOf()).getName();
    }/////////////

    public List<Player> getPlayers() {
        return engine.getPlayers();
    }

    public void setMassage(String s){
        massage = "Turn of " + engine.getPlayers().get(engine.getTurnOf()).getName() + ": "+s;
    }

    public char getTile(int i, int j) {
        return board.getTile(i, j).getLatter();
    }

    public int getBoardSize() {
        return board.getSize();
    }

    public boolean gameIsOver() {
        return engine.gameIsOver(board);
    }


    public void reset() {
        board = new Board();
        board.buildBoard(engine.getTheStash(),engine.getBoardSize());
        massage = "waiting for the room to be full";
        turnOf.clear();
        numPlays.clear();
        wordsComposed.clear();
    }

    public void throwCube() {
        if (ableToThrowCube) {
            ableToQuit = false;
            board.setWordMode(false);
            numOfSelect = engine.throwCube(board);
            setMassage("Select " + numOfSelect + " Tiles");
            board.setGlobalNumOfSelect(numOfSelect);
            board.setSelect(true);
            ableToThrowCube = false;
            afterCube = true;
        }
    }

    public void expose() {
        if (board.getNumOfSelect() < numOfSelect && !engine.getPlayers().get(engine.getTurnOf()).getType().equals("Computer")) {
            setMassage("You have to select " + numOfSelect + " tiles");
            ableToExpose = false;
        } else ableToExpose = true;
        if (ableToExpose && afterCube) {
            ableToTestWord = true;
            board.pressedButtonExopse(true);
            board.setWordMode(true);
            board.setNumOfSelect();
            engine.getPlayers().get(engine.getTurnOf()).setNumOfAttempts(engine.getNumOfAttempts());
            setMassage( "Try to compose a word");
            ableToExpose = false;
            afterCube = false;
        }

    }

    public void testWord() {
        board.setWordMode(true);
        if (ableToTestWord) {
            System.out.println("testing word" + board.getTestWord());
            engine.incNumOfPlays();
            turnOf.add(engine.getTurnOf());
            String word = board.getTestWord();
            wordsComposed.add(word);
            playerLists.add(engine.getPointsList(false));
            numPlays.add(engine.numOfPlaysProperty().getValue());
            points.add(engine.getPlayers().get(engine.getTurnOf()).getPoints());
            if (engine.getPlayers().get(engine.getTurnOf()).getNumOfAttempts() >= 0) {
                if (engine.isLegalWord(word)) {//legal word
                    if (engine.getWinnigAccording().getValue().equals("WordCount")) {//word count
                        engine.getPlayers().get(engine.getTurnOf()).addWord((new SimpleStringProperty(word)));
                        engine.getPlayers().get(engine.getTurnOf()).addPoints(1);
                    } else {//word score
                        int score = board.calculateWordValue() * engine.getSigment(word);
                        engine.getPlayers().get(engine.getTurnOf()).addPoints(score);
                        engine.getPlayers().get(engine.getTurnOf()).addWord((new SimpleStringProperty(word + " (" + score + ")")));
                    }
                    System.out.println("legal word");
                    setMassage("Word is legal");
                    engine.getPlayers().get(engine.getTurnOf()).incNumOfWords();
                    board.updateBoard(engine.getTheStash());
                    board.goldenFishMode(engine.goldenFishModeProperty().getValue());
                    ableToTestWord = false;
                    System.out.println("if game is over: " + engine.gameIsOver(board));
                    if (!engine.gameIsOver(board))
                        moveToNextPlayer();//////////////////////////////
                }//legal word
                else {//////////ilegal word
                    System.out.println("illegal word");
                    if (engine.getPlayers().get(engine.getTurnOf()).getNumOfAttempts() != 0) {
                        setMassage("Illegal word. You have " + engine.getPlayers().get(engine.getTurnOf()).getNumOfAttempts() + " more attempts");
                        engine.getPlayers().get(engine.getTurnOf()).dicNumOfAttempts();
                    } else if (!engine.gameIsOver(board)) {
                        board.goldenFishMode(engine.goldenFishModeProperty().getValue());
                        moveToNextPlayer();
                    }
                }
                board.clearWord();
                board.setChosenLetter();
                if (engine.gameIsOver(board))//game over
                    beforeGameOver();
            } else board.setWordMode(false);
            if (engine.getPlayers().get(engine.getTurnOf()).getType().equals("Computer")) {
                makeComputerMove();
            }
        }
    }
    public void beforeGameOver(){
        massage = "The winner is " + engine.findPlayerWithMostPoints() + " Please press Quit Game to go back to Open Rooms Page";
    }

    public void gameOver() {
        engine.restartNewGame();
        reset();
        boardsIndex = boards.size() - 1;
        ableToTestWord = false;
        ableToThrowCube = false;
        ableToExpose = false;
        ableToStartGame = true;
        ableToLoadXml = true;
    }

    public void moveToNextPlayer() {
        engine.moveToNextPlayer();
        if (board.allTilesExposed()) {
            ableToThrowCube = false;
            ableToTestWord = true;
            ableToQuit = true;
            board.setWordMode(true);
            engine.getPlayers().get(engine.getTurnOf()).setNumOfAttempts(engine.getNumOfAttempts());
            setMassage("Try to compose word.");
        } else {
            ableToThrowCube = true;
            ableToQuit = true;
            if(engine.getPlayers().get(engine.getTurnOf()).getType().equals("Computer")){
                setMassage("Computer move.") ;

            } else
                setMassage("please throw cube.");
        }
    }


    public void startGame() {
        System.out.println("starting the game");
        board.setSelect(false);
        String massagesFromBoard = "";
        board.setMassages(massagesFromBoard);
        setMassage(massagesFromBoard);
        ableToThrowCube = true;
        System.out.println("now able to throw cube");
        if (engine.getPlayers().get(engine.getTurnOf()).getType().equals("Computer")){
            makeComputerMove();
            setMassage("Computer move.") ;
        }else setMassage("please throw cube.");

        ableToQuit = true;

    }

    public void makeComputerMove() {
        System.out.println("making computer move");
        engine.incNumOfPlays();
        playerLists.add(engine.getPointsList(false));
        turnOf.add(engine.getTurnOf());
        numPlays.add(engine.numOfPlaysProperty().getValue());
        points.add(engine.getPlayers().get(engine.getTurnOf()).getPoints());


        boolean threwCube = false;
        if (!board.allTilesExposed()) {
            throwCube();
            threwCube = true;
        }
        StringBuilder letters = new StringBuilder();
        int breaker = numOfSelect;
        while (breaker != 0) {
            boolean validInput = false;
            while (!validInput) {
                for (int m = 0; m < board.getSize() && breaker != 0; m++)
                    for (int n = 0; n < board.getSize() && breaker != 0; n++) {
                        if (!board.allTilesExposed()) {
                            if (!board.isExposed(n, m)) {
                                board.exposeTile(n, m);
                                letters.append(board.getLetter(n, m));
                                board.getTile(n, m).setChosen();
                                validInput = true;
                                breaker--;
                            }
                        } else {
                            letters = new StringBuilder(board.composeWord());
                            validInput = true;
                            breaker = 0;
                        }
                    }
            }
        }
        if (threwCube) {
            expose();
        }
        if (engine.isLegalWord(letters.toString())) {
            engine.getPlayers().get(engine.getTurnOf()).addWord(new SimpleStringProperty(letters.toString()));
            board.updateBoard(engine.getTheStash());
            engine.getPlayers().get(engine.getTurnOf()).incNumOfWords();
            if (engine.getWinnigAccording().getValue().equals("WordCount")) {//word count
                engine.getPlayers().get(engine.getTurnOf()).addWord((new SimpleStringProperty(letters.toString())));
                engine.getPlayers().get(engine.getTurnOf()).addPoints(1);
            } else {//word score
                int score = board.calculateWordValue() * engine.getSigment(letters.toString());
                engine.getPlayers().get(engine.getTurnOf()).addPoints(score);
                engine.getPlayers().get(engine.getTurnOf()).addWord((new SimpleStringProperty(letters.toString() + " (" + score + ")")));
            }
        }
        board.goldenFishMode(engine.goldenFishModeProperty().getValue());
        board.setChosenLetter();
        board.clearWord();
        wordsComposed.add(letters.toString());
        moveToNextPlayer();
        if (engine.getPlayers().get(engine.getTurnOf()).getType().equals("Computer") && !engine.gameIsOver(board)) {
            makeComputerMove();
        }
        if (engine.gameIsOver(board)){
            beforeGameOver();
        }

    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setBoard(int boardSize) {
        board.buildBoard(engine.getTheStash(), boardSize);
    }

    public Engine getEngine() {
        return engine;
    }

    public String getBaordPrint() {
        return board.getBoardPrint();
    }

    public void effectTile(int i, int j) {
        board.takeAction(i, j);
    }

    public String getMassage() {
        return massage;
    }

    public void gameAlive() {
        startGame();
    }

    public void setTurnOf(String name) {
        for(int i=0;i<engine.getPlayers().size();i++){
            if(engine.getPlayers().get(i).getName().equals(name)){
                engine.setTurnOf(i);
                break;
            }
        }
    }
}

