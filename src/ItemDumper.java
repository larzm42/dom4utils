import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ItemDumper {
	
	public static void main(String[] args) {
		List<String> stack = new ArrayList<String>();
		List<String> stackDesc = new ArrayList<String>();
		
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("Dominions4.exe");
			
			byte[] b = new byte[1];
			boolean isName = false;
			stream.skip(Starts.ITEM_DESC);
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
					if (name.toString().startsWith("era")) {
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
				System.out.println("Name: " + name.toString() + "\n" + desc.toString());
				File file = new File("items\\desc\\" + name.toString().replaceAll(" ", "") + ".txt");
				FileOutputStream os = new FileOutputStream(file);
				os.write(desc.toString().getBytes());
				os.close();
			}
		} catch (Exception e) {
			
		}
		System.out.println("end");
	}
}
