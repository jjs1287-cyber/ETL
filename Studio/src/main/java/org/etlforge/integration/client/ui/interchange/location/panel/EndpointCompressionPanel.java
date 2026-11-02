package org.etlforge.integration.client.ui.interchange.location.panel;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.ui.input.InputPanel;
import com.google.common.base.Optional;
public final class EndpointCompressionPanel extends InputPanel {
 private final JCheckBox enabled=new JCheckBox("Enable compression");
 private final JComboBox<String> format=new JComboBox<>(new String[]{"ZIP", "TAR.GZ", "GZIP (one file)", "7z"});
 private final JComboBox<String> mode=new JComboBox<>(new String[]{"One archive for all files in this run", "One archive for each output file"});
 private final JTextField archiveName=new JTextField(28);
 private final JPasswordField password=new JPasswordField(28);
 private final JLabel hint=new JLabel();
 private final boolean source;
 private boolean resetting;
 public EndpointCompressionPanel(SourceTarget side) {
  source=side==SourceTarget.Source;
  JPanel panel=new JPanel(new GridBagLayout());panel.setOpaque(false);panel.setBorder(BorderFactory.createTitledBorder("Compression"));
  GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(3,6,3,6);c.anchor=GridBagConstraints.WEST;c.fill=GridBagConstraints.HORIZONTAL;
  c.gridx=0;c.gridy=0;c.gridwidth=2;panel.add(enabled,c);enabled.setText(source?"The source data is compressed":"Compress target output");
  c.gridwidth=1;c.gridy++;panel.add(new JLabel("Format:"),c);c.gridx=1;c.weightx=1;panel.add(source?new JLabel("Auto-detect (ZIP, TAR/GZIP, 7z, RAR)"):format,c);
  if(!source){c.gridx=0;c.weightx=0;c.gridy++;panel.add(new JLabel("Mode:"),c);c.gridx=1;c.weightx=1;panel.add(mode,c);
   c.gridx=0;c.weightx=0;c.gridy++;panel.add(new JLabel("Combined archive name:"),c);c.gridx=1;c.weightx=1;panel.add(archiveName,c);}
  c.gridx=0;c.weightx=0;c.gridy++;panel.add(new JLabel("Password:"),c);c.gridx=1;c.weightx=1;panel.add(password,c);
  c.gridy++;c.gridx=0;c.gridwidth=2;panel.add(hint,c);wrap(org.etlforge.ui.UiProviderAdapter.of(panel));
  enabled.addActionListener(e->{update();changed();});format.addActionListener(e->{update();changed();});mode.addActionListener(e->{update();changed();});
  DocumentListener listener=new DocumentListener(){public void insertUpdate(DocumentEvent e){changed();}public void removeUpdate(DocumentEvent e){changed();}public void changedUpdate(DocumentEvent e){changed();}};
  password.getDocument().addDocumentListener(listener);archiveName.getDocument().addDocumentListener(listener);update();
 }
 private void changed(){if(!resetting)fireInputChanged(this,"Compression");}
 private void update(){boolean active=enabled.isSelected();format.setEnabled(active);mode.setEnabled(active);archiveName.setEnabled(active&&mode.getSelectedIndex()==0);
  password.setEnabled(active&&(source||format.getSelectedIndex()==0));
  hint.setText(source?"Extract each file before processing. Password support depends on archive format.":"Passwords: ZIP (AES-256) only. GZIP: one file; use TAR.GZ to combine files.");}
 public void reset(FileLocation location){resetting=true;try{
  enabled.setSelected(location.getBooleanProperty("compression_enabled",false));String f=location.getProperty("compression_format","zip");format.setSelectedIndex(f.equals("tar.gz")?1:f.equals("gz")?2:f.equals("7z")?3:0);
  mode.setSelectedIndex(location.getProperty("compression_mode","combined").equals("individual")?1:0);archiveName.setText(location.getProperty("compression_archive_name","output"));
  Optional<Password> secret=PasswordUtils.getPassword(location,"compression_password");password.setText(secret.isPresent()?secret.get().clearText():"");update();
 }finally{resetting=false;}}
 public void apply(FileLocation location){
  if(!enabled.isSelected() && location.getProperty("compression_enabled")==null)return;
  location.setProperty("compression_enabled",Boolean.toString(enabled.isSelected()));
  location.setProperty("compression_format",new String[]{"zip","tar.gz","gz","7z"}[format.getSelectedIndex()]);
  location.setProperty("compression_mode",mode.getSelectedIndex()==0?"combined":"individual");location.setProperty("compression_archive_name",archiveName.getText().trim());
  char[] chars=password.getPassword();try{String text=new String(chars);PasswordUtils.setPassword(location,"compression_password",text.isEmpty()||(!source&&format.getSelectedIndex()!=0)?Optional.<Password>absent():Optional.of(Password.encrypt(text)));}finally{java.util.Arrays.fill(chars,'\0');}
 }
}
