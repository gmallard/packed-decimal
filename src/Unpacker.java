/**
 * This class contains utility methods for converting byte arrays
 * (or slices of byte arrays) to <code>String</code>s representing
 * the numeric value contained in the array or slice.  The input 
 * arrays are assumed to be valid representations of packed decimal
 * data, including a low order sign nybble.  The sign nybble is assumed
 * to signify a positive integral value.
 * 
 * This class is thread safe.
 * 
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
	 * This method is also used internally by other methods in the
	 * package.
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nybble is not inspected.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 */
	public static String getDecString(byte[] packedDecimal)
	{
		return getDecString(packedDecimal, 0, packedDecimal.length);
	}
	/**
	 * 
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nybble is not inspected.
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
	 * This method is also used internally by other methods in the
	 * package.
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nybble is not inspected.
	 * @param startByte The offset of the first byte to be converted.
	 * @param numBytes The maximum number of bytes to convert.  <em>If this
	 * value exceeds the array length, the return value is silently 
	 * truncated to the maximum possible length.</em>
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 * @throws IllegalArgumentException if the array slice contains non-numeric
	 * nybble(s).
	 */
	public static String getDecString(byte[] packedDecimal, int startByte, int numBytes)
	{
		String ret = unpk(packedDecimal, startByte, numBytes);
		//
		if (!ret.matches("[0-9]+"))
			throw new IllegalArgumentException("-result- not numeric, is: " +
					ret);
		return ret;
	}
	/**
	 * 
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nybble is not inspected.
	 * @param startByte The offset of the first byte to be converted.
	 * @param numBytes The maximum number of bytes to convert.  <em>If this
	 * value exceeds the array length, the return value is silently 
	 * truncated to the maximum possible length.</em>
	 * @param checkNumeric Controls whether the result is checked for all
	 * numeric data.  If true, and the result is non-numeric, an exception is
	 * thrown.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
	 * @throws IllegalArgumentException if the array slice contains non-numeric
	 * nybble(s) and <code>checkNumeric</code> is true.
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
	 * 
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nybble is not inspected.
	 * @param startByte The offset of the first byte to be converted.  This defines
	 * the start of the array slice to be processed.
	 * @param numBytes The maximum number of bytes to convert.
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
	 * This method is also used internally by other methods in the
	 * package.
	 * @param abyte An integer value representing the contents of a 
	 * single byte of memory.
	 * @return A <code>String</code> representation of the input 
	 * value in the form of two hexadecimal characters.
	 */
	public static String getHexStringForByte(int abyte)
	{
		String ret =Integer.toHexString(abyte);
		if (ret.length() == 1) ret = "0" + ret;
		return ret.substring(ret.length()-2);
	}
	/**
	 * This method is also used internally by other methods in the
	 * package.
	 * @param decString A <code>String</code> representing a valid
	 * integral value.
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
	 * 
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nybble is not inspected.
	 * @param startByte The offset of the first byte to be converted.
	 * @param numBytes The maximum number of bytes to convert.  If this
	 * value exceeds the array length, the return value is silently 
	 * truncated to the maximum possible length.
	 * @return A <code>String</code> representation of the input packed decimal
	 * value.
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
		bldr.setLength(bldr.length()-1);	// Chop sign nybble
		return bldr.toString();
	}
	/**
	 * Private method to perform checks on client supplied integer values.
	 * 
	 * @param packedDecimal A <code>byte[]</code> array containing a valid
	 * packed decimal value.  The value is assumed to be positive.
	 * The sign nybble is not inspected.
	 * @param startByte The offset of the first byte to be converted.  This defines
	 * the start of the array slice to be processed.
	 * @param numBytes The maximum number of bytes to convert.
	 * @throws IllegalArgumentException if <code>startByte</code> is less than
	 * 0, or <code>numBytes</code> is less than or equal to 0.
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
