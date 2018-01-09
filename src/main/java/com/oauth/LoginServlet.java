package com.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonObjectParser;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthorizationCodeFlow flow;
    /**
     * Default constructor. 
     */
    public LoginServlet() {
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		flow = AuthorizationCodeFlowFactory.getInstance();
		final Credential credential = flow.loadCredential(request.getSession().getId());
		if(credential == null){
			String url = flow.newAuthorizationUrl().build();
			response.sendRedirect(url);
		}else{
			//Request the User Info passing the access token
			HttpRequestFactory requestFactory = flow.getTransport().createRequestFactory(new HttpRequestInitializer() {
				
				public void initialize(HttpRequest request) throws IOException {
					credential.initialize(request);
					request.setParser(new JsonObjectParser(flow.getJsonFactory()));
					
				}
			});
			User u = requestFactory.buildGetRequest(new GenericUrl("https://api.github.com/user")).execute().parseAs(User.class);
			request.setAttribute("user", u);
			request.getRequestDispatcher("/WEB-INF/jsps/home.jsp").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Login Servlet - POST");
		doGet(request, response);
	}

}
