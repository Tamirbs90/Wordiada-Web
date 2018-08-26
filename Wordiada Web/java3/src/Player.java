import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


/**
 * Created by Tamir on 12/04/2017.
 */
public class Player {

    private String name= "";
    private String type;
    private int points = 0;
    private int numOfWords = 0;
    private TreeMap<String,Integer> wordsComposed;
    private int currNumOfAttempts;
    private boolean hasQuit=false;


    public Player(String name, String type) {
        this.name=name;
        this.type=type;
        wordsComposed = new TreeMap<>();
    }

    public void setHasQuit(boolean hasQuit) {this.hasQuit = hasQuit;}

    public boolean isHasQuit() {return hasQuit;}

    public void reset(){
        points = 0;
        numOfWords = 0;
        hasQuit=false;
        wordsComposed.clear();
    }

    public int getNumOfWords() {return numOfWords;}

    public void incNumOfWords(){numOfWords++;}

    public String getName() {
        return name;
    }

    public void addWord(StringProperty word) {
        if(wordsComposed.get(word.getValue()) == null)
            wordsComposed.put(word.getValue(),1);
        else wordsComposed.put(word.getValue(),wordsComposed.get(word.getValue())+1);
    }


    public List<String> wordComposedList(){
        List<String> s = new ArrayList<>();
        for(String e : wordsComposed.keySet())
            if(wordsComposed.get(e)>1)
                s.add(e+" " + wordsComposed.get(e));
            else s.add(e);
        return s;
    }

    public void addPoints(int toAdd){points+=toAdd;}

    public String getType() {
        return type;
    }

    public void setType(String type) {this.type=type;}

    public int getPoints() {
        return points;
    }

    public void setNumOfAttempts(int numOfAttempts) {
        this.currNumOfAttempts = numOfAttempts;
    }

    public int getNumOfAttempts() {
        return currNumOfAttempts;
    }
    public void dicNumOfAttempts() {
        currNumOfAttempts--;
    }
}