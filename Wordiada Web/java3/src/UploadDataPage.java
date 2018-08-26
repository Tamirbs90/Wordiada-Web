import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class UploadDataPage extends HttpServlet {
    private List<Room> rooms = new ArrayList<>();
    private Engine engine;
    private StringBuilder errorText = new StringBuilder();
    private final String ROOMS_URL = "pages/RoomsPage/Rooms.html";
    private final String ROOMS_ERROR_URL = "pages/RoomsPage/RoomsErr.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        engine = new Engine();
        rooms = (List<Room>) getServletContext().getAttribute("rooms");
        resp.setContentType("text/html");
        HttpSession httpSession = req.getSession();
        InputStream xmlConnection = req.getPart("xml").getInputStream();
        InputStream dicConnection = req.getPart("dictionary").getInputStream();
        if (!engine.readXML(getFileName(req.getPart("xml")), xmlConnection, errorText)) {
            resp.sendRedirect(ROOMS_ERROR_URL);
            System.out.println("Error in xml file: " + errorText);
        } else {
            System.out.println("Finished loading XML file");
            engine.init();

            String dictionaryName = getFileName(req.getPart("dictionary"));
            dictionaryName = dictionaryName.substring(0, dictionaryName.length() - 4);
            System.out.println(dictionaryName);
            engine.setDictionaryName(dictionaryName);

            String currUserName = req.getParameter("username");

            System.out.println("Current session name: " + currUserName);
            if (rooms == null) {
                rooms = new ArrayList<>();
                submitPlayer(req, httpSession, resp, dicConnection);
            } else {
                submitPlayer(req, httpSession, resp, dicConnection);
            }
        }
    }
    private void submitPlayer(HttpServletRequest req, HttpSession httpSession,HttpServletResponse resp, InputStream dicConnection) throws IOException {
        addPlayer(req,httpSession,resp);
        initDictionary(dicConnection);
    }

    private void initDictionary(InputStream dicConnection) {
        engine.readDictionary(dicConnection);
        System.out.println("Finished to load Dictionary");
    }

    private void addPlayer(HttpServletRequest req,HttpSession httpSession,HttpServletResponse resp) throws IOException {
        rooms.add(new Room((String)httpSession.getAttribute("username"),engine.getTotalPlayers(),engine,(Player)httpSession.getAttribute("ThisSessionPlayer")));
        getServletContext().setAttribute("rooms", rooms);
        resp.sendRedirect(ROOMS_URL);
    }

    private String getFileName(Part part) {
        String partHeader = part.getHeader("content-disposition");
        for (String cd : partHeader.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}

