import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * Created by Tamir on 12/04/2017.
 */

public class Tile implements Cloneable{
//    public static Label massages = new Label();
    private String letter = "";
    private int points;
    private boolean isExposed=false;
    private boolean showTile =false;
//    private Button cell = new Button();
    public static boolean getWord = false;
    public static String word = "";
    public static int numOfSelect = 0;
    public static int globalNumOfSelect = 0;
    public static int nowSelecting = 0;
    private boolean chosenLetter = false;
    public static boolean select = true;
    private String massage;

    public Tile(char letter, int points){
        this.letter = String.valueOf(letter);
        this.points= points;
        this.isExposed=false;
    }

    public void takeAction(){
        if(select){
            if(numOfSelect<globalNumOfSelect){
                if(!getWord){
                    if(!isExposed){
                        isExposed=true;
                        System.out.println("is exposed");
                        numOfSelect++;
                    }
                }
                else {
                    if(isExposed && !chosenLetter){
                        word = word + this.letter;
                        chosenLetter=true;
                        System.out.println("success on selecting letter");
                        System.out.println(word);
                    }
                }

            }else{
                massage = "You can't peak more then " + globalNumOfSelect + " tiles";
            }
        }else{
                massage = "Not able to press board right now";
        }
    }

    public void setGlobalNumOfSelect(int globalNumOfSelect) {
        Tile.globalNumOfSelect = globalNumOfSelect;
    }

    public int getNumOfSelect() {
        return numOfSelect;
    }

    public char getLatter(){
        if(isExposed)
            return letter.charAt(0);
        else return '*';
    }

//    public Node getCell(){return cell;}

    public String getLetter() {
        return letter;
    }

    public int getPoints() {
        return points;
    }

    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    public void unExpose() {
        this.setExposed(false);
    }

    public void updateCell() {
//        cell.setText(String.valueOf(this.getLatter()));
    }

    public void setGetWord(boolean getWord) {
        this.getWord = getWord;
    }

    public String getTestWord() {
        return word;
    }

    public boolean isChosenLetter() {
        return chosenLetter;
    }

    public void clearWord(){
        word = "";
    }

    public void setSelect(boolean var){
        select = var;
    }

//    public void setText(){cell.setText(this.letter.getValue());}

    public void setMassages(String m){
        massage=m;
    }

    public void setNumOfSelect() {
        numOfSelect=0;
    }

    public void setChosenLetter(){
        chosenLetter = false;
    }

    public void setChosen(){ chosenLetter=true;}

    public boolean getExposed() {
        return isExposed;
    }

    public void pressedButtonExopse(boolean expose) {
        showTile = expose;
    }

    public boolean getPressedButtonexpose(){
        return showTile;
    }

//    @Override
//    public Tile clone() throws CloneNotSupportedException {
//        Tile temp=(Tile)super.clone();
//        temp.cell=new Button(this.cell.getText());
//        temp.cell.setPrefSize(27, 27);
//        temp.cell.setAlignment(Pos.CENTER);
//        temp.cell.setFont(Font.font(11));
//        if (this.cell.isDisabled())
//            temp.cell.setDisable(true);
//        return temp;
//    }
}
