package voter.actions;
import login.model.LoginBean;
import java.util.ArrayList;


import voter.model.ElectionBean;
import voter.model.ListBean;
import voter.model.VoteBean;
import voter.model.FacebookBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.github.scribejava.core.model.Token;
import com.opensymphony.xwork2.ActionSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import java.net.*;
import java.io.*;
import java.awt.*;
import model.VoterBean;
import rmi.VoterRMIInterface;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

import uc.sd.apis.FacebookApi2;




public class VoterAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String elec;
	private String list;
	private String resp;
	private String username;
	private VoteBean voteBean;
	private ElectionBean electionBean;
	private ListBean listBean;
	private FacebookBean facebookBean;
	private String FacebookUrl;
	private String code;
	private String state;
	 private String authorizationUrl=null;
	private ArrayList<String> lista = new ArrayList<>();
	final String NETWORK_NAME = "Facebook";
	final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	final Token EMPTY_TOKEN = null;
	
	public String execute() {

		
	Boolean permission;
		permission = (Boolean) session.get("loggedin");
		if(permission != null && permission) {
		
			setElectionBean(new ElectionBean());
			setLista(electionBean.getLista());
			if(getLista()!=null) {
			return SUCCESS;
			}else {
				return "empty";
			}
			}
		else {
			return LOGIN;
		}	
	
	
	}
	public String executeList() {
		Boolean permission;
		permission = (Boolean) session.get("loggedin");
		if(permission != null && permission) {	
		{
		setElec(this.elec);
		session.put("eleicao", getElec());
		
		setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());
		System.out.println("ELEC: "+ getElec());
		System.out.println("USER: "+ getUsername());
		}
			//this.getElectionBean().setElection();
			setListBean(new ListBean(getElec(),getUsername()));
			setLista(listBean.getLista());
			
			
			if(getLista()!=null) {
				return SUCCESS;
				}else {
					return "empty";
				}
			
	}
	else {
			return LOGIN;
		}	
	
	
	}
	public String executeVote() {
		Boolean permission;
		permission = (Boolean) session.get("loggedin");
		if(permission != null && permission) {	
		{
		setList(this.list);
		setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());
		setElec((String) session.get("eleicao"));
		
		session.put("lista", getList());
		
		
		System.out.println("USER: "+ getUsername());
		System.out.println("ELEC: "+ getElec());
		System.out.println("LIST: "+ getList());
		}
			//this.getElectionBean().setElection();
			setVoteBean(new VoteBean(getElec(),getUsername(),getList()));
			setResp(getVoteBean().getResp());
			System.out.println("RESPOSTA ASDFASDF:  "+getResp());
			
			
			if(getVoteBean().getResp().equals("Thank you for voting")) {
				 
				
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				String apiKey = "154164651894827";
			    String apiSecret = "84b8ecde7405e2286b29c09cf0ee8de6";
			    
			    
			    OAuthService service = new ServiceBuilder()
			                                  .provider(FacebookApi2.class)
			                                  .apiKey(apiKey)
			                                  .apiSecret(apiSecret)
			                                  .callback("http://169.254.103.228:8080/ProjetoSD_Meta2/votervotefinal2") // Do not change this.
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
			    
			    return "success";
				
				
				
				
				
				
				
				
				
				
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				
				}else if(this.voteBean.getResp().equals("You already voted in this election")) {
					return "rep";
				}else {
					return"erro";
				}
			
	}
	else {
			return LOGIN;
		}	
	
	
	}
	public String executeVote2() {
		Boolean permission;
		permission = (Boolean) session.get("loggedin");
		setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());
		System.out.println("USER: "+ getUsername());
		setElec((String) session.get("eleicao"));
		System.out.println("ELEC: "+getElec());
		
		
		
		System.out.println("------->"+getCode()+"<--------");
		String apiKey = "154164651894827";
	    String apiSecret = "84b8ecde7405e2286b29c09cf0ee8de6";
	  
		OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://169.254.103.228:8080/ProjetoSD_Meta2/votervotefinal2") // Do not change this.
                .scope("publish_actions")
                .build();
		
		
	
		System.out.println("DEU ");
		
		    Verifier verifier = new Verifier(getCode());
		    setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());
		    System.out.println("ELEICAO: "+getElec());
		    
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
		
		  String message = "Acabei de usar o iVotas para votar na eleicao "+ getElec()+". Experimenta tambem!";
          message = message.replace(" ", "%20");
         
          System.out.println(accessToken.toString());
          String aux3=accessToken.toString();
          String aux4[]=aux3.split(",");
          System.out.println(aux4[0]);
          String aux5=aux4[0].substring(6);
          System.out.println(aux5);
         
          String postit = "https://graph.facebook.com/" + aux2[1] + "/feed?message=" + message + "&access_token=" + aux5;
          System.out.println(postit);
          postit=postit.replace(" ", "%20");
          
          OAuthRequest post = new OAuthRequest(Verb.POST, postit,service);
          service.signRequest(accessToken, post);
         
          response=post.send() ;
          System.out.println();
		    System.out.println(response.getCode());
		    System.out.println(response.getBody());
          
		
		
		return SUCCESS;
	}
	
	
	
	
	public String faceAssociation() {
		Boolean permission;
		
		permission = (Boolean) session.get("loggedin");
		if(permission != null && permission) {	
			
			
				setList(this.list);
				setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());
				System.out.println("ELEC: "+ getElec());
				System.out.println("LIST: "+ getList());
				System.out.println("USER: "+ getUsername());
				
				String apiKey = "154164651894827";
			    String apiSecret = "84b8ecde7405e2286b29c09cf0ee8de6";
			    
			    
			    OAuthService service = new ServiceBuilder()
			                                  .provider(FacebookApi2.class)
			                                  .apiKey(apiKey)
			                                  .apiSecret(apiSecret)
			                                  .callback("http://169.254.103.228:8080/ProjetoSD_Meta2/voterfaceassociation2") // Do not change this.
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
			    
			    return "success";
			}
			//this.getElectionBean().setElection();
			
			//setResp(voteBean.getResp());
			//if(getResp()!=null) {
			//	return SUCCESS;
			
				
			
		
		return LOGIN;
	}
	public String faceAssociation2() {
		setUsername((String) ((LoginBean) session.get("loginBean")).getUsername());
		System.out.println("USER: "+ getUsername());
		
		System.out.println("------->"+getCode()+"<--------");
		String apiKey = "154164651894827";
	    String apiSecret = "84b8ecde7405e2286b29c09cf0ee8de6";
	  
		OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("http://169.254.103.228:8080/ProjetoSD_Meta2/voterfaceassociation2") // Do not change this.
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
		    setFacebookBean(new FacebookBean(aux2[1],getUsername()));
		    if(facebookBean.getResp().equals("fail")) {
		    	return "fail";
		    }else {
		    return "success";
		    }
	}
	
	
	
	
	public void setElec(String elec){
		this.elec=elec;
	}
	public String getElec() {
		return elec;
	}
	public ElectionBean getElectionBean() {
		return electionBean;
	}

	public void setElectionBean(ElectionBean electionBean) {
		this.electionBean= electionBean;
	}

	@Override
	public void setSession( Map<String, Object> session) {
		
		this.session = session;
		
	}
	public ArrayList<String> getLista() {
		return lista;
	}
	public void setLista(ArrayList<String> lista) {
		this.lista = lista;
	}
	public ListBean getListBean() {
		return listBean;
	}
	public void setListBean(ListBean listBean) {
		this.listBean = listBean;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	public VoteBean getVoteBean() {
		return voteBean;
	}
	public void setVoteBean(VoteBean voteBean) {
		this.voteBean =voteBean;
	}
	public FacebookBean getFacebookBean() {
		return facebookBean;
	}
	public void setFacebookBean(FacebookBean facebookBean) {
		this.facebookBean = facebookBean;
	}
	public String getFacebookUrl() {
		return FacebookUrl;
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
	
}
