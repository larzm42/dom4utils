import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class MonsterDumper {
	
	public static void main(String[] args) {
		List<String> stack = new ArrayList<String>();
		List<String> stackDesc = new ArrayList<String>();
		Map<Integer, String> monsters = new HashMap<Integer, String>();
		
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("Dominions4.exe");

			byte[] b = new byte[32];
			byte[] c = new byte[2];

			stream.skip(Starts.MONSTER);
			int id = 1;
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
				monsters.put(id, name.toString().toUpperCase());
				id++;
				stream.skip(218);
			}
			stream.close();
			
			stream = new FileInputStream("Dominions4.exe");
			b = new byte[1];
			boolean isName = false;
			stream.skip(Starts.MONSTER_DESC);
			while (true) {
				while (stream.read(b) != -1) {
					if (b[0] != 0) {
						break;
					}
				}
				StringBuffer name = new StringBuffer();
				//stream.read(b);
				if (new String(new byte[] {b[0]}).equals(":")) {
					isName = true;
				} else {
					isName = false;
				}
				if (isName) {
					while (stream.read(b) != -1) {
						if (b[0] != 0) {
							name.append(new String(new byte[] {b[0]}));
						} else {
							break;
						}
					}
					if (name.toString().equals("Fire")) {
						break;
					}

					while (stream.read(b) != -1) {
						if (b[0] != 0) {
							break;
						}
					}
				} else {
					if (stack.size() > 0) {
						name.append(stack.remove(0));
					} else {
						StringBuffer stackedDesc = new StringBuffer();
						while (stream.read(b) != -1) {
							if (b[0] != 0) {
								stackedDesc.append(new String(new byte[] {b[0]}));
							} else {
								break;
							}
						}
						stackDesc.add(stackedDesc.toString());
						while (stream.read(b) != -1) {
							if (b[0] != 0) {
								break;
							}
						}
					}
				}

				StringBuffer desc = new StringBuffer();
				int thisStack = 0;
				if (new String(new byte[] {b[0]}).equals(":") && stackDesc.size()>0) {
					desc.append(stackDesc.remove(0));
				} else {
					while (new String(new byte[] {b[0]}).equals(":")) {
						StringBuffer stackedName = new StringBuffer();
						while (stream.read(b) != -1) {
							if (b[0] != 0) {
								stackedName.append(new String(new byte[] {b[0]}));
							} else {
								break;
							}
						}
						stack.add(stackedName.toString());
						thisStack++;
						while (stream.read(b) != -1) {
							if (b[0] != 0) {
								break;
							}
						}
					}
					desc.append(new String(new byte[] {b[0]}));
				}
				while (stream.read(b) != -1) {
					if (b[0] != 0) {
						desc.append(new String(new byte[] {b[0]}));
					} else {
						break;
					}
				}
				//id = 0;
				do {
				List<Integer> ids = new ArrayList<Integer>();
				if (name.toString().startsWith("mon ")) {
					ids.add(Integer.valueOf(name.substring(4)));
				} else if (name.toString().startsWith("mon")) {
					ids.add(Integer.valueOf(name.substring(3)));
				} else {
					try {
						Set<Entry<Integer, String>> entrySet = monsters.entrySet();
						for (Map.Entry<Integer, String> entry : entrySet) {
							if (entry.getValue().equals(name.toString().toUpperCase())) {
								ids.add(entry.getKey());
							}
						}
						//id = monsters.(name.toString().toUpperCase());
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (ids.size() == 0) {
					System.err.println("FAILED");
					System.out.println("Name: " + name.toString() + " Filename: " + String.format("%04d", 0) + ".txt\n" + desc.toString());
				}
//				System.out.println("Name: " + name.toString() + " Filename: " + String.format("%04d", id) + ".txt\n" + desc.toString());
				for (int idToUse : ids) {
					File file = new File("monsters\\desc\\" + String.format("%04d", idToUse) + ".txt");
					FileOutputStream os = new FileOutputStream(file);
					os.write(desc.toString().getBytes());
					os.close();
				}
				if (thisStack > 0) {
					name = new StringBuffer(stack.get(stack.size()-thisStack));
				}
				thisStack--;
				} while (thisStack>-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}
}
