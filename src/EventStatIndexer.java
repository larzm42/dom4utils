import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EventStatIndexer {
	static class Event {
		String description;
		int rarity;
		int id;
		List<Pair> requirements;
		List<Pair> effects;
	}
	
	static class Pair {
		String name;
		String value;
	}
	
	public static void doit(List<Event> events) throws IOException {
		FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.EVENT);
		
		int i = 0;
		int k = 0;
		long numFound = 0;
		byte[] c = new byte[2];
		stream.skip(6);
		while ((stream.read(c, 0, 2)) != -1) {
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			int weapon = Integer.decode("0X" + high + low);
			if (weapon == 0) {
				int value = 0;
				stream.skip(24l - numFound*2l);
				// Values
				for (int x = 0; x < numFound; x++) {
					stream.read(c, 0, 2);
					high = String.format("%02X", c[1]);
					low = String.format("%02X", c[0]);
						int tmp = new BigInteger(high + low, 16).intValue();
						if (tmp < 1000) {
							value = Integer.decode("0X" + high + low);
						} else {
							value = new BigInteger("FFFF" + high + low, 16).intValue();
						}
						//System.out.print(value + " ");
						events.get(i).requirements.get(x).value = Integer.toString(value);
					//}
					stream.skip(6);
				}
				
				//System.out.println("");
				stream.skip(328l - 26l - numFound*8l);
				numFound = 0;
				//pos = -1;
				k = 0;
				i++;
			} else {
//				if (numFound == 0) System.out.print("Requirements: ");
				Pair pair = new Pair();
				pair.name = low + high;
				events.get(i).requirements.add(pair);
//				System.out.print(low + high + " ");
				k++;
				numFound++;
			}				
			if (i >= events.size()) {
				break;
			}
		}
		stream.close();
	}
	
	public static void doit2(List<Event> events) throws IOException {
		FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.EVENT);
		
		int i = 0;
		long numFound = 0;
		byte[] c = new byte[2];
		stream.skip(128);
		while ((stream.read(c, 0, 2)) != -1) {
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			int weapon = Integer.decode("0X" + high + low);
			if (weapon == 0) {
				int value = 0;
				stream.skip(38l - numFound*2l);
				// Values
				for (int x = 0; x < numFound; x++) {
					stream.read(c, 0, 2);
					high = String.format("%02X", c[1]);
					low = String.format("%02X", c[0]);
					//System.out.print(low + high + " ");
						int tmp = new BigInteger(high + low, 16).intValue();
						if (tmp < 1000) {
							value = Integer.decode("0X" + high + low);
						} else {
							value = new BigInteger("FFFF" + high + low, 16).intValue();
						}
		//				System.out.print(value + " ");
						events.get(i).effects.get(x).value = Integer.toString(value);
					stream.skip(6);
				}
				
	//			System.out.println("");
				stream.skip(328l - 40l - numFound*8l);
				numFound = 0;
				i++;
			} else {
				//if (numFound == 0) System.out.print("Effects: ");
				Pair pair = new Pair();
				pair.name = low + high;
				events.get(i).effects.add(pair);
//				System.out.print(low + high + " ");
				numFound++;
			}				
			if (i >= events.size()) {
				break;
			}
		}
		stream.close();
	}

	public static void doit3(List<Event> events) throws IOException {
        int ch;

		for (Event event : events) {
			FileInputStream stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.EVENT_DESC);
			
			stream.skip(event.id - 7124888);
			
			// Name
			InputStreamReader isr = new InputStreamReader(stream, "ISO-8859-1");
	        Reader in = new BufferedReader(isr);
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
				event.description = name.toString();
				break;
			}
			in.close();
			stream.close();
			

		}
	}
	
	public static void main(String[] args) {

		FileInputStream stream = null;
		try {
	        int ch;
	        
	        List<Event> events = new ArrayList<Event>();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.EVENT);
			
			// id
			int i = 0;
			byte[] d = new byte[4];
			while ((stream.read(d, 0, 4)) != -1) {
				Event event = new Event();
				event.id = new BigInteger(new byte[]{d[3], d[2], d[1], d[0]}).intValue();
				event.requirements = new ArrayList<Pair>();
				event.effects = new ArrayList<Pair>();
				events.add(event);
				stream.skip(324l);
				i++;
				if (i >= 2924) {
					break;
				}
			}
			stream.close();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.EVENT);
			
			// rarity
			i = 0;
			byte[] c = new byte[2];
			stream.skip(4);
			while ((stream.read(c, 0, 2)) != -1) {
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				int tmp = new BigInteger(high + low, 16).intValue();
				if (tmp > 100) {
					tmp = new BigInteger("FFFFFF" + low, 16).intValue();
				}
				events.get(i).rarity = tmp;
				//System.out.println(tmp + " " + events.get(i).description);
				stream.skip(326l);
				i++;
				if (i >= events.size()) {
					break;
				}
			}
			stream.close();

			doit(events);
			doit2(events);
			doit3(events);
			
			//System.out.println("sdfsdfds");
			
			for (Event event : events) {
				System.out.print(event.rarity + "\t");
				System.out.print(event.id + "\t");
				System.out.print(event.description + "\t");
				boolean first = true;
				for (Pair pair : event.requirements) {
					System.out.print((first?"":",")+pair.name + " " + pair.value);
					first = false;
				}
				System.out.print("\t");
				first=true;
				for (Pair pair : event.effects) {
					System.out.print((first?"":",")+pair.name + " " + pair.value);
					first = false;
				}
				System.out.println("");
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
