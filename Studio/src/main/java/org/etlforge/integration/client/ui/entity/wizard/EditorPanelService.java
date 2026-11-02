package org.etlforge.integration.client.ui.entity.wizard;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorOwner;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.util.UiUtils;

public final class EditorPanelService implements UiProvider {
   private final EditorPanelService.Display display;
   private final EditorPanel editorPanel;
   private final EditorPanelService.EditorOwnerImpl editorOwner;
   private final EntityExplorerSupport projectExplorer;

   public static EditorPanelService inDialog(EntityExplorerSupport var0, EditorPanel var1) {
      return new EditorPanelService(var0, var1, true);
   }

   public EditorPanelService(EntityExplorerSupport var1, EditorPanel var2) {
      this(var1, var2, false);
   }

   public EditorPanelService(EntityExplorerSupport var1, EditorPanel var2, boolean var3) {
      this.projectExplorer = new EditorPanelService.ProjectExplorerImpl(var1);
      this.editorPanel = var2;
      this.configureEditorPanel();
      this.display = var3 ? new EditorPanelService.DialogDisplay() : new EditorPanelService.CardsDisplay(var2);
      this.editorOwner = new EditorPanelService.EditorOwnerImpl();
   }

   private void configureEditorPanel() {
      EditorPanel.ToolbarMode var1 = this.editorPanel.getToolbarMode();
      LineBorder var2 = var1 == EditorPanel.ToolbarMode.NONE ? null : new LineBorder(Colors.ETLFORGE_BLUE);
      this.editorPanel.getUi().setBorder(var2);
   }

   public EntityExplorerSupport getProjectExplorer() {
      return this.projectExplorer;
   }

   public void setAppWin(ApplicationWindow var1) {
      this.editorPanel.setAppWin(var1);
   }

   public Editor getOpenEditor() {
      return this.editorPanel.getEditor();
   }

   public void openEditorFor(IntegrationEntity var1) {
      this.projectExplorer.openPageFor(var1);
   }

   public void forceCloseWithoutSaving() {
      ApplicationUiUtils.logIfNotOnEDT(EditorPanelService.class, "closeOpenEditor");
      Editor var1 = this.editorPanel.getEditor();
      if (var1 != null) {
         this.editorPanel.close(true);
         this.display.close();
      }
   }

   public void forceCloseAfterSaving() {
      Editor var1 = this.editorPanel.getEditor();
      if (var1 != null) {
         this.editorOwner.setAllowUserToCancelClose(false);

         try {
            var1.close();
         } finally {
            this.editorOwner.setAllowUserToCancelClose(true);
         }
      }
   }

   public boolean closeOpenEditor() {
      return this.closeOpenEditorImpl(false);
   }

   private boolean closeOpenEditorImpl(boolean var1) {
      ApplicationUiUtils.logIfNotOnEDT(EditorPanelService.class, "closeOpenEditor");
      Editor var2 = this.editorPanel.getEditor();
      return var2 == null ? true : this.editorOwner.closeEditor(var2, var1);
   }

   public boolean saveCurrentEditor() {
      Editor var1 = this.getOpenEditor();
      if (var1 == null) {
         return true;
      }

      try {
         var1.save();
         return true;
      } catch (SaveEditorException var3) {
         this.display.showSaveError(var3);
         return false;
      }
   }

   public boolean prepareForRemoval(boolean var1) {
      Editor var2 = this.getOpenEditor();
      if (var2 != null && var2.getWindow() != null) {
         boolean var3 = this.closeOpenEditorImpl(var1);
         EditorPanelService.DisplayedEditorCloseState var4 = new EditorPanelService.DisplayedEditorCloseState(var2, var3);
         return var4.isReadyForRemoval();
      } else {
         return true;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.display.decorate(var1);
   }

   @Override
   public void requestFocus() {
      Editor var1 = this.getOpenEditor();
      if (var1 != null) {
         var1.requestFocus();
      }
   }

   private static class CardsDisplay implements EditorPanelService.Display {
      private final CardPanel cards = new CardPanel();

      public CardsDisplay(EditorPanel var1) {
         this.cards.add(new JLabel(" "), "empty");
         this.cards.add(var1.getUi(), "target");
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.cards.decorate(var1);
      }

      @Override
      public void open(IntegrationEntity var1) {
         this.cards.show("target");
      }

      @Override
      public void close() {
         this.cards.show("empty");
      }

      @Override
      public void showSaveError(SaveEditorException var1) {
         Alert.error(this.cards.container(), var1.getMessage(), "Cannot Save");
      }
   }

   private class DialogDisplay implements EditorPanelService.Display {
      private KongaDialog dialog;

      private DialogDisplay() {
      }

      @Override
      public void open(IntegrationEntity var1) {
         ApplicationWindow var2 = EditorPanelService.this.editorPanel.getAppWin();
         this.dialog = new KongaDialog(var2.getFrame(), var1.getName(), true) {
            @Override
            protected void onDialogClose() {
               super.onDialogClose();
               UiUtils.removeFromParent(EditorPanelService.this.editorPanel.getUi());
            }

            @Override
            protected void onOk() {
               boolean var1x = EditorPanelService.this.saveCurrentEditor();
               if (var1x) {
                  boolean var2x = EditorPanelService.this.closeOpenEditor();
                  if (var2x) {
                     super.onOk();
                  }
               }
            }

            @Override
            protected void onCancel() {
               boolean var1x = EditorPanelService.this.closeOpenEditor();
               if (var1x) {
                  super.onCancel();
               }
            }
         };
         this.dialog.setFixedButtonSize(false);
         this.dialog.standardLayout(EditorPanelService.this.editorPanel.getUi(), KongaDialog.OK_CANCEL);
         this.dialog.getOKButton().setText("Save and Close");
         this.dialog.getCancelButton().setText("Close");
         this.dialog.setOkButtonAsDefault();
         this.dialog.manageLocation(EditorPanelService.class);
         this.dialog.setVisible(true);
      }

      @Override
      public void close() {
         if (this.dialog != null) {
            UiUtils.removeFromParent(EditorPanelService.this.editorPanel.getUi());
            this.dialog.dispose();
            this.dialog = null;
         }
      }

      @Override
      public void showSaveError(SaveEditorException var1) {
         Alert.error(this.dialog, var1.getMessage(), "Cannot Save");
      }

      @Override
      public void decorate(UiCanvas var1) {
         throw new UnsupportedOperationException("Dialog mode does not support display in a UiCanvas");
      }
   }

   private interface Display {
      void open(IntegrationEntity var1);

      void close();

      void showSaveError(SaveEditorException var1);

      void decorate(UiCanvas var1);
   }

   private static class DisplayedEditorCloseState {
      private final Editor editor;
      public final boolean editorWasClosed;

      public DisplayedEditorCloseState(Editor var1, boolean var2) {
         this.editor = var1;
         this.editorWasClosed = var2;
      }

      public boolean isReadyForRemoval() {
         return this.editor == null || this.editorWasClosed && !this.wasTransientItemClosed();
      }

      private boolean wasTransientItemClosed() {
         Object var1 = this.editor.getObject();
         if (var1 instanceof IntegrationEntity) {
            IntegrationEntity var2 = (IntegrationEntity)var1;
            return var2.isTransient();
         } else {
            return false;
         }
      }
   }

   private class EditorOwnerImpl implements EditorOwner {
      private boolean allowUserToCancelClose = true;

      private EditorOwnerImpl() {
      }

      public void setAllowUserToCancelClose(boolean var1) {
         this.allowUserToCancelClose = var1;
      }

      @Override
      public boolean closeEditor(Editor var1) {
         return this.closeEditor(var1, false);
      }

      public boolean closeEditor(Editor var1, boolean var2) {
         if (var1.isDirty()) {
            if (var2) {
               boolean var3 = this.save(var1);
               if (!var3) {
                  return false;
               }
            } else if (this.allowUserToCancelClose) {
               boolean var4 = this.promptCancelableSave(var1);
               if (!var4) {
                  return false;
               }
            } else {
               this.promptNonCancelableSave(var1);
            }
         }

         EditorPanelService.this.editorPanel.close(true);
         EditorPanelService.this.display.close();
         return true;
      }

      private boolean promptCancelableSave(Editor var1) {
         Answer var2 = Ask.yesNoOrCancel("Do you want to save the changes made to \"" + var1.getTitle() + "\"?", "Save?");
         return var2.isYes() ? this.save(var1) : var2.isNo();
      }

      private void promptNonCancelableSave(Editor var1) {
         Answer var2 = Ask.yesOrNo("Do you want to save the changes made to \"" + var1.getTitle() + "\"?", "Save?");
         if (var2.isYes()) {
            this.save(var1);
         }
      }

      private boolean save(Editor var1) {
         try {
            var1.save();
            return true;
         } catch (SaveEditorException var3) {
            ErrorLog.attention(EditorPanelService.class, null, var3);
            return false;
         }
      }
   }

   private class ProjectExplorerImpl extends EntityExplorerSupport {
      private final EntityExplorerSupport impl;

      public ProjectExplorerImpl(EntityExplorerSupport var2) {
         this.impl = var2;
      }

      @Override
      public ApplicationWindow getWindow() {
         return this.impl.getWindow();
      }

      @Override
      public IntegrationProject getProject() {
         return this.impl.getProject();
      }

      @Override
      public Editor createPage(IntegrationEntity var1) {
         Editor var2 = this.impl.createPage(var1);
         if (var2 instanceof GenericEditor) {
            ((GenericEditor)var2).setUseScroll(false);
         }

         return var2;
      }

      @Override
      public Editor findOpenPage(IntegrationEntity var1) {
         return this.impl.findOpenPage(var1);
      }

      @Override
      public Editor openPageFor(IntegrationEntity var1) {
         if (var1.isTransient()) {
            Editor var2 = EditorPanelService.this.editorPanel.getEditor();
            if (var2 != null && var2.getObject() == var1) {
               return var2;
            }
         }

         EditorPanelService.this.forceCloseAfterSaving();
         Editor var3 = this.createPage(var1);
         EditorPanelService.this.editorPanel.open(var3);
         var3.setOwner(EditorPanelService.this.editorOwner);
         EditorPanelService.this.display.open(var1);
         var3.requestFocus();
         return var3;
      }

      @Override
      public UiEntityFactory getEntityFactory() {
         return this.impl.getEntityFactory();
      }
   }
}
