package jira.For.Android.DataTypes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TestIssue {

	@Test
	public void ConstructorTest() {

		Issue issue = new Issue("asd", "sd", "asdas", "sadasd", "sadasd",
				"asd", "sad", "asd", "sdasd", "sadas", "dsada", "Sadas",
				"Sdasd", "sadas", "sdad", 1);

		assertTrue(issue.getAssignee() != null);
		assertTrue(issue.getCreated() != null);
		assertTrue(issue.getDescription() != null);
		assertTrue(issue.getDuedate() != null);
		assertTrue(issue.getEnvironment() != null);
		assertTrue(issue.getId() != null);
		assertTrue(issue.getKey() != null);
		assertTrue(issue.getPriority() != null);
		assertTrue(issue.getProject() != null);
		assertTrue(issue.getReporter() != null);
		assertTrue(issue.getResolution() != null);
		assertTrue(issue.getStatus() != null);
		assertTrue(issue.getSummary() != null);
		assertTrue(issue.getType() != null);
		assertTrue(issue.getUpdated() != null);
		assertTrue(issue.getVotes() == 1);
		
		
		Issue issueNull = new Issue(null, null, null, null, null,
				null,null,null,null,null,null,null,
				null,null,null,0);
		
		assertTrue(issueNull.getAssignee() != null);
		assertTrue(issueNull.getCreated() != null);
		assertTrue(issueNull.getDescription() != null);
		assertTrue(issueNull.getDuedate() != null);
		assertTrue(issueNull.getEnvironment() != null);
		assertTrue(issueNull.getId() != null);
		assertTrue(issueNull.getKey() != null);
		assertTrue(issueNull.getPriority() != null);
		assertTrue(issueNull.getProject() != null);
		assertTrue(issueNull.getReporter() != null);
		assertTrue(issueNull.getResolution() != null);
		assertTrue(issueNull.getStatus() != null);
		assertTrue(issueNull.getSummary() != null);
		assertTrue(issueNull.getType() != null);
		assertTrue(issueNull.getUpdated() != null);
		assertTrue(issueNull.getVotes() == 0);
		
		
		Issue issueNullString = new Issue("null", "null", "null", "null", "null",
				"null","null","null","null","null","null","null",
				"null","null","null",213123123);
		
		assertTrue(issueNullString.getAssignee() != null);
		assertTrue(issueNullString.getCreated() != null);
		assertTrue(issueNullString.getDescription() != null);
		assertTrue(issueNullString.getDuedate() != null);
		assertTrue(issueNullString.getEnvironment() != null);
		assertTrue(issueNullString.getId() != null);
		assertTrue(issueNullString.getKey() != null);
		assertTrue(issueNullString.getPriority() != null);
		assertTrue(issueNullString.getProject() != null);
		assertTrue(issueNullString.getReporter() != null);
		assertTrue(issueNullString.getResolution() != null);
		assertTrue(issueNullString.getStatus() != null);
		assertTrue(issueNullString.getSummary() != null);
		assertTrue(issueNullString.getType() != null);
		assertTrue(issueNullString.getUpdated() != null);
		assertTrue(issueNullString.getVotes() != 0);
	}

}
