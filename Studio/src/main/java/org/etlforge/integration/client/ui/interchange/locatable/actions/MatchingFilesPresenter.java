package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.awt.Dimension;
import java.awt.Window;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.datalocation.connection.MatchingFileList;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.KongaListModel;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

final class MatchingFilesPresenter {
   private final MatchingFilesPresenter.Ui ui;

   public MatchingFilesPresenter(MatchingFileList var1) {
      this.ui = new MatchingFilesPresenter.Ui(var1);
   }

   public void showInDialog(final Window var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            KongaDialog var1x = new KongaDialog(var1, LocationResources.getString("Connection.Success.Title"));
            var1x.standardLayout(MatchingFilesPresenter.this.ui, KongaDialog.OK_ONLY);
            var1x.setOkButtonAsDefault();
            var1x.setMinimumSize(new Dimension(300, 300));
            var1x.setLocationRelativeTo(null);
            var1x.setVisible(true);
         }
      });
   }

   public void showInOverlay(final OverlayContainer var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            JPanel var1x = BorderLayoutBuilder.fillWith(MatchingFilesPresenter.this.ui);
            var1x.setBorder(Empty.border(10));
            OverlayDecorator var2 = new OverlayDecorator(var1x, LocationResources.getString("Connection.Success.Title"));
            var2.setAlpha(0.8F);
            var2.useCloseOnly();
            var2.setBlocking(true);
            var2.show(var1, new OverlayCenterPositioner());
         }
      });
   }

   private static class Ui implements UiProvider {
      private final KongaList fileList;
      private final BorderLayoutBuilder layout;

      public Ui(MatchingFileList var1) {
         this.fileList = createList(var1);
         this.layout = new BorderLayoutBuilder(0, 3);
         String var2 = getCaption(var1);
         this.layout.north(var2);
         this.layout.center(this.fileList);
      }

      private static KongaList createList(MatchingFileList var0) {
         KongaListFactory var1 = ComponentFactories.newKongaListFactory();
         KongaListModel var2 = new KongaListModel();
         if (var0.size() == 0) {
            var2.addElement(LocationResources.getString("Connection.MatchingFiles.NoFiles"));
         } else {
            for (String var4 : var0.getFileNames()) {
               var2.addElement(new MatchingFilesPresenter.Ui.FileItem(var4));
            }
         }

         return var1.newList(var2);
      }

      private static String getCaption(MatchingFileList var0) {
         switch (var0.size()) {
            case 0:
               return LocationResources.getString("Connection.Caption.NoFiles");
            case 1:
               return LocationResources.getString("Connection.Caption.OneFile");
            default:
               return LocationResources.format("Connection.Caption.ManyFiles", var0.size());
         }
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.fileList.requestFocus();
      }

      private static class FileItem implements ListDecorator {
         private static final Icon ICON = ApplicationResources.ICONS.getIcon("TextFile.16");
         private final String name;

         public FileItem(String var1) {
            this.name = var1;
         }

         @Override
         public void decorate(ListDecor var1, boolean var2) {
            var1.setIcon(ICON);
            var1.setText(this.name);
         }

         @Override
         public String toString() {
            return this.name;
         }
      }
   }
}
