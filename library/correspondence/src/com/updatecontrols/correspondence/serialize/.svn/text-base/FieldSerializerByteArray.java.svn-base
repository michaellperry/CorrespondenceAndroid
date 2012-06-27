package com.updatecontrols.correspondence.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FieldSerializerByteArray implements FieldSerializer {

	@Override
	public Object readData(DataInputStream in) throws IOException {
		short length = in.readShort();
		if (length > 1022)
			throw new IOException("Object size too large.");
		byte[] data = new byte[length];
		in.read(data);
		return data;
	}

	@Override
	public void writeData(DataOutputStream out, Object value)
			throws IOException {
		byte[] data = (byte[])value;
		int length = data.length;
		if (length > 1022)
			throw new IOException("Object size too large.");
		out.writeShort(length);
		out.write(data);
	}

}
