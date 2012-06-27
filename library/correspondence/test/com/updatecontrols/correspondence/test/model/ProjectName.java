package com.updatecontrols.correspondence.test.model;

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
public class ProjectName extends CorrespondenceFact {

	public static final Role ROLE_project = new Role(ProjectName.class, "project", false);
	public static final Role ROLE_prior = new Role(ProjectName.class, "prior", false);
	
	private PredecessorObj<Project> project;
	private PredecessorList<ProjectName> prior;

	private Result<ProjectName> next = new Result<ProjectName>(this, new QueryDefinition()
		.joinSuccessors(ROLE_prior));
	
	private @Field String name;
	
	public ProjectName(Project project, FactList<ProjectName> prior, String name) {
		this.project = new PredecessorObj<Project>(this, ROLE_project, project);
		this.prior = new PredecessorList<ProjectName>(this, ROLE_prior, prior);
		
		this.name = name;
	}
	
	public ProjectName(FactMemento factMemento) throws CorrespondenceException {
		this.project = new PredecessorObj<Project>(this, ROLE_project, factMemento);
		this.prior = new PredecessorList<ProjectName>(this, ROLE_prior, factMemento);
	}

	@Override
	protected boolean exists() {
		return next.isVoid();
	}

	public String getName() {
		return name;
	}

}
