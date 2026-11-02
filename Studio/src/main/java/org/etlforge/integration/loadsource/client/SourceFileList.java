package org.etlforge.integration.loadsource.client;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.ListModel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.loadsource.client.resources.LoadSourceResources;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.widget.list.ItalicTextListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class SourceFileList implements UiProvider {
   private final KongaList list;
   private Receiver<Void> finalSelectionMonitor;
   private Action deleteAction;

   public SourceFileList(FileUi[] var1) {
      ListModel var2 = this.createListModel(var1);
      if (var1 != null && var1.length > 0) {
         this.list = this.createListWithFiles(var2);
      } else {
         this.list = this.createEmptyList(var2);
      }

      this.finalSelectionMonitor = NoReceiver.create();
   }

   private ListModel createListModel(FileUi[] var1) {
      if (var1.length == 0) {
         Font var2 = TextStyles.DefaultListText.getFont();
         String var3 = LoadSourceResources.getString("SourceDataFileProvider.NoFilesAvailable");
         ItalicTextListDecorator var4 = new ItalicTextListDecorator(var3, null, null, var2);
         return ListUtils.createModel(var4);
      } else {
         return ListUtils.createModel(var1);
      }
   }

   private KongaList createListWithFiles(ListModel var1) {
      KongaListFactory var2 = this.getListFactory();
      var2.setPopupMenuRequestHandler(new PopupMenuRequestHandler() {
         @Override
         public boolean popupMenuRequested(Component var1, MouseEvent var2x) {
            if (SourceFileList.this.deleteAction == null) {
               return false;
            }

            KongaPopupMenu var3 = new KongaPopupMenu();
            var3.add(SourceFileList.this.deleteAction);
            var3.show(var1, var2x.getX(), var2x.getY());
            return true;
         }
      });
      KongaList var3 = var2.newList(var1);
      var3.setDefaultAction(new AbstractSelectionAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (SourceFileList.this.getSelectedFile() != null) {
               SourceFileList.this.finalSelectionMonitor.handle(null);
            }
         }
      });
      var3.selectFirst();
      return var3;
   }

   private KongaListFactory getListFactory() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(6);
      return var1;
   }

   private KongaList createEmptyList(ListModel var1) {
      KongaListFactory var2 = this.getListFactory();
      return var2.newList(var1);
   }

   public void setFiles(FileUi[] var1) {
      ListModel var2 = this.createListModel(var1);
      this.list.setModel(var2);
      this.list.selectFirst();
   }

   public void setFinalSelectionMonitor(Receiver<Void> var1) {
      this.finalSelectionMonitor = var1;
   }

   public void setDeleteAction(SelectionAction var1) {
      var1.setSelectionSource(this.list);
      this.list.addKeyBinding(KeyUtils.DELETE, "delete", var1);
      this.deleteAction = var1;
   }

   public FileUi getSelectedFile() {
      Object var1 = this.list.getSelection().head();
      return var1 instanceof FileUi ? (FileUi)var1 : null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 2);
      var2.center(this.list);
      if (this.deleteAction != null) {
         BoxBuilder var3 = this.layoutControlPanel();
         var2.south(var3);
      }

      var2.decorate(var1);
   }

   private BoxBuilder layoutControlPanel() {
      JButton var1 = new JButton(this.deleteAction);
      var1.setText("");
      return BoxBuilder.horizontal().glue().add(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }
}
