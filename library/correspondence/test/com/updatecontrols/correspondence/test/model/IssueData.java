package com.updatecontrols.correspondence.test.model;

import com.updatecontrols.correspondence.Correspondence;
import com.updatecontrols.correspondence.CorrespondenceException;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.Field;
import com.updatecontrols.correspondence.PredecessorObj;
import com.updatecontrols.correspondence.Role;
import com.updatecontrols.correspondence.memento.FactMemento;

@Correspondence
public class IssueData extends CorrespondenceFact {

	public static final Role ROLE_issue = new Role(IssueData.class, "issue", false);

	// Predecessors.
	private PredecessorObj<Issue> issue;
	
	// Data.
	private @Field byte[] data;
	
	public IssueData(Issue issue, byte[] data) {
		this.issue = new PredecessorObj<Issue>(this, ROLE_issue, issue);
		this.data = data;
	}
	
	public IssueData(FactMemento factMemento) throws CorrespondenceException {
		this.issue = new PredecessorObj<Issue>(this, ROLE_issue, factMemento);
	}

	public byte[] getData() {
		return data;
	}
}
