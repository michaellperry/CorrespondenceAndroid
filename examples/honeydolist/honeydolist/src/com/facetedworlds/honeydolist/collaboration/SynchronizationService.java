package com.facetedworlds.honeydolist.collaboration;

import android.app.Activity;

import com.facetedworlds.honeydolist.ActivityTaskDispatcher;
import com.updatecontrols.correspondence.Community;
import com.updatecontrols.correspondence.CorrespondenceException;
import com.updatecontrols.correspondence.binary.BinaryHTTPAsynchronousCommunicationStrategy;
import com.updatecontrols.correspondence.memory.MemoryStorageStrategy;

import facetedworlds.honeydo.model.CorrespondenceModel;
import facetedworlds.honeydo.model.Identity;

public class SynchronizationService {
	
	private static SynchronizationService synchronizationService = new SynchronizationService();

	public static SynchronizationService getInstance() {
		return synchronizationService;
	}
	
    private Identity identity;

	public void start(Activity context) throws CorrespondenceException {
		Community community = new Community(new MemoryStorageStrategy(), new ActivityTaskDispatcher(context))
			.addAsynchronousCommunicationStrategy(new BinaryHTTPAsynchronousCommunicationStrategy(
				new HoneyDoHTTPConfigurationProvider()
			))
			.addModule(new CorrespondenceModel());
      
		identity = community.addFact(new Identity("547260202db74f018050e01a6e384112"));
		community.subscribe(new HoneyDoSubscriptionStrategy(identity));
		
		community.beginReceiving();
	}

	public Identity getIdentity() {
		return identity;
	}
}
