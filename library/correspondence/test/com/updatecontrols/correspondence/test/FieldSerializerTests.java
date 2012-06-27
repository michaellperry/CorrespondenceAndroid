package com.updatecontrols.correspondence.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

import org.junit.Test;

import com.updatecontrols.correspondence.serialize.FieldSerializer;
import com.updatecontrols.correspondence.serialize.FieldSerializerInt;
import com.updatecontrols.correspondence.serialize.FieldSerializerString;

public class FieldSerializerTests {

	@Test
	public void intTest() throws Exception {
		FieldSerializerInt serializer = new FieldSerializerInt();

        testSerializer((int)0, new byte[] { 0x00, 0x00, 0x00, 0x00 }, serializer);
        testSerializer((int)1, new byte[] { 0x00, 0x00, 0x00, 0x01 }, serializer);
        testSerializer((int)127, new byte[] { 0x00, 0x00, 0x00, 0x7f }, serializer);
        testSerializer((int)128, new byte[] { 0x00, 0x00, 0x00, (byte)0x80 }, serializer);
        testSerializer((int)255, new byte[] { 0x00, 0x00, 0x00, (byte) 0xff }, serializer);
        testSerializer((int)256, new byte[] { 0x00, 0x00, 0x01, 0x00 }, serializer);
        testSerializer((int)65535, new byte[] { 0x00, 0x00, (byte) 0xff, (byte) 0xff }, serializer);
        testSerializer((int)65536, new byte[] { 0x00, 0x01, 0x00, 0x00 }, serializer);
        testSerializer((int)16777215, new byte[] { 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff }, serializer);
        testSerializer((int)16777216, new byte[] { 0x01, 0x00, 0x00, 0x00 }, serializer);
        testSerializer((int)Integer.MAX_VALUE, new byte[] { 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff }, serializer);

        testSerializer((int)-1, new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff }, serializer);
        testSerializer((int)-127, new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x81 }, serializer);
        testSerializer((int)-128, new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte)0x80 }, serializer);
        testSerializer((int)-255, new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01 }, serializer);
        testSerializer((int)-256, new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00 }, serializer);
        testSerializer((int)-65535, new byte[] { (byte) 0xff, (byte) 0xff, 0x00, 0x01 }, serializer);
        testSerializer((int)-65536, new byte[] { (byte) 0xff, (byte) 0xff, 0x00, 0x00 }, serializer);
        testSerializer((int)-16777215, new byte[] { (byte) 0xff, 0x00, 0x00, 0x01 }, serializer);
        testSerializer((int)-16777216, new byte[] { (byte) 0xff, 0x00, 0x00, 0x00 }, serializer);
        testSerializer((int)Integer.MIN_VALUE, new byte[] { (byte)0x80, 0x00, 0x00, 0x00 }, serializer);
    }

	@Test
    public void stringTest() throws Exception {
        FieldSerializerString serializer = new FieldSerializerString();

        // Special case: null equals empty string.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	DataOutputStream out = new DataOutputStream(byteArrayOutputStream);
    	serializer.writeData(out, null);
    	out.flush();
    	assertTrue(Arrays.equals(new byte[] { 0x00, 0x00 }, byteArrayOutputStream.toByteArray()));

        testSerializer("", new byte[] { 0x00, 0x00 }, serializer);

        testSerializer("Hello, World!", new byte[] { 0x00, 0x0d, 0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21 }, serializer);
        testSerializer("\u07ef", new byte[] { 0x00, 0x02, (byte) 0xdf, (byte) 0xaf }, serializer);
        testSerializer("\u8119", new byte[] { 0x00, 0x03, (byte) 0xe8, (byte) 0x84, (byte) 0x99 }, serializer);
    }

    private static void testSerializer(Object value, byte[] data, FieldSerializer serializer) throws Exception {
    	DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
    	Object read = serializer.readData(in);
    	assertEquals(value, read);

    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	DataOutputStream out = new DataOutputStream(byteArrayOutputStream);
    	serializer.writeData(out, value);
    	out.flush();
    	assertTrue(Arrays.equals(data, byteArrayOutputStream.toByteArray()));
    }
}
