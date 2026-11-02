package org.etlforge.integration.client.ui.interchange.location.panel;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.data.location.FileFilterOptions;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputChangeListeners;
import org.etlforge.ui.input.InputPanelDialog;
import org.etlforge.ui.input.InputPanelOverlay;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;

public final class FileFilterOptionsEditor {
   private FileFilterOptions options;
   private final List<UndoableEditListener> undoRedoListeners;
   private final InputChangeListeners changeListeners;
   private JComponent trigger;
   private boolean enabled;

   public FileFilterOptionsEditor(FileFilterOptions var1) {
      this.options = var1;
      this.undoRedoListeners = new CopyOnWriteArrayList<>();
      this.changeListeners = new InputChangeListeners();
      this.enabled = true;
   }

   public FileFilterOptions getOptions() {
      return this.options;
   }

   public void setOptions(FileFilterOptions var1) {
      this.options = var1;
   }

   public void edit() {
      FileFilterOptionsPanel var1 = new FileFilterOptionsPanel(this.options);
      if (!this.enabled) {
         var1.disable();
      }

      this.getEditorUi().open(var1);
   }

   public void setTrigger(JComponent var1) {
      this.trigger = var1;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public void addUndoRedoListener(UndoableEditListener var1) {
      this.undoRedoListeners.add(var1);
   }

   public void removeUndoRedoListener(UndoableEditListener var1) {
      this.undoRedoListeners.remove(var1);
   }

   public void addInputChangeListener(InputChangeListener var1) {
      this.changeListeners.add(var1);
   }

   public void removeInputChangeListener(InputChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   private void updateOptions(FileFilterOptions var1, boolean var2) {
      FileFilterOptions var3 = this.options;
      this.options = var1;
      if (!var3.equals(var1)) {
         this.fireInputChanged(var3, var1);
         if (var2) {
            this.fireUndoableEdit(var3, var1);
         }
      }
   }

   private void fireInputChanged(FileFilterOptions var1, FileFilterOptions var2) {
      this.changeListeners.call().inputChanged(new InputChangeEvent(this, this, "Options", var1, var2));
   }

   private void fireUndoableEdit(FileFilterOptions var1, FileFilterOptions var2) {
      UndoableEditEvent var3 = new UndoableEditEvent(this, new FileFilterOptionsEditor.UndoableEditImpl(var1, var2));

      for (UndoableEditListener var5 : this.undoRedoListeners) {
         var5.undoableEditHappened(var3);
      }
   }

   public FileFilterOptionsEditor.EditorUi getEditorUi() {
      OverlayContainer var1 = this.getOverlayContainer();
      return var1 != null ? new FileFilterOptionsEditor.EditorOverlayUi(var1) : new FileFilterOptionsEditor.EditorDialogUi();
   }

   private OverlayContainer getOverlayContainer() {
      return this.trigger != null ? WidgetUtils.getOverlayContainerAncestor(this.trigger) : null;
   }

   private class EditorDialogUi implements FileFilterOptionsEditor.EditorUi {
      private EditorDialogUi() {
      }

      @Override
      public void open(FileFilterOptionsPanel var1) {
         String var2 = Strings.get("FileFilterOptions.Title");
         InputPanelDialog var3 = new InputPanelDialog(UiUtils.getActiveWindow(), var1, var2) {
            @Override
            protected boolean useScroll() {
               return false;
            }
         };
         var3.pack();
         var3.manageLocation(FileFilterOptionsEditor.class);
         var3.setOkButtonAsDefault();
         var3.setVisible(true);
         if (var3.wasOkPressed()) {
            FileFilterOptionsEditor.this.updateOptions(var1.getOptions(), true);
         }
      }
   }

   private class EditorOverlayUi implements FileFilterOptionsEditor.EditorUi {
      private final OverlayContainer container;

      public EditorOverlayUi(OverlayContainer var2) {
         this.container = var2;
      }

      @Override
      public void open(final FileFilterOptionsPanel var1) {
         InputPanelOverlay var2 = new InputPanelOverlay(var1, Strings.get("FileFilterOptions.Title")) {
            @Override
            protected void onOk() {
               FileFilterOptionsEditor.this.updateOptions(var1.getOptions(), true);
            }
         };
         var2.setFocusReceiverWhenClosed(FileFilterOptionsEditor.this.trigger);
         var2.setFadeIn(AnimationPreference.get());
         var2.setAlpha(0.8F);
         var2.useOkAndCancel();
         var2.setBlocking(true);
         var2.show(this.container, new OverlayRelativePositioner(FileFilterOptionsEditor.this.trigger, new Point(8, 8)));
      }
   }

   private interface EditorUi {
      void open(FileFilterOptionsPanel var1);
   }

   private class UndoableEditImpl extends AbstractUndoableEdit {
      private final FileFilterOptions oldOptions;
      private final FileFilterOptions newOptions;

      public UndoableEditImpl(FileFilterOptions var2, FileFilterOptions var3) {
         this.oldOptions = var2;
         this.newOptions = var3;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.setOptions(this.newOptions);
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.setOptions(this.oldOptions);
      }

      @Override
      public String getPresentationName() {
         return Strings.get("FileFilterOptions.Edit");
      }

      private void setOptions(FileFilterOptions var1) {
         FileFilterOptionsEditor.this.updateOptions(var1, false);
      }
   }
}
