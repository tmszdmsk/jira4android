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

class ConnectorComments {

    private Connector connector;

    ConnectorComments() {
        connector = Connector.getInstance();
    }

    synchronized List<Comment> jiraGetComments(String issueKey) throws IOException, XmlPullParserException, RemoteException, Exception {
        if (issueKey == null) {
            throw new IllegalArgumentException("issueKey cannot be null");
        }

        SoapObject getComments = new SoapObject(connector.getNameSpace(),
                "getComments");
        getComments.addProperty("token", connector.getToken());
        getComments.addProperty("issueKey", issueKey);

        SoapSerializationEnvelope envelope = connector.getResponseFromServer(getComments);

        Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
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
     * @throws Exception
     * @throws RemotePermissionException
     * @throws RemoteAuthenticationException
     * @throws RemoteException
     */
    void jiraAddComment(String issueKey, Comment comment) throws IOException,
            XmlPullParserException, Exception {

        if (comment == null || issueKey == null) {
            throw new Exception(
                    "Wrong arguments\ncomment: " + comment.toString()
                    + "\nissueKey: " + issueKey);
        }

        SoapObject remoteComment = new SoapObject("http://beans.soap.rpc.jira.atlassian.com",
                "RemoteComment");



        // remoteComment.addProperty("author", comment.getAuthorName());
        remoteComment.addProperty("body", comment.getBody());
        // remoteComment.addProperty("created", "2012-01-06T08:57:36.507Z");
        // remoteComment.addProperty("groupLevel", null);
        // remoteComment.addProperty("id", "-1");
        // remoteComment.addProperty("roleLevel", null);
        // remoteComment.addProperty("updateAuthor", comment.getAuthorName());
        // remoteComment.addProperty("updated", "2012-01-06T08:57:36.507Z");

        SoapObject addComment = new SoapObject(connector.getNameSpace(),
                "addComment");
        addComment.addProperty("token", connector.getToken());
        addComment.addProperty("issueKey", issueKey);

        addComment.addSoapObject(remoteComment);

        connector.getResponseFromServer(addComment);
        // TODO Sprawdzić czy dodaliśmy komentarz?
    }
}
