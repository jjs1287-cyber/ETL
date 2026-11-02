package com.etlforge.integration.client.ui.interchange.project.backup;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListUtils;

public final class ProjectBackupListBrowser implements ProjectBackupBrowser {
   private final Set<ProjectBackupListBrowser.ComboItem> availableDays = new TreeSet<>();
   private final JComboBox daySelector;
   private final KongaList backupSelector;
   private SelectionAction inspectBackupAction;

   public ProjectBackupListBrowser() {
      this(new AvailableBackup[0]);
   }

   public ProjectBackupListBrowser(AvailableBackup[] var1) {
      this.buildDataStructure(var1);
      this.daySelector = this.createDaySelector();
      this.backupSelector = this.createBackupList();
      this.daySelector.addActionListener(new ProjectBackupListBrowser.ComboListener());
   }

   @Override
   public AvailableBackup[] getSelectedBackups() {
      ArrayList var1 = Lists.newArrayList();

      for (Object var3 : this.backupSelector.getSelection().items()) {
         if (var3 instanceof AvailableBackup) {
            var1.add((AvailableBackup)var3);
         }
      }

      return (AvailableBackup[]) var1.toArray(new AvailableBackup[var1.size()]);
   }

   @Override
   public void setBackups(AvailableBackup[] var1) {
      this.availableDays.clear();
      this.buildDataStructure(var1);
      final ComboBoxModel var2 = this.createDaySelectorModel();
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProjectBackupListBrowser.this.daySelector.setModel(var2);
            ProjectBackupListBrowser.this.backupSelector.setModel(new DefaultListModel());
            ProjectBackupListBrowser.this.selectFirst();
         }
      });
   }

   @Override
   public void selectFirst() {
      if (this.daySelector.getItemCount() > 0) {
         this.daySelector.setSelectedIndex(0);
      }
   }

   @Override
   public void setInspectBackupAction(SelectionAction var1) {
      if (this.inspectBackupAction != null) {
         this.inspectBackupAction.setSelectionSource(null);
      }

      var1.setSelectionSource(this);
      this.inspectBackupAction = var1;
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.backupSelector.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
      var2.north("Select a day:").center(this.daySelector);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 3);
      BoxBuilder var4 = BoxBuilder.horizontal().bottomAligned();
      var4.add("Select a backup:").glue().add(this.inspectBackupAction);
      var3.north(var4).center(this.backupSelector);
      BorderLayoutBuilder var5 = new BorderLayoutBuilder(0, 10);
      var5.north(var2).center(var3).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.daySelector.requestFocusInWindow();
   }

   public IndexedSelection getSelection() {
      return this.backupSelector.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.backupSelector.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.backupSelector.removeSelectionListener(var1);
   }

   private void buildDataStructure(AvailableBackup[] var1) {
      ProjectBackupListBrowser.ComboItem var2 = null;

      for (AvailableBackup var6 : var1) {
         var2 = this.getDayNode(var2, var6);
         var2.backups.add(new ProjectBackupListBrowser.ListItem(var6));
      }
   }

   private ProjectBackupListBrowser.ComboItem getDayNode(ProjectBackupListBrowser.ComboItem var1, AvailableBackup var2) {
      if (var1 != null && var1.isSameDayAs(var2)) {
         return var1;
      }

      ProjectBackupListBrowser.ComboItem var3 = new ProjectBackupListBrowser.ComboItem(var2.getTimestamp());
      this.availableDays.add(var3);
      return var3;
   }

   private JComboBox createDaySelector() {
      ComboBoxModel var1 = this.createDaySelectorModel();
      JComboBox var2 = new JComboBox(var1);
      var2.setRenderer(new DefaultListDecorRenderer());
      return var2;
   }

   private ComboBoxModel createDaySelectorModel() {
      DefaultComboBoxModel var1 = new DefaultComboBoxModel();
      if (this.availableDays.isEmpty()) {
         var1.addElement("[No backups are available]");
      } else {
         for (ProjectBackupListBrowser.ComboItem var3 : this.availableDays) {
            var1.addElement(var3);
         }
      }

      return var1;
   }

   private KongaList createBackupList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setDragSource(true);
      var1.setTransferHandler(new AvailableBackupTransferHandler(this));
      return var1.newList();
   }

   private static final class ComboItem implements DayOfBackup, Comparable<DayOfBackup>, ListDecorator {
      public final Date date;
      public final List<AvailableBackup> backups;

      public ComboItem(Date var1) {
         this.date = var1;
         this.backups = Lists.newArrayList();
      }

      @Override
      public Date getDate() {
         return this.date;
      }

      @Override
      public boolean equals(Object var1) {
         return var1 instanceof ProjectBackupListBrowser.ComboItem && this.date.equals(((ProjectBackupListBrowser.ComboItem)var1).date);
      }

      @Override
      public int hashCode() {
         return this.date.hashCode();
      }

      public int compareTo(DayOfBackup var1) {
         return var1.getDate().compareTo(this.date);
      }

      public boolean isSameDayAs(AvailableBackup var1) {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(this.date);
         Calendar var3 = Calendar.getInstance();
         var3.setTime(var1.getTimestamp());
         return var2.get(1) == var3.get(1) && var2.get(6) == var3.get(6);
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.toString());
         var1.setToolTip(RendererUtils.tooltip(this));
      }

      @Override
      public String toString() {
         return RendererUtils.display(this);
      }
   }

   private class ComboListener implements ActionListener {
      private ComboListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Object var2 = ProjectBackupListBrowser.this.daySelector.getSelectedItem();
         if (var2 instanceof ProjectBackupListBrowser.ComboItem) {
            ProjectBackupListBrowser.this.backupSelector.setModel(ListUtils.createModel(((ProjectBackupListBrowser.ComboItem)var2).backups));
            ProjectBackupListBrowser.this.backupSelector.selectFirst();
            ProjectBackupListBrowser.this.backupSelector.requestFocus();
         }
      }
   }

   private static class ListItem implements AvailableBackup, ListDecorator {
      private final AvailableBackup impl;
      private final String string;

      public ListItem(AvailableBackup var1) {
         this.impl = var1;
         this.string = RendererUtils.display(this);
      }

      @Override
      public ProjectLocation getLocation() {
         return this.impl.getLocation();
      }

      @Override
      public Date getTimestamp() {
         return this.impl.getTimestamp();
      }

      @Override
      public String toString() {
         return this.string;
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.string);
         var1.setToolTip(RendererUtils.tooltip(this));
         var1.setIcon(ClientIcons.getIcon("Backup.24"));
      }
   }
}
