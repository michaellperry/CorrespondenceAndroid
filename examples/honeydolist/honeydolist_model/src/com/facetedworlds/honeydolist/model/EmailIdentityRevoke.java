package com.facetedworlds.honeydolist.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;

public class EmailIdentityRevoke extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            EmailIdentityRevoke newFact = new EmailIdentityRevoke(factMemento);

            return newFact;
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.EmailIdentityRevoke" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_grant = new Role( new RoleMemento( TYPE , "grant" , EmailIdentityGrant.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorObj<EmailIdentityGrant> grant;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public EmailIdentityRevoke( EmailIdentityGrant grant ) {
        this.grant = new PredecessorObj<EmailIdentityGrant>( this , ROLE_grant, grant);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private EmailIdentityRevoke( FactMemento factMemento ) throws CorrespondenceException {
        this.grant = new PredecessorObj<EmailIdentityGrant>( this , ROLE_grant, factMemento );
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

    public EmailIdentityGrant getGrant() {
        return this.grant.getFact();
    }

}
