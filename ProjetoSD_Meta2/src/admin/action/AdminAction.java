package admin.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import admin.model.CollegeBean;
import admin.model.DepartmentBean;
import admin.model.ElectionBean;
import admin.model.ListBean;
import admin.model.NrVotesBean;
import admin.model.TableBean;
import admin.model.UserBean;
import admin.model.VoteInAdvanceBean;

public class AdminAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	
	public String execute() {
		Boolean permission;
		permission = (Boolean) session.get("loggedin");
		if(permission != null && permission && (boolean)session.get("admin")) {
			return SUCCESS;
		}
		else {
			return LOGIN;
		}	
	}
	//user bean get and setter
	public UserBean getUserBean() {
		if(!session.containsKey("userBean"))
			this.setUserBean(new UserBean());
		return (UserBean) session.get("userBean");
	}

	public void setUserBean(UserBean userBean) {
		this.session.put("userBean", userBean);
	}
	//end of user bean get and setter

	//college bean get and setter
	public CollegeBean getCollegeBean() {
		if(!session.containsKey("collegeBean"))
			this.setCollegeBean(new CollegeBean());
		return (CollegeBean) session.get("collegeBean");
	}

	public void setCollegeBean(CollegeBean collegeBean) {
		this.session.put("collegeBean", collegeBean);
	}
	//end of college bean get and setter

	//department bean get and setter
	public DepartmentBean getDepartmentBean() {
		if(!session.containsKey("departmentBean"))
			this.setDepartmentBean(new DepartmentBean());
		return (DepartmentBean) session.get("departmentBean");
	}

	public void setDepartmentBean(DepartmentBean departmentBean) {
		this.session.put("departmentBean", departmentBean);
	}
	//end of department bean get and setter

	//election bean get and setter
	public ElectionBean getElectionBean() {
		if(!session.containsKey("electionBean"))
			this.setElectionBean(new ElectionBean());
		return (ElectionBean) session.get("electionBean");
	}

	public void setElectionBean(ElectionBean electionBean) {
		this.session.put("electionBean", electionBean);
	}
	//end of election bean get and setter

	//table bean get and setter
	public TableBean getTableBean() {
		if(!session.containsKey("tableBean"))
			this.setTableBean(new TableBean());
		return (TableBean) session.get("tableBean");
	}

	public void setTableBean(TableBean tableBean) {
		this.session.put("tableBean", tableBean);
	}
	//end table bean get and setter

	//list bean get and setter
	public ListBean getListBean() {
		if(!session.containsKey("listBean"))
			this.setListBean(new ListBean());
		return (ListBean) session.get("listBean");
	}

	public void setListBean(ListBean listBean) {
		this.session.put("listBean", listBean);
	}
	//end list bean get and setter

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

	//nr of vote bean get and setter
	public NrVotesBean getNrVotesBean() {
		if(!session.containsKey("voteInAdvanceBean"))
			this.setNrVotesBean(new NrVotesBean());
		return (NrVotesBean) session.get("nrVotesBean");
	}

	public void setNrVotesBean(NrVotesBean nrVotesBean) {
		this.session.put("nrVotesBean", nrVotesBean);
	}
	//end of nr of vote bean get and setter´

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
