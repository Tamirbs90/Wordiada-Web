import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


//@WebServlet(name = "RoomsList", urlPatterns = "WordiadaWeb/pages/playerAndRoomlist")
public class RoomsList extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        StringBuilder json = new StringBuilder();
        json.append("{\n\t\"entries\": [");
        try (PrintWriter out = response.getWriter()) {
            List<Room> rooms =(List<Room>) getServletContext().getAttribute("rooms");
            System.out.println("rooms size: " + rooms.size());
            for(int i=0;i<rooms.size();i++){
                json.append("{\n" +
                        "\"gameStatus\":"+"\""+(rooms.get(i).isGameIsAlive() ? "Online" : "Offline")+"\""+",\n" +
                        "\"gameTitle\":"+"\""+rooms.get(i).getEngine().getGameTitle()+"\""+",\n" +
                        "\"username\":"+"\""+rooms.get(i).getRoomID()+"\""+",\n" +
                        "\"type\":"+"\""+rooms.get(i).getRoomManager().getType()+"\""+",\n" +
                        "\"boardSize\":"+"\""+rooms.get(i).getEngine().getBoardSize()+"\""+",\n" +
                        "\"playersLimit\":"+"\""+rooms.get(i).getMaxNumberOfOnlineUsers()+"\""+",\n" +
                        "\"playersInGame\":"+"\""+rooms.get(i).getNumberOfOnlinePlayers()+"\""+",\n" +
                        "\"dictionaryName\":"+"\""+rooms.get(i).getEngine().getDictionaryName() + "\""+",\n" +
                        "\"spareTileSize\":"+"\""+rooms.get(i).getEngine().getStashSize() +"    \""+"\n" +"}");
                if(i!=rooms.size()-1)
                    json.append(",\n");
            }
            json.append("],\"players\":[");
            List<String> players = (List<String>) getServletContext().getAttribute("players");
            for(int i=0;i<players.size();i++){
                json.append("\""+players.get(i)+"\"");
                if(i!=players.size()-1)
                    json.append(",");
            }
            json.append("]");
            json.append("}");
            out.println(json.toString());
            out.flush();
        }catch (Exception exception){
            System.out.println(exception.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }
}