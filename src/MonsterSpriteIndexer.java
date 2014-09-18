import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;


public class MonsterSpriteIndexer {
	
	static Map<String, List<String>> indexToInt = new HashMap<String, List<String>>();

	public static void main(String[] args) {
		indexToInt.put("sea", new ArrayList<String>(Arrays.asList(new String[]{"03", "04"})));
		indexToInt.put("gods", new ArrayList<String>(Arrays.asList(new String[]{"07", "08", "09"})));
		indexToInt.put("arco 1", new ArrayList<String>(Arrays.asList(new String[]{"0B"})));
		indexToInt.put("arco 2", new ArrayList<String>(Arrays.asList(new String[]{"0F"})));
		indexToInt.put("arco 3", new ArrayList<String>(Arrays.asList(new String[]{"13"})));
		indexToInt.put("erm 1", new ArrayList<String>(Arrays.asList(new String[]{"17"})));
		indexToInt.put("ermor dead", new ArrayList<String>(Arrays.asList(new String[]{"1B"})));
		indexToInt.put("scel 2", new ArrayList<String>(Arrays.asList(new String[]{"1F"})));
		indexToInt.put("pyth 2", new ArrayList<String>(Arrays.asList(new String[]{"27"})));
		indexToInt.put("pyth 3", new ArrayList<String>(Arrays.asList(new String[]{"2A", "2B"})));
		indexToInt.put("ulm 1", new ArrayList<String>(Arrays.asList(new String[]{"2E", "2F"})));
		indexToInt.put("ulm 2", new ArrayList<String>(Arrays.asList(new String[]{"32", "33"})));
		indexToInt.put("ulm 3", new ArrayList<String>(Arrays.asList(new String[]{"36"})));
		indexToInt.put("marv 1", new ArrayList<String>(Arrays.asList(new String[]{"3A"})));
		indexToInt.put("mar 2", new ArrayList<String>(Arrays.asList(new String[]{"3E"})));
		indexToInt.put("mar 3", new ArrayList<String>(Arrays.asList(new String[]{"42"})));
		indexToInt.put("macha 1", new ArrayList<String>(Arrays.asList(new String[]{"46"})));
		indexToInt.put("macha 2", new ArrayList<String>(Arrays.asList(new String[]{"4A"})));
		indexToInt.put("tir 1", new ArrayList<String>(Arrays.asList(new String[]{"52"})));
		indexToInt.put("fom 1", new ArrayList<String>(Arrays.asList(new String[]{"55", "56"})));
		indexToInt.put("eriu 2", new ArrayList<String>(Arrays.asList(new String[]{"59", "5A"})));
		indexToInt.put("man 2", new ArrayList<String>(Arrays.asList(new String[]{"5D"})));
		indexToInt.put("man 3", new ArrayList<String>(Arrays.asList(new String[]{"61"})));
		indexToInt.put("tien 1", new ArrayList<String>(Arrays.asList(new String[]{"65"})));
		indexToInt.put("tien 2", new ArrayList<String>(Arrays.asList(new String[]{"69"})));
		indexToInt.put("tien 3", new ArrayList<String>(Arrays.asList(new String[]{"6D"})));
		indexToInt.put("yomi 1", new ArrayList<String>(Arrays.asList(new String[]{"71"})));
		indexToInt.put("shinu 2", new ArrayList<String>(Arrays.asList(new String[]{"75"})));
		indexToInt.put("jomon 3", new ArrayList<String>(Arrays.asList(new String[]{"79"})));
		indexToInt.put("van 1", new ArrayList<String>(Arrays.asList(new String[]{"7D"})));
		indexToInt.put("van 1", new ArrayList<String>(Arrays.asList(new String[]{"7D"})));
		indexToInt.put("van 2", new ArrayList<String>(Arrays.asList(new String[]{"84"})));
		indexToInt.put("mid 3", new ArrayList<String>(Arrays.asList(new String[]{"88"})));
		indexToInt.put("niefel 1", new ArrayList<String>(Arrays.asList(new String[]{"8C"})));
		indexToInt.put("jotun 2", new ArrayList<String>(Arrays.asList(new String[]{"90"})));
		indexToInt.put("utg 3", new ArrayList<String>(Arrays.asList(new String[]{"94"})));
		indexToInt.put("rus 2", new ArrayList<String>(Arrays.asList(new String[]{"9C"})));
		indexToInt.put("rus 3", new ArrayList<String>(Arrays.asList(new String[]{"A0"})));
		indexToInt.put("mict 1", new ArrayList<String>(Arrays.asList(new String[]{"A4"})));
		indexToInt.put("mict 2", new ArrayList<String>(Arrays.asList(new String[]{"A7", "A8"})));
		indexToInt.put("mict 3", new ArrayList<String>(Arrays.asList(new String[]{"A7", "AB"})));
		indexToInt.put("aby 1", new ArrayList<String>(Arrays.asList(new String[]{"AF", "B0"})));
		indexToInt.put("aby 2", new ArrayList<String>(Arrays.asList(new String[]{"B3"})));
		indexToInt.put("aby 3", new ArrayList<String>(Arrays.asList(new String[]{"B7"})));
		indexToInt.put("ctis 1", new ArrayList<String>(Arrays.asList(new String[]{"BB"})));
		indexToInt.put("ctis 2", new ArrayList<String>(Arrays.asList(new String[]{"BF"})));
		indexToInt.put("ctis 3", new ArrayList<String>(Arrays.asList(new String[]{"C3"})));
		indexToInt.put("pan 1", new ArrayList<String>(Arrays.asList(new String[]{"C7"})));
		indexToInt.put("pan 2", new ArrayList<String>(Arrays.asList(new String[]{"CB"})));
		indexToInt.put("pan 3", new ArrayList<String>(Arrays.asList(new String[]{"CF"})));
		indexToInt.put("cael 1", new ArrayList<String>(Arrays.asList(new String[]{"D2", "D3"})));
		indexToInt.put("cael 2", new ArrayList<String>(Arrays.asList(new String[]{"D6", "D7"})));
		indexToInt.put("cael 3", new ArrayList<String>(Arrays.asList(new String[]{"DA"})));
		indexToInt.put("aga 1", new ArrayList<String>(Arrays.asList(new String[]{"DE", "DF"})));
		indexToInt.put("aga 2", new ArrayList<String>(Arrays.asList(new String[]{"E2"})));
		indexToInt.put("aga 3", new ArrayList<String>(Arrays.asList(new String[]{"E6"})));
		indexToInt.put("kailasa 1", new ArrayList<String>(Arrays.asList(new String[]{"EA"})));
		indexToInt.put("lanka 1", new ArrayList<String>(Arrays.asList(new String[]{"EE"})));
		indexToInt.put("bandar 2", new ArrayList<String>(Arrays.asList(new String[]{"F2"})));
		indexToInt.put("patala 3", new ArrayList<String>(Arrays.asList(new String[]{"F6"})));
		indexToInt.put("hinnom 1", new ArrayList<String>(Arrays.asList(new String[]{"FA"})));
		indexToInt.put("ashdod 2", new ArrayList<String>(Arrays.asList(new String[]{"FD", "FE"})));
		indexToInt.put("ur", new ArrayList<String>(Arrays.asList(new String[]{"05", "06"})));
		indexToInt.put("asp 2", new ArrayList<String>(Arrays.asList(new String[]{"11"})));
		indexToInt.put("lem 3", new ArrayList<String>(Arrays.asList(new String[]{"15"})));
		indexToInt.put("berytos", new ArrayList<String>(Arrays.asList(new String[]{"1D"})));
		indexToInt.put("atl 1", new ArrayList<String>(Arrays.asList(new String[]{"28", "29"})));
		indexToInt.put("atl 2", new ArrayList<String>(Arrays.asList(new String[]{"2C", "2D"})));
		indexToInt.put("atl 3", new ArrayList<String>(Arrays.asList(new String[]{"30"})));
		indexToInt.put("rylle 1", new ArrayList<String>(Arrays.asList(new String[]{"34"})));
		indexToInt.put("rylle 2", new ArrayList<String>(Arrays.asList(new String[]{"38"})));
		indexToInt.put("ryll 3", new ArrayList<String>(Arrays.asList(new String[]{"3C"})));
		indexToInt.put("ocean 1", new ArrayList<String>(Arrays.asList(new String[]{"40"})));
		indexToInt.put("ocean 2", new ArrayList<String>(Arrays.asList(new String[]{"44"})));
		indexToInt.put("pelag 1", new ArrayList<String>(Arrays.asList(new String[]{"4C"})));
		indexToInt.put("pelag 1", new ArrayList<String>(Arrays.asList(new String[]{"4C"})));
		indexToInt.put("fire", new ArrayList<String>(Arrays.asList(new String[]{"57"})));
		indexToInt.put("earth", new ArrayList<String>(Arrays.asList(new String[]{"5B"})));
		indexToInt.put("water", new ArrayList<String>(Arrays.asList(new String[]{"5F"})));
		indexToInt.put("air", new ArrayList<String>(Arrays.asList(new String[]{"63"})));
		indexToInt.put("nature", new ArrayList<String>(Arrays.asList(new String[]{"67"})));
		indexToInt.put("death", new ArrayList<String>(Arrays.asList(new String[]{"6B", "6C"})));
		indexToInt.put("astral", new ArrayList<String>(Arrays.asList(new String[]{"6F"})));
		indexToInt.put("blood", new ArrayList<String>(Arrays.asList(new String[]{"73"})));
		indexToInt.put("misc", new ArrayList<String>(Arrays.asList(new String[]{"77"})));
		indexToInt.put("hob 1", new ArrayList<String>(Arrays.asList(new String[]{"7E"})));
		indexToInt.put("rag 3", new ArrayList<String>(Arrays.asList(new String[]{"86", "87"})));
		/*
		macha 3: 1975
		rus 1: 3047
		gath 3: 4845
		ur 2: 5005
		empty: 5023
		muspel: 5173
		xib 1: 5236
		xib 3: 5282
		ocean 3: 5838
		pelag 3: 5968
		hob 1: 6785
		hob 2: 6786
		hob 3: 6827
		naz: 6942
		empty: 7280
		empty: 7286
		oklara: 7292*/
	
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("monster.trs");
			stream.skip(Starts.MONSTER_TRS_INDEX);

			SortedMap<Integer, String> indexes1 = new TreeMap<Integer, String>();
			int index1 = 0;
			while (index1 != -1) {
				byte[] d = new byte[4];
				stream.read(d, 0, 4);
				String high1 = String.format("%02X", d[3]);
				String low1 = String.format("%02X", d[2]);
				String high = String.format("%02X", d[1]);
				String low = String.format("%02X", d[0]);
				index1 = new BigInteger(low + high + low1 + high1, 16).intValue();
				if (index1 == -1) break;
				
				StringBuffer buffer = new StringBuffer();
				byte[] b = new byte[1];
				while (stream.read(b) != -1) {
					if (b[0] != 0) {
						buffer.append(new String(new byte[] {b[0]}));
					} else {
						break;
					}
				}
				indexes1.put(index1, buffer.toString());
			}
			stream.close();
			
			for (Map.Entry<Integer, String> entry : indexes1.entrySet()) {
				System.out.println(entry.getValue() + ": " + entry.getKey());
			}
			
			Map<String, List<String>> map = new HashMap<String, List<String>>();
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
					
					id++;
					stream.skip(218);
				}
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
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for (Map.Entry<Integer, String> entry : indexes1.entrySet()) {
				List<String> mappings = indexToInt.get(entry.getValue());
				if (mappings != null) {
					boolean first = true;
					int groupNegativeOffset = 0;
					int groupPositiveOffset = 0;
					for (String group : mappings) {
						List<String> list = map.get(group);
						SortedSet<SortedByOffset> sortedSet = new TreeSet<SortedByOffset>();
						for (String myList : list) {
							if (myList.indexOf("smeg") != -1) continue;
							sortedSet.add(new SortedByOffset(myList));
						}
						if (first) {
							SortedByOffset sortedByOffset = sortedSet.first();
							groupNegativeOffset = sortedByOffset.getIntValue();
							groupPositiveOffset = entry.getKey();
							first = false;
						}
						int tweak = 0;
						if (entry.getValue().equals("man 3")) {
							tweak = 2;
						}
						if (entry.getValue().equals("misc")) {
							tweak = 2;
						}
						if (entry.getValue().equals("hob 1")) {
							tweak = 5;
						}
						
						for (SortedByOffset ugh : sortedSet) {
							System.out.println(ugh.value + ": " + (groupPositiveOffset - groupNegativeOffset + ugh.getIntValue()+2+tweak));
						}
					}
				}
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class SortedByOffset implements Comparable<SortedByOffset> {
	public String value;
	public SortedByOffset(String value) {
		this.value = value;
	}
	public Integer getIntValue() {
		Stack<String> stack = new Stack<String>();
		StringTokenizer tok = new StringTokenizer(value);
		while (tok.hasMoreTokens()) {
			stack.push(tok.nextToken());
		}
		return Integer.decode("0X" + stack.pop() + stack.pop());
	}
	@Override
	public int compareTo(SortedByOffset o) {
		return this.getIntValue().compareTo(o.getIntValue());
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SortedByOffset other = (SortedByOffset) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}

