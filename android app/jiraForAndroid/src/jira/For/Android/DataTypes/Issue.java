package jira.For.Android.DataTypes;

import java.io.Serializable;
import java.util.Date;

/*- Struktura RemoteIssue
 * 
 *
 *<complexType abstract="true" name="AbstractRemoteEntity">
 *	<sequence>
 *		<element name="id" nillable="true" type="xsd:string"/>
 *	</sequence>
 *</complexType>
 * 
 * 
 *<complexType name="RemoteIssue">
 *	<complexContent>
 *		<extension base="tns1:AbstractRemoteEntity">
 *			<sequence>
 *				<element name="affectsVersions" nillable="true" type="impl:ArrayOf_tns1_RemoteVersion"/>
 *				<element name="assignee" nillable="true" type="xsd:string"/>
 *				<element name="attachmentNames" nillable="true" type="impl:ArrayOf_xsd_string"/>
 *				<element name="components" nillable="true" type="impl:ArrayOf_tns1_RemoteComponent"/>
 *				<element name="created" nillable="true" type="xsd:dateTime"/>
 *				<element name="customFieldValues" nillable="true" type="impl:ArrayOf_tns1_RemoteCustomFieldValue"/>
 *				<element name="description" nillable="true" type="xsd:string"/>
 *				<element name="duedate" nillable="true" type="xsd:dateTime"/>
 *				<element name="environment" nillable="true" type="xsd:string"/>
 *				<element name="fixVersions" nillable="true" type="impl:ArrayOf_tns1_RemoteVersion"/>
 *				<element name="key" nillable="true" type="xsd:string"/>
 *				<element name="priority" nillable="true" type="xsd:string"/>
 *				<element name="project" nillable="true" type="xsd:string"/>
 *				<element name="reporter" nillable="true" type="xsd:string"/>
 *				<element name="resolution" nillable="true" type="xsd:string"/>
 *				<element name="status" nillable="true" type="xsd:string"/>
 *				<element name="summary" nillable="true" type="xsd:string"/>
 *				<element name="type" nillable="true" type="xsd:string"/>
 *				<element name="updated" nillable="true" type="xsd:dateTime"/>
 *				<element name="votes" nillable="true" type="xsd:long"/>
 *			</sequence>
 *		</extension>
 *	</complexContent>
 *</complexType>
 */

public class Issue implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id, assignee, assigneeFullName, description, environment, key, priority,
	        project, reporter, reporterFullName, resolution, status, summary, type;
	private long votes;

	private Date updated, created, duedate;// xsd:dateTime

	// Poniższe zostaną poprawione w przyszłości
	//private String fixVersions;// impl:ArrayOf_tns1_RemoteVersion
	//private String customFieldValues;// impl:ArrayOf_tns1_RemoteCustomFieldValue
	//private String components;// impl:ArrayOf_tns1_RemoteComponent
	//private String attachmentNames;// impl:ArrayOf_xsd_string
	//private String affectsVersions;// impl:ArrayOf_tns1_RemoteVersion

	@Deprecated
	public void pochwalSie() {
		System.out.println("\nKey: " + key + "\nassignee: " + assignee
		        + "\nsummary: " + summary + "\nid: " + id + "\ndescription: "
		        + description + "\nevn: " + environment + "\nPriority: "
		        + priority + "\nproject: " + project + "\nreporter: "
		        + reporter + "\nresolution: " + resolution + "\nstatus: "
		        + status + "\nsummary: " + summary + "\ntype: " + type
		        + "\nvotes: " + votes);
	}

	public Issue(String id, String assignee, String assigneeFullName,
				 String description, String updated, String created,
				 String duedate, String environment, String key,
				 String priority, String project, String reporter,
				 String reporterFullName, String resolution, String status,
				 String summary, String type, long votes) {

		setId(id);
		setAssignee(assignee);
		setAssigneeFullName(assigneeFullName);
		setDescription(description);
		setEnvironment(environment);
		setKey(key);
		setPriority(priority);
		setProject(project);
		setReporter(reporter);
		setReporterFullName(reporterFullName);
		setResolution(resolution);
		setStatus(status);
		setSummary(summary);
		setType(type);
		setVotes(votes);

		// Parsing dates
		setDuedate(duedate);
		setCreated(created);
		setUpdated(updated);
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		if (id == null || id.compareToIgnoreCase("null") == 0) this.id = "NO ID";
		else this.id = id;
	}

	public final String getAssigneeName() {
		return assignee;
	}

	public final void setAssignee(String assignee) {
		if (assignee == null || assignee.compareToIgnoreCase("null") == 0) this.assignee = "NO ASSIGNEE";
		else this.assignee = assignee;
	}
	
	public final String getAssigneeFullName() {
		return assigneeFullName;
	}
	
	public final void setAssigneeFullName(String assigneeFullName) {
		if (assigneeFullName == null || assigneeFullName.compareToIgnoreCase("null") == 0)
			this.assigneeFullName = "Unassigned";
		else this.assigneeFullName = assigneeFullName;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		if (description == null || description.compareToIgnoreCase("null") == 0) this.description = "NO DESCRIPTION";
		else this.description = description;
	}

	public final String getEnvironment() {
		return environment;
	}

	public final void setEnvironment(String environment) {
		if (environment == null || environment.compareToIgnoreCase("null") == 0) this.environment = "NO ENVIRONMENT";
		else this.environment = environment;
	}

	public final String getKey() {
		return key;
	}

	public final void setKey(String key) {
		if (key == null || key.compareToIgnoreCase("null") == 0) this.key = "NO KEY";
		else this.key = key;
	}

	public final String getPriority() {
		return priority;
	}

	public final void setPriority(String priority) {
		if (priority == null || priority.compareToIgnoreCase("null") == 0) this.priority = "NO PRIORITY";
		else this.priority = priority;
	}

	public final String getProject() {
		return project;
	}

	public final void setProject(String project) {
		if (project == null || project.compareToIgnoreCase("null") == 0) this.project = "NO PROJECT";
		else this.project = project;
	}

	public final String getReporterName() {
		return reporter;
	}

	public final void setReporter(String reporter) {
		if (reporter == null || reporter.compareToIgnoreCase("null") == 0) this.reporter = "NO REPORTER";
		else this.reporter = reporter;
	}
	
	public final String getReporterFullName() {
		return reporterFullName;
	}
	
	public final void setReporterFullName(String reporterFullName) {
		if (reporterFullName == null || reporterFullName.compareToIgnoreCase("null") == 0)
			this.reporterFullName = "NO REPORTER";
		else this.reporterFullName = reporterFullName;
	}

	public final String getResolution() {

		return resolution;
	}

	public final void setResolution(String resolution) {
		if (resolution == null || resolution.compareToIgnoreCase("null") == 0) this.resolution = "Unresolved";
		//else this.resolution = resolution;
		else this.resolution = "Fixed";
	}

	public final String getStatus() {

		return status;
	}

	public final void setStatus(String status) {
		if (status == null || status.compareToIgnoreCase("null") == 0) this.status = "NO STATUS";
		else this.status = status;
	}

	public final String getSummary() {

		return summary;
	}

	public final void setSummary(String summary) {
		if (summary == null || summary.compareToIgnoreCase("null") == 0) this.summary = "NO SUMMARY";
		else this.summary = summary;
	}

	public final String getType() {

		return type;
	}

	public final void setType(String type) {
		if (type == null || type.compareToIgnoreCase("null") == 0) this.type = "NO TYPE";
		else this.type = type;
	}

	public final long getVotes() {
		return votes;
	}

	public final void setVotes(long votes) {
		this.votes = votes;
	}
	
	public final Date getUpdatedAsDate() {
		return updated;
	}

	public final String getUpdated() {
		if (updated == null) return "NO UPDATED";
		return updated.toLocaleString();
	}

	public final void setUpdated(String updated) {
		this.updated = DataTypesMethods.GMTStringToLocalDate(updated);
	}

	public final String getCreated() {
		if (created == null) return "NO CREATED";
		return created.toLocaleString();
	}

	public final void setCreated(String created) {

		if (created != null && created.compareToIgnoreCase("null") != 0) {
			this.created = DataTypesMethods.GMTStringToLocalDate(created);
		}
	}

	public final String getDuedate() {
		if (duedate == null) return "NO DUEDATE";
		return duedate.toLocaleString();
	}

	public final void setDuedate(String duedate) {

		if (duedate != null && duedate.compareToIgnoreCase("null") != 0) {
			this.duedate = DataTypesMethods.stringToDate(duedate);
		}
	}
}
