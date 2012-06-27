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

public class List extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                List newFact = new List(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.identifier = (String) fieldSerializerByType.get( String.class ).readData(in);
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type List.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            List factToWrite = (List)fact;
            fieldSerializerByType.get( String.class ).writeData(out, factToWrite.identifier);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.List" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_nameCandidates() { 
        return new QueryDefinition()
        .joinSuccessors( List__name.ROLE_list , new ConditionCollection().isEmpty( List__name.QUERY_isCurrent() ) )
        ;
    }
    public static QueryDefinition QUERY_avatarCandidates() { 
        return new QueryDefinition()
        .joinSuccessors( List__avatar.ROLE_list , new ConditionCollection().isEmpty( List__avatar.QUERY_isCurrent() ) )
        ;
    }
    public static QueryDefinition QUERY_tasks() { 
        return new QueryDefinition()
        .joinSuccessors( ListContents.ROLE_list , new ConditionCollection().isEmpty( Task.QUERY_isComplete() ) )
        .joinSuccessors( Task.ROLE_listContents , new ConditionCollection().isEmpty( Task.QUERY_isComplete() ) )
        ;
    }
    public static QueryDefinition QUERY_hasListContents() { 
        return new QueryDefinition()
        .joinSuccessors( ListContents.ROLE_list )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private String identifier;
    private Result<List__name> nameCandidatesResult;
    private Result<List__avatar> avatarCandidatesResult;
    private Result<Task> tasksResult;
    private Result<ListContents> hasListContentsResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public List( String identifier ) {
        this.identifier = identifier;
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private List( FactMemento factMemento ) throws CorrespondenceException {
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.nameCandidatesResult = new Result<List__name>( this , QUERY_nameCandidates() );
        this.avatarCandidatesResult = new Result<List__avatar>( this , QUERY_avatarCandidates() );
        this.tasksResult = new Result<Task>( this , QUERY_tasks() );
        this.hasListContentsResult = new Result<ListContents>( this , QUERY_hasListContents() );
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------

    public java.util.List<List__name> nameCandidates() {
        return this.nameCandidatesResult.getFactList();
    }

    public java.util.List<List__avatar> avatarCandidates() {
        return this.avatarCandidatesResult.getFactList();
    }

    public java.util.List<Task> tasks() {
        return this.tasksResult.getFactList();
    }

    public boolean hasListContents() {
        return this.hasListContentsResult.isEmpty();
    }


    //----------------------------------------------------------------------
    // Getter method declarations
    //----------------------------------------------------------------------

    public String getIdentifier() {
        return this.identifier;
    }

}
