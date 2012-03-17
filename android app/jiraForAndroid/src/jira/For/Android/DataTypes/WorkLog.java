package jira.For.Android.DataTypes;

import java.util.Date;

/*-
 * <complexType name="RemoteWorklog">
 *	<sequence>
 *		<element name="author" nillable="true" type="xsd:string"/>
 *		<element name="comment" nillable="true" type="xsd:string"/>
 *		<element name="created" nillable="true" type="xsd:dateTime"/>
 *		<element name="groupLevel" nillable="true" type="xsd:string"/>
 *		<element name="id" nillable="true" type="xsd:string"/>
 *		<element name="roleLevelId" nillable="true" type="xsd:string"/>
 *		<element name="startDate" nillable="true" type="xsd:dateTime"/>
 *		<element name="timeSpent" nillable="true" type="xsd:string"/>
 *		<element name="timeSpentInSeconds" type="xsd:long"/>
 *		<element name="updateAuthor" nillable="true" type="xsd:string"/>
 *		<element name="updated" nillable="true" type="xsd:dateTime"/>
 *	</sequence>
 *</complexType>
 */

public final class WorkLog {

	private String comment, groupLevel, id, roleLevelId, timeSpent,
	        updateAuthor;
	private Date created, startDate, updated;
	private long timeSpentInSecounds;
	private User author;

	public WorkLog(User author, String comment, String timeSpent,
	               String updateAuthor, Date created, Date startDate,
	               Date updated, long timeSpentInSeconds) {
		setAuthor(author);
		setComment(comment);
		setTimeSpent(timeSpent);
		setUpdateAuthor(updateAuthor);
		setCreated(created);
		setStartDate(startDate);
		setUpdated(updated);
		setTimeSpentInSeconds(timeSpentInSeconds);
	}

	public String getAuthorName() {
		return author.getName();
	}
	
	public String getAuthorFullName() {
		return author.getFullname();
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		if (comment == null || comment.compareToIgnoreCase("null") == 0) this.comment = "NO COMMENT";
		this.comment = comment;
	}

	public String getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(String groupLevel) {
		if (groupLevel == null || groupLevel.compareToIgnoreCase("null") == 0) this.groupLevel = "NO groupLevel";
		this.groupLevel = groupLevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id == null || id.compareToIgnoreCase("null") == 0) this.id = "NO id";
		this.id = id;
	}

	public String getRoleLevelId() {
		return roleLevelId;
	}

	public void setRoleLevelId(String roleLevelId) {
		if (roleLevelId == null || roleLevelId.compareToIgnoreCase("null") == 0) this.roleLevelId = "NO roleLevelId";
		this.roleLevelId = roleLevelId;
	}

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		if (timeSpent == null || timeSpent.compareToIgnoreCase("null") == 0) this.timeSpent = "NO timeSpent";
		this.timeSpent = timeSpent;
	}

	public String getUpdateAuthor() {
		return updateAuthor;
	}

	public void setUpdateAuthor(String updateAuthor) {
		if (comment == null || comment.compareToIgnoreCase("null") == 0) this.comment = "NO COMMENT";
		this.updateAuthor = updateAuthor;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		if (comment == null || comment.compareToIgnoreCase("null") == 0) this.comment = "NO COMMENT";
		this.created = created;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public long getTimeSpentInSeconds() {
		return timeSpentInSecounds;
	}

	public void setTimeSpentInSeconds(long timeSpentInSecounds) {
		this.timeSpentInSecounds = timeSpentInSecounds;
	}
}
