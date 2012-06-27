package com.facetedworlds.honeydolist.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.*;
import com.updatecontrols.correspondence.serialize.*;
import com.updatecontrols.correspondence.memento.*;
import com.updatecontrols.correspondence.query.*;

public class List__avatar extends CorrespondenceFact {

    static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {

        @Override
        public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { 
            List__avatar newFact = new List__avatar(factMemento);

            return newFact;
        }

        @Override
        public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {
        }
    }

    //----------------------------------------------------------------------
    // TYPE static declaration
    //----------------------------------------------------------------------
    static final CorrespondenceFactType TYPE = new CorrespondenceFactType( "FacetedWorlds.HoneyDo.Model.List__avatar" , 1 );
    @Override
    public CorrespondenceFactType getCorrespondenceFactType() { 
        return TYPE;
    }

    //----------------------------------------------------------------------
    // Role declarations
    //----------------------------------------------------------------------
    public static final Role ROLE_domain = new Role( new RoleMemento( TYPE , "domain" , Domain.TYPE , true ));
    public static final Role ROLE_list = new Role( new RoleMemento( TYPE , "list" , List.TYPE , true ));
    public static final Role ROLE_prior = new Role( new RoleMemento( TYPE , "prior" , List__avatar.TYPE , false ));
    public static final Role ROLE_value = new Role( new RoleMemento( TYPE , "value" , Avatar.TYPE , false ));

    //----------------------------------------------------------------------
    // Query definitions
    //----------------------------------------------------------------------
    public static QueryDefinition QUERY_isCurrent() { 
        return new QueryDefinition()
        .joinSuccessors( List__avatar.ROLE_prior )
        ;
    }

    //----------------------------------------------------------------------
    // Field declarations
    //----------------------------------------------------------------------
    private PredecessorObj<Domain> domain;
    private PredecessorObj<List> list;
    private PredecessorList<List__avatar> prior;
    private PredecessorObj<Avatar> value;
    private Result<List__avatar> isCurrentResult;

    //----------------------------------------------------------------------
    // Field Constructor
    //----------------------------------------------------------------------
    public List__avatar( Domain domain,List list,java.util.List<List__avatar> prior,Avatar value ) {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, domain);
        this.list = new PredecessorObj<List>( this , ROLE_list, list);
        this.prior = new PredecessorList<List__avatar>( this , ROLE_prior, prior);
        this.value = new PredecessorObj<Avatar>( this , ROLE_value, value);
        initResults();
    }

    //----------------------------------------------------------------------
    // FactMemento constructor declarations
    //----------------------------------------------------------------------
    private List__avatar( FactMemento factMemento ) throws CorrespondenceException {
        this.domain = new PredecessorObj<Domain>( this , ROLE_domain, factMemento );
        this.list = new PredecessorObj<List>( this , ROLE_list, factMemento );
        this.prior = new PredecessorList<List__avatar>( this , ROLE_prior, factMemento );
        this.value = new PredecessorObj<Avatar>( this , ROLE_value, factMemento );
        initResults();
    }

    //----------------------------------------------------------------------
    // Results initialize method
    //----------------------------------------------------------------------
    private void initResults() {
        this.isCurrentResult = new Result<List__avatar>( this , QUERY_isCurrent() );
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

    public java.util.List<List__avatar> getPrior() {
        return this.prior.getFactList();
    }

    public Avatar getValue() {
        return this.value.getFact();
    }

}
