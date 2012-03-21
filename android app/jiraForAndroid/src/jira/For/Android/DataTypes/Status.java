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
 *<complexType name="RemoteStatus">
 *	<complexContent>
 *		<extension base="tns1:AbstractRemoteConstant">
 *			<sequence/>
 *		</extension>
 *	</complexContent>
 *</complexType>
 */

public class Status implements IconUrlProvider {

	private String id, name, description, icon;
	
	public Status(String id, String name, String description,
				  String icon) {
		setId(id);
		setName(name);
		setDescription(description);
		setIcon(icon);
	}
	
	public final String getId() {
		return id;
	}
	
	public final void setId(String id) {
		this.id = id;
	}
	
	public final String getName() {
		return name;
	}
	
	public final void setName(String name) {
		this.name = name;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public final void setDescription(String description) {
		this.description = description;
	}
	
	public final String getIcon() {
		return icon;
	}
	
	public final void setIcon(String icon) {
		this.icon = icon;
	}
	
}
