import java.awt.*;
import javax.swing.*;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.location.*;
import org.etlforge.integration.client.ui.interchange.location.panel.*;
import org.etlforge.integration.client.ui.interchange.location.tempstore.*;
import org.etlforge.integration.client.ui.interchange.location.local.*;
import org.etlforge.integration.client.ui.interchange.location.fileshare.*;
import org.etlforge.integration.client.ui.interchange.location.ftp.*;
import org.etlforge.integration.client.ui.interchange.location.http.*;
import org.etlforge.integration.client.ui.interchange.location.ldap.*;
import org.etlforge.integration.client.ui.interchange.location.db.*;
public final class EndpointEditorOpeningTest {
 static int passed,failed;
 static void layout(Container c){c.doLayout();for(Component x:c.getComponents())if(x instanceof Container)layout((Container)x);}
 static DataLocationPanel create(int type,SourceTarget side)throws Exception{switch(type){
  case 0:return new DatabaseLocationPanel(side==SourceTarget.Source?new Source("UI test"):new Target("UI test"),new DatabaseLocation(),side);
  case 1:return new FileShareLocationPanel(new FileShareLocation(),side);
  case 2:return new FtpLocationPanel(new FtpLocation(),side);
  case 3:return new HttpLocationPanel(new HttpLocation(),side);
  case 4:return new LdapLocationPanel(new LdapLocation(),side);
  case 5:
   LocalFileLocationPanel local=new LocalFileLocationPanel(new LocalFileLocation(),side);
   java.lang.reflect.Field connection=LocalFileLocationPanel.class.getDeclaredField("serverConnectionService");connection.setAccessible(true);
   connection.set(local,java.lang.reflect.Proxy.newProxyInstance(EndpointEditorOpeningTest.class.getClassLoader(),new Class[]{org.etlforge.integration.client.server.ServerConnectionService.class},(proxy,method,args)->method.getName().equals("getConnectionStatus")?org.etlforge.integration.client.server.ConnectionStatus.DISCONNECTED:null));return local;
  case 6:return new TemporaryStorageLocationPanel(new TemporaryStorageLocation(),side);
  default:return new GlobalVariableLocationPanel(new GlobalVariableLocation(),side);
 }}
 public static void main(String[] args)throws Exception{
  SwingUtilities.invokeAndWait(()->{
   String[] names={"Database","File Share","FTP/SFTP/FTPS","HTTP/HTTPS","LDAP","Local File","Temporary Storage","Global Variable"};
   for(SourceTarget side:SourceTarget.values())for(int type=0;type<names.length;type++)try{
    DataLocationPanel panel=create(type,side);panel.display();JComponent ui=panel.getUi();ui.setSize(1200,950);layout(ui);
    LocationPanelDisplayer displayer=new LocationPanelDisplayer(new org.etlforge.ui.input.InputPanel(),null);displayer.showDataLocationPanel(panel);
    if(displayer.getCurrentLocationPanel()!=panel)throw new AssertionError("editor was not selected");
    panel.display();layout(ui);System.out.println("PASS "+side+" "+names[type]+" create/display/redisplay");passed++;
   }catch(Throwable e){System.out.println("FAIL "+side+" "+names[type]+": "+e);e.printStackTrace(System.out);failed++;}
  });
  System.out.println("Passed "+passed+" endpoint editors; failures="+failed);if(failed>0)System.exit(1);
 }
}
