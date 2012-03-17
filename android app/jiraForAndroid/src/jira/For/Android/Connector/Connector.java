package jira.For.Android.Connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
 * Przykładowe dane do logowania: USERNAME: test.user PASSWORD test.password
 * JIRAURL: http://jira.wmi.amu.edu.pl
 */
/**
 * Connector is a Singleton and is responsible for all connections to jira
 * server getting projects,tasks etc
 */
public final class Connector {

	private static final int TIME_OUT = 50000;
	private HttpTransportSE transportSe;
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
	private String jiraUrl, username, password, url;
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

	private Connector() {}

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

	SoapSerializationEnvelope getResponseFromServer(SoapObject soapObject)
	        throws Exception {

		ENVELOPE = getNewEnvelope();
		ENVELOPE.setOutputSoapObject(soapObject);

		// Zostawić mi printy!!! do testów bardzo potrzebne!
		// System.out.println("Tak wygląda to co chce wysłac: " +
		// ENVELOPE.bodyOut);

		// We send a question to the server in envelope
		transportSe.call("", ENVELOPE);
		// Check our response form server

		// System.out.println("Dump:" + transportSe.requestDump);
		//
		// System.out.println("ODpowiedz: " + ENVELOPE.bodyIn);
		// System.out.println("ODump:" + transportSe.responseDump);

		jiraReturnedException(ENVELOPE, soapObject, 0);
		// Return envelope for do smthing with it :D

		return ENVELOPE;
	}

	/**
	 * @param envelope
	 * @return null if vector size is equal 0 else returns vector with objects
	 */
	Vector<SoapObject> getSoapObjectsFromResponse(
	        SoapSerializationEnvelope envelope) {

		SoapObject body = (SoapObject) envelope.bodyIn;

		@SuppressWarnings("unchecked")
		Vector<SoapObject> vectorOfSoapObjects = (Vector<SoapObject>) body
		        .getProperty(0);
		if (vectorOfSoapObjects == null) DLog.e("WTF",
		        "Problem przy vc body.getProperty");

		if (vectorOfSoapObjects.size() == 0) return null;
		return vectorOfSoapObjects;
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
	 * @throws Exception
	 */
	public boolean jiraLogin(String username, String password, String jiraUrl,
	        boolean isHttps) throws Exception {

		// Dodajemy adres dokumentu WSDL który zawiera definicje metod do
		// komunikacji z jirą
		this.jiraUrl = jiraUrl + wsdl;
		Log.d(this.toString(), "Https is: " + isHttps);
		if (isHttps) transportSe = getHttpsTransportSe(jiraUrl, wsdl);
		else {
			transportSe = getHttpTransportSe();

			// Zwykła komunikacja do servera przez Http
			transportSe.setUrl("http://" + this.jiraUrl);
		}

		// transportSe.debug = true;

		// Tworzymy zapytanie które zostanie odpowiednio opakowane przez
		// envelope
		SoapObject login = new SoapObject(NAMESPACE, "login");
		// Ustawiamy argumenty w takiej kolejności jakiej będą podawane do
		// zdalnych procedur
		login.addProperty("username", username);
		login.addProperty("password", password);

		SoapSerializationEnvelope envelope = getResponseFromServer(login);

		// Zwróci token jeżeli się wszystko powiedzie
		token = (String) envelope.getResponse();

		// Adding me
		users.put(username, new User(username, "Me", ""));

		DLog.i("LOGIN", "Udalo sie zalogowac z tokenem: " + token);
		if (token != null) {

			downloadIssueTypes();
			downloadPriorities();
			downloadStatuses();

			INSTANCE.imagesCacher = ImagesCacher.getInstance2(issueTypes,
			        priorities, statuses,null);//TODO PRzekazać tutaj context! APLIKACJI!
			imagesCacher.downloadAllNeededData();

			if (this.username == null && this.password == null
			        && this.url == null) setUsernamePasswordUrl(username,
			        password, jiraUrl);
			return true;
		}
		else return false;
	}

	/**
	 * This method logout user from the server.
	 * 
	 * @return Returns true if logout gone okey.
	 * @throws Exception
	 */
	public boolean jiraLogout() throws Exception {

		SoapObject logout = new SoapObject(NAMESPACE, "logout");
		logout.addProperty("token", token);

		SoapSerializationEnvelope envelope = getResponseFromServer(logout);

		Boolean answer = (Boolean) envelope.getResponse();
		DLog.i("LOGOUT", "Co do wylogowania server zwrócił: " + answer);

		if (answer.booleanValue()) {
			token = null;
			return true;
		}
		else return false;
	}

	/**
	 * This method gets Projects from the server.
	 * 
	 * @throws Exception
	 */
	public Project[] getProjects(boolean downloadAvatars) throws Exception {
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

		if (issueKey != null) return connectorIssues.jiraGetIssues("parent="
		        + issueKey, 20);
		else throw new Exception("Issue Key is null");
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
	 * 
	 * @throws Exception
	 */

	User downloadUserInformation(String username) throws Exception {

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
	public void downloadIssueTypes() throws Exception {
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
	 * 
	 * @throws Exception
	 */
	public void downloadPriorities() throws Exception {
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
	 * 
	 * @throws Exception
	 */
	public void downloadStatuses() throws Exception {
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
		return token;
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
		if (netInfo == null) return false;

		return netInfo.isConnected();
	}

	private HttpsTransportSE getHttpsTransportSe(String host, String file) {
		return new HttpsTransportSE(host, 443, file, TIME_OUT);
	}

	private HttpTransportSE getHttpTransportSe() {
		return new HttpTransportSE("", TIME_OUT);
	}

	/**
	 * @param envelope
	 * @param lvlOfRecursion
	 *            if this int is < 3 we will try again call the same function
	 *            after recall
	 * @throws Exception
	 */
	void jiraReturnedException(SoapSerializationEnvelope envelope,
	        SoapObject soapObject, int lvlOfRecursion) throws Exception {

		// TODO zarobić szybsze/ładniejsze porównywanie stringów

		String message = null;
		try {
			envelope.getResponse();
		} catch (SoapFault soapFault) {
			message = soapFault.getMessage();
		}

		if (message == null) return;

		DLog.e("Connector", "Have Exception?");
		if (message.contains("RemoteAuthenticationException")) {
			if (message.contains("session timed out")) {
				DLog.i("Connector",
				        "I must reconnect probably token it out of date");
				// System.out.println("Tresc envelope przedIN: " +
				// envelope.bodyIn);
				// System.out.println("Tresc envelope przedOUT: "
				// + envelope.bodyOut);
				jiraLogin(this.username, this.password, this.url, true);

				if (soapObject.hasProperty("token")) {
					soapObject.getProperty(0);// Mam nadzieje ,że zawsze jest
					                          // jako pierwszy
					// TODO czy można spr pozycje tokena w property? jeżeli tak
					// to to zrobić ładniej
					soapObject.setProperty(0, token);
				}
				else throw new Exception("No token in SoapObject?\n"
				        + soapObject.toString() + envelope.bodyIn
				        + envelope.bodyOut);

				envelope.setOutputSoapObject(soapObject);

				transportSe.call("after-relog-try-again", envelope);
				// System.out.println("Tresc envelope poIN: " +
				// envelope.bodyIn);
				// System.out.println("Tresc envelope poOUT: " +
				// envelope.bodyOut);
				// Do 3 razy sztuka <3 :D
				if (lvlOfRecursion < 3) jiraReturnedException(envelope,
				        soapObject, lvlOfRecursion + 1);
			}
			else throw new RemoteException(message);
		}

		else if (message.contains("RemotePermissionException")) throw new RemotePermissionException(
		        message);
		else if (message.contains("RemoteAuthenticationException")) throw new RemoteAuthenticationException(
		        message);
		else if (message.contains("RemoteValidationException")) throw new RemoteValidationException(
		        message);
		else throw new Exception(message);
	}
}
