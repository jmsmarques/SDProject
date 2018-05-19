package admin.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import admin.model.VoteInAdvanceBean;

public class ListElectionsAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private List<String> lists = new ArrayList<String>();
	
	public String execute() {
		Boolean permission;
		permission = (Boolean) session.get("loggedin");
		if(permission != null && permission && (boolean)session.get("admin")) {
			VoteInAdvanceBean aux = ((VoteInAdvanceBean) session.get("voteInAdvanceBean"));
			ArrayList<String> auxList = (ArrayList<String>) aux.getElectionsList();
			if(auxList != null) {
				for(int i = 0; i < auxList.size(); i++) {
					lists.add(auxList.get(i));
				}
			}
			else {
				auxList = new ArrayList<String>();
				auxList.add("No Elections Available");
			}
			aux.setElections(lists);
			return SUCCESS;
		}
		else {
			return LOGIN;
		}	
	}

	//vote in advance bean get and setter
	public VoteInAdvanceBean getVoteInAdvanceBean() {
		if(!session.containsKey("voteInAdvanceBean")) {
			this.setVoteInAdvanceBean(new VoteInAdvanceBean());
		}
		return (VoteInAdvanceBean) session.get("voteInAdvanceBean");
	}

	public void setVoteInAdvanceBean(VoteInAdvanceBean voteInAdvanceBean) {
		this.session.put("voteInAdvanceBean", voteInAdvanceBean);
	}
	//end of vote in advance bean get and setter

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
