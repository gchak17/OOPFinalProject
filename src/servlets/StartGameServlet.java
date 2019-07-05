package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import game.Game;
import game.GameManager;
import game.Player;
import game.Room;

/**
 * Servlet implementation class StartGameServlet
 */
@WebServlet("/StartGameServlet")
public class StartGameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartGameServlet() {
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
		String id = request.getParameter("id");
		Room r = GameManager.getInstance().getRoomById(id);
		Account admin = r.getAdmin();
		if(((Account)request.getSession().getAttribute("user")).equals(admin)) {
			if(r.getPlayers().size() < 2) {
				//utxras daelodos oponentebs
			}else {
				Game g =  new Game(r.getPlayers(), r.getRounds(), r.getTime(), id);
				GameManager.getInstance().addGame(g);
				
				request.getRequestDispatcher("client.html").forward(request, response);
				
	 		}
		}else {
			//only admin can start
		}
	}

}
