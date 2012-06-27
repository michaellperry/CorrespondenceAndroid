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

public class TaskComplete extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                TaskComplete newFact = new TaskComplete(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.unique = (UUID) fieldSerializerByType.get( UUID.class ).readData( in );
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type TaskComplete.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            TaskComplete factToWrite = (TaskComplete)fact;
            fieldSerializerByType.get(UUID.class).writeData(out, factToWrite.unique);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.TaskComplete" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_task = new Role( new RoleMemento( TYPE , "task" , Task.TYPE , true ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_isUndone() { 
        return new QueryDefinition()
        .joinSuccessors( TaskCompleteUndo.ROLE_taskComplete )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private UUID unique;
    private PredecessorOpt<Domain> domain;
    private PredecessorObj<Task> task;
    private Result<TaskCompleteUndo> isUndoneResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public TaskComplete( Domain domain,Task task ) {
        this.unique = UUID.randomUUID();
        this.domain = new PredecessorOpt<Domain>( this , ROLE_domain, domain);
        this.task = new PredecessorObj<Task>( this , ROLE_task, task);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private TaskComplete( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorOpt<Domain>( this , ROLE_domain, factMemento );
        this.task = new PredecessorObj<Task>( this , ROLE_task, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.isUndoneResult = new Result<TaskCompleteUndo>( this , QUERY_isUndone() );
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------

    public boolean isUndone() {
        return this.isUndoneResult.isEmpty();
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

    public Task getTask() {
        return this.task.getFact();
    }

}
