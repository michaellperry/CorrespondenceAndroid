package com.updatecontrols.correspondence.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FieldSerializerUUID implements FieldSerializer {

	@Override
	public Object readData(DataInputStream in) throws IOException {
		long high = in.readLong();
		long low = in.readLong();
		return new UUID(high, low);
	}

	@Override
	public void writeData(DataOutputStream out, Object value)
			throws IOException {
		UUID uuid = (UUID)value;
		long high = uuid.getMostSignificantBits();
		long low = uuid.getLeastSignificantBits();
		out.writeLong(high);
		out.writeLong(low);
	}

}
