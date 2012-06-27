package com.facetedworlds.honeydolist.model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;
import com.updatecontrols.correspondence.query.*;

public class Task__text extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                Task__text newFact = new Task__text(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.value = (String) fieldSerializerByType.get( String.class ).readData(in);
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type Task__text.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            Task__text factToWrite = (Task__text)fact;
            fieldSerializerByType.get( String.class ).writeData(out, factToWrite.value);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.Task__text" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_task = new Role( new RoleMemento( TYPE , "task" , Task.TYPE , true ));
    public static final Role ROLE_prior = new Role( new RoleMemento( TYPE , "prior" , Task__text.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_isCurrent() { 
        return new QueryDefinition()
        .joinSuccessors( Task__text.ROLE_prior )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorOpt<Domain> domain;
    private PredecessorObj<Task> task;
    private PredecessorList<Task__text> prior;
    private String value;
    private Result<Task__text> isCurrentResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public Task__text( Domain domain,Task task,java.util.List<Task__text> prior,String value ) {
        this.domain = new PredecessorOpt<Domain>( this , ROLE_domain, domain);
        this.task = new PredecessorObj<Task>( this , ROLE_task, task);
        this.prior = new PredecessorList<Task__text>( this , ROLE_prior, prior);
        this.value = value;
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private Task__text( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorOpt<Domain>( this , ROLE_domain, factMemento );
        this.task = new PredecessorObj<Task>( this , ROLE_task, factMemento );
        this.prior = new PredecessorList<Task__text>( this , ROLE_prior, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.isCurrentResult = new Result<Task__text>( this , QUERY_isCurrent() );
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

    public java.util.List<Task__text> getPrior() {
        return this.prior.getFactList();
    }

    public String getValue() {
        return this.value;
    }

}
