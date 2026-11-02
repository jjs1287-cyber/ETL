package org.etlforge.ui.util.find;

import com.google.common.base.Preconditions;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;

public abstract class AbstractSearchDialog {
   private final TextSearchController controller;
   private KongaDialog dialog;
   private final JLabel messageLabel;
   private final AbstractSearchDialog.MessagePublisher messagePublisher;
   private String title;
   private final List<Receiver<Void>> closeListeners;

   protected AbstractSearchDialog(TextSearchModel var1, SearchTarget var2, String var3) {
      this.controller = new TextSearchController(var1);
      this.controller.setTarget(var2);
      this.messageLabel = new JLabel();
      this.messagePublisher = new AbstractSearchDialog.MessagePublisher(this.messageLabel);
      this.closeListeners = new CopyOnWriteArrayList<>();
      this.title = var3;
   }

   protected final TextSearchController getController() {
      return this.controller;
   }

   public final void setSearchString(String var1) {
      this.controller.getModel().setSearchString(var1);
   }

   public final void setTitle(String var1) {
      this.title = var1;
      if (this.dialog != null) {
         this.dialog.setTitle(var1);
      }
   }

   public void setTarget(SearchTarget var1) {
      this.controller.setTarget(var1);
   }

   public final void addObserver(TextSearchObserver var1) {
      this.controller.addObserver(var1);
   }

   public final void removeObserver(TextSearchObserver var1) {
      this.controller.removeObserver(var1);
   }

   public final void addCloseListener(Receiver<Void> var1) {
      this.closeListeners.add(var1);
   }

   public final void removeCloseListener(Receiver<Void> var1) {
      this.closeListeners.remove(var1);
   }

   public void toFront() {
      if (this.dialog != null) {
         this.dialog.toFront();
      }
   }

   public final void show(Window var1) {
      Preconditions.checkState(this.controller.hasTarget(), "Must set a search target first.");
      if (this.dialog != null) {
         this.dialog.setVisible(true);
         this.dialog.toFront();
      } else {
         this.controller.addObserver(this.messagePublisher);
         this.dialog = this.createDialog(var1);
         this.dialog.setVisible(true);
      }
   }

   private KongaDialog createDialog(Window var1) {
      if (var1 == null) {
         var1 = UiUtils.getActiveWindow();
      }

      KongaDialog var2 = new KongaDialog(var1, this.title, ModalityType.MODELESS) {
         @Override
         protected void onDialogOpened() {
            AbstractSearchDialog.this.focusUi();
         }

         @Override
         protected void onDialogClose() {
            super.onDialogClose();
            AbstractSearchDialog.this.dispose();
            this.notifyCloseListeners();
         }

         private void notifyCloseListeners() {
            for (Receiver var2x : AbstractSearchDialog.this.closeListeners) {
               var2x.handle(null);
            }
         }
      };
      this.layoutDialog(var2);
      var2.manageLocation(AbstractSearchDialog.class);
      return var2;
   }

   private void layoutDialog(KongaDialog var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(10, 20);
      this.layoutUi(var2);
      JComponent var3 = this.layoutMessageLabelAndCloseButton(var1);
      var2.south(var3);
      var2.withBorder(Empty.border(10));
      var2.asContentPaneFor(var1);
      var1.pack();
   }

   protected abstract void focusUi();

   protected abstract void layoutUi(BorderLayoutBuilder var1);

   private JComponent layoutMessageLabelAndCloseButton(KongaDialog var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      JButton var3 = var1.createCloseButton();
      var2.add(this.messageLabel).glue().add(var3);
      return var2.container();
   }

   public final void dispose() {
      if (this.dialog != null) {
         this.messageLabel.setText(null);
         this.disposeUi();
         this.controller.dispose();
         this.controller.removeObserver(this.messagePublisher);
         this.dialog.setVisible(false);
         this.dialog.dispose();
         this.dialog = null;
      }
   }

   protected abstract void disposeUi();

   private static class MessagePublisher implements TextSearchObserver {
      private final JLabel label;

      public MessagePublisher(JLabel var1) {
         this.label = var1;
      }

      @Override
      public void newMatch(SearchTarget.Match var1) {
         String var2 = null;
         if (isDebugMode()) {
            var2 = var1.toString();
         }

         this.label.setText(var2);
      }

      private static boolean isDebugMode() {
         return Boolean.getBoolean("DebugMode") && "torgil".equals(SystemUtils.USER_NAME);
      }

      @Override
      public void noMatch() {
         this.label.setText("String not found");
      }

      @Override
      public void searchWrappedAround(SearchTarget.Match var1) {
         this.label.setText("Wrapped search");
      }

      @Override
      public void matchReplaced(SearchTarget.Match var1, String var2) {
         this.label.setText(null);
      }

      @Override
      public void matchesReplaced(List<? extends SearchTarget.Match> var1, String var2) {
         assert var1.size() > 0;
         String var3;
         if (var1.size() == 1) {
            var3 = "1 match replaced";
         } else {
            var3 = var1.size() + " matches replaced";
         }

         this.label.setText(var3);
      }
   }
}
