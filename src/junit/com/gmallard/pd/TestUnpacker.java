package com.gmallard.pd;
/*
Copyright (C) 2008 Guy M. Allard

This file is part of the Java packed decimal utilities project.

   The Java packed decimal utilities project is free software: you can redistribute it 
   and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   The Java packed decimal utilities project is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with the Java packed decimal utilities project.  
   If not, see <http://www.gnu.org/licenses/>.
*/
import junit.framework.TestCase;
/**
 * This class contains unit tests for the <code>Unpacker</code>
 * class.
 * @author Guy Allard
 * @since 2008.02.09
 * 
 */

public class TestUnpacker extends TestCase {
	/**
	 * Test the <code>getDecString</code> method signature 1.
	 */
	public void testGetDecStringByteArrayInt() {
		byte[] testa = {
			(byte)0x12, (byte)0x3c,	
		};
		assertEquals("ta1", "3", Unpacker.getDecString(testa, 1));
		assertEquals("ta2", "23", Unpacker.getDecString(testa, 2));
		assertEquals("ta3", "123", Unpacker.getDecString(testa, 3));
		assertEquals("ta4", "0123", Unpacker.getDecString(testa, 4));
	}
	/**
	 * Test the <code>getDecString</code> method signature 2.
	 */
	public void testGetDecStringByteArrayIntInt() {
		byte[] testa = {
				(byte)0x12, (byte)0x3c,	
			};
		assertEquals("tb1","3", Unpacker.getDecString(testa, 1, 1));
		assertEquals("tb2","123", Unpacker.getDecString(testa, 0, 2));
		byte[] testb = {
				(byte)0xa2, (byte)0x3c,	
			};
		try {
			String result = Unpacker.getDecString(testb, 0, 2);
			fail("tb Unexpected pass: " + result);
		} catch(IllegalArgumentException iae) {
			assertTrue("IAEX0", true);
		}
	}
	/**
	 * Test the <code>getDecString</code> method for proper
	 * exception generation.
	 */
	public void testGetDecStringByteArrayIntIntBoolean() {
		byte[] testb = {
				(byte)0xa2, (byte)0x3c,	
			};
		String result = null;
		try {
			result = Unpacker.getDecString(testb, 0, 2, false);
			assertEquals("test01", "a23", result);
		} catch(IllegalArgumentException iae) {
			fail("01 Unexpected fail: " + result);
		}
	}
	/**
	 * More tests of <code>getDecString</code>.
	 */
	public void testGetDecStringByteArrayIntIntInt() {
		byte[] testb = {
				(byte)0x12, (byte)0x3c,	
			};
		assertEquals("ts1", "3",
				Unpacker.getDecString(testb, 0, 2, 1));
		assertEquals("ts2", "23",
				Unpacker.getDecString(testb, 0, 2, 2));
		assertEquals("ts3", "123",
				Unpacker.getDecString(testb, 0, 2, 3));
		assertEquals("ts4", "0123",
				Unpacker.getDecString(testb, 0, 2, 4));
	}
	/**
	 * Test the <code>getHexStringForByte</code> method.
	 */
	public void testGetHexStringForByte() {
		assertEquals("Testh01","01",Unpacker.getHexStringForByte(1));
		assertEquals("Testh02","0f",Unpacker.getHexStringForByte(0xf));
		assertEquals("Testh03","ab",Unpacker.getHexStringForByte(0xffffffab));
	}
	/**
	 * Test the <code>precisionPad</code> method.
	 */
	public void testPrecisionPad() {
		assertEquals("Tpp01", "1", Unpacker.precisionPad("21", 1));
		assertEquals("Tpp02", "21", Unpacker.precisionPad("21", 2));
		assertEquals("Tpp03", "021", Unpacker.precisionPad("21", 3));
		try {
			String result = Unpacker.precisionPad("a1", 3);
			fail("Tpp No exception: " + "a1: " + result);
		} catch(IllegalArgumentException iae) {
			assertTrue("IAEOK", true);
		}
	}
} // end of class
