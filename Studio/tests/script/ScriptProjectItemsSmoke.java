import java.util.*;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.client.ui.script.builder.DefaultProjectItemTreeModelBuilder;
public final class ScriptProjectItemsSmoke {
 public static void main(String[] args) {
  List<EntityType> types=new ArrayList<>(Arrays.asList(EntityType.Source, EntityType.Target, EntityType.Operation, EntityType.Script, EntityType.EmailMessage, EntityType.SalesforceConnector));
  types.remove(EntityType.AutoDeskEndpoint); types.remove(EntityType.NetSuiteEndpoint);
  IntegrationProject project=IntegrationProject.createNewProject("Script editor regression", types);
  if(project.getRootFolder(EntityType.AutoDeskEndpoint)!=null || project.getRootFolder(EntityType.NetSuiteEndpoint)!=null)throw new AssertionError("fixture");
  new DefaultProjectItemTreeModelBuilder().buildModel(project);
  types.remove(EntityType.SalesforceConnector);
  new DefaultProjectItemTreeModelBuilder().buildModel(IntegrationProject.createNewProject("No optional connectors", types));
  System.out.println("PASS: script project-items tree builds without removed or optional connector folders");
 }
}

