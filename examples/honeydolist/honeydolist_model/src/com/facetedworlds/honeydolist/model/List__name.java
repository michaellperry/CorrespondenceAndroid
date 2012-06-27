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

public class List__name extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            try {
                List__name newFact = new List__name(factMemento);

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
                throw new CorrespondenceException("Failed to load fact of type List__name.", e);
            }
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
            List__name factToWrite = (List__name)fact;
            fieldSerializerByType.get( String.class ).writeData(out, factToWrite.value);
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.List__name" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_list = new Role( new RoleMemento( TYPE , "list" , List.TYPE , true ));
    public static final Role ROLE_prior = new Role( new RoleMemento( TYPE , "prior" , List__name.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_isCurrent() { 
        return new QueryDefinition()
        .joinSuccessors( List__name.ROLE_prior )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorObj<Domain> domain;
    private PredecessorObj<List> list;
    private PredecessorList<List__name> prior;
    private String value;
    private Result<List__name> isCurrentResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public List__name( Domain domain,List list,java.util.List<List__name> prior,String value ) {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, domain);
        this.list = new PredecessorObj<List>( this , ROLE_list, list);
        this.prior = new PredecessorList<List__name>( this , ROLE_prior, prior);
        this.value = value;
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private List__name( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, factMemento );
        this.list = new PredecessorObj<List>( this , ROLE_list, factMemento );
        this.prior = new PredecessorList<List__name>( this , ROLE_prior, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.isCurrentResult = new Result<List__name>( this , QUERY_isCurrent() );
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

    public List getList() {
        return this.list.getFact();
    }

    public java.util.List<List__name> getPrior() {
        return this.prior.getFactList();
    }

    public String getValue() {
        return this.value;
    }

}
