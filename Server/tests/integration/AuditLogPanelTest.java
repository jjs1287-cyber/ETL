import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.etlforge.integration.client.ui.admin.audit.AuditLogPanel;

public final class AuditLogPanelTest {
    static final ObjectMapper JSON=new ObjectMapper();
    static AuditLogPanel panel;
    static final AtomicBoolean expired=new AtomicBoolean(false);
    static int checks;
    static void require(boolean condition,String text){checks++;if(!condition)throw new AssertionError(text);}
    static <T> List<T> find(Container root,Class<T> type){List<T> result=new ArrayList<>();for(Component child:root.getComponents()){if(type.isInstance(child))result.add(type.cast(child));if(child instanceof Container)result.addAll(find((Container)child,type));}return result;}
    static void await(java.util.concurrent.Callable<Boolean> ready)throws Exception{
        long end=System.currentTimeMillis()+10000;
        while(System.currentTimeMillis()<end){AtomicBoolean ok=new AtomicBoolean();SwingUtilities.invokeAndWait(()->{try{ok.set(ready.call());}catch(Exception e){throw new RuntimeException(e);}});if(ok.get())return;Thread.sleep(20);}
        throw new AssertionError("Timed out waiting for audit UI");
    }
    static void layout(Container c){c.doLayout();for(Component child:c.getComponents())if(child instanceof Container)layout((Container)child);}
    public static void main(String[] args)throws Exception {
        System.setProperty("java.awt.headless","true");
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        java.util.Enumeration<Object> defaults=UIManager.getDefaults().keys();
        while(defaults.hasMoreElements()){Object key=defaults.nextElement();if(UIManager.get(key) instanceof javax.swing.plaf.FontUIResource)UIManager.put(key,new javax.swing.plaf.FontUIResource("Segoe UI",java.awt.Font.PLAIN,12));}
        for(String key:new String[]{"Panel.background","Table.background","Viewport.background","ScrollPane.background","Label.background","TextField.background","ComboBox.background","Spinner.background"})UIManager.put(key,new Color(25,25,25));
        for(String key:new String[]{"Label.foreground","Table.foreground","TextField.foreground","ComboBox.foreground","TitledBorder.titleColor"})UIManager.put(key,Color.WHITE);
        UIManager.put("Table.selectionBackground",new Color(38,66,90));UIManager.put("Table.selectionForeground",Color.WHITE);
        for(String key:new String[]{"Button.background","CheckBox.background","TableHeader.background","Spinner.background","FormattedTextField.background"})UIManager.put(key,new Color(40,40,40));
        for(String key:new String[]{"Button.foreground","CheckBox.foreground","TableHeader.foreground","FormattedTextField.foreground"})UIManager.put(key,Color.WHITE);
        UIManager.put("Table.gridColor",new Color(80,80,80));
        AuditLogPanel.Service service=request->{
            switch(request.path("action").asText()){
            case "settings":return JSON.readTree("{\"detailDays\":30,\"headerDays\":365,\"retentionEnabled\":false,\"canManageSettings\":true,\"projects\":[{\"name\":\"Test Operations\",\"guid\":\"bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb\"}]}");
            case "list":return JSON.readTree("{\"entries\":[{\"id\":1,\"objectName\":\"test xml source\",\"objectTypeName\":\"Source\",\"action\":\"Updated\",\"userName\":\"Admin\",\"deployedAt\":\"2026-10-27T14:32:18Z\"}],\"hasMore\":false}");
            case "detail":if(expired.get())return JSON.readTree("{\"detailAvailable\":false}");return JSON.readTree("{\"detailAvailable\":true,\"previous\":{\"directory\":\"C:/temp\",\"password\":\"[REDACTED]\"},\"current\":{\"directory\":\"C:/temp2\",\"password\":\"[REDACTED]\"},\"changedPaths\":[\"/directory\",\"/password\"]}");
            case "saveSettings":return JSON.createObjectNode().put("saved",true);
            default:throw new IllegalArgumentException("Unexpected audit request");}
        };
        SwingUtilities.invokeAndWait(()->{panel=new AuditLogPanel(service);panel.setSize(1350,780);layout(panel);panel.start();});
        await(()->find(panel,JTable.class).get(0).getRowCount()==1);
        SwingUtilities.invokeAndWait(()->{
            List<JTable> tables=find(panel,JTable.class);require(tables.size()==3,"event/previous/current tables missing");
            require(tables.get(0).getValueAt(0,0).equals("test xml source"),"history grid missing object name");
            tables.get(0).setRowSelectionInterval(0,0);
        });
        await(()->find(panel,JTable.class).get(1).getRowCount()==2);
        SwingUtilities.invokeAndWait(()->{
            List<JTable> tables=find(panel,JTable.class);
            require(tables.get(1).getValueAt(0,1).equals("C:/temp"),"previous value missing");
            require(tables.get(2).getValueAt(0,1).equals("C:/temp2"),"current value missing");
            require(tables.get(1).getValueAt(1,1).equals("[REDACTED]"),"masked credential missing");
            Color before=tables.get(1).prepareRenderer(tables.get(1).getCellRenderer(0,1),0,1).getBackground();
            Color after=tables.get(2).prepareRenderer(tables.get(2).getCellRenderer(0,1),0,1).getBackground();
            require(before.getRed()>before.getGreen() && after.getGreen()>after.getRed(),"difference colors missing");
            require(!tables.get(0).isCellEditable(0,0),"audit history is editable");
            JLabel marker=new JLabel("IMPLEMENTATION PREVIEW — synthetic test data; not connected to the server");
            marker.setOpaque(true);marker.setBackground(new Color(45,45,45));marker.setForeground(Color.WHITE);
            JPanel preview=new JPanel(new java.awt.BorderLayout());preview.add(marker,java.awt.BorderLayout.NORTH);preview.add(panel,java.awt.BorderLayout.CENTER);
            preview.setSize(1350,810);layout(preview);BufferedImage image=new BufferedImage(1350,810,BufferedImage.TYPE_INT_RGB);java.awt.Graphics2D g=image.createGraphics();preview.printAll(g);g.dispose();
            try{ImageIO.write(image,"png",new File("C:/ETL/Reports/audit-panel-implementation-preview.png"));}catch(Exception e){throw new RuntimeException(e);}
            expired.set(true);tables.get(0).clearSelection();tables.get(0).setRowSelectionInterval(0,0);
        });
        await(()->find(panel,JLabel.class).stream().anyMatch(label->label.getText()!=null && label.getText().startsWith("Details have expired")));
        SwingUtilities.invokeAndWait(()->{
            require(find(panel,JTable.class).get(1).getRowCount()==0,"expired old values still visible");
            panel.disconnect();
            require(find(panel,JTable.class).get(0).getRowCount()==0,"disconnected server history not cleared");
            require(find(panel,JButton.class).stream().filter(button->button.getText().equals("Save Settings")).noneMatch(JButton::isEnabled),"settings editable after disconnect");
            AuditLogPanel unavailable=new AuditLogPanel(null);
            unavailable.start();
            require(((Number)find(unavailable,javax.swing.JSpinner.class).get(0).getValue()).intValue()==30,"detail default is not 30 days");
            require(((Number)find(unavailable,javax.swing.JSpinner.class).get(1).getValue()).intValue()==365,"header default is not 365 days");
            require(find(unavailable,JLabel.class).stream().anyMatch(label->label.getText()!=null && label.getText().contains("not enabled")),"unavailable server message missing");
            require(find(unavailable,JTable.class).get(0).getRowCount()==0,"unavailable server displays invented history");
            require(find(unavailable,JButton.class).stream().filter(button->java.util.Arrays.asList("Refresh","Save Settings","Previous","Next").contains(button.getText())).noneMatch(JButton::isEnabled),"unavailable server permits requests");
            require(find(panel,JTable.class).get(0).getTableHeader().getReorderingAllowed()==false,"audit columns can be detached from their labels");
        });
        System.out.println("PASS "+checks+" audit panel checks; rendered implementation preview");
        System.exit(0);
    }
}
