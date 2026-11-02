package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.ImmutableList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorListWidget;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.widget.button.KongaButton;

public final class SaveDirtyEditorsPrompter implements EditorsToSaveCollector {
   private final SaveDirtyEditorsPrompter.Ui ui;

   public SaveDirtyEditorsPrompter(ApplicationWindow var1, Collection<Editor> var2) {
      this.ui = new SaveDirtyEditorsPrompter.Ui(var1.getFrame(), var2);
   }

   @Override
   public Editor[] collect() {
      this.ui.setVisible(true);
      return this.ui.getEditorsToSave();
   }

   @Override
   public boolean wasCancelled() {
      return this.ui.wasCancelled();
   }

   private static class Ui extends KongaDialog {
      private final EditorListWidget list;
      private final JComponent buttons;
      private final Collection<Editor> editors;
      private Editor[] editorsToSave;

      public Ui(JFrame var1, Collection<Editor> var2) {
         super(var1, PackageResources.SavePrompt.TITLE, true);
         this.editors = var2;
         this.list = new EditorListWidget(var2);
         this.buttons = this.createButtons();
         this.layoutComponents();
         this.pack();
         this.manageLocation(SaveDirtyEditorsPrompter.class);
      }

      public Editor[] getEditorsToSave() {
         return this.editorsToSave;
      }

      private JComponent createButtons() {
         JButton var1 = this.getSaveAllButton();
         JButton var2 = this.getSaveNoneButton();
         JButton var3 = this.getCancelSaveButton();
         ImmutableList var4 = ButtonOrder.defaultActionCancelAndNoAction(var1, var3, var2);
         return Horizontal.box().withEmptyBorder(10, 0, 10, 0).glue().strut(60).addAllWithSpace(10, var4).container();
      }

      private JButton getSaveAllButton() {
         KongaButton var1 = new KongaButton(PackageResources.SavePrompt.SAVE_ALL);
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               Ui.this.editorsToSave = Ui.this.editors.toArray(new Editor[Ui.this.editors.size()]);
               Ui.this.onOk();
            }
         });
         this.getRootPane().setDefaultButton(var1);
         return var1;
      }

      private JButton getSaveNoneButton() {
         KongaButton var1 = new KongaButton(PackageResources.SavePrompt.SAVE_NONE);
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               Ui.this.editorsToSave = new Editor[0];
               Ui.this.onOk();
            }
         });
         return var1;
      }

      private JButton getCancelSaveButton() {
         KongaButton var1 = new KongaButton(PackageResources.SavePrompt.CANCEL);
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               Ui.this.editorsToSave = new Editor[0];
               Ui.this.onCancel();
            }
         });
         return var1;
      }

      private void layoutComponents() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.list);
         var1.north(PackageResources.SavePrompt.CAPTION).south(this.buttons).withEmptyBorder(10);
         var1.asContentPaneFor(this);
      }
   }
}
