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


public class AttributeDumper {
	private static String[] KNOWN_MONSTER_ATTRS = {
		"6C00", // stealthy
		"C900", // coldres
		"DC00", // cold
		"C600", // fireres
		"3C01", // heat
		"C800", // poisonres
		"6A00", // poisoncloud
		"CD01", // patience
		"AF00", // stormimmune
		"BD00", // regeneration
		"C200", // secondshape
		"C300", // firstshape
		"C100", // shapechange
		"C400", // secondtmpshape
		//"1C01", // maxage
		"CA00", // damagerev
		"9E01", // bloodvengeance
		"EB00", // nobadevents
		"E400", // bringeroffortune
		"1901", // darkvision
		"B700", // fear
		"1501", // voidsanity
		"6700", // standard
		"6E01", // formationfighter
		"6F01", // undisciplined
		"9801", // bodyguard
		"A400", // summon1
		"A500", // summon2
		"A600", // summon3
		"7001", // inspirational
		"8300", // pillagebonus
		"BE00", // berserk
		"F200", // startdom
		"F300", // pathcost
		"6F00", // waterbreathing
		"AA01", // realm
		"B401", // batstartsum1
		"B501", // batstartsum2
		"B601", // batstartsum3
		"B701", // batstartsum4
		"B801", // batstartsum5
		"B901", // batstartsum1d6
		"BA01", // batstartsum2d6
		"BB01", // batstartsum3d6
		"BC01", // batstartsum4d6
		"BD01", // batstartsum5d6
		"BE01", // batstartsum6d6
		"2501", // darkpower
		"AE00", // stormpower
		"B100", // firepower
		"B000", // coldpower
		"2501", // darkpower
		"A001", // chaospower
		"4401", // magicpower
		"EA00", // winterpower
		"E700", // springpower
		"E800", // summerpower
		"E900",	// fallpower
		"FB00", // nametype
		"B600",	// itemslots
		"0901", // ressize
		//"1D01", // startage
		"AB00", // blind
		"B200", // eyes
		"7A00", // supplybonus
		"7C01", // slave
		"7900", // researchbonus
		"CA01", // chaosrec
		"7D00", // siegebonus
		"D900", // ambidextrous
};

	private static List<String> attrList = new ArrayList<String>();
	
	private static Map<String, Integer> Summaries = new HashMap<String, Integer>();
	
	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("Dominions4.exe");			
			stream.skip(Starts.MONSTER);
			int i = 0;
			long numFound = 0;
			byte[] c = new byte[2];
			stream.skip(64);
			while ((stream.read(c, 0, 2)) != -1) {
				String high = String.format("%02X", c[1]);
				String low = String.format("%02X", c[0]);
				int weapon = Integer.decode("0X" + high + low);
				if (weapon == 0) {
					stream.skip(46l - numFound*2l);
					System.out.print("id:" + (i+1));
					// Values
					for (int x = 0; x < numFound; x++) {
						byte[] d = new byte[4];
						stream.read(d, 0, 4);
						String high1 = String.format("%02X", d[3]);
						String low1 = String.format("%02X", d[2]);
						high = String.format("%02X", d[1]);
						low = String.format("%02X", d[0]);
						if (!Arrays.asList(KNOWN_MONSTER_ATTRS).contains(attrList.get(x))) {
							System.out.print("\n\t" + attrList.get(x) + ": ");
							System.out.print(new BigInteger(high1 + low1 + high + low, 16).intValue() + " ");
						}
					}

					System.out.println("");
					stream.skip(254l - 46l - numFound*4l);
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
				if (i > 2559) {
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
				if (!Arrays.asList(KNOWN_MONSTER_ATTRS).contains(entry.getKey())) {
					System.out.println("id: " + entry.getKey() + " " + entry.getValue());
				}
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}