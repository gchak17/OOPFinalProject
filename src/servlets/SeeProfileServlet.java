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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		String currAccountUsername = request.getParameter("currAccountUsername");
		Account currAccount = accountData.getAccountByUsername(currAccountUsername);
		request.getSession().setAttribute("currAccount", currAccount);
		System.out.println(currAccountUsername);
		request.getRequestDispatcher("Profile.jsp").forward(request, response);
	}

}
