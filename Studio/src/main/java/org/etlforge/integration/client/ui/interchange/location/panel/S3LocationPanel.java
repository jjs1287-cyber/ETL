package org.etlforge.integration.client.ui.interchange.location.panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.S3Location;
import org.etlforge.ui.UiProviderAdapter;

public final class S3LocationPanel extends DataLocationPanel {
 private final JTextField bucket=new JTextField(40),prefix=new JTextField(40),access=new JTextField(40);
 private final JComboBox<String> region=new JComboBox<>(new String[]{"US East — N. Virginia (us-east-1)","US East — Ohio (us-east-2)","US West — N. California (us-west-1)","US West — Oregon (us-west-2)","ca-central-1","eu-west-1","eu-west-2","eu-central-1","ap-south-1","ap-southeast-1","ap-southeast-2","ap-northeast-1","sa-east-1"});
 private final JComboBox<String> authentication=new JComboBox<>(new String[]{"Access Key","Temporary Credentials"});
 private final JPasswordField secret=new JPasswordField(40),token=new JPasswordField(40);
 private final JLabel tokenLabel=new JLabel("Session token:");
 private final JCheckBox recursive=new JCheckBox("Include subfolders"),overwrite=new JCheckBox("Overwrite existing target objects");
 private final FileLocationPanel fileOptions;
 private boolean resetting;
 public S3LocationPanel(S3Location location,SourceTarget direction){
  super(location,direction);region.setEditable(true);
  fileOptions=new FileLocationPanel(location,direction,false,this,40){@Override protected boolean needsLoginControls(){return false;}};
  JPanel root=new JPanel();root.setOpaque(false);root.setLayout(new BoxLayout(root,BoxLayout.Y_AXIS));
  JPanel connection=new JPanel(new GridBagLayout());connection.setOpaque(false);connection.setBorder(BorderFactory.createTitledBorder("Amazon S3 connection"));
  row(connection,0,new JLabel("Bucket:"),bucket);row(connection,1,new JLabel("Region:"),region);row(connection,2,new JLabel("Authentication:"),authentication);
  row(connection,3,new JLabel("Access Key ID:"),access);row(connection,4,new JLabel("Secret Access Key:"),secret);row(connection,5,tokenLabel,token);row(connection,6,new JLabel("Folder / prefix:"),prefix);
  row(connection,7,new JLabel(""),direction==SourceTarget.Source?recursive:overwrite);root.add(connection);
  root.add(new JLabel("Credentials are encrypted in the project and included in backups and source control."));
  root.add(new JLabel("Prefix is literal and case-sensitive, for example input/. Leave blank for the bucket root."));
  JPanel files=new JPanel();files.setOpaque(false);fileOptions.layoutComponents(files,null);root.add(files);wrap(UiProviderAdapter.of(root));
  DocumentListener listener=new DocumentListener(){public void insertUpdate(DocumentEvent e){changed();}public void removeUpdate(DocumentEvent e){changed();}public void changedUpdate(DocumentEvent e){changed();}};
  for(JTextField field:new JTextField[]{bucket,prefix,access,secret,token})field.getDocument().addDocumentListener(listener);
  ((JTextField)region.getEditor().getEditorComponent()).getDocument().addDocumentListener(listener);
  region.addActionListener(e->changed());authentication.addActionListener(e->{updateToken();changed();});recursive.addActionListener(e->changed());overwrite.addActionListener(e->changed());reset();
 }
 private static void row(JPanel panel,int index,JLabel label,JComponent field){GridBagConstraints c=new GridBagConstraints();c.gridx=0;c.gridy=index;c.insets=new Insets(4,6,4,6);c.anchor=GridBagConstraints.WEST;panel.add(label,c);c.gridx=1;c.weightx=1;c.fill=GridBagConstraints.HORIZONTAL;panel.add(field,c);}
 private static String regionCode(String value){int opening=value.lastIndexOf('(');return opening>=0&&value.endsWith(")")?value.substring(opening+1,value.length()-1).trim():value.trim();}
 private void selectRegion(String value){for(int i=0;i<region.getItemCount();i++)if(regionCode(region.getItemAt(i)).equals(value)){region.setSelectedIndex(i);return;}region.setSelectedItem(value);}
 private void changed(){if(!resetting)fireInputChanged(this,"Amazon S3");}
 private void updateToken(){boolean temporary=authentication.getSelectedIndex()==1;token.setVisible(temporary);tokenLabel.setVisible(temporary);getUi().revalidate();}
 @Override public void reset(){resetting=true;try{S3Location l=(S3Location)getDisplayedObject();bucket.setText(l.getBucket());selectRegion(l.getRegion());prefix.setText(l.getPrefix());authentication.setSelectedIndex(l.getAuthentication().equals("temporary")?1:0);access.setText(l.getCredential("s3_access_key_protected"));secret.setText(l.getCredential("s3_secret_key_protected"));token.setText(l.getCredential("s3_session_token_protected"));recursive.setSelected(l.getBooleanProperty("s3_recursive",true));overwrite.setSelected(l.getBooleanProperty("s3_overwrite",true));fileOptions.reset(l);updateToken();}finally{resetting=false;}}
 @Override public void apply(){S3Location l=(S3Location)getDisplayedObject();fileOptions.apply(l);l.setBucket(bucket.getText());l.setRegion(regionCode(String.valueOf(region.getEditor().getItem())));l.setPrefix(prefix.getText());l.setAuthentication(authentication.getSelectedIndex()==0?"access_key":"temporary");l.setCredential("s3_access_key_protected",access.getText().trim());saveSecret(l,"s3_secret_key_protected",secret);if(authentication.getSelectedIndex()==1)saveSecret(l,"s3_session_token_protected",token);else l.setCredential("s3_session_token_protected","");l.setProperty("s3_recursive",Boolean.toString(recursive.isSelected()));l.setProperty("s3_overwrite",Boolean.toString(overwrite.isSelected()));}
 private void saveSecret(S3Location l,String key,JPasswordField field){char[] chars=field.getPassword();try{l.setCredential(key,new String(chars));}finally{java.util.Arrays.fill(chars,'\0');}}
 @Override public void display(){super.display();}
 @Override public void setInitialFocus(){bucket.requestFocusInWindow();}
}
