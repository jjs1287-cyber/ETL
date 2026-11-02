package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.ListStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ExistingWsdlFileSelector extends AbstractWsdlSelector implements WsdlFileDisplayer {
   private final FileStore fileStore;
   private final KongaList wsdlList;
   private final Action refreshAction;
   private volatile ServerInfo serverInfo;

   public ExistingWsdlFileSelector(FileStore var1) {
      this.fileStore = var1;
      this.refreshAction = new ExistingWsdlFileSelector.RefreshAction();
      this.wsdlList = this.createList();
      this.layoutComponents();
   }

   @Override
   public void installUndoRedo(UndoRedoInstaller var1) {
   }

   @Override
   public void displayFiles(ServerInfo var1, List<WsdlFile> var2) {
      this.serverInfo = var1;
      DefaultListModel var3 = new DefaultListModel();
      if (var2 != null && !var2.isEmpty()) {
         Collections.sort(var2, WsdlFile.LOCATOR_ORDER);

         for (WsdlFile var5 : var2) {
            var3.addElement(new ExistingWsdlFileSelector.WsdlFileListItem(var5));
         }
      } else {
         var3.addElement(PackageResources.ExistingWsdlFileDefiner.NO_FILES_AVAILABLE);
      }

      this.updateList(var3);
   }

   @Override
   public void requestFocus() {
      this.wsdlList.requestFocus();
   }

   @Override
   public void setActive(boolean var1) {
      this.wsdlList.setEnabled(var1);
      if (var1) {
         this.requestFocus();
         if (this.getSelectedFile() == null) {
            this.setDefaultSelection();
         }
      }
   }

   @Override
   public WsdlProcessingJob getProcessingJob() {
      WsdlFile var1 = this.getSelectedFile();
      return var1 == null ? null : new ProcessExistingWsdlFile(this.fileStore, this.serverInfo, var1);
   }

   @Override
   public boolean isValid() {
      return this.getSelectedFile() != null;
   }

   public void populate(boolean var1) {
      GetWsdlFilesJob var2 = new GetWsdlFilesJob(this, var1);
      int var3 = this.getProgressDialogDelay();
      var2.setUseProgressDialog(var3);
      var2.submit();
   }

   private int getProgressDialogDelay() {
      boolean var1 = IntegrationServer.getInstance().getServerInfo() != null;
      return var1 ? 200 : 0;
   }

   private void updateList(final DefaultListModel var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExistingWsdlFileSelector.this.wsdlList.setModel(var1);
            ExistingWsdlFileSelector.this.wsdlList.selectFirst();
            ExistingWsdlFileSelector.this.refreshAction.setEnabled(true);
         }
      });
   }

   private void setDefaultSelection() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExistingWsdlFileSelector.this.wsdlList.selectFirst();
         }
      });
   }

   private WsdlFile getSelectedFile() {
      IndexedSelection var1 = this.wsdlList.getSelection();
      Object var2 = var1.head();
      return var2 instanceof ExistingWsdlFileSelector.WsdlFileListItem ? ((ExistingWsdlFileSelector.WsdlFileListItem)var2).file : null;
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(4);
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      ListStyles.normal().makeOver(var1);
      KongaList var2 = var1.newList();
      var2.setDefaultAction(new AbstractSelectionAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ExistingWsdlFileSelector.this.fireActionEvent();
         }
      });
      var2.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            ExistingWsdlFileSelector.this.fireInputChanged();
         }
      });
      return var2;
   }

   private void layoutComponents() {
      UiProvider var1 = this.createUi();
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      this.registerKeyBindings(var2);
      this.setDisplayer(var2);
   }

   private UiProvider createUi() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      var1.withBorder(Empty.border(FIELD_INSETS));
      var1.center(this.wsdlList);
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.add(InvisiblePanel.of(this.refreshAction)).glue();
      var1.south(var2);
      return var1;
   }

   private void registerKeyBindings(JComponent var1) {
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1, 1);
      this.registerRefreshActionKeyBinding(var2);
      var2.install();
   }

   private void registerRefreshActionKeyBinding(KeyBindingInstaller var1) {
      var1.addBinding(KeyUtils.F5, "refresh", this.refreshAction);
   }

   private class RefreshAction extends AbstractAction {
      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.setEnabled(false);
         ExistingWsdlFileSelector.this.populate(false);
      }
   }

   private static class WsdlFileListItem implements ListDecorator {
      private final WsdlFile file;
      private final String text;

      public WsdlFileListItem(WsdlFile var1) {
         this.file = var1;
         this.text = var1.getLocator();
      }

      @Override
      public String toString() {
         return this.text;
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.text);
         var1.setToolTip(this.text);
      }
   }
}
