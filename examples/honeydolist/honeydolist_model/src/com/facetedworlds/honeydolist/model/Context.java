package com.facetedworlds.honeydolist.model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;

public class Context extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                Context newFact = new Context(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.name = (String) fieldSerializerByType.get( String.class ).readData(in);
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type Context.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            Context factToWrite = (Context)fact;
            fieldSerializerByType.get( String.class ).writeData(out, factToWrite.name);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.Context" , 1 );
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

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private String name;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public Context( String name ) {
        this.name = name;
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private Context( FactMemento factMemento ) throws CorrespondenceException {
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------


    //----------------------------------------------------------------------
    // Getter method declarations
    //----------------------------------------------------------------------

    public String getName() {
        return this.name;
    }

}
