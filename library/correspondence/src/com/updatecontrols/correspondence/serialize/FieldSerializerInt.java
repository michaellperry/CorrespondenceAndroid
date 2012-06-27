package com.updatecontrols.correspondence.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FieldSerializerInt implements FieldSerializer {

	@Override
	public Object readData(DataInputStream in) throws IOException {
		return in.readInt();
	}

	@Override
	public void writeData(DataOutputStream out, Object value) throws IOException {
		out.writeInt((Integer)value);
	}

}
