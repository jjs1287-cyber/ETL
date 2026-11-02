package org.etlforge.application.ui.widget;

import java.awt.Point;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class TextAreaInput {
   private final String title;
   private final Receiver<String> receiver;
   private JComponent caption;
   private JComponent trigger;
   private int rows;
   private int columns;
   private OverlayContainer overlayContainer;
   private OverlayPositioner overlayPositioner;
   private TextAreaInput.Editor currentEditor;
   private boolean modal;
   private UndoRedoInstaller undoRedo;

   public TextAreaInput(String var1, Receiver<String> var2) {
      this.title = var1;
      this.receiver = var2;
      this.rows = 8;
      this.columns = 60;
      this.modal = true;
   }

   public void setOverlayContainer(OverlayContainer var1) {
      this.overlayContainer = var1;
   }

   public void setTrigger(JComponent var1) {
      this.trigger = var1;
   }

   public void setOverlayPositioner(OverlayPositioner var1) {
      this.overlayPositioner = var1;
   }

   public void setSize(int var1, int var2) {
      this.rows = var1;
      this.columns = var2;
   }

   public void setCaption(String var1) {
      this.setCaption(new JLabel(var1));
   }

   public void setCaption(JComponent var1) {
      this.caption = var1;
   }

   public void setModal(boolean var1) {
      this.modal = var1;
   }

   public void show(String var1) {
      this.close();
      this.currentEditor = this.getEditor();
      KongaTextArea var2 = new KongaTextArea(var1, this.rows, this.columns);
      this.undoRedo = new UndoRedoInstaller();
      this.undoRedo.install(var2);
      this.currentEditor.show(var2);
   }

   public void close() {
      if (this.currentEditor != null) {
         this.currentEditor.close();
         this.currentEditor = null;
      }
   }

   private TextAreaInput.Editor getEditor() {
      OverlayContainer var1 = this.getOverlayContainer();
      return var1 != null ? new TextAreaInput.OverlayEditor(var1, this.trigger) : new TextAreaInput.DialogEditor();
   }

   private OverlayContainer getOverlayContainer() {
      if (this.overlayContainer != null) {
         return this.overlayContainer;
      } else {
         return this.trigger != null ? WidgetUtils.getOverlayContainerAncestor(this.trigger) : null;
      }
   }

   private JComponent getEditorLayout(KongaTextArea var1) {
      return CaptionLayout.of(this.caption, var1.asUiProvider()).container();
   }

   private class DialogEditor implements TextAreaInput.Editor {
      private KongaDialog dlg;
      private final Window owner;

      private DialogEditor() {
         if (TextAreaInput.this.trigger != null) {
            this.owner = SwingUtilities.getWindowAncestor(TextAreaInput.this.trigger);
         } else {
            this.owner = UiUtils.getActiveWindow();
         }
      }

      @Override
      public void show(KongaTextArea var1) {
         this.dlg = this.createDialog(var1);
         this.dlg.setVisible(true);
         String var2 = this.dlg.wasOkPressed() ? var1.getText() : null;
         TextAreaInput.this.receiver.handle(var2);
      }

      @Override
      public void close() {
         if (this.dlg != null) {
            this.dlg.setVisible(false);
            TextAreaInput.this.undoRedo.dispose();
         }
      }

      private KongaDialog createDialog(KongaTextArea var1) {
         ModalityType var2 = TextAreaInput.this.modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS;
         KongaDialog var3 = new KongaDialog(this.owner, TextAreaInput.this.title, var2);
         var3.standardLayout(TextAreaInput.this.getEditorLayout(var1), KongaDialog.OK_CANCEL);
         var3.setOkButtonAsDefault();
         var3.manageLocation(TextAreaInput.class);
         return var3;
      }
   }

   private interface Editor {
      void show(KongaTextArea var1);

      void close();
   }

   private class OverlayEditor implements TextAreaInput.Editor {
      private final OverlayContainer container;
      private final JComponent trigger;
      private KongaTextArea textArea;
      private OverlayDecorator overlay;

      public OverlayEditor(OverlayContainer var2, JComponent var3) {
         this.container = var2;
         this.trigger = var3;
      }

      @Override
      public void show(KongaTextArea var1) {
         this.textArea = var1;
         this.overlay = this.createOverlay();
         this.overlay.show(this.container, this.getPositioner());
         this.requestFocus(var1);
      }

      @Override
      public void close() {
         this.overlay.hide();
         TextAreaInput.this.undoRedo.dispose();
         TextAreaInput.this.receiver.handle(null);
      }

      private OverlayDecorator createOverlay() {
         JComponent var1 = TextAreaInput.this.getEditorLayout(this.textArea);
         var1.setBorder(Empty.border(10, 10, 0, 10));
         OverlayDecorator var2 = new OverlayDecorator(var1, TextAreaInput.this.title) {
            @Override
            protected void onCancel() {
               TextAreaInput.this.receiver.handle(null);
            }

            @Override
            protected void onOk() {
               TextAreaInput.this.receiver.handle(OverlayEditor.this.textArea.getText());
            }
         };
         var2.useOkAndCancel();
         var2.setBlocking(TextAreaInput.this.modal);
         var2.setFadeIn(AnimationPreference.get());
         return var2;
      }

      private void requestFocus(KongaTextArea var1) {
         FocusUtils.requestFocusLater(var1, 50);
      }

      private OverlayPositioner getPositioner() {
         if (TextAreaInput.this.overlayPositioner != null) {
            return TextAreaInput.this.overlayPositioner;
         } else {
            return this.trigger != null ? new OverlayRelativePositioner(this.trigger, new Point(8, 8)) : new OverlayCenterPositioner();
         }
      }
   }
}
