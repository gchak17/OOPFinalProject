package main;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import account.Account;
import account.AccountData;
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		String userName = (String) request.getSession().getAttribute("username");
		String password = (String) request.getSession().getAttribute("password");
		Account user = accountData.getAccount(userName, password);
		Player newPlayer = new Player(user);
		
		int RoomId = Integer.parseInt(request.getParameter("id").substring(5));
		
		
		Room r = GameManager.getInstance().getWaitingRooms().get(RoomId);
		r.addPlayer(newPlayer);

		
		
	}
}
