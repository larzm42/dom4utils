import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemIndexer {
	
	public static void main(String[] args) {

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("Dominions4.exe");
			
			byte[] b = new byte[32];
			byte[] c = new byte[2];

			stream.skip(Starts.ITEM);
			int id = 1;
			while (stream.read(b, 0, 32) != -1) {
				stream.skip(10);
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
				List<String> list = map.get(index);
				if (list == null) {
					list = new ArrayList<String>();
					map.put(index, list);
				}
				list.add(name + ": " + offset + " " + index);
				System.out.println(id + ":" + name + ": " + offset + " " + index + "=" + Integer.decode("0X" + index + offset));
				
				int val = Integer.decode("0X" + index + offset);
				String.format("%04d", val);
				if (val > 0) {
					String oldFileName1 = "item_" + String.format("%04d", val) + ".tga";
					String newFileName1 = "item" + id + ".tga";

					System.out.println(oldFileName1 + "->" + newFileName1);

//					Path old1 = Paths.get("items", oldFileName1);
//					Path new1 = Paths.get("items\\output\\", newFileName1);
//					try {
//						Files.copy(old1, new1);
//					} catch (NoSuchFileException e) {
//
//					}
				} else {
					System.err.println("FAILED");
				}
				
				id++;
				stream.skip(164);
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
