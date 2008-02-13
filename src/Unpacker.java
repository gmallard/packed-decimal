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
	public static String getDecString(byte[] packedDecimal, int startByte, int numBytes, int precision)
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
