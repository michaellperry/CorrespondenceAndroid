package com.updatecontrols.correspondence.test.model;

import java.util.Date;

import com.updatecontrols.correspondence.Correspondence;
import com.updatecontrols.correspondence.CorrespondenceException;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.Field;
import com.updatecontrols.correspondence.FactList;
import com.updatecontrols.correspondence.PredecessorObj;
import com.updatecontrols.correspondence.PredecessorList;
import com.updatecontrols.correspondence.Result;
import com.updatecontrols.correspondence.Role;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.query.QueryDefinition;

@Correspondence
public class IssueDate extends CorrespondenceFact {

	public static final Role ROLE_issue = new Role(IssueDate.class, "issue", false);
	public static final Role ROLE_prior = new Role(IssueDate.class, "prior", false);
	
	// Predecessors.
	private PredecessorObj<Issue> issue;
	private PredecessorList<IssueDate> prior;
	
	// Successors.
	private Result<IssueDate> next = new Result<IssueDate>(this, new QueryDefinition()
		.joinSuccessors(ROLE_prior));
	
	// Fields.
	private @Field(indexName = "date") Date date;
	
	public IssueDate(Issue issue, FactList<IssueDate> prior, Date date) {
		this.issue = new PredecessorObj<Issue>(this, ROLE_issue, issue);
		this.prior = new PredecessorList<IssueDate>(this, ROLE_prior, prior);
		this.date = date;
	}
	
	public IssueDate(FactMemento factMemento) throws CorrespondenceException {
		this.issue = new PredecessorObj<Issue>(this, ROLE_issue, factMemento);
		this.prior = new PredecessorList<IssueDate>(this, ROLE_prior, factMemento);
	}
	
	@Override
	protected boolean exists() {
		return next.isEmpty();
	}
	
	public Issue getIssue() {
		return issue.getFact();
	}

	public Date getDate() {
		return date;
	}

}
