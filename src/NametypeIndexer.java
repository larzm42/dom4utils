/* This file is part of dom4utils.
 *
 * dom4utils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dom4utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dom4utils.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		run();
	}
	
	public static void run() {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		FileInputStream stream = null;
		try {
			stream = new FileInputStream("Dominions4.exe");
			
			InputStreamReader isr = new InputStreamReader(stream, "ISO-8859-1");
	        Reader in = new BufferedReader(isr);
	        int ch;

			stream.skip(Starts.NAMES);
			int id = 100;
			System.out.println("100");
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
					id ++;
					if (id > Starts.NAMES_COUNT) { break;}
					System.out.println(id +"");
					continue;
				}
				
				List<String> list = map.get(id);
				if (list == null) {
					list = new ArrayList<String>();
					map.put(id, list);
				}
				list.add(name.toString());
				System.out.println(name);
			}
			
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
