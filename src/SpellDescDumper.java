import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class SpellDescDumper {
	
	public static void main(String[] args) {
		
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("Dominions4.exe");
			stream.skip(Starts.SPELL_DESC_INDEX);

			List<Integer> indexes = new ArrayList<Integer>();
			int index = -1;
			while (index != 0) {
				byte[] d = new byte[4];
				stream.read(d, 0, 4);
				String high1 = String.format("%02X", d[3]);
				String low1 = String.format("%02X", d[2]);
				String high = String.format("%02X", d[1]);
				String low = String.format("%02X", d[0]);
				index = new BigInteger(high1 + low1 + high + low, 16).intValue();
				if (index != 0) {
					indexes.add(index);
				}
			}
			stream.close();
			
			stream = new FileInputStream("Dominions4.exe");
			byte[] b = new byte[1];
			int firstIndex = 0x68a430;
			stream.skip(Starts.SPELL_DESC);
			List<String> names = new ArrayList<String>();
			String desc = null;
			for (Integer offset : indexes) {
				StringBuffer buffer = new StringBuffer();
				stream = new FileInputStream("Dominions4.exe");
				stream.skip(Starts.SPELL_DESC+offset-firstIndex);
				while (stream.read(b) != -1) {
					if (b[0] != 0) {
						buffer.append(new String(new byte[] {b[0]}));
					} else {
						break;
					}
				}
				System.out.println(buffer.toString());
				stream.close();
				
				if (buffer.toString().startsWith(":")) {
					names.add(buffer.toString().substring(1));
					desc = null;
				} else {
					desc = buffer.toString();
				}

				if (names.size() > 0 && desc != null) {
					for (String name : names) {
						File file = new File("spells\\" + name.replaceAll("[^a-zA-Z0-9\\-]", "") + ".txt");
						FileOutputStream os = new FileOutputStream(file);
						os.write(desc.getBytes());
						os.close();
					}
					names.clear();
					desc = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
