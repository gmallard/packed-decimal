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
/**
 * 
 * @author Guy Allard
 * @since 2008.07.20
 *
 */
public final class PDUtils {

	public static final int POSITIVE = 0x0000000c;
	
	public static final int NEGATIVE = 0x0000000d;
	
	public static final int UNSIGNED = 0x0000000f;
	
	/**
	 * Convenience method to determine if a particular byte array
	 * contains a positive or negative sign nibble.
	 * <br /><br />
	 * @param packedDecimal The byte array to check.
	 * @return <code>true</code> if the sign is positive, <code>false</code>
	 * otherwise.
	 * @throws @see {@link Unpacker#isPositive(byte)}
	 */
	public static boolean isPositive(byte[] packedDecimal)
	{
		return isPositive(packedDecimal[packedDecimal.length - 1]);
	}
	/**
	 * Convenience method to determine if a particular byte value
	 * contains a positive or negative sign nibble.
	 * <br /><br />
	 * @param signByte The value of the byte to be checked.
	 * @return <code>true</code> if the sign is positive, <code>false</code>
	 * otherwise.
	 * @throws IllegalArgumentException if an invalid sign nibble is 
	 * detected.
	 */
	public static boolean isPositive(int signByte)
	{
		int tsb = signByte & 0x0f;
		boolean ret = false;
		switch (tsb) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				throw new IllegalArgumentException("Invalid sign byte: 0x" +
						Integer.toHexString(signByte));
			case 10:	// :TODO: These cases need to be checked with hardware specs.
			case 11:	// They may be 'treated as ....'.
			case 14:
				throw new IllegalArgumentException("Invalid sign byte: 0x" +
						Integer.toHexString(signByte));
			case 12:
			case 15:	// Not strictly true, hardware defines this as unsigned
				ret = true;
				break;
			case 13:
				ret = false;
				break;
		};
		return ret;
	}

	public static void setSign(byte[] packedDecimal, int sign)
	{
		int lastByte = packedDecimal[packedDecimal.length-1];
		lastByte &= 0x000000f0;
		lastByte |= sign;
		packedDecimal[packedDecimal.length-1] =
			(byte)lastByte;
		return;
	}
	
}
