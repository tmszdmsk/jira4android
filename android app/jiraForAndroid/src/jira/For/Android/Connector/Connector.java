package jira.For.Android.Connector;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import jira.For.Android.ConnectorsModule;
import jira.For.Android.DLog;
import jira.For.Android.DataTypes.Comment;
import jira.For.Android.DataTypes.Filter;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.DataTypes.IssueType;
import jira.For.Android.DataTypes.Priority;
import jira.For.Android.DataTypes.Project;
import jira.For.Android.DataTypes.Status;
import jira.For.Android.DataTypes.User;
import jira.For.Android.DataTypes.WorkLog;
import jira.For.Android.ImagesCacher.ImagesCacher;
import jira.For.Android.RemoteExceptions.RemoteAuthenticationException;
import jira.For.Android.RemoteExceptions.RemoteException;
import jira.For.Android.RemoteExceptions.RemotePermissionException;
import jira.For.Android.RemoteExceptions.RemoteValidationException;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jira4android.connectors.AuthenthicationService;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/*
 * Przykładowe dane do logowania: USERNAME: test.user PASSWORD test.password
 * JIRAURL: http://jira.wmi.amu.edu.pl
 */
/**
 * Connector is a Singleton and is responsible for all connections to jira
 * server getting projects,tasks etc
 */
public final class Connector {

	public static  URL instanceURL;
	{
		try {
	        instanceURL = new URL("http://jira.wmi.amu.edu.pl");
        } catch (MalformedURLException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	}
	private static final int TIME_OUT = 50000;
	private Transport transportSe;
	private SoapSerializationEnvelope ENVELOPE;
	private ConnectorProjects connectorProjects;
	private ConnectorIssues connectorIssues;
	private ConnectorFilters connectorFilters;
	private ConnectorComments connectorComments;
	private ConnectorWorkLog connectorWorkLog;
	private ConnectorUser connectorUser;
	private ConnectorIssueTypes connectorIssueTypes;
	private ConnectorPriority connectorPriority;
	private ConnectorStatus connectorStatus;
	private AuthenthicationService authenthicationService = new AuthenthicationService();
	private String jiraUrl, username, password, url;
	private boolean acceptAllSSL;
	private HashMap<String, User> users;
	private ImagesCacher imagesCacher;
	public HashMap<String, String> issuesTypesNames = new HashMap<String, String>();
	public HashMap<String, String> issuesPrioritiesNames = new HashMap<String, String>();
	public HashMap<String, String> issuesStatusesNames = new HashMap<String, String>();
	private static HashMap<String, IssueType> issueTypes;
	private static HashMap<String, Priority> priorities;
	private static HashMap<String, Status> statuses;
	private static String token;
	private static String wsdl = "/rpc/soap/jirasoapservice-v2?wsdl";
	// Namespace we dontneed it now.
	private static String NAMESPACE = "something.unical.fuck.yeah";
	private static Connector INSTANCE = null;
	private boolean isConnected = false;

	private Connector() {
		Injector injector = Guice.createInjector(new ConnectorsModule());
		injector.injectMembers(authenthicationService);
	}

	public boolean getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public static synchronized void setInstanceNULL() {
		INSTANCE = null;
	}

	public static synchronized Connector getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Connector();
			// Czy to ładnie rozwiązanie nie wiem....
			INSTANCE.connectorIssues = new ConnectorIssues();
			INSTANCE.connectorProjects = new ConnectorProjects();
			INSTANCE.connectorFilters = new ConnectorFilters();
			INSTANCE.connectorComments = new ConnectorComments();
			INSTANCE.connectorWorkLog = new ConnectorWorkLog();
			INSTANCE.connectorPriority = new ConnectorPriority();
			INSTANCE.connectorIssueTypes = new ConnectorIssueTypes();
			INSTANCE.connectorStatus = new ConnectorStatus();

			Log.i("instance", INSTANCE.connectorIssueTypes.toString());

			INSTANCE.connectorUser = new ConnectorUser();
			INSTANCE.users = new HashMap<String, User>();
			INSTANCE.issueTypes = new HashMap<String, IssueType>();
			INSTANCE.priorities = new HashMap<String, Priority>();
			INSTANCE.statuses = new HashMap<String, Status>();

		}
		return INSTANCE;
	}


	private void setUsernamePasswordUrl(String username, String password,
	        String url) {

		this.username = username;
		this.password = password;
		this.url = url;
	}

	/**
	 * This method login user to jira and get a token form it.
	 * 
	 * @return Returns true if login gone okey.
	 * @throws AuthorizationException
	 * @throws Exception
	 */
	public boolean jiraLogin(String username, String password, String url,
	        boolean secureConnection)
	        throws AuthenticationException, CommunicationException,
	        AuthorizationException {
		this.jiraUrl = url + wsdl;
		String protocol = secureConnection ? "https" : "http";
		String hostandport = url.split("/", 1)[0];
		String host = hostandport.split(":", 1)[0];
		Integer port = null;
		if (hostandport.split(":", 1).length > 1) {
			port = Integer.parseInt(hostandport.split(":", 1)[1]);
		}
		String path = "";
		if (url.split("/", 1).length > 1) {
			path = url.split("/", 1)[1];
		}
		URL surl = null;
		try {
			if (port == null) {

				surl = new URL(protocol, host, path);

			}
			else {
				surl = new URL(protocol, host, port, path);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		authenthicationService.login(username, password, surl);
		
		try {
			downloadIssueTypes();
			downloadPriorities();
			downloadStatuses();
			INSTANCE.imagesCacher = ImagesCacher.getInstance2(issueTypes,
					                   priorities, statuses, null);
			imagesCacher.downloadAllNeededData();
			setUsernamePasswordUrl(username, password, url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * This method logout user from the server.
	 * 
	 * @return Returns true if logout gone okey.
	 * @throws Exception
	 */
	public boolean jiraLogout() {
		authenthicationService.logout();
		return true;
	}

	/**
	 * This method gets Projects from the server.
	 * @throws AuthenticationException 
	 * @throws AuthorizationException 
	 * @throws CommunicationException 
	 * 
	 * @throws Exception
	 */
	public Project[] getProjects(boolean downloadAvatars) throws CommunicationException, AuthorizationException, AuthenticationException  {
		return connectorProjects.jiraGetProjects(downloadAvatars);
	}

	/**
	 * @return Returns an array of Tasks
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws Exception
	 *             with msg Project ID is null or Not Connected
	 */
	public Issue[] getIssues(String projectId) throws IOException,
	        XmlPullParserException, Exception {
		return connectorIssues.jiraGetIssues("type=Story AND project="
		        + projectId, 20);
	}

	/**
	 * @return Returns an array of SubTasks
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws Exception
	 *             with msg Not Connected or Issue Key is null
	 */
	public Issue[] getSubIssues(String issueKey) throws IOException,
	        XmlPullParserException, Exception {

		if (issueKey != null) {
			return connectorIssues.jiraGetIssues("parent=" + issueKey, 20);
		}
		else {
			throw new Exception("Issue Key is null");
		}
	}

	/**
	 * @return Returns an array of Tasks
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws Exception
	 *             with msg Not Connected or Wrong arguments...
	 */
	public Issue[] getIssuesByJQL(String query, int ile) throws IOException,
	        XmlPullParserException, Exception {

		return connectorIssues.jiraGetIssues(query, ile);
	}

	/**
	 * Returns an array of issues which are output of saved filter
	 * 
	 * @return Tab of issues
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws Exception
	 */
	public Issue[] getIssuesFromFilterWithLimit(String filterId, int offSet,
	        int maxNumResults) throws IOException, XmlPullParserException,
	        Exception {

		return connectorIssues.jiraGetIssuesFromFilterWithLimit(filterId,
		        offSet, maxNumResults);
	}

	/**
	 * Returns an array of filters which are saved on server
	 * 
	 * @return Tab of filters
	 * @throws XmlPullParserException
	 *             , Exception
	 * @throws IOException
	 * @throws Exception
	 *             with msg Not Connected or Issue Key is null
	 */
	public Filter[] getFilters() throws IOException, XmlPullParserException,
	        Exception {

		return connectorFilters.jiraGetFilters();
	}

	/**
	 * Returns a list of comments
	 * 
	 * @return List of comments
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws Exception
	 */
	public synchronized List<Comment> getComments(String issueKey)
	        throws IOException, XmlPullParserException, Exception {

		return connectorComments.jiraGetComments(issueKey);
	}

	public void addComment(String issueKey, Comment comment)
	        throws IOException, XmlPullParserException, Exception {

		connectorComments.jiraAddComment(issueKey, comment);
	}

	/**
	 * Returns a list of worklog
	 * 
	 * @return List of worklog
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws Exception
	 */
	public synchronized List<WorkLog> getWorkLog(String issueKey)
	        throws IOException, XmlPullParserException, Exception {

		return connectorWorkLog.jiraGetWorklogs(issueKey);
	}

	/**
	 * Download User full information
	 * @throws AuthenticationException 
	 * @throws AuthorizationException 
	 * @throws CommunicationException 
	 * 
	 * @throws Exception
	 */
	User downloadUserInformation(String username) throws CommunicationException, AuthorizationException, AuthenticationException {

		if (!users.containsKey(username)) {
			users.put(username, connectorUser.jiraGetUser(username));
		}
		return getUser(username);
	}

	/**
	 * Get user form HashMap
	 * 
	 * @return
	 */
	public User getUser(String username) {
		return users.get(username);
	}

	/**
	 * Get all users
	 */
	public HashMap<String, User> getUsers() {
		// System.out.println("Mam tyle userow: " + users.size());
		return users;
	}

	/**
	 * Get user logged in this application
	 * 
	 * @return
	 * @throws Exception
	 */
	public User getThisUser() {

		return getUser(username);
	}

	/*
	 * Poniższe metody dotyczące IssueTypes i Priorities nie działają do końca
	 * (nie wiedzieć czemu, wywalają błędy o nullowości HashMapy), jednakże są
	 * one tutaj, by uniknąć niepotrzebnych mergy w najbliższym czasie, - Michał
	 */
	/**
	 * Download all issue types
	 * 
	 * @throws Exception
	 */
	public void downloadIssueTypes() throws IOException,
	        XmlPullParserException, RemoteException {
		issueTypes = connectorIssueTypes.jiraGetIssueTypes();
	}

	/**
	 * Get issue type from HashMap
	 */
	public IssueType getIssueType(String type) {
		return issueTypes.get(type);
	}

	/**
	 * Get all issue types
	 */
	public HashMap<String, IssueType> getIssueTypes() {
		return issueTypes;
	}

	/**
	 * Download all priorities
	 * @throws AuthenticationException 
	 * @throws AuthorizationException 
	 * @throws CommunicationException 
	 * @throws RemoteException 
	 * 
	 * @throws Exception
	 */
	public void downloadPriorities() throws RemoteException, CommunicationException, AuthorizationException, AuthenticationException  {
		priorities = connectorPriority.jiraGetPriorities();
	}

	/**
	 * Get priority type from HashMap
	 */
	public Priority getPriority(String priority) {
		DLog.i("ilosc priorytetow", String.valueOf(priorities.size()));
		return priorities.get(priority);
	}

	/**
	 * Download all statuses
	 * @throws AuthenticationException 
	 * @throws AuthorizationException 
	 * @throws CommunicationException 
	 * 
	 * @throws Exception
	 */
	public void downloadStatuses() throws CommunicationException, AuthorizationException, AuthenticationException {
		statuses = connectorStatus.jiraGetStatuses();
	}

	/**
	 * Get status type from HashMap
	 */
	public Status getStatus(String status) {
		return statuses.get(status);
	}

	/**
	 * Get all priorities
	 */
	public HashMap<String, Priority> getPriorities() {
		return priorities;
	}

	/**
	 * @return Returns the Token
	 */
	public String getToken() {
		return authenthicationService.getToken();
	}

	/**
	 * @return Returns the NameSpace
	 */
	public String getNameSpace() {
		return NAMESPACE;
	}

	/**
	 * @return Returns the JiraUrl
	 */
	public String getJiraUrl() {
		return jiraUrl;
	}

	/**
	 * Create new Serializable Envelope
	 * 
	 * @return new SoapSerializationEnvelope
	 */
	public SoapSerializationEnvelope getNewEnvelope() {
		return new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
	}

	/**
	 * Check do we have internet connection simply is data transfer is available
	 */
	public boolean doWeHaveInternet(Context context) {

		if (context == null) {
			DLog.e("Connector", "doWeHaveInternet() context is NULL!");
			return false;
		}
		ConnectivityManager manager = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = manager.getActiveNetworkInfo();
		if (netInfo == null) {
			return false;
		}

		return netInfo.isConnected();
	}

}
