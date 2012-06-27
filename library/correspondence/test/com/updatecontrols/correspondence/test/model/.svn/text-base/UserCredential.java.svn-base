package com.updatecontrols.correspondence.test.model;

import com.mallardsoft.query.QuerySpec;
import com.mallardsoft.query.Selector;
import com.updatecontrols.correspondence.Correspondence;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.Field;
import com.updatecontrols.correspondence.Result;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.query.QueryDefinition;

@Correspondence
public class UserCredential extends CorrespondenceFact {

	// The user is identified by user name and password.
	private @Field String userName;
	private @Field String password;
	
	// The credential gives project access.
	private Result<ProjectAccess> projectAccess = new Result<ProjectAccess>(this, new QueryDefinition()
		.joinSuccessors(ProjectAccess.ROLE_userCredential));
	
	public UserCredential(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public UserCredential(FactMemento factMemento) {
		// The user name and password will be read from the menento.
	}
	
	public void accessProject(Project project) {
		getCommunity().addFact(new ProjectAccess(this, project));
	}
	
	public Iterable<Project> getProjects() {
		return QuerySpec
			.from(projectAccess)
			.select(new Selector<ProjectAccess, Project>() {
				public Project select(ProjectAccess row) {
					return row.getProject();
				}
			});
	}
}
