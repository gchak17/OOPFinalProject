package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import managers.AccountData;

/**
 * Servlet implementation class SeeProfileServlet
 */
@WebServlet("/SeeProfileServlet")
public class SeeProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SeeProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
<<<<<<< HEAD:src/servlets/SeeProfileServlet.java
		response.getWriter().append("Served at: ").append(request.getContextPath());
=======
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		String friendName = request.getParameter("friendName");
		Account friend = accountData.getAccountByUsername(friendName);
		request.getSession().setAttribute("friend", friend);
		request.getRequestDispatcher("Profile.jsp").forward(request, response);
		
>>>>>>> 05d02e715fff1027bac19cf50318b947c254101d:src/servlets/SeeFriendProfile.java
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
<<<<<<< HEAD:src/servlets/SeeProfileServlet.java
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		String currAccountUsername = request.getParameter("currAccountUsername");
		Account currAccount = accountData.getAccountByUsername(currAccountUsername);
		request.getSession().setAttribute("currAccount", currAccount);
		System.out.println(currAccountUsername);
		request.getRequestDispatcher("Profile.jsp").forward(request, response);
=======
		doGet(request, response);
>>>>>>> 05d02e715fff1027bac19cf50318b947c254101d:src/servlets/SeeFriendProfile.java
	}

}
