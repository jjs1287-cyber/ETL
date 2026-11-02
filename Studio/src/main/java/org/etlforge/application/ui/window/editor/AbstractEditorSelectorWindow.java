package org.etlforge.application.ui.window.editor;

import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.window.editor.service.DefaultEditorService;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.window.WindowCornerPosition;
import org.etlforge.ui.widget.window.WindowMouseCloser;
import org.etlforge.ui.widget.window.WindowUtils;

public abstract class AbstractEditorSelectorWindow implements EditorSelectorWindow {
   private final EditorService service;
   private final AbstractEditorSelectorWindow.EditorClosedListener editorClosedListener = new AbstractEditorSelectorWindow.EditorClosedListener();
   private final Editor originalEditor;
   private KongaDialog dialog;
   private WindowMouseCloser mouseCloser;
   private Component activeArea;
   private final UiProvider ui;
   private Editor selectedEditor;
   private final List<Editor> editors;
   private Border border;

   protected AbstractEditorSelectorWindow(EditorService var1, List<Editor> var2, boolean var3) {
      this.service = var1;
      this.editors = var2;
      var1.addEditorServiceListener(this.editorClosedListener);
      this.originalEditor = var1.getActiveEditor();
      this.ui = this.createUi(var2, var3);
   }

   protected abstract UiProvider createUi(List<Editor> var1, boolean var2);

   @Override
   public final void setDialog(KongaDialog var1) {
      this.dialog = var1;
      var1.addWindowListener(new AbstractEditorSelectorWindow.WindowCloseHandler());
      this.mouseCloser = new WindowMouseCloser(var1, 0, 0);
   }

   @Override
   public final void dispose() {
      this.service.removeEditorServiceListener(this.editorClosedListener);
      if (this.ui instanceof Disposable) {
         ((Disposable)this.ui).dispose();
      }
   }

   @Override
   public final void open(Window var1, WindowCornerPosition var2) {
      if (this.dialog == null) {
         this.dialog = this.createDialog(var1);
         this.mouseCloser = new WindowMouseCloser(this.dialog, 0, 0);
      }

      this.layoutDialog();
      if (var2 == null) {
         this.dialog.setLocationRelativeTo(var1);
      } else {
         var2.position(this.dialog);
         WindowUtils.ensureWindowCompletelyVisibleOnScreen(this.dialog);
      }

      this.dialog.setVisible(true);
   }

   private KongaDialog createDialog(Window var1) {
      KongaDialog var2 = this.instantiateDialog(var1);
      var2.addWindowListener(new AbstractEditorSelectorWindow.WindowCloseHandler());
      return var2;
   }

   private KongaDialog instantiateDialog(Window var1) {
      KongaDialog var2 = new KongaDialog(var1, "", ModalityType.MODELESS);
      var2.setUndecorated(true);
      var2.setFocusable(false);
      var2.setCancelOnEscape(true);
      return var2;
   }

   private void layoutDialog() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.ui);
      var1.withBackground(Colors.ETLFORGE_BLUE).withBorder(this.border);
      var1.asContentPaneFor(this.dialog);
      UiUtils.revalidate(this.dialog.getRootPane());
      this.dialog.pack();
   }

   protected final EditorService getService() {
      return this.service;
   }

   @Override
   public final void close() {
      if (this.dialog != null) {
         this.dialog.dispose();
      }
   }

   @Override
   public final void setActiveArea(Component var1) {
      this.activeArea = var1;
   }

   @Override
   public final void addBlindSpot(Component var1) {
      this.mouseCloser.addBlindSpot(var1);
   }

   @Override
   public final boolean containsPoint(Point var1) {
      return this.dialog != null && this.dialog.getBounds().contains(var1);
   }

   public final void setBorder(Border var1) {
      this.border = var1;
   }

   protected void onWindowClosed() {
   }

   @Override
   public final void restoreOriginalEditor() {
      if (this.service instanceof DefaultEditorService && this.isStillOpen(this.originalEditor)) {
         ((DefaultEditorService)this.service).switchToEditorSilently(this.originalEditor);
      }
   }

   private boolean isStillOpen(Editor var1) {
      return var1.getWindow() != null;
   }

   private class EditorClosedListener extends EditorServiceAdapter {
      private EditorClosedListener() {
      }

      @Override
      public void editorClosed(EditorServiceEvent var1) {
         Editor var2 = var1.getEditor();
         AbstractEditorSelectorWindow.this.editors.remove(var2);
      }
   }

   protected final class SelectionHandler implements EditorSelectorListener {
      @Override
      public void temporarySelection(Editor var1) {
         if (AbstractEditorSelectorWindow.this.service instanceof DefaultEditorService) {
            AbstractEditorSelectorWindow.this.selectedEditor = var1;
            ((DefaultEditorService)AbstractEditorSelectorWindow.this.service).switchToEditorSilently(var1);
         }
      }

      @Override
      public void finalSelection(final Editor var1) {
         AbstractEditorSelectorWindow.this.selectedEditor = var1;
         this.closeWindow();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AbstractEditorSelectorWindow.this.service.openEditor(var1);
            }
         });
      }

      private void closeWindow() {
         if (AbstractEditorSelectorWindow.this.dialog != null) {
            AbstractEditorSelectorWindow.this.dialog.setVisible(false);
            AbstractEditorSelectorWindow.this.dialog.dispose();
         }
      }

      @Override
      public void closeRequested(Editor var1) {
         this.closeWindow();
         AbstractEditorSelectorWindow.this.service.closeEditor(var1);
      }

      @Override
      public void closeOthersRequested(Editor var1) {
         this.closeWindow();
         HashSet var2 = Sets.newHashSet(AbstractEditorSelectorWindow.this.editors);
         var2.remove(var1);
         if (!var2.isEmpty()) {
            AbstractEditorSelectorWindow.this.service.closeEditors(var2);
         }

         this.finalSelection(var1);
      }
   }

   private class WindowCloseHandler extends WindowAdapter {
      private WindowCloseHandler() {
      }

      @Override
      public void windowClosed(WindowEvent var1) {
         if (AbstractEditorSelectorWindow.this.dialog != null) {
            AbstractEditorSelectorWindow.this.restoreOriginalEditor();
            this.checkHowListWasClosed();
            AbstractEditorSelectorWindow.this.dispose();
            AbstractEditorSelectorWindow.this.onWindowClosed();
            AbstractEditorSelectorWindow.this.mouseCloser = null;
            AbstractEditorSelectorWindow.this.dialog = null;
         }
      }

      private void checkHowListWasClosed() {
         if (this.wasClosingClickInActiveArea()
            && AbstractEditorSelectorWindow.this.selectedEditor != null
            && AbstractEditorSelectorWindow.this.isStillOpen(AbstractEditorSelectorWindow.this.selectedEditor)) {
            AbstractEditorSelectorWindow.this.service.openEditor(AbstractEditorSelectorWindow.this.selectedEditor);
         }
      }

      private boolean wasClosingClickInActiveArea() {
         MouseEvent var1 = AbstractEditorSelectorWindow.this.mouseCloser.getClosingEvent();
         return var1 != null
            && AbstractEditorSelectorWindow.this.activeArea != null
            && SwingUtilities.isDescendingFrom(var1.getComponent(), AbstractEditorSelectorWindow.this.activeArea);
      }
   }
}
