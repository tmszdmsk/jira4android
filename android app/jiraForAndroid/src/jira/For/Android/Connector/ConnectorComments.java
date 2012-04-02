package jira.For.Android.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jira.For.Android.DataTypes.Comment;
import jira.For.Android.RemoteExceptions.RemoteAuthenticationException;
import jira.For.Android.RemoteExceptions.RemoteException;
import jira.For.Android.RemoteExceptions.RemotePermissionException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.jira4android.connectors.KSoapExecutor;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

class ConnectorComments {

    private Connector connector;
    private KSoapExecutor soap = new KSoapExecutor();

    ConnectorComments() {
        connector = Connector.getInstance();
    }

    synchronized List<Comment> jiraGetComments(String issueKey) throws CommunicationException, AuthorizationException, AuthenticationException{
        if (issueKey == null) {
            throw new IllegalArgumentException("issueKey cannot be null");
        }

        SoapObject getComments = new SoapObject(connector.getNameSpace(),
                "getComments");
        getComments.addProperty("token", connector.getToken());
        getComments.addProperty("issueKey", issueKey);


        Vector<SoapObject> vc = soap.execute(getComments, connector.instanceURL, Vector.class);
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
                    p.getPropertySafelyAsString("body"), p.getPropertySafelyAsString("groupLevel"), p.getPropertySafelyAsString("id"), p.getPropertySafelyAsString("roleLevel"), p.getPropertySafelyAsString("updateAuthor"), p.getPropertySafelyAsString("created"), p.getPropertySafelyAsString("updated")));
        }

        return comments;
    }

    /*
     * -
     * void addComment(java.lang.String token, java.lang.String issueKey,
     * RemoteComment remoteComment)
     *
     * throws RemotePermissionException, RemoteAuthenticationException,
     * RemoteException
     *
     */
    /**
     * Adds a comment to the specified issue
     *
     * @param message
     * @param issueKey
     * @throws AuthenticationException 
     * @throws AuthorizationException 
     * @throws CommunicationException 
     */
    void jiraAddComment(String issueKey, Comment comment) throws CommunicationException, AuthorizationException, AuthenticationException {

        if (comment == null || issueKey == null) {
            throw new IllegalArgumentException(
                    "Wrong arguments\ncomment: " + comment.toString()
                    + "\nissueKey: " + issueKey);
        }

        SoapObject remoteComment = new SoapObject("http://beans.soap.rpc.jira.atlassian.com",
                "RemoteComment");



        remoteComment.addProperty("body", comment.getBody());

        SoapObject addComment = new SoapObject(connector.getNameSpace(),
                "addComment");
        addComment.addProperty("token", connector.getToken());
        addComment.addProperty("issueKey", issueKey);

        addComment.addSoapObject(remoteComment);

        soap.execute(addComment, connector.instanceURL);
    }
}
