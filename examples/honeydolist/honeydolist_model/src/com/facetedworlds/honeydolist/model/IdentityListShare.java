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

public class IdentityListShare extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                IdentityListShare newFact = new IdentityListShare(factMemento);

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
                throw new CorrespondenceException("Failed to load fact of type IdentityListShare.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            IdentityListShare factToWrite = (IdentityListShare)fact;
            fieldSerializerByType.get(UUID.class).writeData(out, factToWrite.unique);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.IdentityListShare" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_identity = new Role( new RoleMemento( TYPE , "identity" , Identity.TYPE , true ));
    public static final Role ROLE_list = new Role( new RoleMemento( TYPE , "list" , List.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_ordinal() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListShare__ordinal.ROLE_identityListShare )
        ;
    }
    public static QueryDefinition QUERY_isActive() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListRevoke.ROLE_share )
        ;
    }
    public static QueryDefinition QUERY_isPatched() { 
        return new QueryDefinition()
        .joinSuccessors( IdentityListSharePatch.ROLE_equivalentShares )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private UUID unique;
    private PredecessorObj<Domain> domain;
    private PredecessorObj<Identity> identity;
    private PredecessorObj<List> list;
    private Result<IdentityListShare__ordinal> ordinalResult;
    private Result<IdentityListRevoke> isActiveResult;
    private Result<IdentityListSharePatch> isPatchedResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public IdentityListShare( Domain domain,Identity identity,List list ) {
        this.unique = UUID.randomUUID();
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, domain);
        this.identity = new PredecessorObj<Identity>( this , ROLE_identity, identity);
        this.list = new PredecessorObj<List>( this , ROLE_list, list);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private IdentityListShare( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, factMemento );
        this.identity = new PredecessorObj<Identity>( this , ROLE_identity, factMemento );
        this.list = new PredecessorObj<List>( this , ROLE_list, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.ordinalResult = new Result<IdentityListShare__ordinal>( this , QUERY_ordinal() );
        this.isActiveResult = new Result<IdentityListRevoke>( this , QUERY_isActive() );
        this.isPatchedResult = new Result<IdentityListSharePatch>( this , QUERY_isPatched() );
    }

    //----------------------------------------------------------------------
    // Query methods
    //----------------------------------------------------------------------

    public int ordinal() {
        java.util.List<IdentityListShare__ordinal> returnList = this.ordinalResult.getFactList();
        return returnList.isEmpty() == true ? null : returnList.get(0).getValue();
    }

    public boolean isActive() {
        return !this.isActiveResult.isEmpty();
    }

    public boolean isPatched() {
        return this.isPatchedResult.isEmpty();
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

    public Identity getIdentity() {
        return this.identity.getFact();
    }

    public List getList() {
        return this.list.getFact();
    }

}
