import junit.framework.TestCase;
/**
 * 
 * @author Guy Allard
 * 
 */

public class TestUnpacker extends TestCase {

	public void testGetDecStringByteArrayInt() {
		byte[] testa = {
			(byte)0x12, (byte)0x3c,	
		};
		assertEquals("ta1", "3", Unpacker.getDecString(testa, 1));
		assertEquals("ta2", "23", Unpacker.getDecString(testa, 2));
		assertEquals("ta3", "123", Unpacker.getDecString(testa, 3));
		assertEquals("ta4", "0123", Unpacker.getDecString(testa, 4));
	}

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

	public void testGetHexStringForByte() {
		assertEquals("Testh01","01",Unpacker.getHexStringForByte(1));
		assertEquals("Testh02","0f",Unpacker.getHexStringForByte(0xf));
		assertEquals("Testh03","ab",Unpacker.getHexStringForByte(0xffffffab));
	}

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

	public void testIsPositiveByteArray() {
		byte[][] positives = {
			{(byte)0x00,(byte)0x0c,},
			{(byte)0x00,(byte)0x0f,},
			{(byte)0x00,(byte)0xfc,},
			{(byte)0x00,(byte)0xff,},
		};
		for (byte[] ba : positives)
		{
			assertTrue("TestPBA1" + ba[1], Unpacker.isPositive(ba));
		}
		byte[][] negatives = {
				{(byte)0x00,(byte)0x0d,},
				{(byte)0x00,(byte)0xfd,},
			};
		for (byte[] ba : negatives)
		{
			assertFalse("TestPBA2" + ba[1], Unpacker.isPositive(ba));
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
				boolean test = Unpacker.isPositive(ba);
				fail("PBA3 Bad sign check: " + ba[1] + " " + test);
			} catch(IllegalArgumentException iae)
			{
				assertTrue("IEACHECK", true);
			}
		}
	}

	public void testIsPositiveInt() {
		// The good
		assertTrue("Test01", Unpacker.isPositive(0x0c));
		assertTrue("Test01a", Unpacker.isPositive(0xfc));
		assertTrue("Test02", Unpacker.isPositive(0x0f));
		assertTrue("Test02a", Unpacker.isPositive(0xff));
		// The bad
		assertFalse("Test03", Unpacker.isPositive(0x0d));
		assertFalse("Test03a", Unpacker.isPositive(0xfd));
		// The ugly
		int[] badSigns = {
			0,1,2,3,4,5,6,7,8,9,10,11,14,	
		};
		for (int sign : badSigns)
		{
			try {
				boolean test = Unpacker.isPositive(sign);
				fail("Bad sign check: " + sign + " " + test);
			} catch(IllegalArgumentException iae)
			{
				assertTrue("IEACHECK", true);
			}
		}
	}

}
