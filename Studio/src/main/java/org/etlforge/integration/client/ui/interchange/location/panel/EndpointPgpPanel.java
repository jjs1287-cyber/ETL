package org.etlforge.integration.client.ui.interchange.location.panel;
import java.awt.*;
import javax.swing.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.application.Application;
import org.etlforge.integration.client.ui.admin.audit.AuditLogService;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.ui.input.InputPanel;
public final class EndpointPgpPanel extends InputPanel {
 private final JCheckBox enabled=new JCheckBox(),armor=new JCheckBox("ASCII armored output (.asc)");
 private final JComboBox<Key> keys=new JComboBox<>();private final JButton reload=new JButton("Reload keys");
 private final JLabel hint=new JLabel("Manage keys under Administration → SSH and PGP Keys.");
 private final boolean source;private boolean resetting;private String selected="";private int generation;
 private static final class Key {final String id,label;Key(String id,String label){this.id=id;this.label=label;}public String toString(){return label;}}
 public EndpointPgpPanel(SourceTarget side){source=side==SourceTarget.Source;JPanel panel=new JPanel(new GridLayout(0,1,4,4));panel.setOpaque(false);panel.setBorder(BorderFactory.createTitledBorder("PGP / GPG encryption"));enabled.setText(source?"Decrypt source files before processing":"Encrypt target files before transfer");panel.add(enabled);JPanel row=new JPanel(new BorderLayout(6,0));row.setOpaque(false);row.add(new JLabel(source?"Private key: ":"Recipient key: "),BorderLayout.WEST);row.add(keys);row.add(reload,BorderLayout.EAST);panel.add(row);if(!source)panel.add(armor);panel.add(new JLabel(source?"Decrypt before archive extraction. A private key is required.":"Encrypt after compression. Public recipient keys are sufficient."));panel.add(hint);wrap(org.etlforge.ui.UiProviderAdapter.of(panel));enabled.addActionListener(e->{update();changed();});armor.addActionListener(e->changed());keys.addActionListener(e->{if(!resetting){Key key=(Key)keys.getSelectedItem();selected=key==null?"":key.id;changed();}});reload.addActionListener(e->reload());update();}
 private void changed(){if(!resetting)fireInputChanged(this,"PGP encryption");}
 private void update(){keys.setEnabled(enabled.isSelected());reload.setEnabled(enabled.isSelected());armor.setEnabled(enabled.isSelected());}
 public void reset(FileLocation location){generation++;resetting=true;try{enabled.setSelected(location.getBooleanProperty("pgp_enabled",false));armor.setSelected(location.getBooleanProperty("pgp_armor",false));selected=location.getProperty("pgp_key_id","");keys.removeAllItems();keys.addItem(new Key("","Select a PGP key"));if(!selected.isEmpty()){keys.addItem(new Key(selected,"Saved PGP key: "+selected));keys.setSelectedIndex(1);}update();}finally{resetting=false;}}
 public void apply(FileLocation location){if(!enabled.isSelected()&&location.getProperty("pgp_enabled")==null)return;if(enabled.isSelected()&&selected.isEmpty())throw new IllegalArgumentException("Select a PGP key for this endpoint.");location.setProperty("pgp_enabled",Boolean.toString(enabled.isSelected()));location.setProperty("pgp_key_id",selected);location.setProperty("pgp_armor",Boolean.toString(armor.isSelected()));}
 private void reload(){final int ticket=++generation;reload.setEnabled(false);hint.setText("Loading PGP keys...");new SwingWorker<JsonNode,Void>(){protected JsonNode doInBackground()throws Exception{ObjectNode request=new ObjectMapper().createObjectNode();request.put("action","ssh.list");return new AuditLogService(Application.getBean("ws.config.Cpp",CppWebServiceConfiguration.class)).request(request);}protected void done(){if(ticket!=generation)return;try{JsonNode result=get();resetting=true;keys.removeAllItems();keys.addItem(new Key("","Select a PGP key"));boolean found=selected.isEmpty();for(JsonNode key:result.path("keys")){if(!key.path("type").asText().equals("pgp")||(source&&!key.path("hasPrivate").asBoolean()))continue;Key k=new Key(key.path("id").asText(),key.path("name").asText());keys.addItem(k);if(k.id.equals(selected)){keys.setSelectedItem(k);found=true;}}if(!found){Key k=new Key(selected,"Unavailable PGP key: "+selected);keys.addItem(k);keys.setSelectedItem(k);}hint.setText("Manage keys under Administration → SSH and PGP Keys.");}catch(Exception e){Throwable c=e;while(c.getCause()!=null)c=c.getCause();hint.setText("Could not load keys: "+c.getMessage());}finally{resetting=false;update();}}}.execute();}
}
