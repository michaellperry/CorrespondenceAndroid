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

public class Identity__name extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                Identity__name newFact = new Identity__name(factMemento);

                DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));
                try {
                    newFact.value = (String) fieldSerializerByType.get( String.class ).readData(in);
                }
                finally {
                    try { in.close(); } catch( IOException e ) {}
                }
                return newFact;
            }
            catch( IOException e ) {
                throw new CorrespondenceException("Failed to load fact of type Identity__name.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            Identity__name factToWrite = (Identity__name)fact;
            fieldSerializerByType.get( String.class ).writeData(out, factToWrite.value);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.Identity__name" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_identity = new Role( new RoleMemento( TYPE , "identity" , Identity.TYPE , true ));
    public static final Role ROLE_prior = new Role( new RoleMemento( TYPE , "prior" , Identity__name.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_isCurrent() { 
        return new QueryDefinition()
        .joinSuccessors( Identity__name.ROLE_prior )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorObj<Domain> domain;
    private PredecessorObj<Identity> identity;
    private String value;
    private PredecessorList<Identity__name> prior;
    private Result<Identity__name> isCurrentResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public Identity__name( Domain domain,Identity identity,String value,java.util.List<Identity__name> prior ) {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, domain);
        this.identity = new PredecessorObj<Identity>( this , ROLE_identity, identity);
        this.value = value;
        this.prior = new PredecessorList<Identity__name>( this , ROLE_prior, prior);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private Identity__name( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, factMemento );
        this.identity = new PredecessorObj<Identity>( this , ROLE_identity, factMemento );
        this.prior = new PredecessorList<Identity__name>( this , ROLE_prior, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.isCurrentResult = new Result<Identity__name>( this , QUERY_isCurrent() );
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------

    public boolean isCurrent() {
        return !this.isCurrentResult.isEmpty();
    }


    //----------------------------------------------------------------------
    // Getter method declarations
    //----------------------------------------------------------------------

    public Domain getDomain() {
        return this.domain.getFact();
    }

    public Identity getIdentity() {
        return this.identity.getFact();
    }

    public String getValue() {
        return this.value;
    }

    public java.util.List<Identity__name> getPrior() {
        return this.prior.getFactList();
    }

}
