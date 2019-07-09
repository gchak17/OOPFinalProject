package servlets;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import game.GameManager;
import game.Player;
import game.Room;
import managers.AccountData;

/**
 * Servlet implementation class PublishRoom
 */
@WebServlet("/PublishRoom")
public class PublishRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PublishRoom() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("aq ar unda shesuliyo");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int Rounds = Integer.parseInt(request.getParameter("Rounds"));

		int selectedTime = Integer.parseInt(request.getParameter("time"));

		int MaxPlayer = Integer.parseInt(request.getParameter("MaxPlayers"));

		Account admin = (Account) request.getSession().getAttribute("user");
		Player adminP = new Player(admin);

		Room waitingRoom = new Room(adminP, Rounds, selectedTime, MaxPlayer);
		String id = GameManager.getInstance().registerRoom(waitingRoom);

		request.setAttribute("id", id);

		request.getSession().setAttribute("gameId", id);

		request.getSession().setAttribute("player", adminP);

		request.getRequestDispatcher("WaitingForOpponents.jsp").forward(request, response);
		// mgoni im waiting pageze socketis damateba dachirdeba
	}

}
