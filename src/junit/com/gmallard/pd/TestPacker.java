package com.gmallard.pd;

import java.util.Arrays;

import junit.framework.TestCase;
/**
 * 
 * @author Guy Allard
 * @since 2008.07.18 
 */

public class TestPacker extends TestCase {

	public void testOneChar()
	{
		byte[][] expected = {
				{(byte)0x0c},
				{(byte)0x1c},
				{(byte)0x2c},
				{(byte)0x3c},
				{(byte)0x4c},
				{(byte)0x5c},
				{(byte)0x6c},
				{(byte)0x7c},
				{(byte)0x8c},
				{(byte)0x9c},
		};
		//
		String[] shortOnes = {
			"0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9",
		};
		//
		int i = 0;
		for (String nextShort : shortOnes)
		{
			byte[] got = Packer.pack(nextShort);
			assertTrue("short" + nextShort, Arrays.equals(got,
					expected[i++]));
		}
	}
	
	public void testMultCharBasic()
	{
		byte[] got = Packer.pack("12");
		assertTrue("tcblen12", got.length == 2);
		byte[] expected = {
			(byte)0x01, (byte)0x2c,	
		};
		assertTrue("tcbval12", Arrays.equals(got, expected));
		//
		// showBa(got);
		//
		byte[] got2 = Packer.pack("123");
		assertTrue("tcblen123", got2.length == 2);
		byte[] expected123 = {
			(byte)0x12, (byte)0x3c,	
		};
		//
		// showBa(got2);
		//
		assertTrue("tcbval123", Arrays.equals(got2, expected123));
	}

	public void testMultCharOdd()
	{
		byte[] got = Packer.pack("123456789");
		assertTrue("mcoddlen", got.length == 5);
		//
		byte[] expected = {
			(byte)0x12, (byte)0x34, (byte)0x56,
			(byte)0x78, (byte)0x9c,
		};
		assertTrue("mcoddeq", Arrays.equals(got, expected));
	}

	public void testMultCharEven()
	{
		byte[] got = Packer.pack("12345678");
		assertTrue("mcevenlen", got.length == 5);
		byte[] expected = {
				(byte)0x01, (byte)0x23, (byte)0x45,
				(byte)0x67, (byte)0x8c,
			};
		assertTrue("mceveneq", Arrays.equals(got, expected));
	}
	
	
	private void showBa(byte[] ba)
	{
		int i = 0;
		for (byte b : ba)
		{
			int lhs = b >> 4;
			int rhs = b & 0x0000000f;
			System.out.println(i++ + " " + lhs + " " + rhs);
		}
	}
}
