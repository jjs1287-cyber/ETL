package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdUploadManager;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public final class XmlNodeSettings {
 private XmlNodeSettings(){}
 public static boolean supports(TreeComponent tree,Node node) {
  if(tree.getTreeMapper()==null||node==null||!node.getCROM().isXml()||Boolean.TRUE.equals(tree.getClientProperty("XmlNodeSettingsBusy")))return false;
  Transformation tx=tree.getTreeMapper().getTransformation();
  DataStructure structure=tree.m_bTarget?tx.getOutputStructure():tx.getInputStructure();
  return structure instanceof XmlStructure&&!ProjectUtils.getManagedProject(tx).isReadOnly();
 }
 public static List<EditableXsd.Step> path(CROM selected) {
  List<EditableXsd.Step> path=new ArrayList<>();
  for(CROM c=selected;c!=null;c=c.getParent())if((c.isElementNode()||c.isAttributeNode())&&!c.isTypeDefinition()&&!c.isSubstitutionGroupHead()&&!c.getDisplayName().isEmpty()){
   String name=c.getName();int colon=name.indexOf(':');if(colon>=0)name=name.substring(colon+1);
   path.add(new EditableXsd.Step(name,c.getNamespace(),c.isAttributeNode()));
  }
  Collections.reverse(path);return path;
 }
 public static void show(TreeComponent tree,Node node) {
  TreeMapper mapper=tree.getTreeMapper();Transformation tx=mapper.getTransformation();
  ManagedProject project=ProjectUtils.getManagedProject(tx);
  XmlStructure structure=(XmlStructure)(tree.m_bTarget?tx.getOutputStructure():tx.getInputStructure());
  EditableXsd edit;
  try {
   FileStoreFile file=project.getFileStore().getFile(structure.getFilePath());
   if(file==null)throw new IOException("The XSD is not stored locally in this project. Open Edit Structure and download it first.");
   CROM selected=node.getCROM();
   edit=new EditableXsd(file.getPhysicalRepresentation(),path(selected),selected.isValueLeaf()&&!node.getDisplay().contains("[EV"));
  }catch(Exception e){error(tree,e);return;}
  String[] kinds={"E — Element (EV when it has a scalar value)","A — Attribute","V — Parent text value"};
  JComboBox<String> kind=new JComboBox<>(kinds);kind.setSelectedIndex(edit.getKind().ordinal());
  JComboBox<String> occurrence=new JComboBox<>(new String[]{"E — Exactly one (required)","E? — Zero or one (optional)","E+ — One or more","E* — Zero or more","Custom occurrence range"});
  int min=edit.getMin(),max=edit.getMax();
  occurrence.setSelectedIndex(min==1&&max==1?0:min==0&&max==1?1:min==1&&max==-1?2:min==0&&max==-1?3:4);
  JTextField minimum=new JTextField(Integer.toString(min),8),maximum=new JTextField(max==-1?"unbounded":Integer.toString(max),12);
  JPanel form=new JPanel(new GridBagLayout());GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(5,5,5,5);g.anchor=GridBagConstraints.WEST;g.fill=GridBagConstraints.HORIZONTAL;
  add(form,g,0,"Node:",new JLabel(edit.getName()));add(form,g,1,"Node kind:",kind);add(form,g,2,"Occurrences:",occurrence);add(form,g,3,"Minimum:",minimum);add(form,g,4,"Maximum:",maximum);
  JLabel note=new JLabel("<html>A private XSD copy is saved with this transformation.<br>Attributes cannot repeat. The document root must occur once.<br>V is text inside the parent, not a separate XML element.<br>Changing node kind requires a transformation without mappings.</html>");
  g.gridx=0;g.gridy=5;g.gridwidth=2;form.add(note,g);
  Runnable update=()->{
   int selected=occurrence.getSelectedIndex();minimum.setEditable(selected==4);maximum.setEditable(selected==4);
   if(selected<4){minimum.setText(selected==1||selected==3?"0":"1");maximum.setText(selected>=2?"unbounded":"1");}
  };
  occurrence.addActionListener(e->update.run());update.run();
  if(edit.isRoot()||edit.getKind()==EditableXsd.Kind.VALUE){kind.setEnabled(false);occurrence.setEnabled(false);minimum.setEditable(false);maximum.setEditable(false);}
  for(JTextField field:new JTextField[]{minimum,maximum}){field.setBackground(new Color(28,28,28));field.setForeground(Color.WHITE);field.setCaretColor(Color.WHITE);}
  int answer=JOptionPane.showConfirmDialog(tree,form,"XML Node Settings",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
  if(answer!=JOptionPane.OK_OPTION){close(edit);return;}
  try {
   mapper.applyCurrentFormulas();
   EditableXsd.Kind requested=EditableXsd.Kind.values()[kind.getSelectedIndex()];
   if(requested!=edit.getKind()&&mapper.getMappingManager().getMappings().iterator().hasNext())throw new IOException("Changing element/attribute/text kind changes mapping paths. Create or copy an unmapped transformation first. Occurrence counts can be changed with mappings in place.");
   int newMin=Integer.parseInt(minimum.getText().trim());String maximumText=maximum.getText().trim();int newMax="unbounded".equalsIgnoreCase(maximumText)||"*".equals(maximumText)?-1:Integer.parseInt(maximumText);
   if(requested==edit.getKind()&&newMin==edit.getMin()&&newMax==edit.getMax()){close(edit);return;}
   edit.change(requested,newMin,newMax,edit.getName());
  }catch(Exception e){close(edit);error(tree,e);return;}
  tree.putClientProperty("XmlNodeSettingsBusy",Boolean.TRUE);tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));tree.setEnabled(false);
  new SwingWorker<Result,Void>() {
   protected Result doInBackground() throws Exception {
    File changed=edit.save();
    XmlInfoProvider api=IntegrationServer.getInstance().getServerCall(XmlInfoProvider.class);
    XsdUploadManager uploader=new XsdUploadManager(api,new XsdFileStore(project.getFileStore()));
    uploader.upload(null,changed.getAbsolutePath());
    if(!uploader.isSuccess())throw new IOException("The edited XSD could not be uploaded; the transformation was not changed.");
    XmlStructure replacement=new XmlStructure(structure);replacement.setFilePath(uploader.getMainFile().getFilePath());
    Transformation candidate=new Transformation("Schema edit preview");
    MappingTreeStructure mapping;
    if(tree.m_bTarget){candidate.setOutputStructure(replacement);mapping=mapper.getMappingStructureFactory().getOutputStructure(candidate);}
    else {candidate.setInputStructure(replacement);mapping=mapper.getMappingStructureFactory().getInputStructure(candidate);}
    if(mapping==null)throw new IOException("The server could not build the edited schema. The transformation was not changed.");
    return new Result(replacement,mapping);
   }
   protected void done(){
    try {
     Result result=get();
     org.etlforge.integration.data.project.IntegrationProjectLock.acquire();
     try {
      if(project.isReadOnly()||!structure.equals(tree.m_bTarget?tx.getOutputStructure():tx.getInputStructure()))throw new IOException("The structure changed while the edit was uploading. The newer structure was kept; retry this edit.");
      mapper.applyCurrentFormulas();
      if(tree.m_bTarget){tx.setOutputStructure(result.structure);tx.setOutputMappingStructure(result.mapping);}
      else {tx.setInputStructure(result.structure);tx.setInputMappingStructure(result.mapping);}
      mapper.setDirty(true);
     }finally{org.etlforge.integration.data.project.IntegrationProjectLock.release();}
    }catch(Exception e){error(tree,e.getCause()==null?e:e.getCause());}
    finally{tree.putClientProperty("XmlNodeSettingsBusy",Boolean.FALSE);tree.setCursor(Cursor.getDefaultCursor());tree.setEnabled(true);close(edit);}
   }
  }.execute();
 }
 private static final class Result {final XmlStructure structure;final MappingTreeStructure mapping;Result(XmlStructure s,MappingTreeStructure m){structure=s;mapping=m;}}
 private static void add(JPanel panel,GridBagConstraints g,int row,String label,Component input){g.gridy=row;g.gridx=0;g.weightx=0;panel.add(new JLabel(label),g);g.gridx=1;g.weightx=1;panel.add(input,g);}
 private static void close(EditableXsd edit){try{edit.close();}catch(IOException e){org.etlforge.application.errorlog.ErrorLog.log(XmlNodeSettings.class,"Could not remove schema edit workspace",e);}}
 private static void error(Component parent,Throwable error){JOptionPane.showMessageDialog(parent,error.getMessage(),"XML Node Settings",JOptionPane.ERROR_MESSAGE);}
}
