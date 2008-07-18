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
import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;
/**
 * This class contains utility methods for converting numeric values to
 * byte arrays in the format of classical packed data.
 * 
 * @author Guy Allard
 * @since 2008.07.18
 */
public final class Packer {

	private static Map<String,Byte> xlateTbl =
		new HashMap<String,Byte>(10);
	
	static
	{
		xlateTbl.put("0", new Byte((byte)0x00));
		xlateTbl.put("1", new Byte((byte)0x01));
		xlateTbl.put("2", new Byte((byte)0x02));
		xlateTbl.put("3", new Byte((byte)0x03));
		xlateTbl.put("4", new Byte((byte)0x04));
		xlateTbl.put("5", new Byte((byte)0x05));
		xlateTbl.put("6", new Byte((byte)0x06));
		xlateTbl.put("7", new Byte((byte)0x07));
		xlateTbl.put("8", new Byte((byte)0x08));
		xlateTbl.put("9", new Byte((byte)0x09));
	}
	
	public static byte[] pack(String anumber)
	{
		checkNumeric(anumber);
		//
		int balen = 1 + anumber.length()/2;
		byte[] ret = new byte[balen];
		//
		if (balen == 1)
		{
			ret[0] = xlateTbl.get(anumber).byteValue();
			ret[0] <<= 4;
			ret[0] |= (byte)0x0c;
		} else {
			// Last character -> a special case because of the sign
			// nibble.
			int slen = anumber.length();
			String oneChar = anumber.substring(slen-1);
			ret[balen-1] = xlateTbl.get(oneChar).byteValue();
			ret[balen-1] <<= 4;
			ret[balen-1] |= 0x0c;
			//
			for (int i = slen - 2, bai = balen -2; 
				i >= 0; 
				i-=2, bai--)
			{
				// System.out.println("d1 " + i + " "+ anumber.substring(i, i+1));
				byte rhs = xlateTbl.get(anumber.substring(i, i+1)).byteValue();
				byte lhs = (byte)0x00;
				if (i-1 >= 0)
				{
					// System.out.println("d2 " + anumber.substring(i-1, i));
					lhs = xlateTbl.get(anumber.substring(i-1, i)).byteValue();
					lhs <<= 4;
				}
				ret[bai] = (byte)(lhs | rhs);
			}
		}
		//
		return ret;
	}
	
	public static byte[] pack(long anumber)
	{
		return pack("" + anumber);
	}

	public static byte[] pack(BigInteger anumber)
	{
		return pack(anumber.toString());
	}
	
	private static void checkNumeric(String astring)
	{
		char[] chars = new char[astring.length()];
		astring.getChars(0, astring.length(), chars, 0);
		for (char nextChar : chars)
		{
			if (!Character.isDigit(nextChar))
			{
				throw new IllegalArgumentException("bad numeric character: " +
						nextChar);
			}
		}
	}
}
