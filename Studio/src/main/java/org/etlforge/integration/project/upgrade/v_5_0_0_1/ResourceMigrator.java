package org.etlforge.integration.project.upgrade.v_5_0_0_1;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.project.upgrade.DefaultPreLoader;
                                                                                   
final class ResourceMigrator implements ProjectVersionHandler {
 public PreLoadingResult preLoading(String version,ProjectFile file,UpgradeStrategy strategy){
  DefaultPreLoader loader=new DefaultPreLoader(version,file,strategy);loader.run();return loader.getResult();
 }
 public boolean postLoading(IntegrationProject project,ProjectFile file){return true;}
}