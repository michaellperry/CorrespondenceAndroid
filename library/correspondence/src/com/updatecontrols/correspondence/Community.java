package com.updatecontrols.correspondence;

import java.util.ArrayList;

import com.updatecontrols.correspondence.memento.CorrespondenceFactType;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.networking.Network;
import com.updatecontrols.correspondence.networking.Subscription;
import com.updatecontrols.correspondence.query.QueryDefinition;
import com.updatecontrols.correspondence.serialize.FieldSerializer;
import com.updatecontrols.correspondence.strategy.AsynchronousCommunicationStrategy;
import com.updatecontrols.correspondence.strategy.StorageStrategy;
import com.updatecontrols.correspondence.strategy.SubscriptionStrategy;
import com.updatecontrols.correspondence.strategy.TaskDispatcher;

public class Community {
	
	private Model model;
	private Network network;
	
	public Community(StorageStrategy storageStrategy, TaskDispatcher dispatcher) {
		model = new Model(this, storageStrategy);
		network = new Network(model, storageStrategy, dispatcher);
	}

	public Community addAsynchronousCommunicationStrategy(
			AsynchronousCommunicationStrategy asynchronousCommunicationStrategy) {
		network.addAsynchronousCommunicationStrategy(asynchronousCommunicationStrategy);
		return this;
	}
	
	public void factLoadFailed(CorrespondenceException e) {
		// TODO: Log it.
	}
	
	public Community addType(CorrespondenceFactType type, CorrespondenceFactFactory factory, FactMetadata factMetadata) {
		model.addType(type, factory, factMetadata);
		return this;
	}

	public Community addQuery(CorrespondenceFactType type, QueryDefinition queryDefinition) {
		model.addQuery(type, queryDefinition);
		return this;
	}
	
	public Community addFieldSerializer(Class<?> fieldType, FieldSerializer fieldSerializer) {
		model.addFieldSerializer(fieldType, fieldSerializer);
		return this;
	}
	
	public Community addModule(Module module) throws CorrespondenceException {
		module.registerTypes(this);
		return this;
	}

	public Community subscribe(SubscriptionStrategy subscriptionStrategy) {
		network.subscribe(new Subscription(subscriptionStrategy));
		return this;
	}
	
	public <T extends CorrespondenceFact> T getFact(T prototype) {
		return model.getFact(prototype);
	}
	
	public <T extends CorrespondenceFact> T addFact(T prototype) {
		return model.addFact(prototype);
	}
	
	public int getCacheSize() {
		return model.getCacheSize();
	}

	public CorrespondenceFact getFactById(FactID id) {
		return model.getFactById(id);
	}

	/* internal */ void executeQuery(ArrayList<CorrespondenceFact> objects,
			FactID id, QueryDefinition queryDefinition) {
		// Load the successors from storage through the model.
		model.executeQuery(objects, id, queryDefinition);
	}

	public void beginSending() {
		network.beginSending();
	}

	public void beginReceiving() {
		network.beginReceiving();
	}
}
