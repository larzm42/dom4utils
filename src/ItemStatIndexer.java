 import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ItemStatIndexer {
	
	private static XSSFWorkbook readFile(String filename) throws IOException {
		return new XSSFWorkbook(new FileInputStream(filename));
	}
	
	private static void doit(XSSFSheet sheet, String attr, int column) throws IOException {
		doit(sheet, attr, column, null);
	}
	
	private static void doit(XSSFSheet sheet, String attr, int column, Callback callback) throws IOException {
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
						cell.setCellValue(callback.found(Integer.toString(value)));
					}
				} else {
					if (callback == null) {
						cell.setCellValue("");
					} else {
						cell.setCellValue(callback.notFound());
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
			if (i > 380) {
				break;
			}
		}
		stream.close();

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
				if (i > 380) {
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
				if (i > 380) {
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
				if (i > 380) {
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
				if (i > 380) {
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
				if (i > 380) {
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
				if (i > 380) {
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
				if (i > 380) {
					break;
				}
			}
			stream.close();

//			stream = new FileInputStream("Dominions4.exe");			
//			stream.skip(Starts.ITEM);
//			rowNumber = 1;
//			// Itemspell
//			stream.skip(48);
//			start = start + 48l;
//			i = 0;
//			isr = new InputStreamReader(stream, "ISO-8859-1");
//	        in = new BufferedReader(isr);
//			while ((ch = in.read()) > -1) {
//				StringBuffer name = new StringBuffer();
//				while (ch != 0) {
//					name.append((char)ch);
//					ch = in.read();
//				}
//				in.close();
//
//				stream = new FileInputStream("Dominions4.exe");		
//				start = start + 208l;
//				stream.skip(start);
//				isr = new InputStreamReader(stream, "ISO-8859-1");
//		        in = new BufferedReader(isr);
//
//				//System.out.println(name);
//				XSSFRow row = sheet.getRow(rowNumber);
//				rowNumber++;
//				XSSFCell cell = row.getCell(130, Row.CREATE_NULL_AS_BLANK);
//				cell.setCellValue(name.toString());
//				i++;
//				if (i > 380) {
//					break;
//				}
//			}
//			stream.close();

//	        start = 0x0575ae8l;
//			stream = new FileInputStream("Dominions4.exe");			
//			stream.skip(start);
//			rowNumber = 1;
//			// Startbattlespell Autocombatspell?
//			stream.skip(84);
//			start = start + 84l;
//			i = 0;
//			isr = new InputStreamReader(stream, "ISO-8859-1");
//	        in = new BufferedReader(isr);
//			while ((ch = in.read()) > -1) {
//				StringBuffer name = new StringBuffer();
//				while (ch != 0) {
//					name.append((char)ch);
//					ch = in.read();
//				}
//				in.close();
//
//				stream = new FileInputStream("Dominions4.exe");		
//				start = start + 208l;
//				stream.skip(start);
//				isr = new InputStreamReader(stream, "ISO-8859-1");
//		        in = new BufferedReader(isr);
//
//				//System.out.println(name);
//				XSSFRow row = sheet.getRow(rowNumber);
//				rowNumber++;
//				XSSFCell cell = row.getCell(129, Row.CREATE_NULL_AS_BLANK);
//				cell.setCellValue(name.toString());
//				i++;
//				if (i > 380) {
//					break;
//				}
//			}
//			in.close();
//			stream.close();

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

			// undead leadership
			doit(sheet, "9F00", 62);

//			stream = new FileInputStream("Dominions4.exe");			
//			stream.skip(Starts.ITEM);
//			rowNumber = 1;
//			// fire ritual range
//			i = 0;
//			k = 0;
//			pos = -1;
//			numFound = 0;
//			c = new byte[2];
//			stream.skip(120);
//			while ((stream.read(c, 0, 2)) != -1) {
//				String high = String.format("%02X", c[1]);
//				String low = String.format("%02X", c[0]);
//				int weapon = Integer.decode("0X" + high + low);
//				if (weapon == 0) {
//					boolean found = false;
//					int value = 0;
//					stream.skip(18l - numFound*2l);
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
//							value = new BigInteger(high1 + low1 + high + low, 16).intValue();//Integer.decode("0X" + high + low);
//							//System.out.print(fire);
//							found = true;
//						}
//						//stream.skip(2);
//					}
//					
//					//System.out.println("");
//					XSSFRow row = sheet.getRow(rowNumber);
//					rowNumber++;
//					XSSFCell cell = row.getCell(82, Row.CREATE_NULL_AS_BLANK);
//					if (found) {
//						cell.setCellValue(value);
//					} else {
//						cell.setCellValue("");
//					}
//					stream.skip(206l - 18l - numFound*4l);
//					numFound = 0;
//					pos = -1;
//					k = 0;
//					i++;
//				} else {
//					//System.out.print(low + high + " ");
//					if ((low + high).equals("2800")) {
//						pos = k;
//					}
//					k++;
//					numFound++;
//				}				
//				if (i > 380) {
//					break;
//				}
//			}
//			stream.close();

			// morale
			doit(sheet, "3401", 27);

			// penetration
			doit(sheet, "A100", 36);

			// pillage
			doit(sheet, "8300", 113);

			// fear
			doit(sheet, "B700", 66);

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
