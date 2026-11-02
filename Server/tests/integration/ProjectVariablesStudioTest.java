import java.util.*;
import java.lang.reflect.*;
import java.nio.file.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.*;
import org.etlforge.integration.data.script.de.*;
import org.etlforge.util.persist.*;
import org.etlforge.integration.client.ui.script.debug.ScriptDebugModel;
import org.etlforge.integration.debug.client.de.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl.DeployInterchangeModelWsImplWrapper;
public class ProjectVariablesStudioTest {
 static int checks;
 static void check(boolean value,String name){if(!value)throw new AssertionError(name);checks++;System.out.println("PASS "+name);}
 static void reject(String name){try{ProjectVariable.validateName(name);throw new AssertionError("Accepted "+name);}catch(IllegalArgumentException expected){checks++;}}
 public static void main(String[] args)throws Exception{
  IntegrationProject project=IntegrationProject.createNewProject("Project variables verification"); project.setLocationOnDisk(new java.io.File("C:/ETL/Server/build-probe/project-variables-fixture"));
  ProjectVariable variable=new ProjectVariable("ApiBaseUrl");variable.setDefaultValue("  café 世界 😀\nhttps://example.invalid/\t  ");variable.setParent(project.getRootFolder(EntityType.ProjectVariable));
  check(project.getAllEntitiesOfClass(ProjectVariable.class).iterator().hasNext(),"project variable category and lookup");
  check(EntityType.fromId(35)==EntityType.ProjectVariable,"wire type identifier");
  check(variable.getID().getEntityType()==EntityType.ProjectVariable,"typed project variable identity");
  for(String name:new String[]{"$wrong","bad name","__system","etlforge.operation.name","jitterbit.test","bad/path",""})reject(name);
  for(String name:new String[]{"ApiBaseUrl","project.setting","_value","Name123"})ProjectVariable.validateName(name);
  for(String text:new String[]{"nul\0value","bad\ud800"}){try{ProjectVariable.validateValue(text);throw new AssertionError("Invalid value accepted");}catch(IllegalArgumentException expected){checks++;}}
  ProjectVariable copy=(ProjectVariable)variable.clone();check(copy.getDefaultValue().equals(variable.getDefaultValue()),"clone preserves Unicode and exact whitespace");
  XmlPersistor xml=XmlPersistor.newRoot("Entity");variable.writeTo(xml);Path file=Paths.get("C:/ETL/Server/build-probe/project-variable-roundtrip.xml");xml.store(file.toFile());
  ProjectVariable restored=new ProjectVariable(variable.getID(),"placeholder");restored.restoreFrom(XmlPersistor.load(file.toFile()));
  check(restored.getDefaultValue().equals(variable.getDefaultValue()),"save and reopen preserves exact preset");
  Script script=new Script("Read variables");script.setScript("<trans>$ApiBaseUrl</trans>");script.setParent(project.getRootFolder(EntityType.Script));
  IntegrationProject partial=IntegrationProject.createPartialProject(project,Collections.singleton(script));
  check(partial.getAllEntitiesOfClass(ProjectVariable.class).iterator().hasNext(),"operation/script partial deployments include project defaults");
  DefaultGlobalDataElements globals=new DefaultGlobalDataElements();globals.scanProject(project);check(globals.getAllDataElements().contains("ApiBaseUrl"),"preset names available in script variable browser");
  ScriptDebugModel debug=new ScriptDebugModel(new org.etlforge.integration.debug.client.DebugService(),script.getID());debug.setProject(project);
  Method defaults=ScriptDebugModel.class.getDeclaredMethod("projectDefaults",Collection.class);defaults.setAccessible(true);
  Collection<DataElement> seeded=(Collection<DataElement>)defaults.invoke(debug,Collections.emptyList());
  check(seeded.iterator().next().getValue().equals(variable.getDefaultValue()),"Studio Test receives preset values");
  DataElement override=new DataElement("ApiBaseUrl",DataElementScope.GLOBAL,"string");override.setValue("override");
  seeded=(Collection<DataElement>)defaults.invoke(debug,Collections.singleton(override));check(seeded.size()==1&&seeded.iterator().next().getValue().equals("override"),"explicit Test override takes precedence");
  DeployInterchangeModelWsImplWrapper wrapper=new DeployInterchangeModelWsImplWrapper(null,null);
  Method update=wrapper.getClass().getDeclaredMethod("updateEntity",IntegrationEntity.class);update.setAccessible(true);update.invoke(wrapper,variable);
  Field rows=wrapper.getClass().getDeclaredField("m_extendedEntityProperties");rows.setAccessible(true);
  check(((List<?>)rows.get(wrapper)).size()==4,"Studio serializes preset, name, password mode and description for native deployment");
  check(org.etlforge.integration.uidelegate.entity.EntityIcons.forType(EntityType.ProjectVariable).getDefaultIcon(org.etlforge.integration.uidelegate.entity.EntityIcons.Size.SMALL)!=null,"project variable tree icon");
  System.out.println("Passed "+checks+" Studio project variable checks");
 }
}
