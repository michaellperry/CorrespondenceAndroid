package com.updatecontrols.correspondence.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class FieldSerializerDate implements FieldSerializer {

	@Override
	public Object readData(DataInputStream in) throws IOException {
		return new Date(in.readLong());
	}

	@Override
	public void writeData(DataOutputStream out, Object value)
			throws IOException {
		out.writeLong(((Date)value).getTime());
	}

}
