package jira.For.Android.DataTypes;

/*-
 *
 * Structure in WSDL
 *
 * <complexType abstract="true" name="AbstractRemoteEntity">
 *	<sequence>
 *		<element name="id" nillable="true" type="xsd:string"/>
 *	</sequence>
 * </complexType>
 *
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
 *
 *<complexType name="RemoteFilter">
 *	<complexContent>
 *		<extension base="tns1:AbstractNamedRemoteEntity">
 *			<sequence>
 *				<element name="author" nillable="true" type="xsd:string"/>
 *				<element name="description" nillable="true" type="xsd:string"/>
 *				<element name="project" nillable="true" type="xsd:string"/>
 *				<element name="xml" nillable="true" type="xsd:string"/>
 *			</sequence>
 *		</extension>
 *	</complexContent>
 *</complexType>
 */
public class Filter {

	private String author, description, project, jql, name, id;

	@Deprecated
	public static Filter getSimple() {
		return new Filter("Testowy User", "Jakis Opis", "Nazwa projektu",
		        "XMLLLLL", "NAzwa", "ID z dupy");
	}

	@Deprecated
	public void pochwalSie() {
		System.out.println("\nName: " + name + "\nDescription: " + description);
	}

	public Filter(String author, String description, String project,
	              String name, String id) {
		this.author = author;
		this.description = description;
		this.project = project;
		this.name = name;
		this.id = id;
	}

	// Konstruktor z JQL
	public Filter(String author, String description, String project,
	              String jql, String name, String id) {
		this.author = author;
		this.description = description;
		this.project = project;
		this.name = name;
		this.id = id;
		this.jql = jql;
	}

	// Konstruktor nazwa + JQL
	public Filter(String name, String jql) {
		this.name = name;
		this.jql = jql;
	}

	public final String getAuthor() {
		return author;
	}

	public final void setAuthor(String author) {
		this.author = author;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getProject() {
		return project;
	}

	public final void setProject(String project) {
		this.project = project;
	}

	public final String getJql() {
		return jql;
	}

	public final void setJql(String jql) {
		this.jql = jql;
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
}
