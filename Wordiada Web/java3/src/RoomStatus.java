import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RoomStatus extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        System.out.println("we are in the status room");
        String roomID = request.getParameter("roomID");
        List<Room> rooms = (List<Room>) getServletContext().getAttribute("rooms");
        Room requestedStatusRoom = (Room) request.getSession().getAttribute("room");

        for(Room room : rooms)
            if(roomID.equals(room.getRoomID()))
                requestedStatusRoom = room;
        try (PrintWriter out = response.getWriter()) {
            if(requestedStatusRoom != null)
                ServletUtils.responseJson(requestedStatusRoom,out,false);
        } catch( Exception e){
            System.out.println(e.toString());
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