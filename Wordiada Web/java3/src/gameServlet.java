
import com.sun.deploy.panel.ControlPanel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet(name = "gameServelt",urlPatterns = {"WordiadaWeb/pages/GamePage/wordiada"})
public class gameServlet extends HttpServlet {
    private Controller userGame;
    private boolean quitGame = false;

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        quitGame = false;
        response.setContentType("text/html;charset=UTF-8");
        Room userRoom = (Room) request.getSession().getAttribute("room");//get user's game
        String requestingUser = ((Player) request.getSession().getAttribute("ThisSessionPlayer")).getName();
        userGame = userRoom.getController();
        String btnSelected = request.getParameter("values");//selected button
        if(btnSelected.equals("quitbtn")){
            System.out.println("clicked quit button working on it");
            quitGame = true;
            userRoom.removePlayer((String)request.getSession().getAttribute("username")); //updates turn of too
            if(userGame.gameIsOver()){
                userGame.beforeGameOver();
                if(userRoom.getEngine().getPlayers().size()==0){
                    System.out.println("setting game over");
                    userGame.gameOver();
                    userRoom.setGameIsAlive(false);
                    userRoom.restartPlayers();
                }

            }
        }
        else if (userRoom.isGameIsAlive()) {
            if (requestingUser.equals(userGame.currentPlayerName())) {
                System.out.println(btnSelected);
                if (btnSelected != null) {
                    switch (btnSelected) {
                        case "cubebtn":
                            System.out.println("cube");
                            userGame.throwCube();
                            break;
                        case "exposebtn":
                            System.out.println("expose");

                            userGame.expose();
                            break;
                        case "testwordbtn":
                            System.out.println("test word");

                            userGame.testWord();
                            break;

                        default: ///tile selected
                            System.out.println("tile expose");
                            userGame.effectTile(Integer.parseInt(btnSelected.substring(0, btnSelected.indexOf("_"))),
                                    Integer.parseInt(btnSelected.substring(btnSelected.indexOf("_") + 1)));
                            break;
                        //                    }
                    }
                }
                request.getSession().setAttribute("game", userGame);/// save new game data


            }
        }
        if(!quitGame)
            response.setStatus(204);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
}


