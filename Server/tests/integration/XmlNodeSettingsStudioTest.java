import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.ui.interchange.entity.*;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.*;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.*;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.*;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.*;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
public class XmlNodeSettingsStudioTest {
 static Path report=Paths.get("C:/ETL/Reports/xml-node-settings");static int passed;static TreeMapper mapper;static Throwable timerError;
 interface Work{void run()throws Exception;}
 static void edt(Work w)throws Exception{Throwable[] e={null};SwingUtilities.invokeAndWait(()->{try{w.run();}catch(Throwable t){e[0]=t;}});if(e[0]!=null)throw new Exception(e[0]);}
 static void ok(boolean b,String text){if(!b)throw new AssertionError(text);System.out.println("PASS "+text);passed++;}
 static java.util.List<Component> parts(Container c){java.util.List<Component> r=new ArrayList<>();for(Component p:c.getComponents()){r.add(p);if(p instanceof Container)r.addAll(parts((Container)p));}return r;}
 static Node find(Node node,String name){if(node==null)return null;if(node.getCROM().getName().equals(name)&&node.getCROM().isElementNode())return node;for(Node c:node.getChildren()){Node n=find(c,name);if(n!=null)return n;}return null;}
 static void waitTree()throws Exception{for(int i=0;i<300;i++){boolean[] b={false};edt(()->b[0]=mapper!=null&&find(mapper.m_sourceTreeComponent.m_root,"Customer")!=null);if(b[0])return;Thread.sleep(100);}throw new AssertionError("Tree timeout");}
 static void dialog(boolean accept)throws Exception{dialog(accept,false);}
 static void dialog(boolean accept,boolean target)throws Exception{
  edt(()->{
   javax.swing.Timer timer=new javax.swing.Timer(1000,e->{try{
    for(Window w:Window.getWindows())if(w.isVisible()&&w instanceof JDialog&&((JDialog)w).getTitle().equals("XML Node Settings")){
     for(Component p:parts(w))if(p instanceof JComboBox){JComboBox box=(JComboBox)p;if(box.getItemCount()==5)box.setSelectedIndex(3);}
     BufferedImage image=new BufferedImage(w.getWidth(),w.getHeight(),BufferedImage.TYPE_INT_RGB);w.paint(image.createGraphics());javax.imageio.ImageIO.write(image,"png",report.resolve("node-settings.png").toFile());
     for(Component p:parts(w))if(p instanceof JButton&&((JButton)p).getText().equals(accept?"OK":"Cancel")){((JButton)p).doClick();return;}
     throw new AssertionError("Missing dialog button");
    }throw new AssertionError("No node settings dialog");
   }catch(Throwable t){timerError=t;for(Window w:Window.getWindows())if(w instanceof JDialog&&w.isVisible())w.dispose();}});timer.setRepeats(false);timer.start();
   org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent tree=target?mapper.m_targetTreeComponent:mapper.m_sourceTreeComponent;XmlNodeSettings.show(tree,find(tree.m_root,"Customer"));
  });if(timerError!=null)throw new Exception(timerError);
 }
 public static void main(String[]args){try{run();System.exit(0);}catch(Throwable e){e.printStackTrace(System.out);System.exit(1);}}
 static void run()throws Exception{
  Files.createDirectories(report);Path fixture=Files.createTempDirectory(report,"studio-");
  IntegrationMain.main(new String[]{"-d",fixture.resolve("workspace").toString()});ClientPlatform.waitForInitialization(60,java.util.concurrent.TimeUnit.SECONDS);Thread.sleep(2500);edt(()->{});
  System.setOut(new PrintStream(report.resolve("studio-results.txt").toFile(),"UTF-8"));
  org.etlforge.integration.client.server.login.LoginService login=Application.getBean("platform.LoginService",org.etlforge.integration.client.server.login.LoginService.class);
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  login.login(new org.etlforge.integration.client.server.login.LoginCredentials(org.etlforge.integration.client.server.ServerAddressFactory.usingPorts(47100,47101),"localhost:47100","Admin",password,false,false));password=null;
  ProjectManager manager=Application.getBeansOfType(ProjectManager.class).get(0);ManagedProject[] managed={null};Transformation tx=new Transformation("Node editor verification");
  edt(()->{IntegrationProjectLock.acquire();try{IntegrationProject p=manager.createNewProject("XML node editor verification",fixture.resolve("project").toFile());manager.setCurrentProject(p);managed[0]=manager.getProject(p);p.insertEntityUnderRoot(tx);}finally{IntegrationProjectLock.release();}});
  ManagedProject project=managed[0];String schema="<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='Customers'><xs:complexType><xs:sequence><xs:element name='Customer' maxOccurs='unbounded'><xs:complexType><xs:sequence><xs:element name='Name' type='xs:string'/></xs:sequence></xs:complexType></xs:element></xs:sequence></xs:complexType></xs:element></xs:schema>";
  Path original=fixture.resolve("node-verification-"+System.nanoTime()+".xsd");Files.write(original,schema.getBytes(StandardCharsets.UTF_8));
  XsdUploadManager upload=new XsdUploadManager(IntegrationServer.getInstance().getServerCall(XmlInfoProvider.class),new XsdFileStore(project.getFileStore()));upload.upload(null,original.toString());ok(upload.isSuccess(),"original XSD uploads through Studio API");
  XmlStructure structure=new XmlStructure();structure.setFilePath(upload.getMainFile().getFilePath());structure.setRootNodeName("Customers");tx.setInputStructure(structure);tx.setOutputStructure(new XmlStructure(structure));tx.setMapList(Collections.singletonList("Customers$Customer.Name$\t<trans>Customers$Customer.Name$</trans>"));
  IntegrationEntityPage[] page={null};
  edt(()->{EntityConfig config=null;for(EntityConfig c:Application.getBeansOfType(EntityConfig.class))if(c.getType()==EntityType.Transformation)config=c;page[0]=config.getPageFactory().createPage(tx);Application.getBean("ui.AppWin",ApplicationWindow.class).getEditorService().openEditor(page[0],"Design");for(Component c:parts(((GenericEditor)page[0]).getMainPanel()))if(c instanceof TreeMapper)mapper=(TreeMapper)c;});
  waitTree();ok(mapper!=null,"real transformation tree loads");
  edt(()->ok(XmlNodeSettings.supports(mapper.m_sourceTreeComponent,find(mapper.m_sourceTreeComponent.m_root,"Customer")),"source context menu supports XML node settings"));
  String before=((XmlStructure)tx.getInputStructure()).getFilePath();int errors=ErrorLog.getNumberOfErrors();
  dialog(false);ok(((XmlStructure)tx.getInputStructure()).getFilePath().equals(before),"Cancel preserves original schema reference");
  dialog(true);
  boolean done=false;for(int i=0;i<600;i++){if(!((XmlStructure)tx.getInputStructure()).getFilePath().equals(before)){done=true;break;}Thread.sleep(100);}ok(done,"OK uploads schema and updates transformation");edt(()->{});Thread.sleep(1000);edt(()->{});
  String after=((XmlStructure)tx.getInputStructure()).getFilePath();ok(!after.equals(before),"changed side references a new XSD copy");ok(((XmlStructure)tx.getOutputStructure()).getFilePath().equals(before),"other transformation side retains original XSD");
  ok(new String(Files.readAllBytes(project.getFileStore().getFile(before).getPhysicalRepresentation().toPath()),StandardCharsets.UTF_8).equals(schema),"original project XSD remains unchanged");
  edt(()->{Node customer=find(mapper.m_sourceTreeComponent.m_root,"Customer");ok(customer.getCROM().isLoopNode()&&!customer.getCROM().isAtLeastOneNode(),"tree now displays zero-or-more Customer");});
  dialog(true,true);boolean targetDone=false;for(int i=0;i<600;i++){if(!((XmlStructure)tx.getOutputStructure()).getFilePath().equals(before)){targetDone=true;break;}Thread.sleep(100);}ok(targetDone,"target side XML settings update independently");Thread.sleep(700);edt(()->{});
  boolean[] refreshed={false};edt(()->mapper.refreshTreeStructures(new RefreshStructuresCallback(){public void done(){refreshed[0]=true;}public void cancelled(){}public org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingErrorHandler getMappingErrorHandler(){return org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingErrorHandler.LOGGER;}}));
  for(int i=0;i<300&&!refreshed[0];i++){Thread.sleep(100);edt(()->{});}ok(refreshed[0],"explicit server refresh completes");
  edt(()->ok(!find(mapper.m_sourceTreeComponent.m_root,"Customer").getCROM().isAtLeastOneNode(),"refresh keeps edited occurrence"));
  edt(()->{IntegrationProjectLock.acquire();try{mapper.saveDirtyTreeStructures();project.saveToDisk();}finally{IntegrationProjectLock.release();}});ok(tx.getMapList().size()==1&&tx.getMapList().get(0).contains("Customers$Customer.Name$"),"existing mapping retained");ok(project.getFileStore().getFile(after)!=null,"new XSD persists in project file store");IntegrationProjectLock.acquire();try{ManagedProject saved=manager.inspect(project.getProjectFile().getFile().getName().replaceFirst("\\.jip$",""),project.getLocation());try{Transformation savedTx=(Transformation)saved.getProject().getAllEntitiesOfType(EntityType.Transformation).get(0);ok(((XmlStructure)savedTx.getInputStructure()).getFilePath().equals(after),"saved project reload retains new schema reference");ok(savedTx.getMapList().size()==1,"saved project reload retains mapping");}finally{manager.closeProject(saved);}}finally{IntegrationProjectLock.release();}ok(ErrorLog.getNumberOfErrors()==errors,"no new Studio errors");
  System.out.println("PASSED "+passed+" checks; project="+project.getLocation());
  edt(()->{page[0].setClean();Application.getBean("ui.AppWin",ApplicationWindow.class).getEditorService().closeEditor(page[0]);for(Window w:Window.getWindows())w.dispose();});
 }
}
