package com.updatecontrols.correspondence;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.serialize.FieldSerializer;

public interface CorrespondenceFactFactory {

	CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException;
	void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException;
	
}
