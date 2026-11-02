package org.etlforge.integration.client.ui.admin.ssh;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.integration.client.ui.admin.audit.AuditLogPanel;
import java.awt.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public final class SshKeysPanel extends JPanel {
 private final AuditLogPanel.Service service;
 private final ObjectMapper json=new ObjectMapper();
 private final DefaultTableModel model=new DefaultTableModel(new String[]{"Name","Type","Fingerprint","Updated (UTC)"},0){public boolean isCellEditable(int r,int c){return false;}};
 private final JTable table=new JTable(model);
 private final JComboBox<String> type=new JComboBox<>(new String[]{"SSH", "PGP/GPG"});
 private final JTextField name=new JTextField(28);
 private final JPasswordField passphrase=new JPasswordField(28);
 private final JButton privateButton=new JButton("Import private key..."),publicButton=new JButton("Import public key..."),save=new JButton("Save key"),remove=new JButton("Delete key"),fresh=new JButton("New key"),reload=new JButton("Reload"),export=new JButton("Export public key...");
 private final JLabel status=new JLabel("Connect to a server to manage keys."),files=new JLabel("No key files selected");
 private JsonNode keys=json.createArrayNode();
 private String id="",privateKey="",publicKey="";
 private boolean connected,busy,canManage;
 private int generation;
 public SshKeysPanel(AuditLogPanel.Service service){
  super(new BorderLayout(10,10));this.service=service;setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
  JPanel top=new JPanel(new GridLayout(0,1));JLabel title=new JLabel("SSH and PGP Keys");title.setFont(title.getFont().deriveFont(20f));top.add(title);top.add(new JLabel("Import SSH (SFTP) or armored OpenPGP keys. Projects store references; private keys stay on the server."));add(top,BorderLayout.NORTH);
  JPanel center=new JPanel(new BorderLayout(8,8));center.add(new JScrollPane(table),BorderLayout.CENTER);
  JPanel form=new JPanel(new GridLayout(0,2,8,8));form.add(new JLabel("Key type:"));form.add(type);form.add(new JLabel("Key name:"));form.add(name);form.add(new JLabel("Key files:"));JPanel buttons=new JPanel();buttons.add(privateButton);buttons.add(publicButton);form.add(buttons);form.add(new JLabel("Import passphrase:"));form.add(passphrase);form.add(new JLabel(""));form.add(files);
  JPanel actions=new JPanel();for(JButton b:new JButton[]{fresh,save,remove,export,reload})actions.add(b);JPanel bottom=new JPanel(new BorderLayout());bottom.add(form,BorderLayout.CENTER);bottom.add(actions,BorderLayout.SOUTH);center.add(bottom,BorderLayout.SOUTH);add(center,BorderLayout.CENTER);add(status,BorderLayout.SOUTH);
  privateButton.addActionListener(e->importFile(true));publicButton.addActionListener(e->importFile(false));fresh.addActionListener(e->clear());reload.addActionListener(e->start());
  save.addActionListener(e->{ObjectNode r=json.createObjectNode();r.put("action","ssh.save");r.put("type",type.getSelectedIndex()==0?"ssh":"pgp");r.put("id",id);r.put("name",name.getText().trim());r.put("privateKey",privateKey);r.put("publicKey",publicKey);char[] chars=passphrase.getPassword();try{r.put("passphrase",new String(chars));}finally{java.util.Arrays.fill(chars,'\0');}send(r);});
  remove.addActionListener(e->{if(id.isEmpty())return;if(JOptionPane.showConfirmDialog(this,"Delete this server key? Endpoints referencing it will fail until another key is selected.","Delete key",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){ObjectNode r=json.createObjectNode();r.put("action","ssh.delete");r.put("id",id);send(r);}});
  export.addActionListener(e->exportPublic());table.getSelectionModel().addListSelectionListener(e->{int row=table.getSelectedRow();if(!e.getValueIsAdjusting()&&row>=0){JsonNode k=keys.get(row);id=k.path("id").asText();type.setSelectedIndex(k.path("type").asText("ssh").equals("pgp")?1:0);name.setText(k.path("name").asText());privateKey="";publicKey=k.path("publicKey").asText();passphrase.setText("");files.setText("Saved key; import files to replace key material");controls();}});controls();
 }
 private void controls(){type.setEnabled(connected&&canManage&&!busy&&id.isEmpty());for(JButton b:new JButton[]{privateButton,publicButton,save,fresh})b.setEnabled(connected&&canManage&&!busy);reload.setEnabled(connected&&!busy);remove.setEnabled(connected&&canManage&&!busy&&!id.isEmpty());export.setEnabled(connected&&!busy&&!id.isEmpty());}
 private void clear(){id="";privateKey="";publicKey="";table.clearSelection();type.setSelectedIndex(0);name.setText("");passphrase.setText("");files.setText("No key files selected");controls();}
 private void importFile(boolean secret){JFileChooser chooser=new JFileChooser();if(chooser.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)return;try{Path path=chooser.getSelectedFile().toPath();if(Files.size(path)>65536)throw new Exception("Key file exceeds 64 KiB");String text=StandardCharsets.UTF_8.newDecoder().decode(java.nio.ByteBuffer.wrap(Files.readAllBytes(path))).toString();if(secret)privateKey=text;else publicKey=text.trim();files.setText((privateKey.isEmpty()?"Private: saved / not selected":"Private: imported")+" | "+(publicKey.isEmpty()?"Public: not selected":"Public: selected"));}catch(Exception ex){status.setText("Import failed: "+ex.getMessage());}}
 private void exportPublic(){JFileChooser chooser=new JFileChooser();if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;Path p=chooser.getSelectedFile().toPath();try{if(Files.exists(p)&&JOptionPane.showConfirmDialog(this,"Replace existing public key file?","Export",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;Files.write(p,(publicKey+"\n").getBytes(StandardCharsets.UTF_8));status.setText("Public key exported.");}catch(Exception ex){status.setText("Export failed: "+ex.getMessage());}}
 public void disconnect(){connected=false;busy=false;canManage=false;generation++;keys=json.createArrayNode();model.setRowCount(0);clear();status.setText("Connect to a server to manage keys.");}
 public void start(){connected=true;ObjectNode r=json.createObjectNode();r.put("action","ssh.list");send(r);}
 private void send(ObjectNode request){busy=true;controls();int ticket=++generation;status.setText("Contacting server...");new SwingWorker<JsonNode,Void>(){protected JsonNode doInBackground()throws Exception{return service.request(request);}protected void done(){if(ticket!=generation)return;busy=false;try{JsonNode result=get();canManage=result.path("canManage").asBoolean();keys=result.path("keys");model.setRowCount(0);for(JsonNode k:keys)model.addRow(new Object[]{k.path("name").asText(),k.path("type").asText("ssh").toUpperCase(java.util.Locale.ROOT),k.path("fingerprint").asText(),k.path("updatedAt").asText()});clear();status.setText(keys.size()+" server keys. SSH: SFTP authentication. PGP: source decryption / target encryption.");}catch(Exception ex){Throwable c=ex;while(c.getCause()!=null)c=c.getCause();status.setText("Keys: "+c.getMessage());}finally{privateKey="";passphrase.setText("");controls();}}}.execute();}
}
