import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class MonsterStatIndexer {
	private static Map<String, boolean[]> largeBitmapCache = new HashMap<String, boolean[]>();

	static class CacheKey {
		String key;
		int id;
		public CacheKey(String key, int id) {
			super();
			this.key = key;
			this.id = id;
		}
		
	}
	private static Map<CacheKey, String> attrCache = new HashMap<CacheKey, String>();

	private static XSSFWorkbook readFile(String filename) throws IOException {
		return new XSSFWorkbook(new FileInputStream(filename));
	}
	
	private static int MASK[] = {0x0001, 0x0002, 0x0004, 0x0008, 0x0010, 0x0020, 0x0040, 0x0080,
			0x0100, 0x0200, 0x0400, 0x0800, 0x1000, 0x2000, 0x4000, 0x8000};
	
	//private static String DOTHIS = "coldres15";
	private static String value1[] = {"heal", "mounted", "animal", "amphibian", "wastesurvival", "undead", "coldres15", "heat", "neednoteat", "fireres15", "poisonres15", "aquatic", "flying", "trample", "immobile", "immortal" };
	private static String value2[] = {"cold", "forestsurvival", "shockres15", "swampsurvival", "demon", "sacred", "mountainsurvival", "illusion", "noheal", "ethereal", "pooramphibian", "stealthy40", "misc2", "coldblood", "inanimate", "female" };
	private static String value3[] = {"bluntres", "slashres", "pierceres", "slow_to_recruit", "float", "", "teleport", "", "", "", "", "", "", "", "", "" };
	private static String value4[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	private static String value5[] = {"magicbeing", "", "mindless1?", "mindless2?", "", "", "", "", "", "", "", "", "", "", "", "" };
	private static String value6[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	private static String value7[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	private static String value8[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	
	
	private static String DOTHIS[][] = {
		{"heal", "108"}, 
		{"mounted", "33"},
		{"animal", "81"},
		{"amphibian", "90"},
		{"wastesurvival", "86"},
		{"undead", "77"},
		{"heat", "141"},
		{"neednoteat", "112"},
		{"aquatic", "89"},
		{"flying", "93"},
		{"trample", "159"},
		{"immortal", "109"},
		{"cold", "140"},
		{"forestsurvival", "84"},
		{"swampsurvival", "87"},
		{"demon", "78"},
		{"sacred", "73"},
		{"mountainsurvival", "85"},
		{"illusion", "101"},
		{"noheal", "111"},
		{"ethereal", "156"},
		{"pooramphibian", "91"},
		{"coldblood", "82"},
		{"inanimate", "76"},
		{"female", "83"},
		{"bluntres", "126"},
		{"slashres", "125"},
		{"pierceres", "127"},
		{"slow_to_recruit", "13"},
		{"float", "92"},
		{"teleport", "95"},
		{"magicbeing", "79"},
		{"immobile", "96"},
		{"stealthy40", "100"},
		{"coldres15", "130"},
		{"fireres15", "129"},
		{"poisonres15", "131"},
//		{"misc2", ""},
		};
	
	interface Callback {
		public String found(String value);
		public String notFound();
	}
	
	static class CallbackAdapter implements Callback {
		public String found(String value) {
			return value;
		}
		public String notFound() {
			return "";
		}
	}
	
	public static class Magic {
		public int F;
		public int A;
		public int W;
		public int E;
		public int S;
		public int D;
		public int N;
		public int B;
		public int H;
		public List<RandomMagic> rand;
	}
	
	public static class RandomMagic {
		public int rand;
		public int nbr;
		public int link;
		public int mask;
	}
	
	public static Map<Integer, Magic> monsterMagic = new HashMap<Integer, Magic>();
	
//	private static String magout(int mag) {
//		return (mag > 0 ? mag : "") + "\t";
//	}

	private static String magicStrip(int mag) {
		return mag > 0 ? Integer.toString(mag) : "";
	}

	private static void doit1(int skip, int column, XSSFSheet sheet) throws IOException {
		doit1(skip, column, sheet, null);
	}
	private static void doit1(int skip, int column, XSSFSheet sheet, Callback callback) throws IOException {
        FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.MONSTER);
		int rowNumber = 1;
		int i = 0;
		byte[] c = new byte[2];
		stream.skip(skip);
		while ((stream.read(c, 0, 2)) != -1) {
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			XSSFRow row = sheet.getRow(rowNumber);
			rowNumber++;
			XSSFCell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
			int value = Integer.decode("0X" + high + low);
			if (callback != null) {
				callback.found(Integer.toString(value));
			} else {
				cell.setCellValue(Integer.decode("0X" + high + low));
			}
			stream.skip(254l);
			i++;
			if (i > 2559) {
				break;
			}
		}
		stream.close();
	}
	
	public static boolean[] largeBitmap(String fieldName) throws IOException {
		boolean[] boolArray = largeBitmapCache.get(fieldName);
		if (boolArray != null) {
			return boolArray;
		}
		FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.MONSTER);
		int i = 0;
		byte[] c = new byte[16];
		boolArray = new boolean[2560];
		stream.skip(240);
		while ((stream.read(c, 0, 16)) != -1) {
			boolean found = false;
//			System.out.print("(" + (i+1) + ") ");
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			//System.out.print(high + low + " ");
			int val = Integer.decode("0X" + high + low);
			if (val > 0) {
//				System.out.print("1:{");
				for (int j=0; j < 16; j++) {
					if ((val & MASK[j]) != 0) {
//						System.out.print((found?",":"") + (value1[j].equals("")?("*****"+(j+1)+"*****"):value1[j]));
						if (value1[j].equals(fieldName)) {
							//System.out.println("1");
							found = true;
						}
					}
				}
//				System.out.print("}");
			}
			high = String.format("%02X", c[3]);
			low = String.format("%02X", c[2]);
			//System.out.print(high + low + " ");
			val = Integer.decode("0X" + high + low);
			if (val > 0) {
//				System.out.print(" 2:{");
//				boolean found = false;
				for (int j=0; j < 16; j++) {
					if ((val & MASK[j]) != 0) {
//						System.out.print((found?",":"") + (value2[j].equals("")?("*****"+(j+1)+"*****"):value2[j]));
						if (value2[j].equals(fieldName)) {
							//System.out.println("1");
							found = true;
						}
					}
				}
//				System.out.print("}");
			}
			high = String.format("%02X", c[5]);
			low = String.format("%02X", c[4]);
			//System.out.print(high + low + " ");
			val = Integer.decode("0X" + high + low);
			if (val > 0) {
//				System.out.print(" 3:{");
//				boolean found = false;
				for (int j=0; j < 16; j++) {
					if ((val & MASK[j]) != 0) {
//						System.out.print((found?",":"") + (value3[j].equals("")?("*****"+(j+1)+"*****"):value3[j]));
						if (value3[j].equals(fieldName)) {
							//System.out.println("1");
							found = true;
						}
					}
				}
//				System.out.print("}");
			}

//			high = String.format("%02X", c[7]);
//			low = String.format("%02X", c[6]);
//			//System.out.print(high + low + " ");
//			val = Integer.decode("0X" + high + low);
//			if (val > 0) {
//				System.out.print(" 4:{");
//				boolean found = false;
//				for (int j=0; j < 16; j++) {
//					if ((val & mask[j]) != 0) {
//						System.out.print((found?",":"") + (value4[j].equals("")?("*****"+(j+1)+"*****"):value4[j]));
//						found = true;
//					}
//				}
//				System.out.print("}");
//			}

			high = String.format("%02X", c[9]);
			low = String.format("%02X", c[8]);
			//System.out.print(high + low + " ");
			val = Integer.decode("0X" + high + low);
			if (val > 0) {
//				System.out.print(" 5:{");
//				boolean found = false;
				for (int j=0; j < 16; j++) {
					if ((val & MASK[j]) != 0) {
//						System.out.print((found?",":"") + (value5[j].equals("")?("*****"+(j+1)+"*****"):value5[j]));
						if (value5[j].equals(fieldName)) {
							//System.out.println("1");
							found = true;
						}
					}
				}
//				System.out.print("}");
			}
//			high = String.format("%02X", c[11]);
//			low = String.format("%02X", c[10]);
//			//System.out.print(high + low + " ");
//			val = Integer.decode("0X" + high + low);
//			if (val > 0) {
//				System.out.print(" 6:{");
//				boolean found = false;
//				for (int j=0; j < 16; j++) {
//					if ((val & mask[j]) != 0) {
//						System.out.print((found?",":"") + (value6[j].equals("")?(j+1):value6[j]));
//						found = true;
//					}
//				}
//				System.out.print("}");
//			}
//			high = String.format("%02X", c[13]);
//			low = String.format("%02X", c[12]);
//			//System.out.print(high + low + " ");
//			val = Integer.decode("0X" + high + low);
//			if (val > 0) {
//				System.out.print(" 7:{");
//				boolean found = false;
//				for (int j=0; j < 16; j++) {
//					if ((val & mask[j]) != 0) {
//						System.out.print((found?",":"") + (value7[j].equals("")?(j+1):value7[j]));
//						found = true;
//					}
//				}
//				System.out.print("}");
//			}
//			high = String.format("%02X", c[15]);
//			low = String.format("%02X", c[14]);
//			//System.out.print(high + low + " ");
//			val = Integer.decode("0X" + high + low);
//			if (val > 0) {
//				System.out.print(" 8:{");
//				boolean found = false;
//				for (int j=0; j < 16; j++) {
//					if ((val & mask[j]) != 0) {
//						System.out.print((found?",":"") + (value8[j].equals("")?(j+1):value8[j]));
//						found = true;
//					}
//				}
//				System.out.print("}");
//			}
			
			if (!found) {
				//System.out.println("");
			}
//			System.out.println(" ");
			boolArray[i] = found;
			stream.skip(240l);
			i++;
			if (i > 2559) {
				break;
			}
		}
		stream.close();
		largeBitmapCache.put(fieldName, boolArray);
		return boolArray;
	}
	
	public static String getAttr(String key, int id) throws IOException {
		String attributeString = attrCache.get(new CacheKey(key, id));
		if (attributeString != null) {
			return attributeString;
		}

		FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.MONSTER);
		//int rowNumber = 1;
		// patience
		int i = 0;
		int k = 0;
		int pos = -1;
		long numFound = 0;
		byte[] c = new byte[2];
		stream.skip(64);
		stream.skip(256*(id-1));
		while ((stream.read(c, 0, 2)) != -1) {
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			int weapon = Integer.decode("0X" + high + low);
			if (weapon == 0) {
				boolean found = false;
				int value = 0;
				stream.skip(46l - numFound*2l);
				// Values
				for (int x = 0; x < numFound; x++) {
					byte[] d = new byte[4];
					stream.read(d, 0, 4);
					String high1 = String.format("%02X", d[3]);
					String low1 = String.format("%02X", d[2]);
					high = String.format("%02X", d[1]);
					low = String.format("%02X", d[0]);
					//System.out.print(low + high + " ");
					if (x == pos) {
						value = new BigInteger(high1 + low1 + high + low, 16).intValue();
						//System.out.print(fire);
						found = true;
					}
					//stream.skip(2);
				}
				
				//System.out.println("");
//				XSSFRow row = sheet.getRow(rowNumber);
//				rowNumber++;
//				XSSFCell cell = row.getCell(104, Row.CREATE_NULL_AS_BLANK);
				stream.close();
				if (found) {
					//cell.setCellValue(value);
					attrCache.put(new CacheKey(key, id), Integer.toString(value));
					return Integer.toString(value);
				} else {
					//cell.setCellValue("");
					attrCache.put(new CacheKey(key, id), "");
					return "";
				}
//				stream.skip(254l - 46l - numFound*4l);
//				numFound = 0;
//				pos = -1;
//				k = 0;
//				i++;
			} else {
				//System.out.print(low + high + " ");
				if ((low + high).equals(key)) {
					pos = k;
				}
				k++;
				numFound++;
			}				
			if (i > 2559) {
				break;
			}
		}
		stream.close();
		return null;
	}
	
	public static void doit2(XSSFSheet sheet, String attr, int column) throws IOException {
		doit2(sheet, attr, column, null);
	}
	public static void doit2(XSSFSheet sheet, String attr, int column, Callback callback) throws IOException {
		FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.MONSTER);
		int rowNumber = 1;
		// patience
		int i = 0;
		int k = 0;
		int pos = -1;
		long numFound = 0;
		byte[] c = new byte[2];
		stream.skip(64);
		while ((stream.read(c, 0, 2)) != -1) {
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			int weapon = Integer.decode("0X" + high + low);
			if (weapon == 0) {
				boolean found = false;
				int value = 0;
				stream.skip(46l - numFound*2l);
				// Values
				for (int x = 0; x < numFound; x++) {
					byte[] d = new byte[4];
					stream.read(d, 0, 4);
					String high1 = String.format("%02X", d[3]);
					String low1 = String.format("%02X", d[2]);
					high = String.format("%02X", d[1]);
					low = String.format("%02X", d[0]);
					//System.out.print(low + high + " ");
					if (x == pos) {
						value = new BigInteger(high1 + low1 + high + low, 16).intValue();
						//System.out.print(fire);
						found = true;
					}
					//stream.skip(2);
				}
				
				//System.out.println("");
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
				if (found) {
					if (callback == null) {
						cell.setCellValue(value);
					} else {
						cell.setCellValue(callback.found(Integer.toString(value)));
					}
				} else {
					if (callback == null) {
						cell.setCellValue("");
					} else {
						cell.setCellValue(callback.notFound());
					}
				}
				stream.skip(254l - 46l - numFound*4l);
				numFound = 0;
				pos = -1;
				k = 0;
				i++;
			} else {
				//System.out.print(low + high + " ");
				if ((low + high).equals(attr)) {
					pos = k;
				}
				k++;
				numFound++;
			}				
			if (i > 2559) {
				break;
			}
		}
		stream.close();
	}
	
	public static void main(String[] args) {

		FileInputStream stream = null;
		try {
	        long startIndex = Starts.MONSTER;
	        int ch;

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(startIndex);
			
			XSSFWorkbook wb = MonsterStatIndexer.readFile("BaseU.xlsx");
			FileOutputStream fos = new FileOutputStream("NewBaseU.xlsx");
			XSSFSheet sheet = wb.getSheetAt(0);

			// Name
			InputStreamReader isr = new InputStreamReader(stream, "ISO-8859-1");
	        Reader in = new BufferedReader(isr);
	        int rowNumber = 1;
			while ((ch = in.read()) > -1) {
				StringBuffer name = new StringBuffer();
				while (ch != 0) {
					name.append((char)ch);
					ch = in.read();
				}
				if (name.length() == 0) {
					continue;
				}
				if (name.toString().equals("end")) {
					break;
				}
				in.close();

				stream = new FileInputStream("Dominions4.exe");		
				startIndex = startIndex + 256l;
				stream.skip(startIndex);
				isr = new InputStreamReader(stream, "ISO-8859-1");
		        in = new BufferedReader(isr);

				//System.out.println(name);

				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(name.toString());
			}
			in.close();
			stream.close();

	        // AP
			doit1(40, 31, sheet);

			// MM
			doit1(42, 30, sheet);

			// Size
			doit1(44, 19, sheet);

			// HP
			doit1(46, 21, sheet);

			// Prot
			doit1(48, 22, sheet);

			// STR
			doit1(50, 25, sheet);

			// ENC
			doit1(52, 29, sheet);

			// Prec
			doit1(54, 28, sheet);

			// ATT
			doit1(56, 26, sheet);

			// Def
			doit1(58, 27, sheet);

			// MR
			doit1(60, 23, sheet);

			// Mor
			doit1(62, 24, sheet);

			// wpn1
			doit1(208, 2, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// wpn2
			doit1(210, 3, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// wpn3
			doit1(212, 4, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// wpn4
			doit1(214, 5, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// wpn5
			doit1(216, 6, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// wpn6
			doit1(218, 7, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// wpn7
			doit1(220, 8, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// armor1
			doit1(228, 9, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// armor2
			doit1(230, 10, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// armor3
			doit1(232, 11, sheet, new CallbackAdapter(){
				@Override
				public String found(String value) {
					if (Integer.parseInt(value) == 0) {
						return "";
					}
					return value;
				}
			});

			// basecost
			doit1(234, 15, sheet);

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.MONSTER);
			rowNumber = 1;
			// res
			int i = 0;
			byte[] c = new byte[2];
			stream.skip(236);
			while ((stream.read(c, 0, 2)) != -1) {
				//String high1 = String.format("%02X", c[3]);
				//String low1 = String.format("%02X", c[2]);
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				//System.out.println(Integer.decode("0X" + high + low));
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(18, Row.CREATE_NULL_AS_BLANK);
				int tmp = new BigInteger(high + low, 16).intValue();
				if (tmp < 1000) {
					cell.setCellValue(Integer.decode("0X" + high + low));
				} else {
					cell.setCellValue(new BigInteger("FFFF" + high + low, 16).intValue());
				}
				stream.skip(254l);
				i++;
				if (i > 2559) {
					break;
				}
			}
			stream.close();
			
			// Large bitmap
			for (String[] pair : DOTHIS) {
				rowNumber = 1;
				boolean[] boolArray = largeBitmap(pair[0]);
				for (boolean found : boolArray) {
					XSSFRow row = sheet.getRow(rowNumber);
					rowNumber++;
					XSSFCell cell = row.getCell(Integer.parseInt(pair[1]), Row.CREATE_NULL_AS_BLANK);
					if (found) {
						if (pair[0].equals("slow_to_recruit")) {
							cell.setCellValue(2);
						} else if (pair[0].equals("heat")) {
							cell.setCellValue(3);
						} else if (pair[0].equals("cold")) {
							cell.setCellValue(3);
						} else if (pair[0].equals("stealthy40")) {
							String additional = getAttr("6C00", rowNumber-1);
							boolean glamour = false;
							if (largeBitmap("illusion")[rowNumber-2]) {
								glamour = true;
							}
							cell.setCellValue(40 + Integer.parseInt(additional.equals("")?"0":additional) + (glamour?25:0));
						} else if (pair[0].equals("coldres15")) {
							String additional = getAttr("C900", rowNumber-1);
							boolean cold = false;
							if (largeBitmap("cold")[rowNumber-2] || getAttr("DC00", rowNumber-1).length() > 0) {
								cold = true;
							}
							cell.setCellValue(15 + Integer.parseInt(additional.equals("")?"0":additional) + (cold?10:0));
						} else if (pair[0].equals("fireres15")) {
							String additional = getAttr("C600", rowNumber-1);
							boolean heat = false;
							if (largeBitmap("heat")[rowNumber-2] || getAttr("3C01", rowNumber-1).length() > 0) {
								heat = true;
							}
							cell.setCellValue(15 + Integer.parseInt(additional.equals("")?"0":additional) + (heat?10:0));
						} else if (pair[0].equals("poisonres15")) {
							String additional = getAttr("C800", rowNumber-1);
							boolean poisoncloud = false;
							if (largeBitmap("undead")[rowNumber-2] || largeBitmap("inanimate")[rowNumber-2] || getAttr("6A00", rowNumber-1).length() > 0) {
								poisoncloud = true;
							}
							cell.setCellValue(15 + Integer.parseInt(additional.equals("")?"0":additional) + (poisoncloud?10:0));
						} else {
							cell.setCellValue(1);
						}
					} else {
						if (pair[0].equals("slow_to_recruit")) {
							cell.setCellValue(1);
						} else if (pair[0].equals("heat")) {
							cell.setCellValue(getAttr("3C01", rowNumber-1));
						} else if (pair[0].equals("cold")) {
							cell.setCellValue(getAttr("DC00", rowNumber-1));
						} else if (pair[0].equals("coldres15")) {
							boolean cold = false;
							if (largeBitmap("cold")[rowNumber-2] || getAttr("DC00", rowNumber-1).length() > 0) {
								cold = true;
							}
							String additional = getAttr("C900", rowNumber-1);
							int coldres = Integer.parseInt(additional.equals("")?"0":additional) + (cold?10:0);
							cell.setCellValue(coldres==0?"":Integer.toString(coldres));
						} else if (pair[0].equals("fireres15")) {
							boolean heat = false;
							if (largeBitmap("heat")[rowNumber-2] || getAttr("3C01", rowNumber-1).length() > 0) {
								heat = true;
							}
							String additional = getAttr("C600", rowNumber-1);
							int coldres = Integer.parseInt(additional.equals("")?"0":additional) + (heat?10:0);
							cell.setCellValue(coldres==0?"":Integer.toString(coldres));
						} else if (pair[0].equals("poisonres15")) {
							boolean poisoncloud = false;
							if (largeBitmap("undead")[rowNumber-2] || largeBitmap("inanimate")[rowNumber-2] || getAttr("6A00", rowNumber-1).length() > 0) {
								poisoncloud = true;
							}
							String additional = getAttr("C800", rowNumber-1);
							int poisonres = Integer.parseInt(additional.equals("")?"0":additional) + (poisoncloud?10:0);
							cell.setCellValue(poisonres==0?"":Integer.toString(poisonres));
						} else if (pair[0].equals("stealthy40")
								|| pair[0].equals("immobile")
								|| pair[0].equals("teleport")
								|| pair[0].equals("float")
								|| pair[0].equals("bluntres")
								|| pair[0].equals("slashres")
								|| pair[0].equals("pierceres")
								) {
							cell.setCellValue("");
						} else {
							cell.setCellValue(0);
						}
					}
				}
			}
			
			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.MONSTER);
			// test
			i = 0;
			c = new byte[16];
			stream.skip(240);
			while ((stream.read(c, 0, 16)) != -1) {
				boolean found = false;
				System.out.print("(" + (i+1) + ") ");
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				//System.out.print(high + low + " ");
				int val = Integer.decode("0X" + high + low);
				if (val > 0) {
					System.out.print("1:{");
					for (int j=0; j < 16; j++) {
						if ((val & MASK[j]) != 0) {
							System.out.print((found?",":"") + (value1[j].equals("")?("*****"+(j+1)+"*****"):value1[j]));
							//if (value1[j].equals(DOTHIS)) {
							//	System.out.println("1");
								found = true;
						//	}
						}
					}
					System.out.print("}");
				}
				high = String.format("%02X", c[3]);
				low = String.format("%02X", c[2]);
				//System.out.print(high + low + " ");
				val = Integer.decode("0X" + high + low);
				if (val > 0) {
					System.out.print(" 2:{");
					found = false;
					for (int j=0; j < 16; j++) {
						if ((val & MASK[j]) != 0) {
							System.out.print((found?",":"") + (value2[j].equals("")?("*****"+(j+1)+"*****"):value2[j]));
							//if (value2[j].equals(DOTHIS)) {
								//System.out.println("1");
								found = true;
							//}
						}
					}
					System.out.print("}");
				}
				high = String.format("%02X", c[5]);
				low = String.format("%02X", c[4]);
				//System.out.print(high + low + " ");
				val = Integer.decode("0X" + high + low);
				if (val > 0) {
					System.out.print(" 3:{");
					found = false;
					for (int j=0; j < 16; j++) {
						if ((val & MASK[j]) != 0) {
							System.out.print((found?",":"") + (value3[j].equals("")?("*****"+(j+1)+"*****"):value3[j]));
							//if (value3[j].equals(DOTHIS)) {
								//System.out.println("1");
								found = true;
							//}
						}
					}
					System.out.print("}");
				}

//				high = String.format("%02X", c[7]);
//				low = String.format("%02X", c[6]);
//				//System.out.print(high + low + " ");
//				val = Integer.decode("0X" + high + low);
//				if (val > 0) {
//					System.out.print(" 4:{");
//					boolean found = false;
//					for (int j=0; j < 16; j++) {
//						if ((val & mask[j]) != 0) {
//							System.out.print((found?",":"") + (value4[j].equals("")?("*****"+(j+1)+"*****"):value4[j]));
//							found = true;
//						}
//					}
//					System.out.print("}");
//				}

				high = String.format("%02X", c[9]);
				low = String.format("%02X", c[8]);
				//System.out.print(high + low + " ");
				val = Integer.decode("0X" + high + low);
				if (val > 0) {
					System.out.print(" 5:{");
					found = false;
					for (int j=0; j < 16; j++) {
						if ((val & MASK[j]) != 0) {
							System.out.print((found?",":"") + (value5[j].equals("")?("*****"+(j+1)+"*****"):value5[j]));
							//if (value5[j].equals(DOTHIS)) {
							//	System.out.println("1");
								found = true;
							//}
						}
					}
					System.out.print("}");
				}
//				high = String.format("%02X", c[11]);
//				low = String.format("%02X", c[10]);
//				//System.out.print(high + low + " ");
//				val = Integer.decode("0X" + high + low);
//				if (val > 0) {
//					System.out.print(" 6:{");
//					boolean found = false;
//					for (int j=0; j < 16; j++) {
//						if ((val & mask[j]) != 0) {
//							System.out.print((found?",":"") + (value6[j].equals("")?(j+1):value6[j]));
//							found = true;
//						}
//					}
//					System.out.print("}");
//				}
//				high = String.format("%02X", c[13]);
//				low = String.format("%02X", c[12]);
//				//System.out.print(high + low + " ");
//				val = Integer.decode("0X" + high + low);
//				if (val > 0) {
//					System.out.print(" 7:{");
//					boolean found = false;
//					for (int j=0; j < 16; j++) {
//						if ((val & mask[j]) != 0) {
//							System.out.print((found?",":"") + (value7[j].equals("")?(j+1):value7[j]));
//							found = true;
//						}
//					}
//					System.out.print("}");
//				}
//				high = String.format("%02X", c[15]);
//				low = String.format("%02X", c[14]);
//				//System.out.print(high + low + " ");
//				val = Integer.decode("0X" + high + low);
//				if (val > 0) {
//					System.out.print(" 8:{");
//					boolean found = false;
//					for (int j=0; j < 16; j++) {
//						if ((val & mask[j]) != 0) {
//							System.out.print((found?",":"") + (value8[j].equals("")?(j+1):value8[j]));
//							found = true;
//						}
//					}
//					System.out.print("}");
//				}
				
				if (!found) {
					//System.out.println("");
				}
				System.out.println(" ");
				stream.skip(240l);
				i++;
				if (i > 2559) {
					break;
				}
			}
			stream.close();

			// realm
//			int i = 0;
//			int k = 0;
//			int pos = -1;
//			long numFound = 0;
//			byte[] c = new byte[2];
//			stream.skip(64);
//			while ((stream.read(c, 0, 2)) != -1) {
//				String high = String.format("%02X", c[1]);
//				String low = String.format("%02X", c[0]);
//				int weapon = Integer.decode("0X" + high + low);
//				if (weapon == 0) {
//					stream.skip(46l - numFound*2l);
//					// Values
//					for (int x = 0; x < numFound; x++) {
//						byte[] d = new byte[4];
//						stream.read(d, 0, 4);
//						String high1 = String.format("%02X", d[3]);
//						String low1 = String.format("%02X", d[2]);
//						high = String.format("%02X", d[1]);
//						low = String.format("%02X", d[0]);
//						//System.out.print(low + high + " ");
//						if (x == pos) {
//							int fire = new BigInteger(high1 + low1 + high + low, 16).intValue();//Integer.decode("0X" + high + low);
//							System.out.print(i+1 + "\t" + fire);
//							System.out.println("");
//						}
//						//stream.skip(2);
//					}
//					
////					System.out.println("");
//					stream.skip(254l - 46l - numFound*4l);
//					numFound = 0;
//					pos = -1;
//					k = 0;
//					i++;
//				} else {
//					//System.out.print(low + high + " ");
//					if ((low + high).equals("AA01")) {
//						pos = k;
//					}
//					k++;
//					numFound++;
//				}				
//				if (i > 2559) {
//					break;
//				}
//			}
			
			// patience
			doit2(sheet, "CD01", 104);

			// stormimmune
			doit2(sheet, "AF00", 94, new CallbackAdapter() {
				@Override
				public String notFound() {
					return "0";
				}
			});
			
			// regeneration
			doit2(sheet, "BD00", 151);

			// secondshape
			doit2(sheet, "C200", 210);

			// firstshape
			doit2(sheet, "C300", 209);

			// shapechange
			doit2(sheet, "C100", 208);

			// secondtmpshape
			doit2(sheet, "C400", 211);

//			stream = new FileInputStream("Dominions4.exe");			
//			stream.skip(start);
//			rowNumber = 1;
//			// maxage
//			i = 0;
//			k = 0;
//			pos = -1;
//			numFound = 0;
//			c = new byte[2];
//			stream.skip(64);
//			while ((stream.read(c, 0, 2)) != -1) {
//				String high = String.format("%02X", c[1]);
//				String low = String.format("%02X", c[0]);
//				int weapon = Integer.decode("0X" + high + low);
//				if (weapon == 0) {
//					boolean found = false;
//					stream.skip(46l - numFound*2l);
//					// Values
//					for (int x = 0; x < numFound; x++) {
//						byte[] d = new byte[4];
//						stream.read(d, 0, 4);
//						String high1 = String.format("%02X", d[3]);
//						String low1 = String.format("%02X", d[2]);
//						high = String.format("%02X", d[1]);
//						low = String.format("%02X", d[0]);
//						//System.out.print(low + high + " ");
//						if (x == pos) {
//							int fire = new BigInteger(high1 + low1 + high + low, 16).intValue();//Integer.decode("0X" + high + low);
//							//System.out.print(fire);
//							found = true;
//							XSSFRow row = sheet.getRow(rowNumber);
//							rowNumber++;
//							XSSFCell cell = row.getCell(39, Row.CREATE_NULL_AS_BLANK);
//							cell.setCellValue(fire);
//						}
//						//stream.skip(2);
//					}
//					
//					if (!found) {
//						//System.out.print("");
//						rowNumber++;
//					}
//					//System.out.println("");
//					stream.skip(254l - 46l - numFound*4l);
//					numFound = 0;
//					pos = -1;
//					k = 0;
//					i++;
//				} else {
//					//System.out.print(low + high + " ");
//					if ((low + high).equals("1C01")) {
//						pos = k;
//					}
//					k++;
//					numFound++;
//				}				
//				if (i > 2559) {
//					break;
//				}
//			}
//			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.MONSTER_MAGIC);
			
			// magic
			c = new byte[4];
			while ((stream.read(c, 0, 4)) != -1) {
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				if ((high + low).equals("FFFF")) {
					break;
				}
				int id = Integer.decode("0X" + high + low);
				Magic monMagic = monsterMagic.get(id);
				if (monMagic == null) {
					monMagic = new Magic();
					monsterMagic.put(id, monMagic);
				}
				
				//System.out.print("id: " + Integer.decode("0X" + high + low));
				stream.read(c, 0, 4);
				high = String.format("%02X", c[1]);
				low = String.format("%02X", c[0]);
				int path = Integer.decode("0X" + high + low);
				//System.out.print(" path: " + Integer.decode("0X" + high + low));
				stream.read(c, 0, 4);
				high = String.format("%02X", c[1]);
				low = String.format("%02X", c[0]);
				int value = Integer.decode("0X" + high + low);
				//System.out.println(" value: " + Integer.decode("0X" + high + low));
				switch (path) {
				case 0:
					monMagic.F = value;
					break;
				case 1:
					monMagic.A = value;
					break;
				case 2:
					monMagic.W = value;
					break;
				case 3:
					monMagic.E = value;
					break;
				case 4:
					monMagic.S = value;
					break;
				case 5:
					monMagic.D = value;
					break;
				case 6:
					monMagic.N = value;
					break;
				case 7:
					monMagic.B = value;
					break;
				case 8:
					monMagic.H = value;
					break;
				default:
					RandomMagic monRandomMagic = null;
					List<RandomMagic> randomMagicList = monMagic.rand;
					if (randomMagicList == null) {
						randomMagicList = new ArrayList<RandomMagic>();
						monRandomMagic = new RandomMagic();
						if (path == 50) {
							monRandomMagic.mask = 32640;
							monRandomMagic.nbr = 1;
							monRandomMagic.link = value;
							monRandomMagic.rand = 100;
						} else if (path == 51) {
							monRandomMagic.mask = 1920;
							monRandomMagic.nbr = 1;
							monRandomMagic.link = value;
							monRandomMagic.rand = 100;
						} else {
							monRandomMagic.mask = path;
							monRandomMagic.nbr = 1;
							if (value > 100) {
								monRandomMagic.link = value / 100;
								monRandomMagic.rand = 100;
							} else {
								monRandomMagic.link = 1;
								monRandomMagic.rand = value;
							}
						}
						randomMagicList.add(monRandomMagic);
						monMagic.rand = randomMagicList;
					} else {
						boolean found = false;
						for (RandomMagic ranMagic : randomMagicList) {
							if (ranMagic.mask == path && ranMagic.rand == value) {
								ranMagic.nbr++;
								found = true;
							}
							if (ranMagic.mask == 32640 && path == 50 && ranMagic.link == value) {
								ranMagic.nbr++;
								found = true;
							}
							if (ranMagic.mask == 1920 && path == 51 && ranMagic.link == value) {
								ranMagic.nbr++;
								found = true;
							}
						}
						if (!found) {
							monRandomMagic = new RandomMagic();
							if (path == 50) {
								monRandomMagic.mask = 32640;
								monRandomMagic.nbr = 1;
								monRandomMagic.link = value;
								monRandomMagic.rand = 100;
							} else if (path == 51) {
								monRandomMagic.mask = 1920;
								monRandomMagic.nbr = 1;
								monRandomMagic.link = value;
								monRandomMagic.rand = 100;
							} else {
								monRandomMagic.mask = path;
								monRandomMagic.nbr = 1;
								if (value > 100) {
									monRandomMagic.link = value / 100;
									monRandomMagic.rand = 100;
								} else {
									monRandomMagic.link = 1;
									monRandomMagic.rand = value;
								}
							}
							randomMagicList.add(monRandomMagic);
						}
					}
				}
			}
			for (int j = 1; j < 2561; j++) {
				Magic monMagic = monsterMagic.get(j);
				if (monMagic != null) {
					XSSFRow row = sheet.getRow(j);
					XSSFCell cell = row.getCell(48, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.F));
					cell = row.getCell(49, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.A));
					cell = row.getCell(50, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.W));
					cell = row.getCell(51, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.E));
					cell = row.getCell(52, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.S));
					cell = row.getCell(53, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.D));
					cell = row.getCell(54, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.N));
					cell = row.getCell(55, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.B));
					cell = row.getCell(56, Row.CREATE_NULL_AS_BLANK);
					cell.setCellValue(magicStrip(monMagic.H));
					//System.out.print(magout(monMagic.F) + magout(monMagic.A ) + magout(monMagic.W) + magout(monMagic.E) + magout(monMagic.S) + magout(monMagic.D) + magout(monMagic.N) + magout(monMagic.B) + magout(monMagic.H));
					
					if (monMagic.rand != null) {
						int count = 0;
						for (RandomMagic ranMag : monMagic.rand) {
							cell = row.getCell(57 + count*4, Row.CREATE_NULL_AS_BLANK);
							cell.setCellValue(magicStrip(ranMag.rand));
							cell = row.getCell(58 + count*4, Row.CREATE_NULL_AS_BLANK);
							cell.setCellValue(magicStrip(ranMag.nbr));
							cell = row.getCell(59 + count*4, Row.CREATE_NULL_AS_BLANK);
							cell.setCellValue(magicStrip(ranMag.link));
							cell = row.getCell(60 + count*4, Row.CREATE_NULL_AS_BLANK);
							cell.setCellValue(magicStrip(ranMag.mask));
							count++;
							//System.out.print(ranMag.rand + "\t" + ranMag.nbr + "\t" + ranMag.link + "\t" + ranMag.mask + "\t");
						}
					}
				}
				//System.out.println("");
			}
			stream.close();

			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
