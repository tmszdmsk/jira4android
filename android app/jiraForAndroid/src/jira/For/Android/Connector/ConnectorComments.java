package jira.For.Android.Connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jira.For.Android.DataTypes.Comment;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;
@Singleton
public class ConnectorComments {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	public synchronized List<Comment> getComments(String issueKey)
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {
		if (issueKey == null) {
			throw new IllegalArgumentException("issueKey cannot be null");
		}

		SoapObject getComments = SoapObjectBuilder.start()
		        .withMethod("getComments")
		        .withProperty("token", connector.getToken())
		        .withProperty("issueKey", issueKey).build();

		Vector<SoapObject> vc = soap.execute(getComments, Vector.class);
		if (vc == null) {
			return null;
		}

		List<Comment> comments = new ArrayList<Comment>();

		SoapObject p;// Temporary variable!
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);

			// TODO consider whether all these fields are necessary

			String author = p.getPropertySafelyAsString("author");
			if (author == null || author.compareToIgnoreCase("null") == 0) {
				System.out.println("Ej no tak nie powinno być!!!!!!!!!");
			}

			comments.add(new Comment(connector.downloadUserInformation(author),
			        p.getPropertySafelyAsString("body"), p
			                .getPropertySafelyAsString("groupLevel"), p
			                .getPropertySafelyAsString("id"), p
			                .getPropertySafelyAsString("roleLevel"), p
			                .getPropertySafelyAsString("updateAuthor"), p
			                .getPropertySafelyAsString("created"), p
			                .getPropertySafelyAsString("updated")));
		}

		return comments;
	}

	/**
	 * Adds a comment to the specified issue
	 * 
	 * @param message
	 * @param issueKey
	 * @throws AuthenticationException
	 * @throws AuthorizationException
	 * @throws CommunicationException
	 */
	public void addComment(String issueKey, Comment comment)
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {

		if (comment == null || issueKey == null) {
			throw new IllegalArgumentException("Wrong arguments\ncomment: "
			        + comment.toString() + "\nissueKey: " + issueKey);
		}

		SoapObject remoteComment = new SoapObject(
		        "http://beans.soap.rpc.jira.atlassian.com", "RemoteComment");

		remoteComment.addProperty("body", comment.getBody());

		SoapObject addComment = SoapObjectBuilder.start()
		        .withMethod("addComment")
		        .withProperty("token", connector.getToken())
		        .withProperty("issueKey", issueKey)
		        .withSoapObject(remoteComment).build();

		soap.execute(addComment);
	}
}
