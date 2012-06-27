package com.updatecontrols.correspondence.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.updatecontrols.correspondence.Community;
import com.updatecontrols.correspondence.CorrespondenceException;
import com.updatecontrols.correspondence.memento.CorrespondenceFactType;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.RoleMemento;
import com.updatecontrols.correspondence.memory.MemoryStorageStrategy;
import com.updatecontrols.correspondence.test.model.Issue;
import com.updatecontrols.correspondence.test.model.IssueData;
import com.updatecontrols.correspondence.test.model.IssueDate;
import com.updatecontrols.correspondence.test.model.Project;
import com.updatecontrols.correspondence.test.model.ProjectAccess;
import com.updatecontrols.correspondence.test.model.ProjectName;
import com.updatecontrols.correspondence.test.model.UserCredential;

public class ProjectTests {
	
	private static final CorrespondenceFactType Issue = new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.Issue", 1);
	private static final CorrespondenceFactType IssueData = new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.IssueData", 1);
	private static final RoleMemento IssueDataIssue = new RoleMemento(IssueData, "issue", false);
	private static final CorrespondenceFactType ProjectAccess = new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.ProjectAccess", 1);
	private static final CorrespondenceFactType Project = new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.Project", 1);
	private static final RoleMemento IssueProject = new RoleMemento(Issue, "project", false);
	private static final RoleMemento ProjectAccessProject = new RoleMemento(ProjectAccess, "project", false);
	private static final CorrespondenceFactType UserCredential = new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.UserCredential", 1);
	private static final RoleMemento ProjectAccessUserCredential = new RoleMemento(ProjectAccess, "userCredential", false);
	private Community community;
	private MemoryStorageStrategy database;
	
	@Before
	public void setUp() throws CorrespondenceException {
		database = new MemoryStorageStrategy();
		community = new Community(database) {
			@Override
			public void factLoadFailed(CorrespondenceException e) {
				// Fail the test.
				assertTrue(e.getMessage(), false);
			}
		}
			.addType(Project.class)
			.addType(UserCredential.class)
			.addType(ProjectAccess.class)
			.addType(ProjectName.class)
			.addType(Issue.class)
			.addType(IssueDate.class)
			.addType(IssueData.class);
	}
	
	@Test
	public void testDefaultName() throws Exception {
		Project project = community.addFact(new Project());
		String name = project.getName();
		assertEquals("<New Project>", name);
	}

	@Test
	public void testSetName() throws Exception {
		Project project = community.addFact(new Project());
		project.setName("Jinaga");
		String name = project.getName();
		assertEquals("Jinaga", name);
	}
	
	@Test
	public void testUserCredential() throws Exception {
		// Create a user credential.
		UserCredential userCredential = community.addFact(new UserCredential("mperry", "dontpeek"));
		
		// Log in.
		UserCredential login = community.addFact(new UserCredential("mperry", "dontpeek"));
		
		// Should be the same object.
		assertSame(userCredential, login);
	}
	
	@Test
	public void testUserProjectAccess() throws Exception {
		// Create a project and give a user access to it.
		Project project = community.addFact(new Project());
		UserCredential userCredential = community.addFact(new UserCredential("mperry", "dontpeek"));
		community.addFact(new ProjectAccess(userCredential, project));
		
		// Login and gain access to the project.
		UserCredential login = community.addFact(new UserCredential("mperry", "dontpeek"));
		Project myProject = getProject(login);

		// It should be the same project.
		assertSame(project, myProject);
	}
	
	private Project getProject(UserCredential login) {
		Project project = null;
		int count = 0;
		for (Project myProject: login.getProjects()) {
			project = myProject;
			++count;
		}
		assertEquals(1, count);
		return project;
	}
	
	@Test
	public void testWriteUserCredentialMemento() throws Exception {
		// Create a user credential.
		community.addFact(new UserCredential("mperry", "dontpeek"));

		// Create a memento for the same object.
		FactMemento userCredentialMemento = new FactMemento(UserCredential);
		userCredentialMemento.setData(new byte[] {
				0, 6, 'm', 'p', 'e', 'r', 'r', 'y',
				0, 8, 'd', 'o', 'n', 't', 'p', 'e', 'e', 'k'});
		
		// They should be equal.
		FactMemento actualMemento = database.load(new FactID(1));
		assertEquals(userCredentialMemento, actualMemento);
	}
	
	@Test
	public void testSerializedUserCredential() throws Exception {
		// Write the user credential, project, and access to the database.
		// Write the version 1 credential.
		FactMemento userCredentialMemento = new FactMemento(UserCredential);
		userCredentialMemento.setData(new byte[] {0, 6, 'm', 'p', 'e', 'r', 'r', 'y', 0, 8, 'd', 'o', 'n', 't', 'p', 'e', 'e', 'k'});
		FactID userCredentialId = database.save(userCredentialMemento);
		
		FactMemento projectMemento = new FactMemento(Project);
		projectMemento.setData(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
		FactID projectId = database.save(projectMemento);
		
		FactMemento userAccessMemento = new FactMemento(ProjectAccess);
		userAccessMemento.addPredecessor(ProjectAccessUserCredential, userCredentialId);
		userAccessMemento.addPredecessor(ProjectAccessProject, projectId);
		userAccessMemento.setData(new byte[] {});
		database.save(userAccessMemento);
		
		// Login and gain access to the project.
		UserCredential login = community.addFact(new UserCredential("mperry", "dontpeek"));
		Project myProject = getProject(login);
		
		// Make sure we got one.
		assertNotNull(myProject);
		assertNull(myProject.getCreatedBy());
	}
	
	@Test
	public void testSerializedVersion2() throws Exception {
		// Write the user credential, project, and access to the database.
		// Write the version 1 credential.
		FactMemento userCredentialMemento = new FactMemento(UserCredential);
		userCredentialMemento.setData(new byte[] {0, 6, 'm', 'p', 'e', 'r', 'r', 'y', 0, 8, 'd', 'o', 'n', 't', 'p', 'e', 'e', 'k'});
		FactID userCredentialId = database.save(userCredentialMemento);
		
		FactMemento projectMemento = new FactMemento(new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.Project", 2));
		projectMemento.setData(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			0, 15, 'M', 'i', 'c', 'h', 'a', 'e', 'l', ' ', 'L', ' ', 'P', 'e', 'r', 'r', 'y'});
		FactID projectId = database.save(projectMemento);
		
		FactMemento userAccessMemento = new FactMemento(ProjectAccess);
		userAccessMemento.addPredecessor(ProjectAccessUserCredential, userCredentialId);
		userAccessMemento.addPredecessor(ProjectAccessProject, projectId);
		userAccessMemento.setData(new byte[] {});
		database.save(userAccessMemento);
		
		// Login and gain access to the project.
		UserCredential login = community.addFact(new UserCredential("mperry", "dontpeek"));
		Project myProject = getProject(login);
		
		// Make sure we got one.
		assertNotNull(myProject);
		assertEquals("Michael L Perry", myProject.getCreatedBy());
	}

	@Test
	public void testSaveIssueData() throws Exception {
		// Create a project with an issue.
		Project project = community.addFact(new Project());
		UserCredential userCredential = community.addFact(new UserCredential("mperry", "dontpeek"));
		userCredential.accessProject(project);
		Issue issue = community.addFact(new Issue(project));
		issue.addData(new byte[] {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7});
	}
	
	@Test
	public void testLoadIssueData() throws Exception {
		// Write the user credential, project, and access to the database.
		FactMemento userCredentialMemento = new FactMemento(UserCredential);
		userCredentialMemento.setData(new byte[] {0, 6, 'm', 'p', 'e', 'r', 'r', 'y', 0, 8, 'd', 'o', 'n', 't', 'p', 'e', 'e', 'k'});
		FactID userCredentialId = database.save(userCredentialMemento);
		
		FactMemento projectMemento = new FactMemento(new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.Project", 2));
		projectMemento.setData(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			0, 15, 'M', 'i', 'c', 'h', 'a', 'e', 'l', ' ', 'L', ' ', 'P', 'e', 'r', 'r', 'y'});
		FactID projectId = database.save(projectMemento);
		
		FactMemento userAccessMemento = new FactMemento(ProjectAccess);
		userAccessMemento.addPredecessor(ProjectAccessUserCredential, userCredentialId);
		userAccessMemento.addPredecessor(ProjectAccessProject, projectId);
		userAccessMemento.setData(new byte[] {});
		database.save(userAccessMemento);
		
		// Write an issue and some data.
		FactMemento issueMemento = new FactMemento(new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.Issue", 1));
		issueMemento.addPredecessor(IssueProject, projectId);
		issueMemento.setData(new byte[] { 2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17});
		FactID issueId = database.save(issueMemento);
		
		FactMemento dataMemento = new FactMemento(new CorrespondenceFactType("com.updatecontrols.correspondence.test.model.IssueData", 1));
		dataMemento.addPredecessor(IssueDataIssue, issueId);
		dataMemento.setData(new byte[] {0, 14, 3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7});
		database.save(dataMemento);
		
		// Login and gain access to the project.
		UserCredential login = community.addFact(new UserCredential("mperry", "dontpeek"));
		Project myProject = getProject(login);
		
		// Make sure we got one.
		assertNotNull(myProject);
		assertEquals("Michael L Perry", myProject.getCreatedBy());
		
		// Get the issue.
		Issue issue = null;
		int count = 0;
		for (Issue anIssue: myProject.getIssues()) {
			issue = anIssue;
			++count;
		}
		assertEquals(1, count);
		
		// Get the data.
		byte[] data = issue.getData();
		assertTrue(Arrays.equals(new byte[] {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7}, data));
	}
}
