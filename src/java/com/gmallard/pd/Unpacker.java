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
 * This class contains utility methods for converting byte arrays
 * (or slices of byte arrays) to <code>String</code> objects representing
 * the numeric value contained in the array or slice.
 * <br /><br /> 
 * The input byte arrays or slices are assumed to be <em>valid 
 * representations of packed decimal
 * data</em>, including a low order sign nibble.  
 * <br /><br />
 * The sign nibble is assumed to signify a positive integral value.
 * <hr />
 * This class is thread safe.
 * <hr />
 * @author Guy Allard
 * @since 2008.02.09
 */
public final class Unpacker {
	/**
	 * Padding characters.
	 */
	private static final String ZEROES = 
		"000000000000000000000000000000000000000000000000000000000000000000000000000000" +
		"000000000000000000000000000000000000000000000000000000000000000000000000000000"
		;
	/**
	 * Return a <code>String</code> representing the numeric value of the
	 * input packed decimal byte array.
	 * <br /><br />
	 * This method is also used internally by other methods in the
	 * package.
	 * <br /><br />
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nibble is not inspected.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 */
	public static String getDecString(byte[] packedDecimal)
	{
		return getDecString(packedDecimal, 0, packedDecimal.length);
	}
	/**
	 * Return a <code>String</code> representing the numeric value of the
	 * input packed decimal byte array, truncated or expanded to the 
	 * requested precision.
	 * <br /><br />
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nibble is not inspected.
	 * @param precision The precision (number of decimal places) to return.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 * @throws @see {@link Unpacker#precisionPad(String, int)} 
	 */
	public static String getDecString(byte[] packedDecimal, int precision)
	{
		String ret = getDecString(packedDecimal);
		return precisionPad(ret, precision);		
	}
	/**
	 * Return a <code>String</code> representing the numeric value of the
	 * input packed decimal byte array, start byte, and length.
	 * <br /><br />
	 * This method is also used internally by other methods in the
	 * package.
	 * <br /><br />
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nibble is not inspected.
	 * <br /><br />
	 * @param startByte The offset of the first byte to be converted.
	 * <br /><br />
	 * @param numBytes The maximum number of bytes to convert.  <em>If this
	 * value exceeds the array length, the return value is silently 
	 * truncated to the maximum possible length.</em>
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 * @throws IllegalArgumentException if the array slice contains non-numeric
	 * nibble(s).
	 */
	public static String getDecString(byte[] packedDecimal, int startByte, int numBytes)
	{
		String ret = unpk(packedDecimal, startByte, numBytes);
		//
		if (!ret.matches("[0-9]+"))
			throw new IllegalArgumentException("<result> not numeric, is: <" +
					ret + ">");
		return ret;
	}
	/**
	 * Return a <code>String</code> representing the numeric value of the
	 * input packed decimal byte array.  Optionally ignore invalid data 
	 * in the byte array.
	 * <br /><br />
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nibble is not inspected.
	 * <br /><br />
	 * @param startByte The offset of the first byte to be converted.
	 * <br /><br />
	 * @param numBytes The maximum number of bytes to convert.  <em>If this
	 * value exceeds the array length, the return value is silently 
	 * truncated to the maximum possible length.</em>
	 * <br /><br />
	 * @param checkNumeric Controls whether the result is checked for all
	 * numeric data.  If true, and the result is non-numeric, an exception is
	 * thrown.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 * @throws IllegalArgumentException if the array slice contains non-numeric
	 * nibble(s) and <code>checkNumeric</code> is true.
	 * @throws IllegalArgumentException if <code>startByte</code> is less than 0,
	 * or <code>numBytes</code> is less than or equal to 0.
	 */
	public static String getDecString(byte[] packedDecimal, int startByte, int numBytes,
			boolean checkNumeric)
	{
		String ret = unpk(packedDecimal, startByte, numBytes);
		//
		if (checkNumeric && !ret.matches("[0-9]+"))
			throw new IllegalArgumentException("<result> not numeric, is: <" +
					ret + ">");
		return ret;
	}
	/**
	 * Return a <code>String</code> representing the numeric value of the
	 * input packed decimal byte array, start byte, and length.  Return
	 * value is of the requested precision.
	 * <br /><br />
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nibble is not inspected.
	 * <br /><br />
	 * @param startByte The offset of the first byte to be converted.  This defines
	 * the start of the array slice to be processed.
	 * <br /><br />
	 * @param numBytes The maximum number of bytes to convert.
	 * <br /><br />
	 * @param precision The precision (number of decimal places) to return.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 * @throws @see {@link Unpacker#precisionPad(String, int)}
	 * @throws IllegalArgumentException if <code>startByte</code> is less than 0,
	 * or <code>numBytes</code> is less than or equal to 0.
	 */
	public static String getDecString(byte[] packedDecimal, int startByte, 
			int numBytes, int precision)
	{
		String ret = getDecString(packedDecimal, startByte, numBytes);
		return precisionPad(ret, precision);
	}
	/**
	 * Return the two character <code>String</code> representing the value of
	 * the input.
	 * <br /><br />
	 * This method is also used internally by other methods in the
	 * package.
	 * <br /><br />
	 * @param abyte An integer value representing the contents of a 
	 * single byte of memory.
	 * @return A <code>String</code> representation of the input 
	 * value in the form of two hexadecimal characters.
	 */
	public static String getHexStringForByte(int abyte)
	{
		String ret = Integer.toHexString(abyte);
		if (ret.length() == 1) ret = "0" + ret;
		return ret.substring(ret.length()-2);
	}
	/**
	 * Pad or truncate the input to a specified precision.
	 * <br /><br />
	 * This method is also used internally by other methods in the
	 * package.
	 * <br /><br />
	 * @param decString A <code>String</code> representing a valid
	 * integral value.
	 * <br /><br />
	 * @param precision The precision (number of decimal places) to return.
	 * @return A <code>String</code> representation of the input
	 * value adjusted to the specified precision.
	 * @throws IllegalArgumentException if the <code>precision</code>
	 * is less than or equal to 0, or if the <code>decString</code>
	 * argument contains characters other than digits.
	 */
	public static String precisionPad(String decString, int precision)
	{
		if (precision <= 0)
			throw new IllegalArgumentException("precision not positive, is: " +
					precision);
		//
		if (!decString.matches("[0-9]+"))
			throw new IllegalArgumentException("decString not numeric, is: " +
					decString);
		//
		String ret = "";
		if (precision <= decString.length())
			ret = decString.substring(decString.length()-precision);
		else
			ret = ZEROES.substring(0,precision-decString.length()) + decString;
		return ret;
	}
	
	/**
	 * Private method to perform the basic unpacking of the data.
	 * <br /><br />
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nibble is not inspected.
	 * <br /><br />
	 * @param startByte The offset of the first byte to be converted.
	 * <br /><br />
	 * @param numBytes The maximum number of bytes to convert.  If this
	 * value exceeds the array length, the return value is <em>silently 
	 * truncated</em> to the maximum possible length.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 * @throws @see {@link Unpacker#checkArray(byte[], int, int)}
	*/
	private static String unpk(byte[] packedDecimal, int startByte, int numBytes)
	{
		checkArray(packedDecimal, startByte, numBytes);
		StringBuilder bldr = new StringBuilder(2 * packedDecimal.length);
		//
		int endByte = startByte + numBytes;
		if (endByte > packedDecimal.length)		// Prevent .... 
			endByte = packedDecimal.length;		// .... ArrayIndexOutOfBounds
		//
		for (int nextByte = startByte; nextByte < endByte; nextByte++)
			bldr.append(getHexStringForByte(packedDecimal[nextByte]));
		//
		bldr.setLength(bldr.length()-1);	// Chop sign nibble
		return bldr.toString();
	}
	/**
	 * Private method to perform checks on client supplied integer values.
	 * <br /><br />
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nibble is not inspected.
	 * <br /><br />
	 * @param startByte The offset of the first byte to be converted.  This defines
	 * the start of the array slice to be processed.
	 * <br /><br />
	 * @param numBytes The maximum number of bytes to convert.
	 * @throws IllegalArgumentException when:
	 * <ul>
	 * <li><code>startByte</code> is less than 0</li>
	 * <li><code>numBytes</code> is less than or equal to 0</li>
	 * </ul>
	 */
	private static void checkArray(byte[] packedDecimal, int startByte, int numBytes)
	{
		if (startByte < 0 ||
				numBytes <= 0)
			throw new IllegalArgumentException("Bad value(s), startByte: " +
					startByte + ", numBytes: " + numBytes);
		return;
	}
} // end of class
