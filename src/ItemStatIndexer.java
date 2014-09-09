 import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ItemStatIndexer {
	private static Map<Integer, String> columnsUsed = new HashMap<Integer, String>();
	private static String SkipColumns[] = {
		"id",
		"name",
		"type",
		"constlevel",
		"mainpath",
		"mainlevel",
		"secondarypath",
		"secondarylevel",
		"weapon",
		"armor",
		"test",
		"special",
		"autocombatspell",
		"startbattlespell",
		"itemspell",
		"restricted1",
		"restricted2",
		"restricted3",
		"restrictions",
		"ritual"
	};

	private static XSSFWorkbook readFile(String filename) throws IOException {
		return new XSSFWorkbook(new FileInputStream(filename));
	}
	
	private static void doit(XSSFSheet sheet, String attr, int column) throws IOException {
		doit(sheet, attr, column, null);
	}
	
	private static void doit(XSSFSheet sheet, String attr, int column, Callback callback) throws IOException {
        columnsUsed.remove(column);

        FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.ITEM);
		int rowNumber = 1;
		int i = 0;
		int k = 0;
		int pos = -1;
		long numFound = 0;
		byte[] c = new byte[2];
		stream.skip(120);
		while ((stream.read(c, 0, 2)) != -1) {
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			int weapon = Integer.decode("0X" + high + low);
			if (weapon == 0) {
				boolean found = false;
				int value = 0;
				stream.skip(18l - numFound*2l);
				// Values
				for (int x = 0; x < numFound; x++) {
					byte[] d = new byte[4];
					stream.read(d, 0, 4);
					String high1 = String.format("%02X", d[3]);
					String low1 = String.format("%02X", d[2]);
					high = String.format("%02X", d[1]);
					low = String.format("%02X", d[0]);
					if (x == pos) {
						value = new BigInteger(high1 + low1 + high + low, 16).intValue();
						//System.out.print(value);
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
						if (callback.found(Integer.toString(value)) != null) {
							cell.setCellValue(callback.found(Integer.toString(value)));
						}
					}
				} else {
					if (callback == null) {
						cell.setCellValue("");
					} else {
						if (callback.notFound() != null) {
							cell.setCellValue(callback.notFound());
						}
					}
				}
				stream.skip(206l - 18l - numFound*4l);
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
			if (i > 384) {
				break;
			}
		}
		stream.close();

	}
	
	private static boolean hasAttr(String attr, long position) throws IOException {
        FileInputStream stream = new FileInputStream("Dominions4.exe");	
        try {
    		stream.skip(position);
    		int i = 0;
    		byte[] c = new byte[2];
    		while ((stream.read(c, 0, 2)) != -1) {
    			String high = String.format("%02X", c[1]);
    			String low = String.format("%02X", c[0]);
    			int weapon = Integer.decode("0X" + high + low);
    			if (weapon == 0) {
    				return false;
    			} else {
    				if ((low + high).equals(attr)) {
    					return true;
    				}
    			}				
    			if (i > 384) {
    				break;
    			}
    		}
    		return false;
        } finally {
    		stream.close();
        }
	}

	public static void main(String[] args) {

		FileInputStream stream = null;
		try {
	        long startIndex = Starts.ITEM;
	        int ch;

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(startIndex);
			
			XSSFWorkbook wb = ItemStatIndexer.readFile("BaseI.xlsx");
			FileOutputStream fos = new FileOutputStream("NewBaseI.xlsx");
			XSSFSheet sheet = wb.getSheetAt(0);

			XSSFRow titleRow = sheet.getRow(0);
			int cellNum = 0;
			XSSFCell titleCell = titleRow.getCell(cellNum);
			Set<String> skip = new HashSet<String>(Arrays.asList(SkipColumns));
			while (titleCell != null) {
				String stringCellValue = titleCell.getStringCellValue();
				if (!skip.contains(stringCellValue)) {
					columnsUsed.put(cellNum, stringCellValue);
				}
				cellNum++;
				titleCell = titleRow.getCell(cellNum);
			}
			
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
				startIndex = startIndex + 208l;
				stream.skip(startIndex);
				isr = new InputStreamReader(stream, "ISO-8859-1");
		        in = new BufferedReader(isr);

				//System.out.println(name);
				
				XSSFRow row = sheet.getRow(rowNumber);
				XSSFCell cell1 = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
				cell1.setCellValue(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(name.toString());
			}
			in.close();
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
			// Const level
			int i = 0;
			byte[] c = new byte[1];
			stream.skip(36);
			while ((stream.read(c, 0, 1)) != -1) {
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
				if (c[0] == -1) {
					//System.out.println("");
					cell.setCellValue("");
				} else {
					//System.out.println(c[0]*2);
					cell.setCellValue(c[0]*2);
				}
				stream.skip(207l);
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
			// Mainpath
			String[] paths = {"F", "A", "W", "E", "S", "D", "N", "B"};
			i = 0;
			c = new byte[1];
			stream.skip(37);
			while ((stream.read(c, 0, 1)) != -1) {
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
				if (c[0] == -1) {
					//System.out.println("");
					cell.setCellValue("");
				} else {
					//System.out.println(paths[c[0]]);
					cell.setCellValue(paths[c[0]]);
				}
				stream.skip(207l);
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();
			
			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
			// main level
			i = 0;
			c = new byte[1];
			stream.skip(39);
			while ((stream.read(c, 0, 1)) != -1) {
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(5, Row.CREATE_NULL_AS_BLANK);
				if (c[0] == -1) {
					//System.out.println("");
					cell.setCellValue("");
				} else {
					//System.out.println(c[0]);
					cell.setCellValue(c[0]);
				}
				stream.skip(207l);
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
			// Secondary path
			i = 0;
			c = new byte[1];
			stream.skip(38);
			while ((stream.read(c, 0, 1)) != -1) {
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(6, Row.CREATE_NULL_AS_BLANK);
				if (c[0] == -1) {
					//System.out.println("");
					cell.setCellValue("");
				} else {
					//System.out.println(paths[c[0]]);
					cell.setCellValue(paths[c[0]]);
				}
				stream.skip(207l);
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
			// Secondary level
			i = 0;
			c = new byte[1];
			stream.skip(40);
			while ((stream.read(c, 0, 1)) != -1) {
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(7, Row.CREATE_NULL_AS_BLANK);
				if (c[0] == -1 || c[0] == 0) {
					//System.out.println("");
					cell.setCellValue("");
				} else {
					//System.out.println(c[0]);
					cell.setCellValue(c[0]);
				}
				stream.skip(207l);
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
			// Weapon
			i = 0;
			c = new byte[2];
			stream.skip(44);
			while ((stream.read(c, 0, 2)) != -1) {
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(8, Row.CREATE_NULL_AS_BLANK);
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				int weapon = Integer.decode("0X" + high + low);
				if (weapon == 0) {
					//System.out.println("");
					cell.setCellValue("");
				} else {
					//System.out.println(Integer.decode("0X" + high + low));
					cell.setCellValue(Integer.decode("0X" + high + low));
				}
				stream.skip(206l);
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
			// Armor
			i = 0;
			c = new byte[2];
			stream.skip(46);
			while ((stream.read(c, 0, 2)) != -1) {
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(9, Row.CREATE_NULL_AS_BLANK);
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				int weapon = Integer.decode("0X" + high + low);
				if (weapon == 0) {
					//System.out.println("");
					cell.setCellValue("");
				} else {
					//System.out.println(Integer.decode("0X" + high + low));
					cell.setCellValue(Integer.decode("0X" + high + low));
				}
				stream.skip(206l);
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
	        startIndex = Starts.ITEM;
			// Itemspell
			stream.skip(48);
			startIndex = startIndex + 48l;
			i = 0;
			isr = new InputStreamReader(stream, "ISO-8859-1");
	        in = new BufferedReader(isr);
			while ((ch = in.read()) > -1) {
				StringBuffer name = new StringBuffer();
				while (ch != 0) {
					name.append((char)ch);
					ch = in.read();
				}
				in.close();

				stream = new FileInputStream("Dominions4.exe");		
				startIndex = startIndex + 208l;
				stream.skip(startIndex);
				isr = new InputStreamReader(stream, "ISO-8859-1");
		        in = new BufferedReader(isr);

				//System.out.println(name);
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(130, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(name.toString());
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			rowNumber = 1;
	        startIndex = Starts.ITEM;
			// Startbattlespell and Autocombatspell
			stream.skip(84);
			startIndex = startIndex + 84l;
			i = 0;
			isr = new InputStreamReader(stream, "ISO-8859-1");
	        in = new BufferedReader(isr);
			while ((ch = in.read()) > -1) {
				StringBuffer name = new StringBuffer();
				while (ch != 0) {
					name.append((char)ch);
					ch = in.read();
				}
				in.close();

				int blankCol = 129;
				int column = 128;
				if (hasAttr("8500", startIndex+36l)) {
					column = 129;
					blankCol = 128;
				}
				stream = new FileInputStream("Dominions4.exe");		
				startIndex = startIndex + 208l;
				stream.skip(startIndex);
				isr = new InputStreamReader(stream, "ISO-8859-1");
		        in = new BufferedReader(isr);

				//System.out.println(name);
				XSSFRow row = sheet.getRow(rowNumber);
				rowNumber++;
				XSSFCell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
				cell.setCellValue(name.toString());
				XSSFCell blankcell = row.getCell(blankCol, Row.CREATE_NULL_AS_BLANK);
				blankcell.setCellValue("");
				i++;
				if (i > 384) {
					break;
				}
			}
			stream.close();

			// Fireres
			doit(sheet, "C600", 11);

			// Coldres
			doit(sheet, "C900", 12);

			// Poisonres
			doit(sheet, "C800", 13);

			// Shockres
			doit(sheet, "C700", 10);

			// Leadership
			doit(sheet, "9D00", 61);

			// str
			doit(sheet, "9700", 28);

			// fixforge
			doit(sheet, "C501", 18);

			// magic leadership
			doit(sheet, "9E00", 63);

			// undead leadership
			doit(sheet, "9F00", 62);

			// inspirational leadership
			doit(sheet, "7001", 64);

			// morale
			doit(sheet, "3401", 27);

			// penetration
			doit(sheet, "A100", 36);

			// pillage
			doit(sheet, "8300", 113);

			// fear
			doit(sheet, "B700", 66);

			// mr
			doit(sheet, "A000", 26);

			// taint
			doit(sheet, "0601", 60);

			// reinvigoration
			doit(sheet, "7500", 35);

			// awe
			doit(sheet, "6900", 67);

			// F
			doit(sheet, "0A00", 73);

			// A
			doit(sheet, "0B00", 74);

			// W
			doit(sheet, "0C00", 75);

			// E
			doit(sheet, "0D00", 76);

			// S
			doit(sheet, "0E00", 77);

			// D
			doit(sheet, "0F00", 78);

			// N
			doit(sheet, "1000", 79);

			// B
			doit(sheet, "1100", 80);

			// H
			doit(sheet, "1200", 81);

			// elemental
			doit(sheet, "1400", 73, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1400", 74, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1400", 75, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1400", 76, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});

			// sorcery
			doit(sheet, "1500", 77, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1500", 78, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1500", 79, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1500", 80, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			
			// all paths
			doit(sheet, "1600", 73, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1600", 74, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1600", 75, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1600", 76, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1600", 77, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1600", 78, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1600", 79, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1600", 80, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});

			// fire ritual range
			doit(sheet, "2800", 82);

			// air ritual range
			doit(sheet, "2900", 83);

			// water ritual range
			doit(sheet, "2A00", 84);

			// earth ritual range
			doit(sheet, "2B00", 85);

			// astral ritual range
			doit(sheet, "2C00", 86);

			// death ritual range
			doit(sheet, "2D00", 87);

			// nature ritual range
			doit(sheet, "2E00", 88);

			// blood ritual range
			doit(sheet, "2F00", 89);

			// elemental range
			doit(sheet, "1700", 82, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1700", 83, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1700", 84, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1700", 85, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});

			// sorcery range
			doit(sheet, "1800", 86, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1800", 87, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1800", 88, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1800", 89, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});

			// all range
			doit(sheet, "1900", 82, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1900", 83, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1900", 84, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1900", 85, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1900", 86, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1900", 87, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1900", 88, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});
			doit(sheet, "1900", 89, new CallbackAdapter() {
				@Override
				public String notFound() {
					return null;
				}
			});

			// darkvision
			doit(sheet, "1901", 14);

			// limited regeneration
			doit(sheet, "CE01", 16);

			// regeneration
			doit(sheet, "BD00", 141);

			// waterbreathing
			doit(sheet, "6E00", 47);

			// stealthb
			doit(sheet, "8601", 38);

			// stealth
			doit(sheet, "6C00", 37);

			// att
			doit(sheet, "9600", 29);

			// def
			doit(sheet, "7901", 30);

			// woundfend
			doit(sheet, "9601", 17);

			// restricted
			//doit(sheet, "1601", 142);
			// restricted
			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			i = 0;
			int k = 0;
			Set<Integer> posSet = new HashSet<Integer>();
			long numFound = 0;
			c = new byte[2];
			stream.skip(120);
			while ((stream.read(c, 0, 2)) != -1) {
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				int weapon = Integer.decode("0X" + high + low);
				if (weapon == 0) {
					stream.skip(18l - numFound*2l);
					int numRealms = 0;
					// Values
					for (int x = 0; x < numFound; x++) {
						byte[] d = new byte[4];
						stream.read(d, 0, 4);
						String high1 = String.format("%02X", d[3]);
						String low1 = String.format("%02X", d[2]);
						high = String.format("%02X", d[1]);
						low = String.format("%02X", d[0]);
						//System.out.print(low + high + " ");
						if (posSet.contains(x)) {
							int fire = new BigInteger(high1 + low1 + high + low, 16).intValue();//Integer.decode("0X" + high + low);
							//System.out.print(i+1 + "\t" + fire);
							//System.out.println("");
							XSSFRow row = sheet.getRow(i+1);
							XSSFCell cell = row.getCell(142+numRealms, Row.CREATE_NULL_AS_BLANK);							
							cell.setCellValue(fire-100);
							numRealms++;
						}
						//stream.skip(2);
					}
					
//					System.out.println("");
					stream.skip(206l - 18l - numFound*4l);
					numFound = 0;
					posSet.clear();
					k = 0;
					i++;
				} else {
					//System.out.print(low + high + " ");
					if ((low + high).equals("1601")) {
						posSet.add(k);
					}
					k++;
					numFound++;
				}				
				if (i > 384) {
					break;
				}
			}
			stream.close();

			wb.write(fos);
			fos.close();

			for (String col :columnsUsed.values()) {
				System.out.println(col);
			}
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
