package jd.jenkins.trggerafterSCM;

import java.io.IOException;
import java.io.PrintStream;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;

public class AfterSCM extends BuildWrapper{

	@Override
	public Environment setUp(AbstractBuild build, Launcher launcher,
			BuildListener listener) throws IOException, InterruptedException {
		Environment environment = new Environment() { };
		PrintStream logger = listener.getLogger();
		FilePath ws = build.getWorkspace();

		try{
			/*
			 * 提取最外层pom文件-主要针对单一maven model的情况
			 */
			FilePath [] paths = ws.list("pom.xml");
			for(int i = 0 ,len = paths.length; i < len ; i++){
				AnalysisXml.checkPom(paths[i].getRemote());
			}
			
			/*
			 * 提取内层pom文件-针对多model情况
			 */
			paths = ws.list("*/pom.xml");
			for(int i = 0 ,len = paths.length; i < len ; i++){
				AnalysisXml.checkPom(paths[i].getRemote());
			}
			
		}catch(Exception e){
			logger.println(e.getMessage());
			build.setResult(Result.FAILURE);
			throw new InterruptedException("Interrupt the process!");
		}
		
		return environment;
	}
	
	@Extension
	public static final class DescriptorImpl extends BuildWrapperDescriptor {

		@Override
		public boolean isApplicable(AbstractProject<?, ?> item) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Check Pom Files";
		}
	}
	
}
