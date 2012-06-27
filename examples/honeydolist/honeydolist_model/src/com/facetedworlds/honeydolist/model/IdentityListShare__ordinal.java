package com.facetedworlds.honeydolist.model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;

public class IdentityListShare__ordinal extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                IdentityListShare__ordinal newFact = new IdentityListShare__ordinal(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.value = (Integer) fieldSerializerByType.get( Integer.class ).readData(in);
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type IdentityListShare__ordinal.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            IdentityListShare__ordinal factToWrite = (IdentityListShare__ordinal)fact;
            fieldSerializerByType.get( int.class ).writeData(out, factToWrite.value);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.IdentityListShare__ordinal" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_identityListShare = new Role( new RoleMemento( TYPE , "identityListShare" , IdentityListShare.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorObj<IdentityListShare> identityListShare;
    private int value;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public IdentityListShare__ordinal( IdentityListShare identityListShare,int value ) {
        this.identityListShare = new PredecessorObj<IdentityListShare>( this , ROLE_identityListShare, identityListShare);
        this.value = value;
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private IdentityListShare__ordinal( FactMemento factMemento ) throws CorrespondenceException {
        this.identityListShare = new PredecessorObj<IdentityListShare>( this , ROLE_identityListShare, factMemento );
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

    public IdentityListShare getIdentityListShare() {
        return this.identityListShare.getFact();
    }

    public int getValue() {
        return this.value;
    }

}
