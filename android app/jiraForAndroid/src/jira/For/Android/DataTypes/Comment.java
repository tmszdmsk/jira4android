package jira.For.Android.DataTypes;

import java.util.Date;

/*-
 * Structure from WSDL
 *<complexType name="RemoteComment">
 *	<sequence>
 *		<element name="author" nillable="true" type="xsd:string"/>
 *		<element name="body" nillable="true" type="xsd:string"/>
 *		<element name="created" nillable="true" type="xsd:dateTime"/>
 *		<element name="groupLevel" nillable="true" type="xsd:string"/>
 *		<element name="id" nillable="true" type="xsd:string"/>
 *		<element name="roleLevel" nillable="true" type="xsd:string"/>
 *		<element name="updateAuthor" nillable="true" type="xsd:string"/>
 *		<element name="updated" nillable="true" type="xsd:dateTime"/>
 *	</sequence>
 *</complexType>
 */

/*
 * ****************************INFO ABOUT FIELDS*******************************
 * Only one field we should sent to server is "body". "groupLevel" let us set
 * comment visible for only group of people, for us rather useless. When we send
 * groupLevel as "null", Jira will take it and show "restricted to". Other
 * fields are ignored/unnecessary.
 */

public class Comment {

	private String body, groupLevel, id, roleLevel, updateAuthor;
	private Date created, updated;
	private User author;

	@Deprecated
	public void pochwalSie() {
		System.out.println("\nAuthor: " + getAuthorFullName() + "\nBody: "
		        + getBody() + "\nGroup Level: " + getGroupLevel() + "\nid: "
		        + getId() + "\nRoleLevel: " + getRoleLevel()
		        + "\nUpdate author: " + getUpdateAuthor() + "\nCreated: "
		        + getCreated() + "\nUpdated: " + getUpdated());
	}

	public Comment(User author, String body, String groupLevel, String id,
	               String roleLevel, String updateAuthor, String created,
	               String updated) {
		setAuthor(author);
		setBody(body);
		setGroupLevel(groupLevel);
		setId(id);
		setRoleLevel(roleLevel);
		setUpdateAuthor(updateAuthor);
		setCreated(created);
		setUpdated(updated);
	}

	// TODO Zaimplementować toString()
	@Override
	public String toString() {
		return "RemoteComment{author=" + author.getName() + "; body=" + body
		        + "; roleLevel=null; }";// tak na szybkiego
	};

	public final String getAuthorName() {
		return author.getName();
	}

	public String getAuthorFullName() {
		return author.getFullname();
	}

	public final String getBody() {
		return body;
	}

	public final String getGroupLevel() {
		return groupLevel;
	}

	public final String getId() {
		return id;
	}

	public final String getRoleLevel() {
		return roleLevel;
	}

	public final String getUpdateAuthor() {
		return updateAuthor;
	}

	public final String getCreated() {
		return created.toLocaleString();
	}

	public final String getUpdated() {
		return updated.toLocaleString();
	}

	public final void setAuthor(User author) {
		this.author = author;
	}

	public final void setBody(String body) {
		if (body == null || body.compareToIgnoreCase("null") == 0) this.body = "NO BODY";
		else this.body = body;
	}

	public final void setGroupLevel(String groupLevel) {
		if (groupLevel == null || groupLevel.compareToIgnoreCase("null") == 0) this.groupLevel = "NO GROUP LEVEL";
		else this.groupLevel = groupLevel;
	}

	public final void setId(String id) {
		if (id == null || id.compareToIgnoreCase("null") == 0) this.id = "NO ID";
		else this.id = id;
	}

	public final void setRoleLevel(String roleLevel) {
		if (roleLevel == null || roleLevel.compareToIgnoreCase("null") == 0) this.roleLevel = "NO ROLE LEVEL";
		else this.roleLevel = roleLevel;
	}

	public final void setUpdateAuthor(String updateAuthor) {
		if (updateAuthor == null
		        || updateAuthor.compareToIgnoreCase("null") == 0) this.updateAuthor = "NO UPDATE AUTHOR";
		else this.updateAuthor = updateAuthor;
	}

	public final void setCreated(String created) {
		if (created != null && created.compareToIgnoreCase("null") != 0) {
			this.created = DataTypesMethods.GMTStringToLocalDate(created);
		}
	}

	public final void setUpdated(String updated) {
		if (updated != null && updated.compareToIgnoreCase("null") != 0) {
			this.updated = DataTypesMethods.GMTStringToLocalDate(updated);
		}
	}
}
