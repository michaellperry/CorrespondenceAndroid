package com.facetedworlds.honeydolist.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;

public class IdentityListShareRevoke extends CorrespondenceFact {

    class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            IdentityListShareRevoke newFact = new IdentityListShareRevoke(factMemento);

            return newFact;
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.IdentityListShareRevoke" , 1 );
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

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public IdentityListShareRevoke( IdentityListShare identityListShare ) {
        this.identityListShare = new PredecessorObj<IdentityListShare>( this , ROLE_identityListShare, identityListShare);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private IdentityListShareRevoke( FactMemento factMemento ) throws CorrespondenceException {
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

}
