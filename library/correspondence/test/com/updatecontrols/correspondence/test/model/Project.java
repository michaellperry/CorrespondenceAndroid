package com.updatecontrols.correspondence.test.model;

import java.util.UUID;

import com.updatecontrols.correspondence.Correspondence;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.Field;
import com.updatecontrols.correspondence.Result;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.query.QueryDefinition;

@Correspondence(maxVersionNumber = 2)
public class Project extends CorrespondenceFact {

	// Successors.
	private Result<ProjectName> name = new Result<ProjectName>(this, new QueryDefinition()
		.joinSuccessors(ProjectName.ROLE_project));
	private Result<Issue> issue = new Result<Issue>(this, new QueryDefinition()
		.joinSuccessors(Issue.ROLE_project));
	
	// Data.
	private @Field UUID id;
	private @Field(minVersionNumber = 2) String createdBy;
	
	public Project() {
		id = UUID.randomUUID();
	}
	
	public Project(String createdBy) {
		id = UUID.randomUUID();
		this.createdBy = createdBy;
	}
	
	public Project(FactMemento factMemento) {
	}

	public String getName() {
		// Get the most recent leaf.
		ProjectName leaf = name.getLast();
		if (leaf == null)
			return "<New Project>";
		else
			return leaf.getName();
	}
	
	public void setName(String name) {
		getCommunity().addFact(new ProjectName(this, this.name.getObjectList(), name));
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public Iterable<Issue> getIssues() {
		return issue;
	}
	
}
