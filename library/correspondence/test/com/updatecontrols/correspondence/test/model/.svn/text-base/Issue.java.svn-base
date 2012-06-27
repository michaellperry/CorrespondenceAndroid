package com.updatecontrols.correspondence.test.model;

import java.util.Date;
import java.util.UUID;

import com.updatecontrols.correspondence.Correspondence;
import com.updatecontrols.correspondence.CorrespondenceException;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.Field;
import com.updatecontrols.correspondence.PredecessorObj;
import com.updatecontrols.correspondence.Result;
import com.updatecontrols.correspondence.Role;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.query.QueryDefinition;

@Correspondence
public class Issue extends CorrespondenceFact {
	
	public static final Role ROLE_project = new Role(Issue.class, "project", false);
	
	// The project to which this issue belongs.
	private PredecessorObj<Project> project;
	
	// Result for deletion of this issue.
	private Result<IssueDelete> delete = new Result<IssueDelete>(this, new QueryDefinition()
		.joinSuccessors(IssueDelete.ROLE_issue));
	
	// Identity.
	private @Field UUID id;

	public Issue(Project project) {
		this.project = new PredecessorObj<Project>(this, ROLE_project, project);
		this.id = UUID.randomUUID();
	}
	
	public Issue(FactMemento factMemento) throws CorrespondenceException {
		this.project = new PredecessorObj<Project>(this, ROLE_project, factMemento);
	}
	
	public Project getProject() {
		return project.getFact();
	}
	
	@Override
	protected boolean exists() {
		return delete.isEmpty();
	}

	public void delete() {
		getCommunity().addFact(new IssueDelete(this));
	}

	private Result<IssueDate> date = new Result<IssueDate>(this, new QueryDefinition()
		.joinSuccessors(IssueDate.ROLE_issue));
	private Result<IssueData> data = new Result<IssueData>(this, new QueryDefinition()
		.joinSuccessors(IssueData.ROLE_issue));
	
	public Date getDate() {
		IssueDate last = date.getLast();
		if (last == null)
			return null;
		else
			return last.getDate();
	}
	
	public void setDate(Date date) {
		getCommunity().addFact(new IssueDate(this, this.date.getObjectList(), date));
	}

	public IssueData addData(byte[] data) {
		return getCommunity().addFact(new IssueData(this, data));
	}

	public byte[] getData() {
		IssueData last = data.getLast();
		if (last == null)
			return null;
		else
			return last.getData();
	}
}
