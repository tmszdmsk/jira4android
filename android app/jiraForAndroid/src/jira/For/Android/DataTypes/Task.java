package jira.For.Android.DataTypes;

@Deprecated
public class Task {
	private String name;
	private String type;
	private String priority;
	private String affectsVersion;
	private String component;
	private String label;
	private String status;
	private String resolution;
	private String fixVersion;
	private String assignee;
	private String reporter;
	private String created;
	private String updated;
	private String id;
	private String description;
	private String icon;
	private boolean subTask;

	public Task() {
		super();
		name = "";
		type = "";
		priority = "";
		affectsVersion = "";
		component = "";
		label = "";
		status = "";
		resolution = "";
		fixVersion = "";
		assignee = "";
		reporter = "";
		created = "";
		updated = "";
		id = "";
		description = "";
		icon = "";
		subTask = false;
	}

	public Task(String name, String type, String priority,
			String affectsVersion, String component, String label,
			String status, String resolution, String fixVersion,
			String assignee, String reporter, String created, String updated,
			String id, String description, String icon, boolean subTask) {
		super();
		this.name = name;
		this.type = type;
		this.priority = priority;
		this.affectsVersion = affectsVersion;
		this.component = component;
		this.label = label;
		this.status = status;
		this.resolution = resolution;
		this.fixVersion = fixVersion;
		this.assignee = assignee;
		this.reporter = reporter;
		this.created = created;
		this.updated = updated;
		this.id = id;
		this.description = description;
		this.icon = icon;
		this.subTask = subTask;
	}

	public final String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public final String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public final String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isSubTask() {
		return subTask;
	}

	public void setSubTask(boolean subTask) {
		this.subTask = subTask;
	}

	public final String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public final String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public final String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public final String getAffectsVersion() {
		return affectsVersion;
	}

	public void setAffectsVersion(String affectsVersion) {
		this.affectsVersion = affectsVersion;
	}

	public final String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public final String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public final String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public final String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public final String getFixVersion() {
		return fixVersion;
	}

	public void setFixVersion(String fixVersion) {
		this.fixVersion = fixVersion;
	}

	public final String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public final String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public final String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public final String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

}
