import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.*;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.ui.interchange.entity.*;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.*;
import org.etlforge.integration.data.location.*;

public final class StudioScreenLoadingTest {
 static ApplicationWindow win; static IntegrationProject project; static int passed,failed,index;
 static final File out=new File("C:/ETL/Reports/studio-screen-loading");
 interface Task {void run() throws Exception;}
 static void edt(Task task)throws Exception{final Throwable[] error={null};SwingUtilities.invokeAndWait(()->{try{task.run();}catch(Throwable e){error[0]=e;}});if(error[0]!=null)throw new Exception(error[0]);}
 static void check(String name,Task task)throws Exception {
  System.out.println("OPEN "+name);System.out.flush();int before=ErrorLog.getNumberOfErrors();
  try{task.run();Thread.sleep(700);edt(()->{});if(ErrorLog.getNumberOfErrors()>before)throw new AssertionError(ErrorLog.lastError().getReport());System.out.println("PASS "+name);passed++;}
  catch(Throwable e){System.out.println("FAIL "+name);e.printStackTrace(System.out);failed++;}
  System.out.flush();
 }
 static void snapshot(Editor editor,String label)throws Exception {
  Image image=editor.render(new Dimension(1150,800));if(image!=null){BufferedImage b=new BufferedImage(1150,800,BufferedImage.TYPE_INT_RGB);b.getGraphics().drawImage(image,0,0,null);javax.imageio.ImageIO.write(b,"png",new File(out,String.format("%03d-",++index)+label.replaceAll("[^A-Za-z0-9_-]","_")+".png"));}
 }
 static java.util.List<Component> components(Container c){java.util.List<Component> list=new ArrayList<>();for(Component child:c.getComponents()){list.add(child);if(child instanceof Container)list.addAll(components((Container)child));}return list;}
 static void tabs(Editor editor,String name)throws Exception{
  if(!(editor instanceof GenericEditor))return;
  java.util.List<JTabbedPane> panes=new ArrayList<>();edt(()->{for(Component c:components(((GenericEditor)editor).getMainPanel()))if(c instanceof JTabbedPane)panes.add((JTabbedPane)c);});
  for(JTabbedPane pane:panes)for(int i=0;i<pane.getTabCount();i++){final int tab=i;check(name+" tab "+pane.getTitleAt(i),()->{edt(()->pane.setSelectedIndex(tab));Thread.sleep(300);edt(()->snapshot(editor,name+"-tab-"+tab));});}
 }
 static void openEntity(EntityConfig config,IntegrationEntity entity,String name)throws Exception{
  final IntegrationEntityPage[] page={null};
  edt(()->{page[0]=config.getPageFactory().createPage(entity);if(page[0]==null)throw new AssertionError("No editor page");win.getEditorService().openEditor(page[0],"Design");snapshot(page[0],name);});
  Thread.sleep(400);tabs(page[0],name);edt(()->{page[0].setClean();win.getEditorService().closeEditor(page[0]);});
 }
 static DataLocation location(int type){switch(type){case 0:return new DatabaseLocation();case 1:return new FileShareLocation();case 2:return new FtpLocation();case 3:return new HttpLocation();case 4:return new LdapLocation();case 5:return new LocalFileLocation();case 6:return new TemporaryStorageLocation();case 7:return new GlobalVariableLocation();default:return new S3Location();}}
 public static void main(String[] args)throws Exception{
  out.mkdirs();IntegrationMain.main(new String[]{"-d",new File(out,"workspace").getPath()});
  ClientPlatform.waitForInitialization(60,java.util.concurrent.TimeUnit.SECONDS);Thread.sleep(2500);edt(()->{});
  win=Application.getBean("ui.AppWin",ApplicationWindow.class);
  org.etlforge.integration.client.server.login.LoginService login=Application.getBean("platform.LoginService",org.etlforge.integration.client.server.login.LoginService.class);
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),java.nio.charset.StandardCharsets.UTF_8).trim();
  login.login(new org.etlforge.integration.client.server.login.LoginCredentials(org.etlforge.integration.client.server.ServerAddressFactory.usingPorts(47100,47101),"localhost:47100","Admin",password,false,false));
  password=null;Thread.sleep(2000);edt(()->{});
  ProjectManager manager=Application.getBeansOfType(ProjectManager.class).get(0);
  edt(()->{IntegrationProjectLock.acquire();try{project=manager.createNewProject("Screen Loading Test "+System.currentTimeMillis(),new File(out,"project-"+System.currentTimeMillis()));manager.setCurrentProject(project);}finally{IntegrationProjectLock.release();}});
  System.setOut(new PrintStream(new File(out,"results.txt"),"UTF-8"));
  System.out.println("START initialized; startup errors="+ErrorLog.getNumberOfErrors());
  Map<EntityType,EntityConfig> configs=new LinkedHashMap<>();for(EntityConfig config:Application.getBeansOfType(EntityConfig.class))configs.put(config.getType(),config);
  for(EntityConfig config:configs.values()){
   EntityType type=config.getType();if(type==null||type==EntityType.Source||type==EntityType.Target||type==EntityType.Folder||type==EntityType.RootFolder||type==EntityType.Project)continue;
   if(type.name().endsWith("Connect")){System.out.println("EXCLUDED removed connector template "+type);continue;}
   check("Entity "+type,()->{final IntegrationEntity[] item={null};edt(()->{item[0]=config.getFactory().createEntity(project.getRootFolder(type));item[0].setName("Screen test "+type);project.insertEntityUnderRoot(item[0]);prepare(item[0]);});openEntity(config,item[0],type.name());});
  }
  String[] names={"Database","FileShare","FTP","HTTP","LDAP","LocalFile","TemporaryStorage","GlobalVariable","AmazonS3"};
  for(EntityType type:new EntityType[]{EntityType.Source,EntityType.Target})for(int n=0;n<names.length;n++){final int choice=n;check(type+" "+names[n],()->{final IntegrationEntity[] item={null};edt(()->{item[0]=configs.get(type).getFactory().createEntity(project.getRootFolder(type));item[0].setName("Screen test "+type+names[choice]);if(item[0] instanceof Source)((Source)item[0]).setLocation(location(choice));else ((Target)item[0]).setLocation(location(choice));project.getRootFolder(type).addItem(item[0]);});openEntity(configs.get(type),item[0],type+"-"+names[choice]);});}
  for(String id:new String[]{"admin.action.SystemConsole","admin.action.OperationQueue","admin.action.OperationLog","admin.action.OperationHistory","admin.action.SchedulerConsole","admin.action.UserConsole","admin.action.ProjectConsole","admin.action.ServerProxyConsole","admin.action.ServerLogs","admin.action.AuditLog","admin.action.CloudLogging","admin.action.SshKeys","admin.action.Cluster","admin.action.ProjectComparison"})check(id,()->{
   edt(()->Application.getBean(id,org.etlforge.application.ui.action.ApplicationAction.class).execute(null));Thread.sleep(1200);final Editor[] editor={null};edt(()->{editor[0]=win.getEditorService().getActiveEditor();if(editor[0]==null)throw new AssertionError("No active editor");snapshot(editor[0],id);});tabs(editor[0],id);edt(()->win.getEditorService().closeEditor(editor[0]));
  });
  for(org.etlforge.integration.data.entity.operation.OperationType type:org.etlforge.integration.data.entity.operation.OperationType.getSelectableTypes())check("Operation type "+type.name(),()->{final Operation[] op={null};edt(()->{op[0]=new Operation("Screen test "+type.name());op[0].setOperationType(type);op[0].setPipeline(type.createNewTemplate());op[0].setTransient(true);project.insertEntityUnderRoot(op[0]);});openEntity(configs.get(EntityType.Operation),op[0],"operation-"+type.name());check("Operation options "+type.name(),()->edt(()->{org.etlforge.integration.client.ui.operation.options.OperationOptionsPanel panel=new org.etlforge.integration.client.ui.operation.options.OperationOptionsPanel(op[0],Application.getBean("ic.EntityExplorerService",org.etlforge.integration.client.ui.entity.EntityExplorerSupport.class));preview("Operation Options "+type.name(),org.etlforge.ui.UiProviderAdapter.of(panel.getUi()));panel.dispose();}));});
  for(EntityType side:new EntityType[]{EntityType.Source,EntityType.Target})for(org.etlforge.integration.data.location.ftp.FtpSecurityOption security:new org.etlforge.integration.data.location.ftp.FtpSecurityOption[]{org.etlforge.integration.data.location.ftp.FtpSecurityOption.SFTP,org.etlforge.integration.data.location.ftp.FtpSecurityOption.REQUIRE_SSL_FOR_ALL})check(side+" "+security,()->{final IntegrationEntity[] item={null};edt(()->{item[0]=configs.get(side).getFactory().createEntity(project.getRootFolder(side));FtpLocation ftp=new FtpLocation();ftp.setSecurityOption(security);if(item[0] instanceof Source)((Source)item[0]).setLocation(ftp);else ((Target)item[0]).setLocation(ftp);project.insertEntityUnderRoot(item[0]);});openEntity(configs.get(side),item[0],side+"-"+security.name());});
  check("New Transformation wizard",()->edt(()->{org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext context=Application.getBean("ic.EntityExplorerService",org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext.class);Transformation entity=new Transformation("Screen test wizard");org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard wizard=(org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard)configs.get(EntityType.Transformation).getEntityWizardFactory().createWizard(context,entity,project.getRootFolder(EntityType.Transformation));org.etlforge.application.ui.wizard.WizardPage page=new org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizardPageChain(wizard,entity,project.getRootFolder(EntityType.Transformation)).getFirstPage();page.addToWizard();preview("New Transformation",page);page.removeFromWizard();}));
  check("New Source/Target selector",()->modal(()->new LocationTypeSelectorDialog(EnumSet.complementOf(EnumSet.of(DataLocationType.None))).selectType()));
  check("New Operation selector",()->edt(()->{java.lang.reflect.Constructor<?> constructor=Class.forName("org.etlforge.integration.client.ui.interchange.entity.operation.OperationActivitySelector").getDeclaredConstructor();constructor.setAccessible(true);preview("New Operation",(org.etlforge.ui.UiProvider)constructor.newInstance());}));
  check("New Project dialog",()->modal(()->new org.etlforge.integration.client.ui.interchange.util.dialog.NewProjectDialog(manager,false).open()));
  check("Import Backup screen",()->edt(()->preview("Import Backup",new org.etlforge.integration.client.ui.jitterpack.ImportJitterPackUi(manager,win))));
  check("Export Backup screen",()->edt(()->preview("Export Backup",new org.etlforge.integration.client.ui.jitterpack.ExportJitterPackUi(project))));
  check("Search and Replace",()->modal(()->new org.etlforge.integration.client.ui.entity.search.SearchAndReplaceDialog(project.getRootFolder(EntityType.Script),configs.get(EntityType.Script).getSearchAndReplaceMetaData()).open(null)));
  for(org.etlforge.integration.data.SourceTarget side:org.etlforge.integration.data.SourceTarget.values())check(side+" advanced HTTP properties",()->modal(()->new org.etlforge.integration.client.ui.http.HttpPropertiesEditor(new HttpLocation()).display()));
  org.etlforge.application.ui.prefs.ApplicationPreferences prefs=Application.getBean("platform.Preferences",org.etlforge.application.ui.prefs.ApplicationPreferences.class);
  preferences(prefs,prefs.getRoot());
  check("Dark table styles",()->edt(()->{for(org.etlforge.ui.style.TableStyle style:new org.etlforge.ui.style.TableStyle[]{org.etlforge.application.ui.look.TableStyles.narrow(),org.etlforge.application.ui.look.TableStyles.normal(),org.etlforge.application.ui.look.TableStyles.wide()}){JTable table=new JTable();style.makeOver(table);if(table.getBackground().getRed()>80||table.getForeground().getRed()<200)throw new AssertionError("Unreadable table theme");}}));
  System.out.println("TOTAL passed="+passed+" failed="+failed+" loggedErrors="+ErrorLog.getNumberOfErrors());System.out.flush();
  Files.write(new File(out,"summary.txt").toPath(),("passed="+passed+" failed="+failed+"\n").getBytes(java.nio.charset.StandardCharsets.UTF_8));
  edt(()->{for(Window w:Window.getWindows())w.dispose();});System.exit(failed==0?0:1);
 }
 static void preview(String title,org.etlforge.ui.UiProvider provider)throws Exception{
  JComponent panel=org.etlforge.application.ui.widget.WizardStyleDialog.wrapUi(provider);JFrame frame=new JFrame(title);frame.setContentPane(panel);frame.setSize(1000,750);frame.setVisible(true);panel.validate();BufferedImage image=new BufferedImage(1000,750,BufferedImage.TYPE_INT_RGB);panel.printAll(image.createGraphics());javax.imageio.ImageIO.write(image,"png",new File(out,title.replaceAll("[^A-Za-z0-9]","_")+".png"));frame.dispose();
 }
 static void modal(Task task)throws Exception{
  edt(()->{Set<Window> existing=new HashSet<>(Arrays.asList(Window.getWindows()));final boolean[] seen={false};Runnable capture=()->{for(Window window:Window.getWindows())if(window instanceof Dialog&&window.isShowing()&&!existing.contains(window)){seen[0]=true;try{BufferedImage image=new BufferedImage(Math.max(1,window.getWidth()),Math.max(1,window.getHeight()),BufferedImage.TYPE_INT_RGB);window.paint(image.createGraphics());javax.imageio.ImageIO.write(image,"png",new File(out,"dialog-"+(++index)+".png"));}catch(Exception ex){throw new RuntimeException(ex);}window.dispose();}};javax.swing.Timer timer=new javax.swing.Timer(1100,e->capture.run());timer.setRepeats(false);timer.start();try{task.run();capture.run();}finally{timer.stop();}if(!seen[0])throw new AssertionError("Dialog never became visible");});
 }
 static void prepare(IntegrationEntity entity)throws Exception{
  if(entity instanceof Transformation){Transformation tx=(Transformation)entity;org.etlforge.integration.data.structure.SimpleTextStructure in=new org.etlforge.integration.data.structure.SimpleTextStructure("Source",false);in.setDelimiter(org.etlforge.integration.data.structure.text.FieldDelimiter.COMMA);in.setStringQualifier("\"");org.etlforge.integration.data.structure.SimpleTextStructure target=new org.etlforge.integration.data.structure.SimpleTextStructure("Target",false);target.setDelimiter(org.etlforge.integration.data.structure.text.FieldDelimiter.COMMA);target.setStringQualifier("\"");tx.setInputStructure(in);tx.setOutputStructure(target);}
  if(entity instanceof SalesforceWizardEntity){
   SalesforceWizardEntity item=(SalesforceWizardEntity)entity;SalesforceConnector connector=new SalesforceConnector("Screen test org");project.insertEntityUnderRoot(connector);item.setConnector(connector);
   Operation op=new Operation("Screen test implementing operation");op.setOperationType(org.etlforge.integration.data.entity.operation.WebServiceOperationType.TYPE);op.setPipeline(org.etlforge.integration.data.entity.operation.WebServiceOperationType.TYPE.createNewTemplate());project.insertEntityUnderRoot(op);item.setImplementingOperation(op);
   entity.setProperty("salesforce_object",org.etlforge.xml.XmlNameFactory.conforming().newExpandedName("urn:partner.soap.sforce.com","Account").toString());
   if(entity instanceof SalesforceQuery)entity.setProperty("query_string","SELECT Id, Name FROM Account");
   if(entity instanceof SalesforceBulkLoad){((SalesforceBulkLoad)entity).setAction(org.etlforge.integration.salesforce.bulkload.BulkAction.QUERY);((SalesforceBulkLoad)entity).setObjectName("Account");entity.setProperty("query_string","SELECT Id, Name FROM Account");}
  }
 }
 static void preferences(org.etlforge.application.ui.prefs.ApplicationPreferences prefs,org.etlforge.application.ui.prefs.ApplicationPreferences.Node node)throws Exception{
  org.etlforge.application.ui.prefs.PreferencesPage page=prefs.getPage(node);
  if(page!=null)check("Preferences "+Arrays.toString(node.getPath()),()->edt(()->{JPanel panel=new JPanel();page.restore();page.display(panel);JFrame frame=new JFrame("Preferences: "+Arrays.toString(node.getPath()));frame.setContentPane(panel);frame.setSize(1000,750);frame.setVisible(true);panel.doLayout();BufferedImage b=new BufferedImage(1000,750,BufferedImage.TYPE_INT_RGB);panel.printAll(b.createGraphics());javax.imageio.ImageIO.write(b,"png",new File(out,"prefs-"+node.getName().replaceAll("[^A-Za-z0-9]","_")+".png"));frame.dispose();}));
  for(org.etlforge.application.ui.prefs.ApplicationPreferences.Node child:node.getChildren())preferences(prefs,child);
 }
}

