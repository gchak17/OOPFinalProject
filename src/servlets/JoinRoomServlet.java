package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import game.GameManager;
import game.Player;
import game.Room;

/**
 * Servlet implementation class JoinRoomServlet
 */
@WebServlet("/JoinRoomServlet")
public class JoinRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JoinRoomServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("joinRoomis geti");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account user = (Account)request.getSession().getAttribute("user");
		Player newPlayer = new Player(user);
		
		String id = request.getParameter("id");
		
		Room r = GameManager.getInstance().getRoomById(id);
		
		request.getSession().setAttribute("gameId", id);//game id da room id erti da igiveebia 
		request.getSession().setAttribute("player", newPlayer);
		
		request.setAttribute("id", id);
		
		if(r.addPlayer(newPlayer)) {
			request.getRequestDispatcher("WaitingForOpponents.jsp").forward(request, response);
		}else {
			//utxras ro daarefreshos an sxva airchios
			//igive pageze rom fanjara amoxtes egrec gamova 	
			//request.getRequestDispatcher("ShowWaitingRooms.jsp").forward(request, response);
		}
		
	}

}
