import java.io.PrintWriter;
import java.util.List;

public class ServletUtils {

    public static void responseJson(Room room, PrintWriter out,boolean withWords) {
        StringBuilder gameJson = new StringBuilder();
        gameJson.append("{\n\t\"boadprint\": ");
        gameJson.append(room.getController().getBaordPrint());
        gameJson.append((",\"boardsize\":"+room.getEngine().getBoardSize()));
        if(withWords) {
            gameJson.append(",\"wordcomposed\": [");
            List<Player> players = room.getController().getEngine().getPlayers();

            Player currentPlayer = players.get(room.getController().getEngine().getTurnOf());
            for (int i = 0; i < currentPlayer.wordComposedList().size(); i++) {
                gameJson.append("\"" + currentPlayer.wordComposedList().get(i) + "\"");
                if (i != currentPlayer.wordComposedList().size() - 1)
                    gameJson.append(",");
            }
            gameJson.append("]");
            gameJson.append(",\"players\":[");
            for (int i = 0; i < players.size(); i++) {
                gameJson.append("\"" + players.get(i).getName() +" Points:"+players.get(i).getPoints()+ "\"");
                if (i != players.size() - 1)
                    gameJson.append(",");
            }
            gameJson.append("]");
            gameJson.append(",\"massage\":\"" + room.getController().getMassage() + "\"");
        }
        gameJson.append("}");
        out.println(gameJson.toString());
        out.flush();
    }
}
