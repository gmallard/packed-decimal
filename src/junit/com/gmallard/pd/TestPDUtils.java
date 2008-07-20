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
 * 
 * @author Guy Allard
 * @since 2008.07.20
 *
 */
public class TestPDUtils extends TestCase {

	public void testIsPositiveByteArray() {
		byte[][] positives = {
			{(byte)0x00,(byte)0x0c,},
			{(byte)0x00,(byte)0x0f,},
			{(byte)0x00,(byte)0xfc,},
			{(byte)0x00,(byte)0xff,},
		};
		for (byte[] ba : positives)
		{
			assertTrue("TestPBA1" + ba[1], PDUtils.isPositive(ba));
		}
		byte[][] negatives = {
				{(byte)0x00,(byte)0x0d,},
				{(byte)0x00,(byte)0xfd,},
			};
		for (byte[] ba : negatives)
		{
			assertFalse("TestPBA2" + ba[1], PDUtils.isPositive(ba));
		}
		byte[][] badvalues = {
				{(byte)0x00,(byte)0x00,},
				{(byte)0x00,(byte)0x01,},
				{(byte)0x00,(byte)0x02,},
				{(byte)0x00,(byte)0x03,},
				{(byte)0x00,(byte)0x04,},
				{(byte)0x00,(byte)0x05,},
				{(byte)0x00,(byte)0x06,},
				{(byte)0x00,(byte)0x07,},
				{(byte)0x00,(byte)0x08,},
				{(byte)0x00,(byte)0x09,},
				{(byte)0x00,(byte)0x0a,},
				{(byte)0x00,(byte)0x0b,},
				{(byte)0x00,(byte)0x0e},
		};
		for (byte[] ba : badvalues)
		{
			try {
				boolean test = PDUtils.isPositive(ba);
				fail("PBA3 Bad sign check: " + ba[1] + " " + test);
			} catch(IllegalArgumentException iae)
			{
				assertTrue("IEACHECK", true);
			}
		}
	}

	public void testIsPositiveInt() {
		// The good
		assertTrue("Test01", PDUtils.isPositive(0x0c));
		assertTrue("Test01a", PDUtils.isPositive(0xfc));
		assertTrue("Test02", PDUtils.isPositive(0x0f));
		assertTrue("Test02a", PDUtils.isPositive(0xff));
		// The bad
		assertFalse("Test03", PDUtils.isPositive(0x0d));
		assertFalse("Test03a", PDUtils.isPositive(0xfd));
		// The ugly
		int[] badSigns = {
			0,1,2,3,4,5,6,7,8,9,10,11,14,	
		};
		for (int sign : badSigns)
		{
			try {
				boolean test = PDUtils.isPositive(sign);
				fail("Bad sign check: " + sign + " " + test);
			} catch(IllegalArgumentException iae)
			{
				assertTrue("IEACHECK", true);
			}
		}
	}

	public void testSignSet()
	{
		byte[] workBa = {
			(byte)0x12, (byte)0x30,	
		};
		PDUtils.setSign(workBa, PDUtils.POSITIVE);
		assertTrue("spos", PDUtils.isPositive(workBa));
		//
		workBa[workBa.length-1] = (byte)0x30;	// reset
		PDUtils.setSign(workBa, PDUtils.UNSIGNED);
		assertTrue("suns", PDUtils.isPositive(workBa));
		//
		workBa[workBa.length-1] = (byte)0x30;	// reset
		PDUtils.setSign(workBa, PDUtils.NEGATIVE);
		assertFalse("sneg", PDUtils.isPositive(workBa));
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
