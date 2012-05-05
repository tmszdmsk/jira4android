package jira.For.Android.DataTypes;

import jira.For.Android.IconUrlProvider;

/*- Structure of RemoteIssueType
 *
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
 *<complexType abstract="true" name="AbstractRemoteConstant">
 *	<complexContent>
 *		<extension base="tns1:AbstractNamedRemoteEntity">
 *			<sequence>
 *				<element name="description" nillable="true" type="xsd:string"/>
 *				<element name="icon" nillable="true" type="xsd:string"/>
 *			</sequence>
 *		</extension>
 *	</complexContent>
 *</complexType>
 *
 *<complexType name="RemoteIssueType">
 *	<complexContent>
 *		<extension base="tns1:AbstractRemoteConstant">
 *			<sequence>
 *				<element name="subTask" type="xsd:boolean"/>
 *			</sequence>
 *		</extension>
 *	</complexContent>
 *</complexType>
 */

public class IssueType implements IconUrlProvider{

	private String id, name, description, icon;
	private boolean subTask;

	public IssueType(String id, String name, String description, String icon,
	                 boolean subTask) {
		setId(id);
		setName(name);
		setDescription(description);
		setIcon(icon);
		setSubTask(subTask);
	}

	public final String getId() {
		return id;
	}

	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	public boolean isSubTask() {
		return subTask;
	}

	public final void setId(String id) {
		if (id == null || id.compareToIgnoreCase("null") == 0) this.id = "NO ID";
		else this.id = id;
	}

	public final void setName(String name) {
		if (name == null || name.compareToIgnoreCase("null") == 0) this.name = "NO NAME";
		else this.name = name;
	}

	public final void setDescription(String description) {
		if (description == null || description.compareToIgnoreCase("null") == 0) this.description = "NO DESCRIPTION";
		else this.description = description;
	}

	public final void setIcon(String icon) {

		this.icon = icon;
	}

	public void setSubTask(boolean subTask) {
		this.subTask = subTask;
	}

	public String getIcon() {
		return icon;
    }
}
