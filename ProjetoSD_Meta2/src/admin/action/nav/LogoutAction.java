package admin.action.nav;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import login.model.LoginBean;


public class LogoutAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	
	public String execute() {
		if(!((boolean) session.get("admin"))) {
			((LoginBean)session.get("loginBean")).getLogout();
		}
		session.put("loggedin", false);
		session.put("admin", false);
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
