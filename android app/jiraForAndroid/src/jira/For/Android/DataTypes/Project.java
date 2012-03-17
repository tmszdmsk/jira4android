package jira.For.Android.DataTypes;

import org.kobjects.base64.Base64;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*-
 * 	Struktury z WSDL
 *<complexType abstract="true" name="AbstractRemoteEntity">
 *	<sequence>
 *		<element name="id" nillable="true" type="xsd:string"/>
 *	</sequence>
 *</complexType>
 *
 *<complexType abstract="true" name="AbstractNamedRemoteEntity">
 *	<complexContent>
 *		<extension base="tns1:AbstractRemoteEntity">
 *			<sequence>
 *				<element name="name" nillable="true" type="xsd:string"/>
 *			</sequence>
 *		</extension>
 *	</complexContent>
 *</complexType>
 *
 *<complexType name="RemoteProject">
 *	 <complexContent>
 *		 <extension base="tns1:AbstractNamedRemoteEntity">
 *			 <sequence>
 *				 <element name="description" nillable="true" type="xsd:string"/>
 *				 <element name="issueSecurityScheme" nillable="true" type="tns1:RemoteScheme"/>
 *				 <element name="key" nillable="true" type="xsd:string"/>
 *				 <element name="lead" nillable="true" type="xsd:string"/>
 *				 <element name="notificationScheme" nillable="true" type="tns1:RemoteScheme"/>
 *				 <element name="permissionScheme" nillable="true" type="tns1:RemotePermissionScheme"/>
 *				 <element name="projectUrl" nillable="true" type="xsd:string"/>
 *				 <element name="url" nillable="true" type="xsd:string"/>
 *			 </sequence>
 *		 </extension>
 *	 </complexContent>
 *</complexType>
 */

public class Project {

	private String description, name, id, issueSecurityScheme, key, lead,
	        notificationScheme, permissionScheme, projectUrl, url;
	private Bitmap avatar;

	public Project(String description, String id, String issueSecurityScheme,
	               String key, String lead, String name,
	               String notificationScheme, String permissionScheme,
	               String projectUrl, String url) {
		this.description = description;
		this.name = name;
		this.id = id;
		this.issueSecurityScheme = issueSecurityScheme;
		this.key = key;
		this.lead = lead;
		this.notificationScheme = notificationScheme;
		this.permissionScheme = permissionScheme;
		this.projectUrl = projectUrl;
		this.url = url;
	}

	@Deprecated
	public void pochwalSie() {
		System.out.println("\nName: " + name + "\nid: " + id + "\nlead: "
		        + lead + "\nkey: " + key + "\nurl: " + url + "\nprojecturl: "
		        + projectUrl + "\nperrmissionScheme: " + permissionScheme
		        + "\nnotificationScheme: " + notificationScheme
		        + "\nDescription: " + description);
	}

	@Deprecated
	public static Project getSample() {
		return new Project("description", "id", "issueSecurityScheme", "key",
		        "lead", "name", "notificationScheme", "permissionScheme",
		        "projectUrl", "projectUrl");
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getIssueSecurityScheme() {
		return issueSecurityScheme;
	}

	public final void setIssueSecurityScheme(String issueSecurityScheme) {
		this.issueSecurityScheme = issueSecurityScheme;
	}

	public final String getKey() {
		return key;
	}

	public final void setKey(String key) {
		this.key = key;
	}

	public final String getLead() {
		return lead;
	}

	public final void setLead(String lead) {
		this.lead = lead;
	}

	public final String getNotificationScheme() {
		return notificationScheme;
	}

	public final void setNotificationScheme(String notificationScheme) {
		this.notificationScheme = notificationScheme;
	}

	public final String getPermissionScheme() {
		return permissionScheme;
	}

	public final void setPermissionScheme(String permissionScheme) {
		this.permissionScheme = permissionScheme;
	}

	public final String getProjectUrl() {
		return projectUrl;
	}

	public final void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	public final String getUrl() {
		return url;
	}

	public final void setUrl(String url) {
		this.url = url;
	}

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(String stringBase64Data) {

		byte[] imageAsByte = Base64.decode(stringBase64Data);
		avatar = BitmapFactory.decodeByteArray(imageAsByte, 0,
		        imageAsByte.length);
	}
}
