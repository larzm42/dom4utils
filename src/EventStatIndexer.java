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
	
	static String[][] requirementMapping = {
		{"1300", "mydominion"}, 
		{"1200", "nation"}, 
		{"1000", "maxdominion"}, 
		{"0200", "pop"}, 
		{"0400", "temple"}, 
		{"0700", "maxunrest"}, 
		{"0600", "minunrest"}, 
		{"0C00", "dominion"}, 
		{"1F00", "era"}, 
		{"6400", "chaos"}, 
		{"0500", "land"}, 
		{"4000", "pop0ok"}, 
		{"6600", "cold"}, 
		{"7300", "magic"}, 
		{"7100", "growth"}, 
		{"7200", "luck"}, 
		{"6E00", "order"}, 
		{"6400", "chaos"}, 
		{"6500", "lazy"}, 
		{"6700", "death"}, 
		{"6800", "unluck"}, 
		{"6900", "unmagic"}, 
		{"6F00", "prod"}, 
		{"1400", "turn"}, 
		{"4500", "maxturn"}, 
		{"1C00", "noseason"}, 
		{"1B00", "season"}, 
		{"2000", "noera"}, 
		{"7000", "heat"}, 
		{"1900", "waste"}, 
		{"1800", "swamp"}, 
		{"0300", "coast"}, 
		{"0100", "lab"}, 
		{"0D00", "mountain"}, 
		{"0E00", "forest"}, 
		{"1D00", "freesites"}, 
		{"3A00", "unique"}, 
		{"3D00", "monster"}, 
		{"3900", "fort"}, 
		{"1500", "fullowner"}, 
		{"1700", "notnation"}, 
		{"2100", "r33"}, 
		{"1600", "r22"}, 
		{"4100", "r65"}, 
		{"0A00", "gem"}, 
		{"3500", "rare"}, 
		{"0900", "maxtroops"}, 
		{"3600", "mindef"}, 
		{"0B00", "commander"}, 
		{"3B00", "code"}, 
		{"2300", "reseacher"}, 
		{"1E00", "freshwater"}, 
		{"2500", "capital"}, 
		{"2400", "capital"}, 
		{"2C00", "pathdeath"}, 
		{"2D00", "pathnature"}, 
		{"2900", "pathwater"}, 
		{"2A00", "pathearth"}, 
		{"2E00", "pathblood"}, 
		{"2200", "humanoidres"}, 
		{"3100", "foundsite"}, 
		{"3200", "hiddensite"}, 
		{"3300", "site"}, 
		{"3400", "nearbysite"}, 
		{"3700", "maxdef"}, 
		{"3800", "poptype"}, 
		{"1100", "nativesoil"}, 

	};
	static String[][] effectMapping = {
		{"4000", "incdom"}, 
		{"3900", "decscale Turmoil"}, 
		{"3A00", "decscale Misfortune"}, 
		{"3B00", "decscale Death"}, 
		{"2A00", "gold"}, 
		{"3200", "defence"}, 
		{"0800", "landgold"}, 
		{"0100", "nation"}, 
		{"1800", "1d6units"}, 
		{"1900", "2d6units"}, 
		{"1A00", "3d6units"}, 
		{"1B00", "4d6units"}, 
		{"1C00", "5d6units"}, 
		{"1D00", "6d6units"}, 
		{"1E00", "7d6units"}, 
		{"1F00", "8d6units"}, 
		{"2000", "9d6units"}, 
		{"2100", "10d6units"}, 
		{"2200", "11d6units"}, 
		{"2300", "12d6units"}, 
		{"2400", "13d6units"}, 
		{"2500", "14d6units"}, 
		{"2600", "15d6units"}, 
		{"2700", "16d6units"}, 
		{"2900", "magicitem"}, 
		{"0E00", "1d6vis?"}, 
		{"0F00", "1d6vis?"}, 
		{"1000", "1d6vis?"}, 
		{"1100", "2d6vis"}, 
		{"1200", "3d6vis"}, 
		{"1300", "4d6vis"}, 
		{"3500", "gemloss"}, 
		{"0A00", "kill"}, 
		{"1400", "1com"}, 
		{"5D00", "code"}, 
		{"0C00", "unrest"}, 
		{"4E00", "taxboost"}, 
		{"0D00", "lab"}, 
		{"4100", "newsite"}, 
		{"0900", "landprod"}, 
		{"3300", "temple"}, 
		{"2F00", "fort"}, 
		{"0300", "e3"}, 
		{"3F00", "killmon"}, 
		{"4400", "killcom"}, 
		{"5A00", "bloodboost"}, 
		{"2300", "stealthcom"}, 
		{"3400", "revolt"}, 
		{"2B00", "newdom"}, 
		{"1600", "3com"}, 
		{"2800", "id"}, 
		{"4500", "worldunrest"}, 
		{"4700", "worldincscale"}, 
		{"4A00", "worlddecscale"}, 
		{"4F00", "worldritrebate"}, 
		{"6600", "worldheal"}, 
		{"6100", "linger"}, 
	 
	};
	
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
				pair.name = translateRequirements(low + high);
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
				pair.name = translateEffects(low + high);
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
	
	private static String translateRequirements(String value) {
		for (String[]pair : requirementMapping) {
			if (pair[0].equals(value)) {
				return pair[1];
			}
		}
		return "!!!"+value+"!!!";
	}
	
	private static String translateEffects(String value) {
		for (String[]pair : effectMapping) {
			if (pair[0].equals(value)) {
				return pair[1];
			}
		}
		return "!!!"+value+"!!!";
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
