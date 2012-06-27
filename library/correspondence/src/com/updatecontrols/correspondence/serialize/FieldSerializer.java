package com.updatecontrols.correspondence.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface FieldSerializer {

	Object readData(DataInputStream in) throws IOException;
	void writeData(DataOutputStream out, Object value) throws IOException;
}
