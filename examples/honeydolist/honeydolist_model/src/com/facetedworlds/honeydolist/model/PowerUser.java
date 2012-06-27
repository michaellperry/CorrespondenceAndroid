package com.facetedworlds.honeydolist.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;

public class PowerUser extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            PowerUser newFact = new PowerUser(factMemento);

            return newFact;
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.PowerUser" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_identity = new Role( new RoleMemento( TYPE , "identity" , Identity.TYPE , true ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorObj<Identity> identity;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public PowerUser( Identity identity ) {
        this.identity = new PredecessorObj<Identity>( this , ROLE_identity, identity);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private PowerUser( FactMemento factMemento ) throws CorrespondenceException {
        this.identity = new PredecessorObj<Identity>( this , ROLE_identity, factMemento );
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

    public Identity getIdentity() {
        return this.identity.getFact();
    }

}
