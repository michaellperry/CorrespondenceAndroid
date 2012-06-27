package com.facetedworlds.honeydolist.model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;
import com.updatecontrols.correspondence.query.*;

public class Task extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                Task newFact = new Task(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.unique = (UUID) fieldSerializerByType.get( UUID.class ).readData( in );
                    newFact.created = (java.util.Date) fieldSerializerByType.get( java.util.Date.class ).readData(in);
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type Task.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            Task factToWrite = (Task)fact;
            fieldSerializerByType.get(UUID.class).writeData(out, factToWrite.unique);
            fieldSerializerByType.get( java.util.Date.class ).writeData(out, factToWrite.created);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.Task" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_listContents = new Role( new RoleMemento( TYPE , "listContents" , ListContents.TYPE , true ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_complete() { 
        return new QueryDefinition()
        .joinSuccessors( TaskComplete.ROLE_task , new ConditionCollection().isEmpty( TaskComplete.QUERY_isUndone() ) )
        ;
    }
    public static QueryDefinition QUERY_isComplete() { 
        return new QueryDefinition()
        .joinSuccessors( TaskComplete.ROLE_task , new ConditionCollection().isEmpty( TaskComplete.QUERY_isUndone() ) )
        ;
    }
    public static QueryDefinition QUERY_textCandidates() { 
        return new QueryDefinition()
        .joinSuccessors( Task__text.ROLE_task , new ConditionCollection().isEmpty( Task__text.QUERY_isCurrent() ) )
        ;
    }
    public static QueryDefinition QUERY_contextCandidates() { 
        return new QueryDefinition()
        .joinSuccessors( Task__context.ROLE_task , new ConditionCollection().isEmpty( Task__context.QUERY_isCurrent() ) )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private UUID unique;
    private PredecessorObj<Domain> domain;
    private PredecessorObj<ListContents> listContents;
    private java.util.Date created;
    private Result<TaskComplete> completeResult;
    private Result<TaskComplete> isCompleteResult;
    private Result<Task__text> textCandidatesResult;
    private Result<Task__context> contextCandidatesResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public Task( Domain domain,ListContents listContents,java.util.Date created ) {
        this.unique = UUID.randomUUID();
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, domain);
        this.listContents = new PredecessorObj<ListContents>( this , ROLE_listContents, listContents);
        this.created = created;
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private Task( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, factMemento );
        this.listContents = new PredecessorObj<ListContents>( this , ROLE_listContents, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.completeResult = new Result<TaskComplete>( this , QUERY_complete() );
        this.isCompleteResult = new Result<TaskComplete>( this , QUERY_isComplete() );
        this.textCandidatesResult = new Result<Task__text>( this , QUERY_textCandidates() );
        this.contextCandidatesResult = new Result<Task__context>( this , QUERY_contextCandidates() );
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------

    public java.util.List<TaskComplete> complete() {
        return this.completeResult.getFactList();
    }

    public boolean isComplete() {
        return this.isCompleteResult.isEmpty();
    }

    public java.util.List<Task__text> textCandidates() {
        return this.textCandidatesResult.getFactList();
    }

    public java.util.List<Task__context> contextCandidates() {
        return this.contextCandidatesResult.getFactList();
    }


    //----------------------------------------------------------------------
    // Getter method declarations
    //----------------------------------------------------------------------

    public UUID getUnique() {
        return this.unique;
    }

    public Domain getDomain() {
        return this.domain.getFact();
    }

    public ListContents getListContents() {
        return this.listContents.getFact();
    }

    public java.util.Date getCreated() {
        return this.created;
    }

}
