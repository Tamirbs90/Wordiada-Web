import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Created by Tamir on 12/04/2017.
 */
public class Board {

    private Tile[][] board;
    private int size;


    public void buildBoard(List<Tile> lst, int boardSize) {
        size= boardSize;
        board = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //pane.add(lst.get(0).getCell(), i, j);
                board[i][j] = lst.get(0);
                lst.remove(0);
            }
        }
    }

    public void setWordMode(boolean getWord) {
        board[0][0].setGetWord(getWord);
    }

    public void buildArray() {
        board = new Tile[size][size];
    }

    public void clearWord() {
        board[0][0].clearWord();
    }

    public void updateReplayPane(GridPane pane) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
//                pane.add(board[i][j].getCell(), i, j);
            }
        }
    }

    public String getLetter(int i, int j) {
        return board[i][j].getLetter();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isExposed(int i, int j) {
        return board[i][j].isExposed();
    }

    public Tile getTile(int i, int j) {
        return board[i][j];
    }

    public void setTile(Tile tile, int i, int j) throws CloneNotSupportedException {
//        board[i][j] = tile.clone();
    }

    public int tilesUnExposed() {
        int res = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!isExposed(i, j)) {
                    res++;
                }
            }
        }
        return res;
    }

    public void goldenFishMode(boolean goldenFishMode) {
        if (goldenFishMode) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    board[i][j].unExpose();
                    board[i][j].pressedButtonExopse(false);
                }
            }
        }
    }

    public String composeWord(){
        String word="";
        int num=0;
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                if (!getLetter(i,j).equals(' ')) {
                    word += getLetter(i, j);
                    board[i][j].setChosen();
                    num++;
                    if (num==2)
                        break;
                }
            }
            if (num==2)
                break;
        }
        return word;
    }

    public boolean allTilesExposed(){
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                if (!board[i][j].isExposed())
                    return false;
            }
        }
        return true;
    }

    public void updateBoard(List<Tile> lst) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].isChosenLetter()) {
                    if (lst.size() > 0) {
                        board[i][j] = lst.get(0);
                        lst.remove(0);
                    }
                    else{
                        Tile newTile=new Tile('*',0);
//                        newTile.getCell().setDisable(true);
                        newTile.setExposed(true);
                        newTile.pressedButtonExopse(true);
                        board[i][j]=newTile;
                    }

                }
            }
        }
    }

    public String getTestWord() {
        return board[0][0].getTestWord();
    }

    public void setGlobalNumOfSelect(int x) {
        board[0][0].setGlobalNumOfSelect(x);
    }

    public void setSelect(boolean var) {
        board[0][0].setSelect(var);
    }

    public void setMassages(String m) {
        board[0][0].setMassages(m);
    }

    public void setNumOfSelect() {
        board[0][0].setNumOfSelect();
    }

    public int calculateWordValue() {
        int value = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].isChosenLetter()) {
                    value += board[i][j].getPoints();
                }
            }
        }
        return value;
    }

    public int getSize() {
        return size;
    }

    public void exposeTile(int m, int n) {
        board[m][n].setExposed(true);
    }

    public void setChosenLetter() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j].setChosenLetter();
            }
        }
    }

    public String getExposedLetters() {
        String letters = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].isExposed())
                    letters += board[i][j].getLatter();
            }
        }
        return letters;
    }


    public int getNumOfSelect(){
        return board[0][0].getNumOfSelect();
    }

    public void takeAction(int i, int j){
        board[i][j].takeAction();
    }

    public String getBoardPrint() {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(!(i==size-1&&j==size-1))
                    json.append("{\"letter\":"+"\""+board[i][j].getLetter()+"\","+"\"i\":"+"\""+i+"\","+"\"j\":"+"\""+j+"\","+"\"exposed\":\""+(board[i][j].getPressedButtonexpose() ? "true" : "false")+"\"},");
                else json.append("{\"letter\":"+"\""+board[i][j].getLetter()+"\","+"\"i\":"+"\""+i+"\","+"\"j\":"+"\""+j+"\","+"\"exposed\":\""+(board[i][j].getPressedButtonexpose() ? "true" : "false")+"\"}");
            }
        }
        json.append("]");
        return json.toString();
    }

    public void pressedButtonExopse(boolean expose) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].isExposed())
                    board[i][j].pressedButtonExopse(expose);
            }
        }
    }
}
