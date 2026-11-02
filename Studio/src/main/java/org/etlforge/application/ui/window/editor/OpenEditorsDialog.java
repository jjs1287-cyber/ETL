package org.etlforge.application.ui.window.editor;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class OpenEditorsDialog extends KongaDialog {
   private static final Border COMPONENT_BORDER = Empty.border(5, 5, 0, 5);
   private final EditorService editorService;
   private EditorListWidget editorList;
   private OpenEditorsDialog.CloseSelectedEditorsAction closeEditorsAction;
   private OpenEditorsDialog.SetActiveEditorAction setActiveEditorAction;
   private JComponent listPanel;
   private EditorServiceListener editorListener;

   private OpenEditorsDialog(Window var1, EditorService var2) {
      super(var1, Strings.get("Editor.OpenEditorsDialog.Title"));
      this.editorService = var2;
      this.createContents();
      this.pack();
      this.manageLocation();
   }

   public static void displayPages(Window var0, EditorService var1) {
      OpenEditorsDialog var2 = new OpenEditorsDialog(var0, var1);
      var2.setVisible(true);
   }

   @Override
   protected void onDialogClose() {
      this.editorService.removeEditorServiceListener(this.editorListener);
      super.onDialogClose();
   }

   @Override
   protected void onDialogOpened() {
      this.editorListener = new OpenEditorsDialog.EditorListener();
      this.editorService.addEditorServiceListener(this.editorListener);
      super.onDialogOpened();
   }

   private void createContents() {
      this.createContentPane();
      this.createList();
      this.createControlPanel();
      this.createButtonPanel();
   }

   private void createContentPane() {
      this.listPanel = new JPanel(new BorderLayout());
      this.listPanel.setBorder(COMPONENT_BORDER);
      this.getContentPane().add(this.listPanel, "Center");
   }

   private void createList() {
      this.editorList = new EditorListWidget(this.getOpenEditors()) {
         @Override
         protected void customizeFactory(KongaListFactory var1, ListModel var2) {
            var1.setVisibleRowCount(Math.max(10, Math.min(var2.getSize(), 20)));
            var1.setBorder(Empty.border(2, 2, 2, 10));
         }
      };
      ContainerCanvas.decorate(this.editorList, this.listPanel, "Center");
   }

   private Editor[] getOpenEditors() {
      return this.editorService.getOpenEditors();
   }

   private void populateList() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            OpenEditorsDialog.this.editorList.setEditors(OpenEditorsDialog.this.getOpenEditors());
         }
      });
   }

   private void createControlPanel() {
      this.createActions();
      this.getContentPane().add(this.createButtons(), "East");
   }

   private void createActions() {
      this.closeEditorsAction = new OpenEditorsDialog.CloseSelectedEditorsAction();
      this.closeEditorsAction.setSelectionSource(this.editorList);
      this.setActiveEditorAction = new OpenEditorsDialog.SetActiveEditorAction();
      this.editorList.setDefaultAction(this.setActiveEditorAction);
   }

   private JComponent createButtons() {
      Grid var1 = Grid.oneColumn(5);
      var1.addAll(this.setActiveEditorAction, this.closeEditorsAction);
      var1.container().setBorder(COMPONENT_BORDER);
      return InvisiblePanel.of(var1);
   }

   private void createButtonPanel() {
      JPanel var1 = this.getButtonPanel(OK_ONLY, 0);
      var1.setBorder(Empty.border(10, 10, 10, 0));
      this.listPanel.add(var1, "South");
      this.setOkButtonAsDefault();
   }

   private final class CloseSelectedEditorsAction extends AbstractSingleTypeSelectionAction<Editor> {
      public CloseSelectedEditorsAction() {
         super(Editor.class, Strings.get("Editor.OpenEditorsDialog.Control.Close"));
      }

      @Override
      protected void run(List<Editor> var1) {
         OpenEditorsDialog.this.editorService.closeEditors(var1);
      }
   }

   private final class EditorListener extends EditorServiceAdapter {
      private EditorListener() {
      }

      @Override
      public void editorClosed(EditorServiceEvent var1) {
         OpenEditorsDialog.this.populateList();
      }

      @Override
      public void editorOpened(EditorServiceEvent var1) {
         OpenEditorsDialog.this.populateList();
      }
   }

   private final class SetActiveEditorAction extends AbstractSingleObjectSelectionAction<Editor> {
      public SetActiveEditorAction() {
         super(Editor.class, Strings.get("Editor.OpenEditorsDialog.Control.SetActive"));
      }

      protected void run(Editor var1, ActionEvent var2) {
         OpenEditorsDialog.this.editorService.openEditor(var1);
      }
   }
}
