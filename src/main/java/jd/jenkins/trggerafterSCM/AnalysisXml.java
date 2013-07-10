package jd.jenkins.trggerafterSCM;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class AnalysisXml {
	
	/**
	 * 检查pom文件
	 * @param filePath
	 * @throws DocumentException
	 */
	public static void checkPom(String filePath){
		SAXReader sax = new SAXReader();
		Document doc;
		try {
			doc = sax.read(filePath);
		} catch (DocumentException e1) {
			throw new RuntimeException(e1.getMessage());
		}

		@SuppressWarnings("unchecked")
		List<Element> elemList = doc.selectNodes("//*[name()='build']/*[name()='pluginManagement']/*[name()='plugins']/*[name()='plugin']/*[name()='artifactId']");
		
		for(Element e : elemList){
			boolean flag = LegalPlugins.isLegal(e.getText().trim());
			if(!flag){
				throw new RuntimeException("[INFO] 非法的插件：'" + e.getText() +"' 在pom文件中被发现,编译将终止,请检查代码!");
			}
		}
	}
}
