import java.lang.reflect.*;
import java.util.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.id.ProjectVariableId;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl.DeployInterchangeModelWsImplWrapper;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
public class ProjectVariableEditorLiveTest extends ProjectVariablesLiveTest {
 public static void main(String[] args)throws Exception{
 init();
 ProjectVariable v=new ProjectVariable(new ProjectVariableId("42480000-0000-4000-8000-000000000090"),"EditorPasswordFixture");v.setDefaultValue("test preset");v.setPassword(true);v.setDescription("Description café 世界 😀\nsecond line");
 if(args.length>0){
 org.etlforge.integration.client.ui.interchange.entity.projectvariable.ProjectVariablePage.Fields fields=new org.etlforge.integration.client.ui.interchange.entity.projectvariable.ProjectVariablePage.Fields(v);
 fields.password.doClick();fields.apply(v);
 if(v.isPassword() || !v.getDefaultValue().isEmpty())throw new AssertionError("Uncheck did not clear");
 }
 DeployInterchangeModelWsImplWrapper wrapper=new DeployInterchangeModelWsImplWrapper(null,null);
 Method update=wrapper.getClass().getDeclaredMethod("updateEntity",IntegrationEntity.class);update.setAccessible(true);update.invoke(wrapper,v);
 Field field=wrapper.getClass().getDeclaredField("m_extendedEntityProperties");field.setAccessible(true);
 List<InterchangeDataRow> rows=(List<InterchangeDataRow>)field.get(wrapper);
 InterchangeDataHolder data=data(1);data.setEntityProperties(new InterchangeDataTable(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},rows.toArray(new InterchangeDataRow[0])));deploy(data);
 System.out.println("PASS deployed password flag and Unicode description through Studio serializer");
 }
}
