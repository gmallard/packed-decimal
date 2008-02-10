
public class UnpackDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("MAXL: \t" + Long.MAX_VALUE);
		long l;
		//
		System.out.println("B2DBI1: " + Unpacker.getDecString(tbaa));
		l = Long.parseLong(Unpacker.getDecString(tbaa));
		System.out.println("LV: \t" + l);
		//
		System.out.println("B2DBI2: " + Unpacker.getDecString(tbab));
		l = Long.parseLong(Unpacker.getDecString(tbab));
		System.out.println("LV: \t" + l);
		//
		System.out.println("B2DBI3: " + Unpacker.getDecString(tba9));
		l = Long.parseLong(Unpacker.getDecString(tba9));
		System.out.println("LV: \t" + l);
		//
		System.out.println("");
		System.out.println("PRT1A: \t" + Unpacker.getDecString(tbaa, 6, 3));
		//
		System.out.println("");
		System.out.println("PRT1B: \t" + Unpacker.getDecString(tbaa, 7, 5));
		//
		System.out.println("B2DBI4: " + Unpacker.getDecString(tbaa, 7));
		l = Long.parseLong(Unpacker.getDecString(tbaa, 7));
		System.out.println("LV: \t" + l);
		//
		System.out.println("B2DBI5: " + Unpacker.getDecString(tbaa, 8));
		l = Long.parseLong(Unpacker.getDecString(tbaa, 8));
		System.out.println("LV: \t" + l);
		//
		System.out.println("B2DBI6: " + Unpacker.getDecString(tbac, 8));
		l = Long.parseLong(Unpacker.getDecString(tbac, 8));
		System.out.println("LV: \t" + l);
		//
		System.out.println("B2DBI7: " + Unpacker.getDecString(tbac, 5));
		l = Long.parseLong(Unpacker.getDecString(tbac, 5));
		System.out.println("LV: \t" + l);
		//
		try {
			System.out.println("BAD01: " + Unpacker.getDecString(badData));
			l = Long.parseLong(Unpacker.getDecString(badData));
			System.out.println("LV: \t" + l);
		} catch(IllegalArgumentException ex) {
			System.err.println("Expected exception!");
			System.err.println("Caught1: " + ex.getMessage());
			ex.printStackTrace();
		}
		//
		try {
			System.out.println("BAD02: " + Unpacker.getDecString(badData,
					0, badData.length, false));
		} catch(IllegalArgumentException ex) {
			System.err.println("Caught2: " + ex.getMessage());
			ex.printStackTrace();
		}
		//
		try {
			System.out.println("BAD03: " + Unpacker.getDecString(badData,
					0, badData.length, true));
		} catch(IllegalArgumentException ex) {
			System.err.println("Expected exception!");
			System.err.println("Caught3: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	// Test data
	private static byte[] tbaa = {
		(byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,
		(byte)0x90, (byte)0x12, (byte)0x34, (byte)0x56,
		(byte)0x7d, // 
	};

	private static byte[] tbab = {
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,		
		(byte)0x20, // 
	};

	private static byte[] tba9 = {
		(byte)0x99, (byte)0x99, (byte)0x99, (byte)0x99,
		(byte)0x99, (byte)0x99, (byte)0x99, (byte)0x99,		
		(byte)0x91, // 
	};

	private static byte[] tbac = {
		(byte)0x12, (byte)0x34, (byte)0x5f,
	};

	private static byte[] badData = {
		(byte)0x12, (byte)0xa4, (byte)0x5f,
	};

}
