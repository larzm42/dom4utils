import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NametypeIndexer {
	
	public static void main(String[] args) {

		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("Dominions4.exe");
			
			InputStreamReader isr = new InputStreamReader(stream, "ISO-8859-1");
	        Reader in = new BufferedReader(isr);
	        int ch;
			//byte[] b = new byte[1];
			//byte[] c = new byte[2];

			stream.skip(Starts.NAMES);
			int id = 100;
			System.out.println("100");
			while ((ch = in.read()) > -1) {
				//stream.skip(10);
				//stream.read(c, 0, 2);
				
				StringBuffer name = new StringBuffer();
				while (ch != 0) {
					name.append((char)ch);
					ch = in.read();
				}
				if (name.length() == 0) {
					continue;
				}
				if (name.toString().equals("end")) {
					id ++;
					if (id > Starts.NAMES_COUNT) { break;}
					System.out.println(id +"");
					continue;
				}
				
//				String index = String.format("%02X", c[1]);
//				String offset = String.format("%02X", c[0]);
				List<String> list = map.get(id);
				if (list == null) {
					list = new ArrayList<String>();
					map.put(id, list);
				}
				list.add(name.toString());
				System.out.println(name);
//				
//				int val = Integer.decode("0X" + index + offset);
//				String.format("%04d", val);
//				if (val > 0) {
//					String oldFileName1 = "item_" + String.format("%04d", val) + ".tga";
//					String newFileName1 = "item" + id + ".tga";
//
//					System.out.println(oldFileName1 + "->" + newFileName1);
//
//					Path old1 = Paths.get("items", oldFileName1);
//					Path new1 = Paths.get("items\\output\\", newFileName1);
//					try {
//						Files.copy(old1, new1);
//					} catch (NoSuchFileException e) {
//
//					}
//				} else {
//					System.err.println("FAILED");
//				}
//				
//				id++;
//				stream.skip(164);
			}
			
//			Set<Entry<Integer, List<String>>> entrySet = map.entrySet();
//			for (Entry<Integer, List<String>> entry : entrySet) {
//				if (entry.)
//			}
			in.close();
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
