import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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
		{"4500", "national"}, 
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
		{"3C00", "nomonster"}, 
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
		{"2B00", "pathastral"}, 
		{"2E00", "pathblood"}, 
		{"2200", "humanoidres"}, 
		{"3100", "foundsite"}, 
		{"3200", "hiddensite"}, 
		{"3300", "site"}, 
		{"3400", "nearbysite"}, 
		{"3700", "maxdef"}, 
		{"3800", "poptype"}, 
		{"1100", "nativesoil"}, 
		{"0F00", "farm"}, 
		{"0800", "mintroops"}, 
		{"2600", "maxturn"}, 
		{"3E00", "claimedthrone"}, 

	};
	
	static String[] requirementToUnit = {
		"monster", 
		"nomonster", 
	};

	
	static String[][] effectMapping = {
		{"4000", "incdom"}, 
		{"3600", "incscale1"}, 
		{"3700", "incscale2"}, 
		{"3900", "decscale1"}, 
		{"3A00", "decscale2"}, 
		{"3B00", "decscale3"}, 
		{"2A00", "gold"}, 
		{"3200", "defence"}, 
		{"0800", "landgold"}, 
		{"0100", "nation"}, 
		{"5C00", "1d3units"}, 
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
		{"0E00", "1d3gems"}, 
		{"0F00", "1d6gems"}, 
		{"1000", "1d6gems"}, 
		{"1100", "2d6gems"}, 
		{"1200", "3d6gems"}, 
		{"1300", "4d6gems"}, 
		{"3500", "gemloss"}, 
		{"0A00", "kill"}, 
		{"1400", "1com"}, 
		{"1500", "2com"}, 
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
		{"5300", "fireboost"}, 
		{"5400", "airboost"}, 
		{"5500", "waterboost"}, 
		{"5600", "earthboost"}, 
		{"5700", "astralboost"}, 
		{"5800", "deathboost"}, 
		{"5900", "natureboost"}, 
		{"5A00", "bloodboost"}, 
		{"5B00", "holyboost"}, 
		{"4300", "stealthcom"}, 
		{"3400", "revolt"}, 
		{"2B00", "newdom"}, 
		{"1600", "3com"}, 
		{"1700", "4com"}, 
		{"2800", "id"}, 
		{"4500", "worldunrest"}, 
		{"4700", "worldincscale"}, 
		{"4800", "worldincscale2"}, 
		{"4A00", "worlddecscale"}, 
		{"4F00", "worldritrebate"}, 
		{"5000", "worldincdom"}, 
		{"6600", "worldheal"}, 
		{"6100", "linger"}, 
		{"3D00", "curse"}, 
		{"3800", "incscale2"}, 
		{"0B00", "emigration"}, 
		{"0200", "assassin"}, 
		{"3C00", "visitors"}, 
		{"3E00", "disease"}, 
		{"2C00", "addequip"}, 
		{"6000", "revealprov"}, 
		{"5F00", "strikeunits"}, 
		{"5100", "incpop"}, 
		{"4D00", "researchaff"}, 
		{"5200", "revealsite"}, 
		{"6800", "1unit"}, 
		{"6400", "worlddisease"}, 
		{"6200", "worlddarkness"}, 
		{"6500", "worldmark"}, 
		{"6300", "worldcurse"}, 
		{"6700", "worldage"}, 
		{"6D00", "otherplane"}, 
	 
	};
	
	static String[] effectToUnit = {
		"1d3units", 
		"1d6units", 
		"2d6units", 
		"3d6units", 
		"4d6units", 
		"5d6units", 
		"6d6units", 
		"7d6units", 
		"8d6units", 
		"9d6units", 
		"10d6units", 
		"11d6units", 
		"12d6units", 
		"13d6units", 
		"14d6units", 
		"15d6units", 
		"16d6units", 
		"1com", 
		"2com", 
		"3com", 
		"4com", 
		"1unit", 
		"killmon", 
		"killcom", 
		"stealthcom", 
		"assassin", 
		"fireboost", 
		"airboost", 
		"waterboost", 
		"earthboost", 
		"astralboost", 
		"deathboost", 
		"natureboost", 
		"bloodboost", 
		"holyboost", 
	};

	static String[] effectToGem = {
		"1d3gems", 
		"1d6gems", 
		"2d6gems", 
		"3d6gems", 
		"4d6gems", 
		"gemloss", 
	};

	static String[] effectToScale = {
		"incscale1", 
		"incscale2", 
		"decscale1", 
		"decscale2", 
		"decscale3", 
		"worldincscale", 
		"worldincscale2", 
		"worlddecscale", 
	};
	
	static Set<String> effectToUnitSet = new HashSet<String>();
	static Set<String> requirementToUnitSet = new HashSet<String>();
	static Set<String> effectToGemSet = new HashSet<String>();
	static Set<String> effectToScaleSet = new HashSet<String>();
	
	static Map<Integer, String> unitMap = new HashMap<Integer, String>();
	static Map<Integer, String> gemMap = new HashMap<Integer, String>();
	static Map<Integer, String> scaleMap = new HashMap<Integer, String>();

	public static void requirements(List<Event> events) throws IOException {
		FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.EVENT);
		stream.skip(1200l);
		
		int i = 0;
		long numFound = 0;
		byte[] c = new byte[2];
		stream.skip(2l);
		while ((stream.read(c, 0, 2)) != -1) {
			String high = String.format("%02X", c[1]);
			String low = String.format("%02X", c[0]);
			int weapon = Integer.decode("0X" + high + low);
			if (weapon == 0) {
				int value = 0;
				stream.skip(28l - numFound*2l);
				// Values
				for (int x = 0; x < numFound; x++) {
					stream.read(c, 0, 2);
					high = String.format("%02X", c[1]);
					low = String.format("%02X", c[0]);
					int tmp = new BigInteger(high + low, 16).intValue();
					if (tmp < 5000) {
						value = Integer.decode("0X" + high + low);
					} else {
						value = new BigInteger("FFFF" + high + low, 16).intValue();
					}
					if (requirementToUnitSet.contains(events.get(i).requirements.get(x).name)) {
						events.get(i).requirements.get(x).value = unitMap.get(value) != null ? unitMap.get(value) : Integer.toString(value);
					} else {
						events.get(i).requirements.get(x).value = Integer.toString(value);
					}
					stream.skip(6);
				}
				
				stream.skip(1528l - 30l - numFound*8l);
				numFound = 0;
				i++;
			} else {
				Pair pair = new Pair();
				pair.name = translateRequirements(low + high);
				events.get(i).requirements.add(pair);
				numFound++;
			}				
			if (i >= events.size()) {
				break;
			}
		}
		stream.close();
	}
	
	public static void effects(List<Event> events) throws IOException {
		FileInputStream stream = new FileInputStream("Dominions4.exe");			
		stream.skip(Starts.EVENT);
		stream.skip(1200l);

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
					int tmp = new BigInteger(high + low, 16).intValue();
					if (tmp < 5000) {
						value = Integer.decode("0X" + high + low);
					} else {
						value = new BigInteger("FFFF" + high + low, 16).intValue();
					}
					if (effectToUnitSet.contains(events.get(i).effects.get(x).name)) {
						events.get(i).effects.get(x).value = unitMap.get(value) != null ? unitMap.get(value) : Integer.toString(value);
					} else if (effectToGemSet.contains(events.get(i).effects.get(x).name)) {
						events.get(i).effects.get(x).value = gemMap.get(value) != null ? gemMap.get(value) : Integer.toString(value);
					} else if (effectToScaleSet.contains(events.get(i).effects.get(x).name)) {
						events.get(i).effects.get(x).value = scaleMap.get(value) != null ? scaleMap.get(value) : Integer.toString(value);
					} else {
						events.get(i).effects.get(x).value = Integer.toString(value);
					}
					stream.skip(6);
				}
				stream.skip(1528l - 40l - numFound*8l);
				numFound = 0;
				i++;
			} else {
				Pair pair = new Pair();
				pair.name = translateEffects(low + high);
				events.get(i).effects.add(pair);
				numFound++;
			}				
			if (i >= events.size()) {
				break;
			}
		}
		stream.close();
	}

	private static String translateRequirements(String value) {
		for (String[]pair : requirementMapping) {
			if (pair[0].equals(value)) {
				return pair[1];
			}
		}
		return "0x"+value+"";
	}
	
	private static String translateEffects(String value) {
		for (String[]pair : effectMapping) {
			if (pair[0].equals(value)) {
				return pair[1];
			}
		}
		return "0x"+value+"";
	}
	
	public static void main(String[] args) {
		effectToUnitSet.addAll(Arrays.asList(effectToUnit));
		requirementToUnitSet.addAll(Arrays.asList(requirementToUnit));
		effectToGemSet.addAll(Arrays.asList(effectToGem));
		effectToScaleSet.addAll(Arrays.asList(effectToScale));
		FileInputStream stream = null;
		try {
			File file = new File("units.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(line, "\t");
				Integer key = Integer.parseInt(tok.nextToken());
				String value = tok.nextToken();
				if (key != 1) {
					unitMap.put(key, value);
				}
			}
			fileReader.close();
			
			gemMap.put(0, "Fire");
			gemMap.put(1, "Air");
			gemMap.put(2, "Water");
			gemMap.put(3, "Earth");
			gemMap.put(4, "Astral");
			gemMap.put(5, "Death");
			gemMap.put(6, "Nature");
			gemMap.put(7, "Slaves");
			gemMap.put(50, "Random");
			gemMap.put(51, "Elemental");
			gemMap.put(52, "Sorcery");
			gemMap.put(56, "all7");
			
			scaleMap.put(0, "Turmoil");
			scaleMap.put(1, "Sloth");
			scaleMap.put(2, "Cold");
			scaleMap.put(3, "Death");
			scaleMap.put(4, "Misfortune");
			scaleMap.put(5, "Drain");

	        List<Event> events = new ArrayList<Event>();

			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.EVENT);
			long startIndex = Starts.EVENT;
				
			// Name
			InputStreamReader isr = new InputStreamReader(stream, "ISO-8859-1");
			Reader in = new BufferedReader(isr);
			int ch;
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
				Event event = new Event();
				event.description = name.toString();
				event.requirements = new ArrayList<Pair>();
				event.effects = new ArrayList<Pair>();
				events.add(event);

				stream = new FileInputStream("Dominions4.exe");		
				startIndex = startIndex + 1528l;
				stream.skip(startIndex);
				isr = new InputStreamReader(stream, "ISO-8859-1");
				in = new BufferedReader(isr);
			}
			in.close();
			stream.close();
				
			stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.EVENT);
			stream.skip(1200l);
			
			// rarity
			int i = 0;
			byte[] c = new byte[2];
			while ((stream.read(c, 0, 2)) != -1) {
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				int tmp = new BigInteger(high + low, 16).intValue();
				if (tmp > 100) {
					tmp = new BigInteger("FFFFFF" + low, 16).intValue();
				}
				events.get(i).rarity = tmp;
				stream.skip(1526l);
				i++;
				if (i >= events.size()) {
					break;
				}
			}
			stream.close();

			requirements(events);
			effects(events);
			
			for (Event event : events) {
				System.out.print(event.rarity + "\t");
				System.out.print(event.description + "\t");
				boolean first = true;
				for (Pair pair : event.requirements) {
					System.out.print((first?"":", ")+pair.name + " " + pair.value);
					first = false;
				}
				System.out.print("\t");
				first=true;
				for (Pair pair : event.effects) {
					System.out.print((first?"":", ")+pair.name + " " + pair.value);
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
