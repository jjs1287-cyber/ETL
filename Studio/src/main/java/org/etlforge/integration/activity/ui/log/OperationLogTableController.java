package org.etlforge.integration.activity.ui.log;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.log.OperationLogEntryPlainTextRepresentation;
import org.etlforge.integration.activity.client.log.OperationLogEntryTextRepresentation;
import org.etlforge.integration.activity.client.log.OperationLogEntryToClipBoard;
import org.etlforge.integration.activity.client.log.OperationLogEntryXmlRepresentation;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.util.preference.PreferencesKey;

final class OperationLogTableController {
   private static final RecentFileStore LAST_SAVED_FILE;
   private final OperationLogTable table;
   private final OperationLogTableController.TableAction[] copyActions;
   private final OperationLogTableController.TableAction[] saveActions;

   OperationLogTableController(OperationLogTable var1) {
      assert var1 != null;
      this.table = var1;
      OperationLogEntryPlainTextRepresentation var2 = new OperationLogEntryPlainTextRepresentation();
      OperationLogEntryXmlRepresentation var3 = new OperationLogEntryXmlRepresentation();
      this.copyActions = this.createCopyActions(var2, var3);
      this.saveActions = this.createSaveActions(var2, var3);
   }

   private OperationLogTableController.TableAction[] createCopyActions(OperationLogEntryTextRepresentation var1, OperationLogEntryTextRepresentation var2) {
      return new OperationLogTableController.TableAction[]{
         new OperationLogTableController.CopyToClipBoardAction(PackageResources.TableController.AS_PLAIN_TEXT, var1),
         new OperationLogTableController.CopyToClipBoardAction(PackageResources.TableController.AS_XML, var2)
      };
   }

   private OperationLogTableController.TableAction[] createSaveActions(OperationLogEntryTextRepresentation var1, OperationLogEntryTextRepresentation var2) {
      return new OperationLogTableController.TableAction[]{
         new OperationLogTableController.SaveAction(PackageResources.TableController.AS_PLAIN_TEXT, var1, ".txt"),
         new OperationLogTableController.SaveAction(PackageResources.TableController.AS_XML, var2, ".xml")
      };
   }

   public void install() {
      this.table.addMouseListener(new OperationLogTableController.PopupMenuHandler());
   }

   static {
      PreferencesKey var0 = new PreferencesKey(OperationLogTable.class, "LastSavedFile");
      LAST_SAVED_FILE = new PreferencesFileStore(var0, 1);
   }

   private abstract static class AbstractTableAction extends AbstractAction implements OperationLogTableController.TableAction {
      private final MnemonicString name;
      private OperationLogEntry selected;

      public AbstractTableAction(MnemonicString var1) {
         super(var1.getText());
         this.name = var1;
      }

      @Override
      public MnemonicString getName() {
         return this.name;
      }

      @Override
      public void setSelectedLogEntry(OperationLogEntry var1) {
         this.selected = var1;
         this.setEnabled(var1 != null);
      }

      protected final OperationLogEntry getSelectedEntry() {
         return this.selected;
      }
   }

   private static final class CopyToClipBoardAction extends OperationLogTableController.AbstractTableAction {
      private final OperationLogEntryToClipBoard toClipBoard;

      public CopyToClipBoardAction(MnemonicString var1, OperationLogEntryTextRepresentation var2) {
         super(var1);
         this.toClipBoard = new OperationLogEntryToClipBoard(var2);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OperationLogEntry var2 = this.getSelectedEntry();
         if (var2 != null) {
            this.toClipBoard.copy(var2);
         }
      }
   }

   private class PopupMenuHandler extends MouseActionListener {
      private JPopupMenu popup;

      private PopupMenuHandler() {
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         OperationLogEntry var2 = OperationLogTableController.this.table.getSelectedLogEntry();

         for (OperationLogTableController.TableAction var6 : OperationLogTableController.this.copyActions) {
            var6.setSelectedLogEntry(var2);
         }

         for (OperationLogTableController.TableAction var10 : OperationLogTableController.this.saveActions) {
            var10.setSelectedLogEntry(var2);
         }

         if (this.popup == null) {
            this.popup = this.createPopup();
         }

         this.popup.show(OperationLogTableController.this.table, var1.getX(), var1.getY());
      }

      private JPopupMenu createPopup() {
         KongaPopupMenu var1 = new KongaPopupMenu();
         this.addCopyActionsToPopup(var1);
         this.addSaveActionsToPopup(var1);
         return var1;
      }

      private void addCopyActionsToPopup(JPopupMenu var1) {
         KongaMenu var2 = new KongaMenu(PackageResources.TableController.Copy.NAME, PackageResources.TableController.Copy.ICON);
         var2.setToolTipText(PackageResources.TableController.Copy.TOOLTIP);

         for (OperationLogTableController.TableAction var6 : OperationLogTableController.this.copyActions) {
            this.addTableActionMenuItem(var6, var2);
         }

         var1.add(var2);
      }

      private void addSaveActionsToPopup(JPopupMenu var1) {
         KongaMenu var2 = new KongaMenu(PackageResources.TableController.Save.NAME, PackageResources.TableController.Save.ICON);
         var2.setToolTipText(PackageResources.TableController.Save.TOOLTIP);

         for (OperationLogTableController.TableAction var6 : OperationLogTableController.this.saveActions) {
            this.addTableActionMenuItem(var6, var2);
         }

         var1.add(var2);
      }

      private void addTableActionMenuItem(OperationLogTableController.TableAction var1, JMenu var2) {
         KongaMenuItem var3 = new KongaMenuItem(var1);
         ButtonUtils.decorate(var3, var1.getName());
         var2.add(var3);
      }
   }

   private static final class SaveAction extends OperationLogTableController.AbstractTableAction {
      private final OperationLogEntryTextRepresentation textRepresenter;
      private final String fileExtension;

      public SaveAction(MnemonicString var1, OperationLogEntryTextRepresentation var2, String var3) {
         super(var1);
         this.textRepresenter = var2;
         this.fileExtension = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         final OperationLogEntry var2 = this.getSelectedEntry();
         if (var2 != null) {
            ApplicationWorker var3 = Application.getWorker();
            var3.submitForParallel(new ApplicationWorker.SafeRunnable() {
               @Override
               public void run() {
                  SaveAction.this.doSave(var2);
               }
            });
         }
      }

      private void doSave(OperationLogEntry var1) {
         File var2 = this.getOutputFile(var1);
         if (var2 != null) {
            try {
               String var3 = this.textRepresenter.getText(var1);
               KongaIoUtils.createAndWrite(var2, var3);
            } catch (IOException var4) {
               ErrorLog.attention(OperationLogTableController.class, PackageResources.TableController.Save.ERROR, var4);
            }
         }
      }

      private File getOutputFile(OperationLogEntry var1) {
         FileChooser var2 = ApplicationFileChooser.getInstance(null);
         var2.setSelectedFile(this.getSuggestedFile(var1));
         return var2.getFileToSave(
            UiUtils.getActiveFrame(),
            new SuffixFileChooserFilter(this.fileExtension),
            OperationLogTableController.LAST_SAVED_FILE,
            null,
            this.getSuggestedFile(var1)
         );
      }

      private File getSuggestedFile(OperationLogEntry var1) {
         File var2 = null;
         FileUi var3 = OperationLogTableController.LAST_SAVED_FILE.latestFile();
         if (var3 != null) {
            var2 = var3.getFile().getParentFile();
         } else {
            var2 = SystemUtils.USER_HOME;
         }

         return new File(var2, var1.getOperationName() + this.fileExtension);
      }
   }

   private interface TableAction extends Action {
      void setSelectedLogEntry(OperationLogEntry var1);

      MnemonicString getName();
   }
}
