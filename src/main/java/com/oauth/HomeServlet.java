package com.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonObjectParser;

/**
 * Servlet implementation class HomeServlet
 */
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthorizationCodeFlow flow;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		flow = AuthorizationCodeFlowFactory.getInstance();
		//authorization code
		String code = request.getParameter("code");
		//Request Access token passing the auth code
		TokenResponse tokenResponse = flow.newTokenRequest(code).setScopes(Collections.singletonList("user:email"))
				.setRequestInitializer(new HttpRequestInitializer() {
					public void initialize(HttpRequest request) throws IOException {
						request.getHeaders().setAccept("application/json");
					}
				}).execute();
		System.out.println(request.getSession().getId());
		//Create Credential
		final Credential credential = flow.createAndStoreCredential(tokenResponse, request.getSession().getId());
		//Request User Info passing the access token
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
