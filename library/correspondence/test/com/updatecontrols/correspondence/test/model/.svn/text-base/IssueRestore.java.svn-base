package com.updatecontrols.correspondence.test.model;

import com.updatecontrols.correspondence.Correspondence;
import com.updatecontrols.correspondence.CorrespondenceException;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.PredecessorObj;
import com.updatecontrols.correspondence.Role;
import com.updatecontrols.correspondence.memento.FactMemento;

@Correspondence
public class IssueRestore extends CorrespondenceFact {

	public static final Role ROLE_delete = new Role(IssueRestore.class, "delete", false);
	
	// The deletion to undo.
	private PredecessorObj<IssueDelete> delete;
	
	public IssueRestore(IssueDelete delete) {
		this.delete = new PredecessorObj<IssueDelete>(this, ROLE_delete, delete);
	}
	
	public IssueRestore(FactMemento factMemento) throws CorrespondenceException {
		this.delete = new PredecessorObj<IssueDelete>(this, ROLE_delete, factMemento);
	}
}
