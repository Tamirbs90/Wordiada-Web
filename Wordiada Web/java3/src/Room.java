import com.sun.deploy.panel.ControlPanel;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomID;
    private List<Player> players = new ArrayList<>();
    private int numberOfOnlinePlayers;
    private int maxNumberOfOnlineUsers;
    private Player roomManager;
    private Controller controller = new Controller();
    private boolean gameIsAlive=false;

    public Room(String roomID, int maxNumberOfOnlineUsers, Engine engine, Player roomManager){
        this.roomID = roomID;
        this.maxNumberOfOnlineUsers = maxNumberOfOnlineUsers;
        this.numberOfOnlinePlayers = 0;
        this.roomManager = roomManager;
        this.controller.setEngine(engine);
        this.controller.setBoard(engine.getBoardSize());
    }

    void addPlayer(Player player){
        players.add(player);
        numberOfOnlinePlayers++;
        if(numberOfOnlinePlayers==maxNumberOfOnlineUsers) {
            gameIsAlive = true;
            controller.gameAlive();
        }
    }

    public void setGameIsAlive(boolean bool){
        this.gameIsAlive = bool;
    }

    public String getRoomID() {
        return roomID;
    }

    public void removePlayer(String username){
        String name = controller.getEngine().getPlayers().get(controller.getEngine().getTurnOf()).getName();
        for(int i = 0 ; i< players.size(); i++){
            if(players.get(i).getName().equals(username)){
                players.remove(players.get(i));
                numberOfOnlinePlayers--;
                System.out.println("removed player");
            }
        }
        System.out.println("setting players");
        setPlayers();
        System.out.println("settiing turn of");
        controller.setTurnOf(name);
    }

    public void restartPlayers(){
        players = new ArrayList<>();
        setPlayers();
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getNumberOfOnlinePlayers() {
        return numberOfOnlinePlayers;
    }

    public void setNumberOfOnlinePlayers(int numberOfOnlinePlayers) {
        this.numberOfOnlinePlayers = numberOfOnlinePlayers;
    }

    public int getMaxNumberOfOnlineUsers() {
        return maxNumberOfOnlineUsers;
    }

    public void setMaxNumberOfOnlineUsers(int maxNumberOfOnlineUsers) {
        this.maxNumberOfOnlineUsers = maxNumberOfOnlineUsers;
    }

    public Engine getEngine() {
        return controller.getEngine();
    }

    public Player getRoomManager() {
        return roomManager;
    }

    public Controller getController() {
        return controller;
    }

    public boolean isGameIsAlive() {
        return gameIsAlive;
    }

    public void setPlayers() {
        controller.getEngine().setPlayers(players);
    }
}
