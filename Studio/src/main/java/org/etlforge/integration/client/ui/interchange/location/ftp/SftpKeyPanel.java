package org.etlforge.integration.client.ui.interchange.location.ftp;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.application.Application;
import org.etlforge.integration.client.ui.admin.audit.AuditLogService;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.UiProviderAdapter;
public final class SftpKeyPanel extends InputPanel {
 private final JComboBox<Key> keys=new JComboBox<>();
 private final JTextField fingerprint=new JTextField(36);
 private final JButton reload=new JButton("Reload keys");
 private final JLabel status=new JLabel("Manage keys under Administration → SSH Keys.");
 private final JPanel panel=new JPanel(new GridLayout(0,1,3,3));
 private boolean resetting;
 private String selected="";
 static final class Key {final String id,label;Key(String id,String label){this.id=id;this.label=label;}public String toString(){return label;}}
 public SftpKeyPanel(){
  panel.setOpaque(false);panel.add(new JLabel("SSH protocol: SSH 2 (automatic algorithm negotiation)"));panel.add(new JLabel("Authentication key:"));JPanel row=new JPanel(new BorderLayout(6,0));row.setOpaque(false);row.add(keys,BorderLayout.CENTER);row.add(reload,BorderLayout.EAST);panel.add(row);panel.add(new JLabel("Verified server host fingerprint (SHA256):"));panel.add(fingerprint);panel.add(status);wrap(UiProviderAdapter.of(panel));
  keys.addActionListener(e->{if(!resetting){Key k=(Key)keys.getSelectedItem();selected=k==null?"":k.id;fireInputChanged(this,"SFTP key");}});
  fingerprint.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){changed();}public void removeUpdate(DocumentEvent e){changed();}public void changedUpdate(DocumentEvent e){changed();}private void changed(){if(!resetting)fireInputChanged(SftpKeyPanel.this,"SFTP host fingerprint");}});
  reload.addActionListener(e->reload());
 }
 public void reset(FtpLocation location){resetting=true;try{selected=location.getProperty("sftp_key_id","");keys.removeAllItems();keys.addItem(new Key("","Password (use endpoint login/password)"));if(!selected.isEmpty()){keys.addItem(new Key(selected,"Saved key: "+selected));keys.setSelectedIndex(1);}fingerprint.setText(location.getProperty("sftp_host_sha256",""));}finally{resetting=false;}}
 public void apply(FtpLocation location){if(!selected.isEmpty()||location.getProperty("sftp_key_id")!=null)location.setProperty("sftp_key_id",selected);if(!fingerprint.getText().trim().isEmpty()||location.getProperty("sftp_host_sha256")!=null)location.setProperty("sftp_host_sha256",fingerprint.getText().trim());}
 public void setSftpVisible(boolean visible){panel.setVisible(visible);}
 public void reload(){reload.setEnabled(false);status.setText("Loading server SSH keys...");new SwingWorker<JsonNode,Void>(){protected JsonNode doInBackground()throws Exception{CppWebServiceConfiguration config=Application.getBean("ws.config.Cpp",CppWebServiceConfiguration.class);ObjectNode r=new ObjectMapper().createObjectNode();r.put("action","ssh.list");return new AuditLogService(config).request(r);}protected void done(){reload.setEnabled(true);try{JsonNode result=get();resetting=true;keys.removeAllItems();keys.addItem(new Key("","Password (use endpoint login/password)"));boolean found=selected.isEmpty();for(JsonNode k:result.path("keys")){if(!k.path("type").asText("ssh").equals("ssh"))continue;Key key=new Key(k.path("id").asText(),k.path("name").asText());keys.addItem(key);if(key.id.equals(selected)){keys.setSelectedItem(key);found=true;}}if(!found){Key missing=new Key(selected,"Missing key: "+selected);keys.addItem(missing);keys.setSelectedItem(missing);}status.setText("Manage keys under Administration → SSH Keys.");}catch(Exception ex){Throwable c=ex;while(c.getCause()!=null)c=c.getCause();status.setText("Could not load keys: "+c.getMessage());}finally{resetting=false;}}}.execute();}
}
