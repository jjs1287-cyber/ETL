package org.etlforge.application.ui.window.editor;

import com.google.common.base.Preconditions;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.action.SaveEditorAction;
import org.etlforge.system.Disposable;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.KongaFocusManager;
import org.etlforge.ui.widget.button.KongaButton;

public final class EditorPanel {
   private ApplicationWindow appWin;
   private final BorderLayoutBuilder panel;
   private Editor editor;
   private Action saveAction;
   private EditorPanel.KeyBindings keyBindings;
   private FocusListener focusListener;
   private EditorPanel.ToolbarMode toolbarMode = EditorPanel.ToolbarMode.SAVE_AND_CLOSE;

   public EditorPanel() {
      this.panel = new BorderLayoutBuilder();
   }

   public EditorPanel(ApplicationWindow var1) {
      this();
      this.setAppWin(var1);
   }

   public void setAppWin(ApplicationWindow var1) {
      this.appWin = var1;
   }

   public ApplicationWindow getAppWin() {
      return this.appWin;
   }

   public void setToolbarMode(EditorPanel.ToolbarMode var1) {
      this.toolbarMode = Preconditions.checkNotNull(var1);
   }

   public EditorPanel.ToolbarMode getToolbarMode() {
      return this.toolbarMode;
   }

   public JComponent getUi() {
      return this.panel.container();
   }

   public Editor getEditor() {
      return this.editor;
   }

   public void open(Editor var1) {
      Preconditions.checkNotNull(var1, "editor");
      Preconditions.checkState(this.appWin != null, "Must provide an ApplicationWindow");
      if (var1 != this.editor) {
         Preconditions.checkState(this.editor == null, "This panel is already displaying an editor");
         this.editor = var1;
         var1.addToWindow(this.appWin);
         this.saveAction = new SaveEditorAction(var1);
         this.layoutEditor(var1);
         this.keyBindings = new EditorPanel.KeyBindings(var1, this.saveAction, this.panel.container());
         var1.appendState(WindowElementState.OPEN);
         var1.appendState(WindowElementState.ACTIVE);
         var1.requestFocus();
         this.installFocusListener();
         this.revalidate();
      }
   }

   private void layoutEditor(Editor var1) {
      switch (this.toolbarMode) {
         case SAVE_AND_CLOSE:
            boolean var2 = var1.decorateWithoutToolbar(this.panel);
            if (var2) {
               this.addSaveAndCloseButtonsInNoToolbarMode(var1);
               return;
            }
         case NONE:
            if (var1.decorateWithoutToolbar(this.panel)) {
               return;
            }
         case FULL:
         default:
            var1.decorate(this.panel);
      }
   }

   private void addSaveAndCloseButtonsInNoToolbarMode(Editor var1) {
      KongaButton var2 = KongaButton.forToolbar(this.saveAction).setUseSmallActionIcon(true);
      KongaButton var3 = KongaButton.forToolbar(new CloseEditorAction(var1)).setUseSmallActionIcon(true);
      BorderLayoutBuilder var4 = new BorderLayoutBuilder();
      var4.east(Grid.oneRow().addAll(var2, var3).nonOpaque());
      var4.withBackground(Colors.LIGHT_BLUE_BACKGROUND);
      this.panel.north(var4);
   }

   private void revalidate() {
      JComponent var1 = this.panel.container();
      UiUtils.revalidate(var1);
   }

   private void installFocusListener() {
      KongaFocusManager var1 = ApplicationUi.getFocusManager();
      this.focusListener = new EditorPanel.FocusListenerImpl();
      var1.addFocusRoot(this.panel.container(), this.focusListener);
   }

   public boolean close(boolean var1) {
      if (this.editor.removeFromWindow(var1)) {
         this.editor.revokeState(WindowElementState.ACTIVE);
         this.editor.revokeState(WindowElementState.OPEN);
         this.keyBindings.dispose();
         this.keyBindings = null;
         this.saveAction = null;
         this.editor = null;
         this.uninstallFocusListener();
         this.panel.container().removeAll();
         this.revalidate();
         return true;
      } else {
         return false;
      }
   }

   private void uninstallFocusListener() {
      KongaFocusManager var1 = ApplicationUi.getFocusManager();
      var1.removeFocusRoot(this.panel.container());
   }

   private class FocusListenerImpl implements FocusListener {
      private FocusListenerImpl() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         if (EditorPanel.this.editor != null) {
            EditorPanel.this.editor.appendState(WindowElementState.ACTIVE);
         }
      }

      @Override
      public void focusLost(FocusEvent var1) {
         if (EditorPanel.this.editor != null) {
            EditorPanel.this.editor.revokeState(WindowElementState.ACTIVE);
         }
      }
   }

   private static class KeyBindings implements Disposable {
      private final KeyBindingInstaller installer;

      public KeyBindings(Editor var1, Action var2, JComponent var3) {
         this.installer = new KeyBindingInstaller(var3, 1);
         this.installer.addBinding(KeyUtils.SAVE, "save", var2);
         UndoService var4 = var1.getUndoService();
         Action var5 = var4.getUndoActivity().asAction();
         Action var6 = var4.getRedoActivity().asAction();
         if (var5 != null && var6 != null) {
            this.installer.addBinding(KeyUtils.UNDO, "undo", var5);
            this.installer.addBinding(KeyUtils.REDO, "redo", var6);
         }

         this.installer.install();
      }

      @Override
      public void dispose() {
         this.installer.remove();
      }
   }

   public enum ToolbarMode {
      FULL,
      SAVE_AND_CLOSE,
      NONE;
   }
}
