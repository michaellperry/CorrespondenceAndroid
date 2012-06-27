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

public class EmailIdentityGrant extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                EmailIdentityGrant newFact = new EmailIdentityGrant(factMemento);

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
                throw new CorrespondenceException("Failed to load fact of type EmailIdentityGrant.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            EmailIdentityGrant factToWrite = (EmailIdentityGrant)fact;
            fieldSerializerByType.get(UUID.class).writeData(out, factToWrite.unique);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.EmailIdentityGrant" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_email = new Role( new RoleMemento( TYPE , "email" , Email.TYPE , false ));
    public static final Role ROLE_identity = new Role( new RoleMemento( TYPE , "identity" , Identity.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private UUID unique;
    private PredecessorObj<Domain> domain;
    private PredecessorObj<Email> email;
    private PredecessorObj<Identity> identity;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public EmailIdentityGrant( Domain domain,Email email,Identity identity ) {
        this.unique = UUID.randomUUID();
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, domain);
        this.email = new PredecessorObj<Email>( this , ROLE_email, email);
        this.identity = new PredecessorObj<Identity>( this , ROLE_identity, identity);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private EmailIdentityGrant( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, factMemento );
        this.email = new PredecessorObj<Email>( this , ROLE_email, factMemento );
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

    public UUID getUnique() {
        return this.unique;
    }

    public Domain getDomain() {
        return this.domain.getFact();
    }

    public Email getEmail() {
        return this.email.getFact();
    }

    public Identity getIdentity() {
        return this.identity.getFact();
    }

}
