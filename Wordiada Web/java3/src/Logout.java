import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logout extends HttpServlet {
    private List<String> players = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private final String GAME_URL = "index.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession httpSession = req.getSession();
        rooms = (List<Room>) getServletContext().getAttribute("rooms");
        players =(List<String>) getServletContext().getAttribute("players");
        String username = (String) httpSession.getAttribute("username");
        for(Room room : rooms){
            if(room.getRoomID().equals(username)) {
                rooms.remove(room);
                break;
            }
        }
        for(String player : players){
            if(player.equals(username)){
                players.remove(player);
                break;
            }
        }
        resp.sendRedirect(GAME_URL);
    }
}