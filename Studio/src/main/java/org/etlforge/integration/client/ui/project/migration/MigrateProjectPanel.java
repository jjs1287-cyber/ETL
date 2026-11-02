package org.etlforge.integration.client.ui.project.migration;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.project.migration.ProjectMigration;
import org.etlforge.integration.data.project.IntegrationProjectLock;

public final class MigrateProjectPanel extends JPanel {
   private final ProjectManager manager;
   private final ManagedProject source;
   private final JTextField destination=new JTextField();
   private final JButton browse=new JButton("Browse..."),preview=new JButton("Preview"),migrate=new JButton("Migrate"),close=new JButton("Close");
   private final JLabel status=new JLabel("Select the destination Studio project and preview the changes.");
   private final DefaultTableModel rows=new DefaultTableModel(new String[]{"Object","Type","Action"},0) { public boolean isCellEditable(int row,int column){return false;} };
   private boolean busy;
   public boolean isBusy(){return busy;}
   private File reviewed;
   private long reviewedStamp;
   public MigrateProjectPanel(ProjectManager manager,ManagedProject source) {
      super(new BorderLayout(10,10));this.manager=manager;this.source=source;
      setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
      JPanel top=new JPanel(new GridLayout(0,1,0,8));
      top.add(new JLabel("From: "+source.getProject().getName()));
      JPanel selector=new JPanel(new BorderLayout(8,0));selector.add(new JLabel("Destination project:"),BorderLayout.WEST);selector.add(destination);selector.add(browse,BorderLayout.EAST);top.add(selector);
      top.add(new JLabel("Replace matching objects. Preserve existing Project Variables, sources and targets."));
      top.add(new JLabel("Create missing objects. Keep destination-only objects. A backup is created before migration."));
      add(top,BorderLayout.NORTH);add(new JScrollPane(new JTable(rows)),BorderLayout.CENTER);
      JPanel bottom=new JPanel(new BorderLayout(5,8));bottom.add(status,BorderLayout.NORTH);
      JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));buttons.add(preview);buttons.add(migrate);buttons.add(close);bottom.add(buttons,BorderLayout.SOUTH);add(bottom,BorderLayout.SOUTH);
      migrate.setEnabled(false);
      destination.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){changed();}public void removeUpdate(DocumentEvent e){changed();}public void changedUpdate(DocumentEvent e){changed();}});
      browse.addActionListener(e->{JFileChooser chooser=new JFileChooser(manager.getProjectDirectory().getLocation());chooser.setFileFilter(new FileNameExtensionFilter("ETLForge project (*.jip)","jip"));if(chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)destination.setText(chooser.getSelectedFile().getAbsolutePath());});
      preview.addActionListener(e->preview());migrate.addActionListener(e->migrate());close.addActionListener(e->{Window window=SwingUtilities.getWindowAncestor(this);if(window!=null&&!busy)window.dispose();});
   }
   private void changed(){reviewed=null;migrate.setEnabled(false);rows.setRowCount(0);}
   private void busy(boolean value){busy=value;destination.setEnabled(!value);browse.setEnabled(!value);preview.setEnabled(!value);close.setEnabled(!value);migrate.setEnabled(!value&&reviewed!=null);}
   private static String name(File file){String name=file.getName();if(!name.endsWith(".jip"))throw new IllegalArgumentException("Select a Studio project (.jip) file.");return name.substring(0,name.length()-4);}
   private void fail(Exception error){Throwable cause=error.getCause()==null?error:error.getCause();status.setText("Migration stopped: "+cause.getMessage());JOptionPane.showMessageDialog(this,cause.getMessage(),"Migrate Project",JOptionPane.ERROR_MESSAGE);}
   private void preview(){
      File file=new File(destination.getText().trim());busy(true);status.setText("Reading destination project...");
      new SwingWorker<ProjectMigration.Plan,Void>(){
         protected ProjectMigration.Plan doInBackground()throws Exception{
            if(file.getParentFile().getCanonicalFile().equals(source.getLocation().getCanonicalFile()))throw new IllegalArgumentException("Select a different destination project.");
            IntegrationProjectLock.acquire();ManagedProject target=null;
            try{target=manager.inspect(name(file),file.getParentFile());return ProjectMigration.plan(source.getProject(),target.getProject());}
            finally{try{if(target!=null)manager.closeProject(target);}finally{IntegrationProjectLock.release();}}
         }
         protected void done(){try{ProjectMigration.Plan plan=get();rows.setRowCount(0);for(ProjectMigration.Entry entry:plan.entries)rows.addRow(new Object[]{entry.source.getName(),entry.source.getEntityType().getDisplayName(),entry.action});reviewed=file;reviewedStamp=file.lastModified();status.setText(plan.summary()+". Ready to migrate.");}catch(Exception error){reviewed=null;fail(error);}finally{busy(false);}}
      }.execute();
   }
   private void migrate(){
      final File file=reviewed;if(file==null)return;
      if(file.lastModified()!=reviewedStamp){changed();status.setText("Destination changed. Preview it again before migrating.");return;}
      busy(true);status.setText("Migrating project...");
      new SwingWorker<ProjectMigration.Result,Void>(){
         protected ProjectMigration.Result doInBackground()throws Exception{return ProjectMigration.migrate(manager,source,file);}
         protected void done(){try{ProjectMigration.Result result=get();reviewed=null;status.setText("Complete: "+result.plan.summary());JOptionPane.showMessageDialog(MigrateProjectPanel.this,"Migration complete.\n"+result.plan.summary()+"\nBackup: "+result.backup,"Migrate Project",JOptionPane.INFORMATION_MESSAGE);}catch(Exception error){reviewed=null;fail(error);}finally{busy(false);}}
      }.execute();
   }
}
