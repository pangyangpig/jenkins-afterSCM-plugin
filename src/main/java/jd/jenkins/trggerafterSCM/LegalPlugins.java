package jd.jenkins.trggerafterSCM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class LegalPlugins {
	private static Set<String> pluginSet = new HashSet<String>();
	
	/**
	 * init
	 */
	static{
		BufferedReader reader = null;
		try {
		URL url = LegalPlugins.class.getClassLoader().getResource("legalplugins.ptxt");
		File file = new File(url.getFile());
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null){
				if(line.trim().length() > 0 && !line.trim().contains("#")){
					pluginSet.add(line.trim());
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("[ERROR]" + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("[ERROR]" + e.getMessage());
		} catch(ExceptionInInitializerError ex){
			throw new RuntimeException("[ERROR]" + ex.getMessage());
		}
		finally{
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	/**
	 * check the plugin
	 * @param pluginName
	 * @return
	 */
	public static boolean isLegal(String pluginName) {
		return pluginSet.contains(pluginName.trim());
	}
}
