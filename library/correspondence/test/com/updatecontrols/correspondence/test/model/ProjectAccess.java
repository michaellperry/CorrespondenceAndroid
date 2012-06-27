package com.updatecontrols.correspondence.test.model;

import com.updatecontrols.correspondence.Correspondence;
import com.updatecontrols.correspondence.CorrespondenceException;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.PredecessorObj;
import com.updatecontrols.correspondence.Role;
import com.updatecontrols.correspondence.memento.FactMemento;

@Correspondence
public class ProjectAccess extends CorrespondenceFact {

	public static final Role ROLE_userCredential = new Role(ProjectAccess.class, "userCredential", false);
	public static final Role ROLE_project = new Role(ProjectAccess.class, "project", false);
	
	// This gives a user access to a project.
	private PredecessorObj<UserCredential> userCredential;
	private PredecessorObj<Project> project;
	
	public ProjectAccess(UserCredential userCredential, Project project) {
		// Initialize the predecessors from the given objects.
		this.userCredential = new PredecessorObj<UserCredential>(this, ROLE_userCredential, userCredential);
		this.project = new PredecessorObj<Project>(this, ROLE_project, project);
	}
	
	public ProjectAccess(FactMemento factMemento) throws CorrespondenceException {
		// Initialize the predecessors from the menento.
		this.userCredential = new PredecessorObj<UserCredential>(this, ROLE_userCredential, factMemento);
		this.project = new PredecessorObj<Project>(this, ROLE_project, factMemento);
	}
	
	public UserCredential getUserCredential() {
		return userCredential.getFact();
	}
	
	public Project getProject() {
		return project.getFact();
	}
}
