package login.action;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

import login.model.FacebookLoginBean;
import login.model.LoginBean;
import uc.sd.apis.FacebookApi2;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;



public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null;
	private String FacebookUrl;
	private String code;
	private String state;
	private String authorizationUrl;
	final String NETWORK_NAME = "Facebook";
	final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	final Token EMPTY_TOKEN = null;
	private FacebookLoginBean facebookLoginBean;

	@Override
	public String execute() {
		//sees if the user is already logged in
		Boolean result;
		result = (Boolean)session.get("loggedin");
		if(result != null && result) {
			if((boolean) session.get("admin"))
				return SUCCESS;
			else
				return "voter";
	}
		//if he isn't then
		else if(this.username != null && !username.equals("")) {
			this.getLoginBean().setUsername(this.username);
			this.getLoginBean().setPassword(this.password);
			if(this.getLoginBean().getAdminPassword()) {
				session.put("username", username);
				session.put("password", password);
				session.put("admin", true); //indicates that the user logged in is an admin
				session.put("loggedin", true); // this marks the user as logged in
				return SUCCESS;
			}
			else if(this.getLoginBean().getAuthentication()) {
				session.put("username", username);
				//session.put("password", password);
				session.put("loggedin", true); // this marks the user as logged in
				return "voter";
			}
			return LOGIN;
		}
		else {
			return LOGIN;
		}
	}
	public String faceLogin() {
		String apiKey = "154164651894827";
	    String apiSecret = "84b8ecde7405e2286b29c09cf0ee8de6";
	    
	    
	    OAuthService service = new ServiceBuilder()
	                                  .provider(FacebookApi2.class)
	                                  .apiKey(apiKey)
	                                  .apiSecret(apiSecret)
	                                  .callback("http://169.254.103.228:8080/ProjetoSD_Meta2/voterfacelogin2") // Do not change this.
	                                  .scope("publish_actions")
	                                  .build();
	    //Scanner in = new Scanner(System.in);

	    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
	    System.out.println();

	    // Obtain the Authorization URL
	    System.out.println("Fetching the Authorization URL...");
	    
		this.authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
	    System.out.println("Got the Authorization URL!");
	    System.out.println("Now go and authorize Scribe here:");
	   
	    //session.put("facebookurl", authorizationUrl);
	    this.FacebookUrl=authorizationUrl;
	    System.out.println(authorizationUrl);
	    
	   
		return SUCCESS;
	}
	public String faceLogin2() {
		//setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());
		
		
		System.out.println("------->"+getCode()+"<--------");
		String apiKey = "154164651894827";
	    String apiSecret = "84b8ecde7405e2286b29c09cf0ee8de6";
	  
		OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://169.254.103.228:8080/ProjetoSD_Meta2/voterfacelogin2") // Do not change this.
                .scope("publish_actions")
                .build();
		
		
	
		System.out.println("DEU ");
		
		    Verifier verifier = new Verifier(getCode());
		
		    System.out.println();
		    
		    // Trade the Request Token and Verfier for the Access Token
		    System.out.println("Trading the Request Token for an Access Token...");
		    
		   Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
		    System.out.println("Got the Access Token!");
		   
		    System.out.println();

		   // Now let's go and ask for a protected resource!
		 
		   OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
		    service.signRequest(accessToken, request);
		    Response response = request.send();
		    
		    System.out.println();
		    System.out.println(response.getCode());
		    System.out.println(response.getBody());
		    String aux[]=response.getBody().split(",");
		    String aux1[]=aux[1].split(":");
		    String aux2[]=aux1[1].split("\"");
		    System.out.println("id -------->"+aux2[1]);
		    
	    
		    System.out.println();
		    setFacebookLoginBean(new FacebookLoginBean(aux2[1]));
		    setUsername(this.facebookLoginBean.getId(aux2[1]));
		   
		    System.out.println("isto->" + getUsername());
		    
		    if(getUsername()==null) {
		    	return "login";
		    }else {
		    	session.put("username", getUsername());
				session.put("password", password);
				session.put("loggedin", true);
				getLoginBean().setUsername(getUsername());
				System.out.println(getLoginBean().getUsername() + " " + getLoginBean().getPassword());
				getLoginBean().getAuthentication();
		    return "success";
		    }
	}
	
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		this.password = password; // what about this input? 
	}
	
	public LoginBean getLoginBean() {
		if(!session.containsKey("loginBean"))
			this.setLoginBean(new LoginBean());
		return (LoginBean) session.get("loginBean");
	}

	public void setLoginBean(LoginBean loginBean) {
		this.session.put("loginBean", loginBean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getFacebookUrl() {
		return FacebookUrl;
	}
	public void setFacebookLoginBean(FacebookLoginBean facebookLoginBean){
		this.facebookLoginBean=facebookLoginBean;
	}
	public String getUsername() {
		return this.username;
	}
}
