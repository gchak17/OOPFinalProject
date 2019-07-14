package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Account;
import dao.Avatar;
import managers.AccountData;
import managers.AvatarManager;

/**
 * Servlet implementation class ChangeAvatarServlet
 */
@WebServlet("/ChangeAvatarServlet")
public class ChangeAvatarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeAvatarServlet() {
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
		
		Account acc = (Account) request.getSession().getAttribute("user");
		if (acc == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		Account account = (Account) request.getSession().getAttribute("user");
		AvatarManager avatarManager = (AvatarManager) getServletContext().getAttribute("avatarManager");
		long avatar_id = Long.valueOf(request.getParameter("avatar_id"));
		Avatar avatar = avatarManager.getAvatarByID(avatar_id);
		accountData.changeAccountAvatar(account, avatar);
		request.getSession().setAttribute("user", accountData.getAccountByID(account.getID()));
		request.getRequestDispatcher("Main.jsp").forward(request, response);
	}

}
