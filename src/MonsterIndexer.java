import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;


public class MonsterIndexer {
	static Map<String, Integer> indexToInt = new HashMap<String, Integer>();
	
	public static void main(String[] args) {
//		indexToInt.put("00", 0);
//		indexToInt.put("01", 256);
//		indexToInt.put("02", 2072);
//		indexToInt.put("03", 161);
//		indexToInt.put("04", 417);
//		indexToInt.put("05", 4921);
//		indexToInt.put("06", 5177);
//		indexToInt.put("07", 252);
		indexToInt.put("08", 508);
		indexToInt.put("09", 764);
//		indexToInt.put("0A", 5498);
//		indexToInt.put("0B", 896);
		
//		indexToInt.put("0C", 5346);
//		indexToInt.put("0F", 714);
//		indexToInt.put("11", 4830);
//		indexToInt.put("13", 790);
//		indexToInt.put("15", 4925);
//		indexToInt.put("17", 842);
//		indexToInt.put("1B", 937);
//		indexToInt.put("1D", 5052);
//		indexToInt.put("1F", 1017);
//		indexToInt.put("23", 1094);
//		indexToInt.put("27", 1219);
//		indexToInt.put("28", 4937);
//		indexToInt.put("29", 5193);
//		indexToInt.put("2A", 1060);
//		indexToInt.put("2B", 1316);
//		indexToInt.put("2C", 5028);
//		indexToInt.put("2D", 5284);
//		indexToInt.put("2E", 1163);
//		indexToInt.put("2F", 1419);
//		indexToInt.put("30", 5117);
//		indexToInt.put("32", 1240);
//		indexToInt.put("33", 1496);
//		indexToInt.put("34", 5218);
//		indexToInt.put("36", 1345);
//		indexToInt.put("38", 5311);
//		indexToInt.put("3A", 1426);
//		indexToInt.put("3C", 5408);
//		indexToInt.put("3E", 1523);
//		indexToInt.put("40", 5503);
//		indexToInt.put("42", 1610);
//		indexToInt.put("44", 5598);
//		indexToInt.put("46", 1705);
//		indexToInt.put("4A", 1798);
//		indexToInt.put("4C", 5709);
//		indexToInt.put("4F", 5542);
//		indexToInt.put("50", 5798);
//		indexToInt.put("52", 1935);
//		indexToInt.put("55", 1762);
//		indexToInt.put("56", 2018);
		indexToInt.put("57", 5778);
//		indexToInt.put("59", 1841);
//		indexToInt.put("5A", 2097);
		
//		indexToInt.put("5B", 6020);
//		indexToInt.put("5D", 2169);
		
//		indexToInt.put("5F", 5817);
//		indexToInt.put("61", 2001);
//		indexToInt.put("63", 5906);
//		indexToInt.put("65", 2078);
		indexToInt.put("67", 6094);
//		indexToInt.put("69", 2173);
//		indexToInt.put("6B", 6102);
//		indexToInt.put("6C", 6358);
//		indexToInt.put("6D", 2272);
//		indexToInt.put("6F", 6330);
//		indexToInt.put("71", 2333);
//		indexToInt.put("73", 6387);
//		indexToInt.put("75", 2446);
//		indexToInt.put("77", 6580);
//		indexToInt.put("79", 2537);
//		indexToInt.put("7D", 2662);
		
//		indexToInt.put("7E", 6741);
		
//		indexToInt.put("80", 2485);
//		indexToInt.put("81", 2741);
		
//		indexToInt.put("84", 2815);
		
		indexToInt.put("86", 6650);
		indexToInt.put("87", 6906);

//		indexToInt.put("88", 2629);
//		indexToInt.put("8C", 2684);
//		indexToInt.put("90", 2777);
//		indexToInt.put("94", 2880);
//		indexToInt.put("9C", 2949);
//		indexToInt.put("A0", 3024);
//		indexToInt.put("A4", 3163);
//		indexToInt.put("A7", 3044);
//		indexToInt.put("A8", 3300);
//		indexToInt.put("AB", 3108);
//		indexToInt.put("AF", 3160);
//		indexToInt.put("B0", 3416);
//		indexToInt.put("B3", 3249);
		indexToInt.put("B7", 3355);
//		indexToInt.put("BB", 3387);
//		indexToInt.put("BF", 3486);
//		indexToInt.put("C3", 3551);
//		indexToInt.put("C7", 3640);
//		indexToInt.put("CB", 3737);
//		indexToInt.put("CF", 3800);
		indexToInt.put("D2", 3648);
		indexToInt.put("D3", 3904);		
		indexToInt.put("D6", 3779);
		indexToInt.put("D7", 4084);
		indexToInt.put("DA", 3860);

//		indexToInt.put("DE", 3846);
//		indexToInt.put("DF", 4102);
//		indexToInt.put("E2", 3971);
//		indexToInt.put("E6", 4054);
		
//		indexToInt.put("EA", 4452);
		
//		indexToInt.put("EE", 4291);
//		indexToInt.put("F2", 4430);
//		indexToInt.put("F6", 4523);
//		indexToInt.put("FA", 4586);
//		indexToInt.put("FD", 4447);
//		indexToInt.put("FE", 4703);

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("Dominions4.exe");
			
			byte[] b = new byte[32];
			byte[] c = new byte[2];

			stream.skip(Starts.MONSTER);
			int id = 1;
			Set<String> indexes = new HashSet<String>();
			while (stream.read(b, 0, 32) != -1) {
				stream.skip(4);
				stream.read(c, 0, 2);
				
				StringBuffer name = new StringBuffer();
				for (int i = 0; i < 32; i++) {
					if (b[i] != 0) {
						name.append(new String(new byte[] {b[i]}));
					}
				}
				if (name.toString().equals("end")) {
					break;
				}
				String index = String.format("%02X", c[1]);
				String offset = String.format("%02X", c[0]);
				indexes.add(index);
				List<String> list = map.get(index);
				if (list == null) {
					list = new ArrayList<String>();
					map.put(index, list);
				}
				list.add(name + ": " + offset + " " + index);
				System.out.println(id + ":" + name + ": " + offset + " " + index);
				
				if (indexToInt.get(index) != null) {
					int indexedValue = 0;
					if (index.equals("01")) {
						if (Integer.decode("0X" + offset) < 128) {
							indexedValue = 256;
						} else {
							indexedValue = 4550;
						}
					} else {
						indexedValue = indexToInt.get(index).intValue();
					}
					int val = Integer.decode("0X" + offset) + indexedValue;
					String.format("%04d", val);
					if (val > 0) {
						String oldFileName1 = "monster_" + String.format("%04d", val) + ".tga";
						String oldFileName2 = "monster_" + String.format("%04d", ++val) + ".tga";
						String newFileName1 = String.format("%04d", id) + "_1.tga";
						String newFileName2 = String.format("%04d", id) + "_2.tga";
						
						System.out.println(oldFileName1 + "->" + newFileName1);
						System.out.println(oldFileName2 + "->" + newFileName2);

//						Path old1 = Paths.get("monsters", oldFileName1);
//						Path new1 = Paths.get("monsters\\output\\", newFileName1);
//						Path old2 = Paths.get("monsters", oldFileName2);
//						Path new2 = Paths.get("monsters\\output\\", newFileName2);
//						try {
//							Files.copy(old1, new1);
//						} catch (NoSuchFileException e) {
//							
//						}
//						try {
//							Files.copy(old2, new2);
//						} catch (NoSuchFileException e) {
//							
//						}
					} else {
						System.err.println("FAILED");
					}
				}
				
				id++;
				stream.skip(218);
			}
			/*indexes.remove("00");
			indexes.remove("01");
			indexes.remove("02");
			indexes.remove("03");
			indexes.remove("04");
			indexes.remove("05");
			indexes.remove("06");
			indexes.remove("07");
			indexes.remove("08");
			indexes.remove("09");
			indexes.remove("0A");
			indexes.remove("0B");
			indexes.remove("0C");
			indexes.remove("0F");
			indexes.remove("11");
			indexes.remove("13");
			indexes.remove("15");
			indexes.remove("17");
			indexes.remove("1B");
			indexes.remove("1D");
			indexes.remove("1F");
			indexes.remove("23");
			indexes.remove("27");
			indexes.remove("28");
			indexes.remove("29");
			indexes.remove("2A");
			indexes.remove("2B");
			indexes.remove("2C");
			indexes.remove("2D");
			indexes.remove("2E");
			indexes.remove("2F");
			indexes.remove("30");
			indexes.remove("32");
			indexes.remove("33");
			indexes.remove("34");
			indexes.remove("36");
			indexes.remove("38");
			indexes.remove("3A");
			indexes.remove("3C");
			indexes.remove("3E");
			indexes.remove("40");
			indexes.remove("42");
			indexes.remove("44");
			indexes.remove("46");
			indexes.remove("4A");
			indexes.remove("4C");
			indexes.remove("4F");
			indexes.remove("50");
			indexes.remove("52");
			indexes.remove("55");
			indexes.remove("56");
			indexes.remove("57");
			indexes.remove("59");
			indexes.remove("5A");
			indexes.remove("5B");
			indexes.remove("5D");
			indexes.remove("5F");
			indexes.remove("61");
			indexes.remove("63");
			indexes.remove("65");
			indexes.remove("67");
			indexes.remove("69");
			indexes.remove("6B");
			indexes.remove("6C");
			indexes.remove("6D");
			indexes.remove("6F");
			indexes.remove("71");
			indexes.remove("73");
			indexes.remove("75");
			indexes.remove("77");
			indexes.remove("79");
			indexes.remove("7D");
			indexes.remove("7E");
			indexes.remove("80");
			indexes.remove("81");
			indexes.remove("84");
			indexes.remove("86");
			indexes.remove("87");
			indexes.remove("88");
			indexes.remove("8C");
			indexes.remove("90");
			indexes.remove("94");
			indexes.remove("9C");
			indexes.remove("A0");
			indexes.remove("A4");
			indexes.remove("A7");
			indexes.remove("A8");
			indexes.remove("AB");
			indexes.remove("AF");
			indexes.remove("B0");
			indexes.remove("B3");
			indexes.remove("B7");
			indexes.remove("BB");
			indexes.remove("BF");
			indexes.remove("C3");
			indexes.remove("C7");
			indexes.remove("CB");
			indexes.remove("CF");
			indexes.remove("D2");
			indexes.remove("D3");
			indexes.remove("D6");
			indexes.remove("D7");
			indexes.remove("DA");
			indexes.remove("DE");
			indexes.remove("DF");
			indexes.remove("E2");
			indexes.remove("E6");
			indexes.remove("EA");
			indexes.remove("EE");
			indexes.remove("F2");
			indexes.remove("F6");
			indexes.remove("FA");
			indexes.remove("FD");
			indexes.remove("FE");*/
			TreeSet<String> sorted = new TreeSet<String>(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return Integer.decode("0X" + o1).compareTo(Integer.decode("0X" + o2));
				}
			});
			sorted.addAll(indexes);
			int things = 0;
			Iterator<String> iter = sorted.iterator();
			while (iter.hasNext()) {
				String ind = iter.next();
				System.out.println(ind);
				List<String> list = map.get(ind);
				SortedSet<SortedByOffset> sortedSet = new TreeSet<SortedByOffset>();
				for (String myList : list) {
					sortedSet.add(new SortedByOffset(myList));
				}
				for (SortedByOffset thing : sortedSet) {
					System.out.println("  " + thing.value);
					things++;
				}
			}
			System.out.println("------------------------");
			System.out.println("Indexes:" + indexes.size() + " Monsters:" + things);
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

/*class SortedByOffset implements Comparable<SortedByOffset> {
	public String value;
	public SortedByOffset(String value) {
		this.value = value;
	}
	@Override
	public int compareTo(SortedByOffset o) {
		Stack<String> stack = new Stack<String>();
		StringTokenizer tok = new StringTokenizer(value);
		while (tok.hasMoreTokens()) {
			stack.push(tok.nextToken());
		}
		Integer myValue = Integer.decode("0X" + stack.pop() + stack.pop());
		
		stack.clear();
		StringTokenizer tok2 = new StringTokenizer(o.value);
		while (tok2.hasMoreTokens()) {
			stack.push(tok2.nextToken());
		}
		Integer otherValue = Integer.decode("0X" + stack.pop() + stack.pop());
		return myValue.compareTo(otherValue);
	}
	
}*/

