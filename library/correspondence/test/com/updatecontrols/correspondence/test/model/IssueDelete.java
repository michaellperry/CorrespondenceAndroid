package com.updatecontrols.correspondence.test.model;

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
public class IssueDelete extends CorrespondenceFact {

	public static Role ROLE_issue = new Role(IssueDelete.class, "issue", false);
	
	// Issue that was deleted.
	private PredecessorObj<Issue> issue;
	
	// Result for restore.
	private Result<IssueRestore> restore = new Result<IssueRestore>(this, new QueryDefinition()
		.joinSuccessors(IssueRestore.ROLE_delete));
	
	// Identity.
	private @Field UUID id;
	
	public IssueDelete(Issue issue) {
		this.issue = new PredecessorObj<Issue>(this, ROLE_issue, issue);
		id = UUID.randomUUID();
	}
	
	public IssueDelete(FactMemento factMemento) throws CorrespondenceException {
		this.issue = new PredecessorObj<Issue>(this, ROLE_issue, factMemento);
	}

	@Override
	protected boolean exists() {
		return restore.isEmpty();
	}
}
