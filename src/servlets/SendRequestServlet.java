package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.Account;
import dao.Avatar;
import managers.AccountData;
import managers.AccountIDGenerator;
import managers.AvatarManager;
import managers.FriendRequestManager;

/**
 * Servlet implementation class SendRequestServlet
 */
@WebServlet("/SendRequestServlet")
public class SendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendRequestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
        }
		JSONObject data = new JSONObject(json);
		String friendUserName = data.getString("friendusername");
		
		AccountData accountData = (AccountData) getServletContext().getAttribute("accountData");
		FriendRequestManager friendRequestManager = (FriendRequestManager) getServletContext().getAttribute("friendRequestManager");
		Account user = (Account)request.getSession().getAttribute("user");
		
		PrintWriter out = response.getWriter(); 
		
		response.setContentType("application/json");
		JSONObject resp = new JSONObject();
		resp.put("success", false);
		resp.put("responseText", "unknown error");
		if(user.getUsername().equals(friendUserName)) {
			resp.put("responseText", "that's your username.");
		}else if(accountData.nameInUse(friendUserName)){
			if(user.getFriendByUsername(friendUserName) != null){
				resp.put("responseText", friendUserName + " is already your friend.");
			}else{
				//call send request method
				friendRequestManager.sendFriendRequest(user.getID(), accountData.getAccountByUsername(friendUserName).getID());
				resp.put("responseText", "friend request to " + friendUserName + " is sent.");
				resp.put("success", true);
				JSONObject notificationLog = new JSONObject();
				notificationLog.put("notificationType", "friendRequest");
				notificationLog.put("senderID", user.getID());
				notificationLog.put("receiverID", accountData.getAccountByUsername(friendUserName).getID());
				resp.put("notificationLog", notificationLog);
			}
		}else{
			resp.put("responseText", "user with that name does not exist.");
			out.append(resp.toString());
		}
		
		out.append(resp.toString());
	}
}