package org.etlforge.integration.client.ui.admin.cloud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import org.etlforge.integration.client.ui.admin.audit.AuditLogPanel;

public final class CloudLoggingPanel extends JPanel {
    private final AuditLogPanel.Service service;
    private final ObjectMapper json = new ObjectMapper();
    private final JCheckBox enabled = new JCheckBox("Enable cloud logging");
    private final JCheckBox history = new JCheckBox("Include retained completed operations");
    private final JComboBox<String> type = new JComboBox<>(new String[]{"SFTP (CSV)", "REST / HTTP (JSON)", "SMB Windows share (CSV)"});
    private final Map<String,JTextField> fields = new LinkedHashMap<>();
    private final Map<String,JLabel> labels = new LinkedHashMap<>();
    private final JPasswordField password = new JPasswordField(36), bearer = new JPasswordField(36);
    private final JButton save = new JButton("Save Settings"), reload = new JButton("Reload");
    private final JLabel status = new JLabel("Connect to a server to configure cloud logging.");
    private final JPanel form = new JPanel(new GridBagLayout());
    private long revision;
    private int generation;
    private boolean connected, busy;

    public CloudLoggingPanel(AuditLogPanel.Service service) {
        super(new BorderLayout(12,12));this.service=service;
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JPanel top=new JPanel();top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
        JLabel title=new JLabel("Cloud logging");title.setFont(title.getFont().deriveFont(20f));top.add(title);
        top.add(new JLabel("Send completed operation logs to one write-only destination. Local logging always remains active."));
        top.add(new JLabel("Messages: 1 MiB maximum. Failed uploads: five attempts maximum, with delayed retries and local error logs."));
        add(top,BorderLayout.NORTH);
        row(0,"Enabled",enabled);row(1,"Destination",type);
        field(2,"environment","Environment");field(3,"host","SFTP hostname");field(4,"port","SFTP port");
        field(5,"path","Remote directory / UNC path");field(6,"login","Login");row(7,"Password (blank keeps saved)",password);
        field(8,"hostKeySha256","SFTP host key SHA256");field(9,"url","HTTP endpoint URL");row(10,"Bearer token (blank keeps saved)",bearer);
        row(11,"History",history);field(12,"excludeOperationIds","Exclude receiver operation IDs (comma-separated)");
        JPanel buttons=new JPanel();buttons.add(save);buttons.add(reload);row(13,"",buttons);
        GridBagConstraints fill=new GridBagConstraints();fill.gridx=0;fill.gridy=14;fill.weighty=1;form.add(new JPanel(),fill);
        add(new JScrollPane(form),BorderLayout.CENTER);add(status,BorderLayout.SOUTH);
        type.addActionListener(e->visibility());save.addActionListener(e->save());reload.addActionListener(e->start());
        fields.get("port").setText("22");fields.get("environment").setText("dev");visibility();controls();
    }
    private void row(int row,String label,JComponent component) {
        GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(6,6,6,6);c.anchor=GridBagConstraints.WEST;c.gridx=0;c.gridy=row;
        JLabel name=new JLabel(label);form.add(name,c);labels.put(label,name);
        c.gridx=1;c.weightx=1;c.fill=GridBagConstraints.HORIZONTAL;form.add(component,c);
    }
    private void field(int row,String key,String label){JTextField field=new JTextField(36);field.setName(key);fields.put(key,field);row(row,label,field);}
    private void visible(String key,String label,boolean value){fields.get(key).setVisible(value);labels.get(label).setVisible(value);}
    private void visibility(){
        int kind=type.getSelectedIndex();boolean sftp=kind==0,http=kind==1;
        visible("host","SFTP hostname",sftp);visible("port","SFTP port",sftp);visible("hostKeySha256","SFTP host key SHA256",sftp);
        visible("path","Remote directory / UNC path",!http);visible("login","Login",true);visible("url","HTTP endpoint URL",http);
        password.setVisible(true);labels.get("Password (blank keeps saved)").setVisible(true);
        bearer.setVisible(http);labels.get("Bearer token (blank keeps saved)").setVisible(http);form.revalidate();form.repaint();
    }
    private void controls(){save.setEnabled(connected&&!busy);reload.setEnabled(connected&&!busy);}
    public void disconnect(){connected=false;busy=false;generation++;password.setText("");bearer.setText("");controls();status.setText("Connect to a server to configure cloud logging.");}
    public void start(){connected=true;ObjectNode request=json.createObjectNode();request.put("action","cloud.settings");send(request);}
    private void save(){
        ObjectNode settings=json.createObjectNode();settings.put("enabled",enabled.isSelected());settings.put("includeHistory",history.isSelected());
        settings.put("type",new String[]{"sftp","http","smb"}[type.getSelectedIndex()]);
        for(Map.Entry<String,JTextField> entry:fields.entrySet())settings.put(entry.getKey(),entry.getValue().getText().trim());
        try{settings.put("port",Integer.parseInt(fields.get("port").getText().trim()));}catch(NumberFormatException e){status.setText("SFTP port must be a number.");return;}
        settings.put("password",new String(password.getPassword()));settings.put("bearerToken",new String(bearer.getPassword()));
        ObjectNode request=json.createObjectNode();request.put("action","cloud.save");request.put("revision",revision);request.set("settings",settings);send(request);
    }
    private void send(final ObjectNode request){
        busy=true;controls();status.setText("Contacting server...");final int ticket=++generation;
        new SwingWorker<JsonNode,Void>() {
            protected JsonNode doInBackground() throws Exception{return service.request(request);}
            protected void done(){
                if(ticket!=generation)return;
                busy=false;controls();
                try{JsonNode result=get(),s=result.path("settings");revision=result.path("revision").asLong();
                    enabled.setSelected(s.path("enabled").asBoolean());history.setSelected(s.path("includeHistory").asBoolean());
                    type.setSelectedIndex("http".equals(s.path("type").asText())?1:"smb".equals(s.path("type").asText())?2:0);
                    for(Map.Entry<String,JTextField> entry:fields.entrySet())entry.getValue().setText(s.path(entry.getKey()).asText("port".equals(entry.getKey())?"22":""));
                    password.setText("");bearer.setText("");visibility();
                    status.setText((enabled.isSelected()?"Enabled":"Disabled")+" | Uploaded: "+result.path("counts").path("1").asInt()+" | Upload errors: "+result.path("counts").path("2").asInt()+" | Password: "+(s.path("hasPassword").asBoolean()?"saved":"not set"));
                }catch(Exception e){Throwable cause=e;while(cause.getCause()!=null)cause=cause.getCause();status.setText("Cloud logging: "+cause.getMessage());}
            }
        }.execute();
    }
}
