import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//@WebServlet(name = "LoginPage", urlPatterns = "WordiadaWeb/LoginPage")
//@MultipartConfig
public class LoginPage extends HttpServlet {
    private List<String> players = new ArrayList<>();
    private final String ROOMS_URL = "pages/RoomsPage/Rooms.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        players =(List<String>) getServletContext().getAttribute("players");

        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("username",req.getParameter("username"));
        String currUserName = req.getParameter("username");
        System.out.println("Current session name: " + currUserName);
        if (players == null) {
            players = new ArrayList<>();
            submitPlayer(req,httpSession,resp);
        } else {
            if (isNameAvailable(currUserName)) {
                submitPlayer(req,httpSession,resp);
            } else {
                printWriter.println(printHTML("The name exists already, please enter other name"));
            }
        }
    }

    private void submitPlayer(HttpServletRequest req, HttpSession httpSession,HttpServletResponse resp) throws IOException {
        addPlayer(req,httpSession,resp);
    }

    private void addPlayer(HttpServletRequest req,HttpSession httpSession,HttpServletResponse resp) throws IOException {
        players.add(req.getParameter("username"));
        Player thisSessionPlayer = new Player(req.getParameter("username"),req.getParameter("type"));
        httpSession.setAttribute("ThisSessionPlayer",thisSessionPlayer);
        getServletContext().setAttribute("players",players);
        resp.sendRedirect(ROOMS_URL);
    }

    private String printHTML(String input) {
        return "<html>\n" +
                "<head>\n" +
                "    <title>Wordiada</title>\n" +
                "</head>\n" +
                "<style>\n" +
                "    img {\n" +
                "        display: block;\n" +
                "        margin: auto;\n" +
                "        width: 40%;\n" +
                "    }\n" +
                "    body {\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        background-color: azure;\n" +
                "    }\n" +
                "\n" +
                "    .container {\n" +
                "        display:block;\n" +
                "        margin: auto;\n" +
                "        width: 20%;\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    .right {\n" +
                "        display:block;\n" +
                "        margin: auto;\n" +
                "        width: 0%;\n" +
                "    }\n" +
                "\t.red {\n" +
                "\t\tcolor:red;\n" +
                "\t\tdisplay:block;\n" +
                "        margin: auto;\n" +
                "        width: 35%;\n" +
                "\t}\n" +
                "</style>\n" +
                "<body background=\"images/Professional-Website-Background-White-106363.jpg\">\n" +
                "<header>\n" +
                "    <img src=\"images/WordiadaMain.png\">\n" +
                "</header>\n" +
                "<nav>\n" +
                "        <form accept-charset=\"UTF-8\" action=\"http://localhost:8080/WordiadaWeb/LoginPage\" autocomplete=\"off\" method=\"post\">\n" +
                "            <div class=\"container\">\n" +
                "                User Name: <input type=\"text\" name=\"username\" value=\"kobi\" align=\"center\"><br/>\n" +
                "                Choose Type:<br/>\n" +
                "                <input checked=\"checked\" name=\"type\" type=\"radio\" value=\"Human\" /> Human <br />\n" +
                "                <input name=\"type\" type=\"radio\" value=\"Computer\" align=\"center\" /> Computer <br />\n" +
                "                <div class=\"right\">\n" +
                "                    <button type=\"submit\" value=\"Submit\">Submit</button>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "        </form>\n" +
                "<label class=\"red\">"+input+"</label>\n" +
                "</nav>\n" +
                "</body>\n" +
                "</html>";
    }

    private boolean isNameAvailable(String name) {
        for(String player : players){
            if(player.equals(name))
                return false;
        }
        return true;
    }
}

