package com.facetedworlds.honeydolist.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;
import com.updatecontrols.correspondence.query.*;

public class Task__context extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            Task__context newFact = new Task__context(factMemento);

            return newFact;
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.Task__context" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_task = new Role( new RoleMemento( TYPE , "task" , Task.TYPE , true ));
    public static final Role ROLE_prior = new Role( new RoleMemento( TYPE , "prior" , Task__context.TYPE , false ));
    public static final Role ROLE_value = new Role( new RoleMemento( TYPE , "value" , Context.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_isCurrent() { 
        return new QueryDefinition()
        .joinSuccessors( Task__context.ROLE_prior )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorOpt<Domain> domain;
    private PredecessorObj<Task> task;
    private PredecessorList<Task__context> prior;
    private PredecessorObj<Context> value;
    private Result<Task__context> isCurrentResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public Task__context( Domain domain,Task task,java.util.List<Task__context> prior,Context value ) {
        this.domain = new PredecessorOpt<Domain>( this , ROLE_domain, domain);
        this.task = new PredecessorObj<Task>( this , ROLE_task, task);
        this.prior = new PredecessorList<Task__context>( this , ROLE_prior, prior);
        this.value = new PredecessorObj<Context>( this , ROLE_value, value);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private Task__context( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorOpt<Domain>( this , ROLE_domain, factMemento );
        this.task = new PredecessorObj<Task>( this , ROLE_task, factMemento );
        this.prior = new PredecessorList<Task__context>( this , ROLE_prior, factMemento );
        this.value = new PredecessorObj<Context>( this , ROLE_value, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.isCurrentResult = new Result<Task__context>( this , QUERY_isCurrent() );
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------

    public boolean isCurrent() {
        return !this.isCurrentResult.isEmpty();
    }


    //----------------------------------------------------------------------
    // Getter method declarations
    //----------------------------------------------------------------------

    public Domain getDomain() {
        return this.domain.getFact();
    }

    public Task getTask() {
        return this.task.getFact();
    }

    public java.util.List<Task__context> getPrior() {
        return this.prior.getFactList();
    }

    public Context getValue() {
        return this.value.getFact();
    }

}
