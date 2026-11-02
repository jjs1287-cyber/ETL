package org.etlforge.integration.client.ui.admin.compare;
import java.awt.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.project.compare.ProjectComparison;
import org.etlforge.integration.data.project.IntegrationProjectLock;
public final class ProjectComparisonPanel extends JPanel {
 private final ProjectManager manager;
 private final JTextField left=new JTextField(),right=new JTextField();
 private final JButton compare=new JButton("Compare"),leftBrowse=new JButton("Browse..."),rightBrowse=new JButton("Browse...");
 private final JLabel status=new JLabel("Choose two local Studio projects (.jip). Positioning and deployment state are ignored.");
 private final DefaultTableModel rows=new DefaultTableModel(new String[]{"Object","Type","Difference"},0){public boolean isCellEditable(int r,int c){return false;}};
 private final DefaultTableModel fields=new DefaultTableModel(new String[]{"Field","Left project","Right project"},0){public boolean isCellEditable(int r,int c){return false;}};
 private java.util.List<ProjectComparison.Difference> differences=Collections.emptyList();
 private final JTable table=new JTable(rows);
 private final JTable fieldTable=new JTable(fields);
 private final JTextArea leftValue=new JTextArea(),rightValue=new JTextArea();
 public ProjectComparisonPanel(ProjectManager manager){super(new BorderLayout(8,8));this.manager=manager;setBorder(BorderFactory.createEmptyBorder(16,16,16,16));JPanel top=new JPanel(new GridLayout(0,1,5,8));JLabel title=new JLabel("Project Differences");title.setFont(title.getFont().deriveFont(20f));top.add(title);top.add(selector("Left project:",left,leftBrowse));top.add(selector("Right project:",right,rightBrowse));top.add(compare);add(top,BorderLayout.NORTH);JSplitPane split=new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(table),new JScrollPane(fieldTable));split.setResizeWeight(.5);leftValue.setEditable(false);rightValue.setEditable(false);JSplitPane values=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,new JScrollPane(leftValue),new JScrollPane(rightValue));values.setResizeWeight(.5);JSplitPane full=new JSplitPane(JSplitPane.VERTICAL_SPLIT,split,values);full.setResizeWeight(.72);add(full);fieldTable.getSelectionModel().addListSelectionListener(e->{int row=fieldTable.getSelectedRow();leftValue.setText(row<0?"":String.valueOf(fields.getValueAt(row,1)));rightValue.setText(row<0?"":String.valueOf(fields.getValueAt(row,2)));leftValue.setCaretPosition(0);rightValue.setCaretPosition(0);});add(status,BorderLayout.SOUTH);leftBrowse.addActionListener(e->browse(left));rightBrowse.addActionListener(e->browse(right));compare.addActionListener(e->load());table.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())details();});}
 private JPanel selector(String label,JTextField text,JButton button){JPanel p=new JPanel(new BorderLayout(8,0));p.add(new JLabel(label),BorderLayout.WEST);p.add(text);p.add(button,BorderLayout.EAST);return p;}
 private void browse(JTextField field){JFileChooser c=new JFileChooser(manager.getProjectDirectory().getLocation());c.setFileFilter(new FileNameExtensionFilter("ETLForge project (*.jip)","jip"));if(c.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)field.setText(c.getSelectedFile().getAbsolutePath());}
 private File selected(JTextField text)throws Exception{File f=new File(text.getText().trim()).getCanonicalFile();if(!f.isFile()||!f.getName().endsWith(".jip"))throw new IllegalArgumentException("Select an existing Studio project (.jip) for each side.");return f;}
 private void controls(boolean busy){for(JComponent c:new JComponent[]{left,right,leftBrowse,rightBrowse,compare})c.setEnabled(!busy);}
 private void load(){try{final File a=selected(left),b=selected(right);controls(true);rows.setRowCount(0);fields.setRowCount(0);status.setText("Reading project snapshots...");new SwingWorker<java.util.List<ProjectComparison.Difference>,Void>(){protected java.util.List<ProjectComparison.Difference> doInBackground()throws Exception{IntegrationProjectLock.acquire();ManagedProject x=null,y=null;try{x=manager.inspect(a.getName().replaceFirst("\\.jip$",""),a.getParentFile());y=manager.inspect(b.getName().replaceFirst("\\.jip$",""),b.getParentFile());return ProjectComparison.compare(x,y);}finally{try{if(x!=null)manager.closeProject(x);if(y!=null)manager.closeProject(y);}finally{IntegrationProjectLock.release();}}}protected void done(){try{differences=get();for(ProjectComparison.Difference d:differences)rows.addRow(new Object[]{d.object,d.type,d.change});status.setText(differences.size()+" different objects. Positioning and deployment state ignored. Credentials masked. Saved definitions compared.");}catch(Exception ex){Throwable c=ex;while(c.getCause()!=null)c=c.getCause();status.setText("Comparison failed: "+c.getMessage());}finally{controls(false);}}}.execute();}catch(Exception ex){status.setText(ex.getMessage());}}
 private String visible(String s){if(s==null)return "(not present)";return s.startsWith("[protected SHA256 ")?"[protected value]":s;}
 private void details(){fields.setRowCount(0);int row=table.getSelectedRow();if(row<0||row>=differences.size())return;ProjectComparison.Difference d=differences.get(row);SortedSet<String> keys=new TreeSet<>(d.left.keySet());keys.addAll(d.right.keySet());for(String key:keys)if(!Objects.equals(d.left.get(key),d.right.get(key)))fields.addRow(new Object[]{key,visible(d.left.get(key)),visible(d.right.get(key))});}
}
