package org.etlforge.integration.client.ui.interchange.entity.script;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaButton;

public final class ScriptPadEditor extends GenericEditor {
   private final ScriptPad pad;

   public ScriptPadEditor(ScriptPad var1) {
      super("script.pad", "Script Pad");
      this.pad = var1;
      this.setIncludeCloseButton(true);
      this.setContributeGlobalUndoRedo();
      this.installUndoRedo(var1);
   }

   private void installUndoRedo(ScriptPad var1) {
      TextInputField2 var2 = var1.getUi().getExpressionArea().asInputField("");
      var2.setSendUndoEvents(true);
      var2.addUndoableEditListener(this);
   }

   public void requestAttention() {
      this.firePropertyChange("notification", Boolean.FALSE, Boolean.TRUE);
   }

   @Override
   public boolean isDirty() {
      return false;
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.createContents();
   }

   private void createContents() {
      this.createTitlePanel();
      this.setToolbarButtons(this.createToolbarButtons());
      this.setToolBar(this.createToolBar());
      this.setMainPanel(BorderLayoutBuilder.fillWith(this.pad.getUi()));
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ScriptPadEditor.this.pad.getUi().requestFocus();
         }
      });
   }

   private List<JButton> createToolbarButtons() {
      ArrayList var1 = Lists.newArrayList();
      KongaButton var2 = KongaButton.forToolbar(this.pad.getUi().getTestAction());
      var2.setUseSmallActionIcon(true);
      var1.add(var2);
      var1.add(null);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.pad.getUi().requestFocus();
      this.pad.getUi().setDefaultCaretPosition();
   }
}
