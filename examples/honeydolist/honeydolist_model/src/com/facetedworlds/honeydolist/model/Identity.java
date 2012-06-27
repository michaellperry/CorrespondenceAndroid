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

public class Identity extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                Identity newFact = new Identity(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.anonymousId = (String) fieldSerializerByType.get( String.class ).readData(in);
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type Identity.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            Identity factToWrite = (Identity)fact;
            fieldSerializerByType.get( String.class ).writeData(out, factToWrite.anonymousId);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.Identity" , 1 );
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
        .joinSuccessors( Identity__name.ROLE_identity , new ConditionCollection().isEmpty( Identity__name.QUERY_isCurrent() ) )
        ;
    }
    public static QueryDefinition QUERY_activeShares() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListShare.ROLE_identity , new ConditionCollection().isEmpty( IdentityListShare.QUERY_isActive() ) )
        ;
    }
    public static QueryDefinition QUERY_sharedLists() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListShare.ROLE_identity , new ConditionCollection().isEmpty( IdentityListShare.QUERY_isActive() ) )
        .joinPredecessors( IdentityListShare.ROLE_list )
        ;
    }
    public static QueryDefinition QUERY_sharedListContents() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListShare.ROLE_identity , new ConditionCollection().isEmpty( IdentityListShare.QUERY_isActive() ) )
        .joinPredecessors( IdentityListShare.ROLE_list )
        .joinSuccessors( ListContents.ROLE_list )
        ;
    }
    public static QueryDefinition QUERY_openTasks() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListShare.ROLE_identity , new ConditionCollection().isEmpty( IdentityListShare.QUERY_isActive() ) )
        .joinPredecessors( IdentityListShare.ROLE_list, new ConditionCollection().isEmpty( Task.QUERY_isComplete() ) )
        .joinSuccessors( ListContents.ROLE_list , new ConditionCollection().isEmpty( Task.QUERY_isComplete() ) )
        .joinSuccessors( Task.ROLE_listContents , new ConditionCollection().isEmpty( Task.QUERY_isComplete() ) )
        ;
    }
    public static QueryDefinition QUERY_powerUserCandidates() { 
        return new QueryDefinition()
        .joinSuccessors( PowerUser.ROLE_identity )
        ;
    }
    public static QueryDefinition QUERY_listsNeedingContents() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListShare.ROLE_identity , new ConditionCollection().isEmpty( IdentityListShare.QUERY_isActive() ) )
        .joinPredecessors( IdentityListShare.ROLE_list, new ConditionCollection().isEmpty( List.QUERY_hasListContents() ) )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private String anonymousId;
    private Result<Identity__name> nameCandidatesResult;
    private Result<IdentityListShare> activeSharesResult;
    private Result<List> sharedListsResult;
    private Result<ListContents> sharedListContentsResult;
    private Result<Task> openTasksResult;
    private Result<PowerUser> powerUserCandidatesResult;
    private Result<List> listsNeedingContentsResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public Identity( String anonymousId ) {
        this.anonymousId = anonymousId;
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private Identity( FactMemento factMemento ) throws CorrespondenceException {
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.nameCandidatesResult = new Result<Identity__name>( this , QUERY_nameCandidates() );
        this.activeSharesResult = new Result<IdentityListShare>( this , QUERY_activeShares() );
        this.sharedListsResult = new Result<List>( this , QUERY_sharedLists() );
        this.sharedListContentsResult = new Result<ListContents>( this , QUERY_sharedListContents() );
        this.openTasksResult = new Result<Task>( this , QUERY_openTasks() );
        this.powerUserCandidatesResult = new Result<PowerUser>( this , QUERY_powerUserCandidates() );
        this.listsNeedingContentsResult = new Result<List>( this , QUERY_listsNeedingContents() );
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------

    public java.util.List<Identity__name> nameCandidates() {
        return this.nameCandidatesResult.getFactList();
    }

    public java.util.List<IdentityListShare> activeShares() {
        return this.activeSharesResult.getFactList();
    }

    public java.util.List<List> sharedLists() {
        return this.sharedListsResult.getFactList();
    }

    public java.util.List<ListContents> sharedListContents() {
        return this.sharedListContentsResult.getFactList();
    }

    public java.util.List<Task> openTasks() {
        return this.openTasksResult.getFactList();
    }

    public java.util.List<PowerUser> powerUserCandidates() {
        return this.powerUserCandidatesResult.getFactList();
    }

    public java.util.List<List> listsNeedingContents() {
        return this.listsNeedingContentsResult.getFactList();
    }


    //----------------------------------------------------------------------
    // Getter method declarations
    //----------------------------------------------------------------------

    public String getAnonymousId() {
        return this.anonymousId;
    }

}
