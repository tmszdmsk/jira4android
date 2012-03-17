package jira.For.Android.DataTypes;

/*-
 * 
 *<complexType name="RemoteEntity">
 *	<sequence/>
 *</complexType>
 *<complexType name="RemoteUser">
 *	<complexContent>
 *		<extension base="tns1:RemoteEntity">
 *			<sequence>
 *				<element name="email" nillable="true" type="xsd:string"/>
 *				<element name="fullname" nillable="true" type="xsd:string"/>
 *				<element name="name" nillable="true" type="xsd:string"/>
 *			</sequence>
 *		</extension>
 *	</complexContent>
 *</complexType>
 */
public final class User {

	private String name, fullname, email;

	@Deprecated
	public void pochwalSie() {
		System.out.println("\nName:" + name + "\nFullname:" + fullname
		        + "\ne-mail:" + email);
	}

	public User(String name, String fullname, String email) {
		super();
		this.name = name;
		this.fullname = fullname;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
