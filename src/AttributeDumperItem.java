import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class AttributeDumperItem {
	private static String[] KNOWN_ITEM_ATTRS = {
		"C600", // Fireres
		"C900", // Coldres
		"C800", // Poisonres
		"C700", // Shockres
		"9D00", // Leadership
		"9700", // str
		"C501", // fixforge
		"9E00", // magic leadership
		"9F00", // undead leadership
		"7001", // inspirational leadership
		"3401", // morale
		"A100", // penetration
		"8300", // pillage
		"B700", // fear
		"A000", // mr
		"0601", // taint
		"7500", // reinvigoration
		"6900", // awe
		"8500", // autospell
		"0A00", // F
		"0B00", // A
		"0C00", // W
		"0D00", // E
		"0E00", // S
		"0F00", // D
		"1000", // N
		"1100", // B
		"1200", // H
		"1400", // elemental
		"1500", // sorcery
		"1600", // all
		"1700", // elemental range
		"1800", // sorcery range
		"1900", // all range
		"2800", // fire ritual range
		"2900", // air ritual range
		"2A00", // water ritual range
		"2B00", // earth ritual range
		"2C00", // astral ritual range
		"2D00", // death ritual range
		"2E00", // nature ritual range
		"2F00", // blood ritual range
		"1901", // darkvision
		//"CE01", // regeneration
		"6E00", // waterbreathing
		"8601", // stealthb
		"6C00", // stealth
		"9600", // att
		"7901", // def
		"9601", // woundfend
		//"1601", // restricted
};

	private static List<String> attrList = new ArrayList<String>();
	
	private static Map<String, Integer> Summaries = new HashMap<String, Integer>();
	
	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.ITEM);
			int i = 0;
			long numFound = 0;
			byte[] c = new byte[2];
			stream.skip(120);
			while ((stream.read(c, 0, 2)) != -1) {
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				int weapon = Integer.decode("0X" + high + low);
				if (weapon == 0) {
					stream.skip(18l - numFound*2l);
					System.out.print("id:" + (i+1));
					// Values
					for (int x = 0; x < numFound; x++) {
						byte[] d = new byte[4];
						stream.read(d, 0, 4);
						String high1 = String.format("%02X", d[3]);
						String low1 = String.format("%02X", d[2]);
						high = String.format("%02X", d[1]);
						low = String.format("%02X", d[0]);
						if (!Arrays.asList(KNOWN_ITEM_ATTRS).contains(attrList.get(x))) {
							System.out.print("\n\t" + attrList.get(x) + ": ");
							System.out.print(new BigInteger(high1 + low1 + high + low, 16).intValue() + " ");
						}
					}

					System.out.println("");
					stream.skip(206l - 18l - numFound*4l);
					numFound = 0;
					i++;
					
					for (String ids : attrList) {
						Integer count = Summaries.get(ids);
						if (count == null) {
							Summaries.put(ids, Integer.valueOf(1));
						} else {
							Summaries.put(ids, Integer.valueOf(count.intValue()+1));
						}
					}
					attrList = new ArrayList<String>();
				} else {
					attrList.add(low + high);
					numFound++;
				}				
				if (i > 384) {
					break;
				}
			}
			
			Set<Entry<String, Integer>> set = Summaries.entrySet();
	        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
	        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
	        {
	            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
	            {
	                return (o2.getValue()).compareTo( o1.getValue() );
	            }
	        } );

			System.out.println("Summary:");
			for (Entry<String, Integer> entry : list) {
				if (!Arrays.asList(KNOWN_ITEM_ATTRS).contains(entry.getKey())) {
					System.out.println("id: " + entry.getKey() + " " + entry.getValue());
				}
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
