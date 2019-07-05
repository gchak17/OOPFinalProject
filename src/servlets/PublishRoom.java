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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setAttribute("id", id);	
		request.getRequestDispatcher("WaitingForOpponents.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int Rounds = Integer.parseInt(request.getParameter("Rounds"));
		
		int selectedTime = Integer.parseInt(request.getParameter("time"));
		
		int MaxPlayer = Integer.parseInt(request.getParameter("MaxPlayers"));
		//pre game klasi gvchirdeba ragac rac amat chaimaxsovrebs
		
		Account admin = (Account)request.getSession().getAttribute("user");
		//System.out.println(admin.toString());
		
		
		//int id = GameManager.getInstance().getWaitingRooms().size();
		Room waitingRoom = new Room(admin, Rounds, selectedTime, MaxPlayer);
		
		id = GameManager.getInstance().getWaitingRooms().size();
		request.setAttribute("id", GameManager.getInstance().getWaitingRooms().size());	
		GameManager.getInstance().getWaitingRooms().add(waitingRoom);
//		
//		System.out.println(GameManager.getInstance().getWaitingRooms());
//		System.out.println("aq movida");
		
		request.getRequestDispatcher("WaitingForOpponents.jsp").forward(request, response);
		//mgoni im waiting pageze socketis damateba dachirdeba
	}

	private Object id;
}
