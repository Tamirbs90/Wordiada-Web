import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//@WebServlet(name = "RoomsPage", urlPatterns = "WordiadaWeb/secondpage")
public class RoomsPage extends HttpServlet {
    private List<Room> rooms = new ArrayList<>();
    private final String GAME_URL = "pages/GamePage/Game.html";
    private boolean gameIsAlive=false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession httpSession = req.getSession();
        rooms = (List<Room>) getServletContext().getAttribute("rooms");
        Room currRoom;
        for(Room room : rooms){
            if(room.getRoomID().equals(req.getParameter("room"))){
                currRoom =room;
                if(currRoom.isGameIsAlive()) {
                    resp.setStatus(204);
                }
                else{
                    room.addPlayer(new Player(((Player)httpSession.getAttribute("ThisSessionPlayer")).getName(),((Player)httpSession.getAttribute("ThisSessionPlayer")).getType()));
                        room.setPlayers();
                        httpSession.setAttribute("room",room);
                        resp.sendRedirect(GAME_URL);
                    }
                break;
            }
        }

    }
}