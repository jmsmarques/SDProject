package admin.action.nav;

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
		Boolean admin;
		String user, password;
		result = (Boolean)session.get("loggedin");
		admin = (Boolean)session.get("admin");
		if(result != null && admin != null) {
			user = (String) ((LoginBean) session.get("loginBean")).getUsername(); //gets the user thats logged in
			password = (String) ((LoginBean)session.get("loginBean")).getPassword();
			if(result && admin && session.get("username").equals(user) && session.get("password").equals(password)) { //only allows navigation if user is logged in
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
