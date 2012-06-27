package com.updatecontrols.correspondence.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FieldSerializerString implements FieldSerializer {

	@Override
	public Object readData(DataInputStream in) throws IOException {
		// Read the number of bytes.
		short length = in.readShort();
		if (length == 0)
			return "";
		
		// Allocate an array of bytes.
		byte[] bytes = new byte[length];
		
		// Read the array.
		in.readFully(bytes);
		
		// Convert the array to a string.
		return new String(bytes, "UTF-8");
	}

	@Override
	public void writeData(DataOutputStream out, Object value)
			throws IOException {
		String string = (String)value;
		if (string == null)
			string = "";
		
		// Convert the string to a byte array.
		byte[] bytes = string.getBytes("UTF-8");
		
		// Write the number of bytes.
		out.writeShort(bytes.length);
		
		// Write the array.
		out.write(bytes);
	}

}
