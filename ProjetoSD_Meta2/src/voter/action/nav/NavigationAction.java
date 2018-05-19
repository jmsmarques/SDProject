package voter.action.nav;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import login.model.LoginBean;

public class NavigationAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	
	//authorizes navigation operation
	public String execute() {
		Boolean result;
		
		String user, password;
		result = (Boolean)session.get("loggedin");
		
		if(result != null) {
			user = (String) (session.get("username")); //gets the user thats logged in
			//password = (String) ((LoginBean)session.get("loginBean")).getPassword();
			if(result  && session.get("username").equals(user)) { //only allows navigation if user is logged in
				return SUCCESS;
			}
			else {
				return LOGIN;
			}
		}
		else 
			return LOGIN;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
