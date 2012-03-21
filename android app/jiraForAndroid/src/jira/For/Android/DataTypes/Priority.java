package jira.For.Android.DataTypes;

import jira.For.Android.IconUrlProvider;

/*- Structure of RemotePriority
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
 *<complexType name="RemotePriority">
 *	<complexContent>
 *		<extension base="tns1:AbstractRemoteConstant">
 *			<sequence>
 *				<element name="color" nillable="true" type="xsd:string"/>
 *			</sequence>
 *		</extension>
 *	</complexContent>
 *</complexType>
 */

public class Priority implements IconUrlProvider {

	private String id, name, description, icon, color;

	public Priority(String id, String name, String description, String icon,
	                String color) {
		setId(id);
		setName(name);
		setDescription(description);
		setIcon(icon);
		setColor(color);
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

	public final String getIcon() {
		return icon;
	}

	public final String getColor() {
		return color;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final void setIcon(String icon) {
		this.icon = icon;
	}

	public final void setColor(String color) {
		this.color = color;
	}

}
