package org.etlforge.ui.util.file;

import com.google.common.base.Preconditions;
import java.io.File;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.widget.ComponentCustomizer;
import org.etlforge.ui.widget.text.KongaTextComponent;
import org.etlforge.ui.widget.text.KongaTextComponentCustomizer;

public final class RecentFileSelectorPanel extends AbstractFileSelectorPanel {
   private ComboBoxInputField comboInputField;

   public RecentFileSelectorPanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4) {
      super(var1, var2, var3, var4);
      Preconditions.checkNotNull(var4, "recentFiles");
   }

   public void customizeComboBox(ComponentCustomizer<JComboBox> var1) {
      JComboBox var2 = this.comboInputField.getInputComponent();
      var1.customize(var2);
   }

   @Override
   public void displayLastFile() {
      RecentFileStore var1 = this.getRecentFiles();
      FileUi var2 = var1.latestFile();
      if (var2 != null) {
         this.setAutoCompletionEnabled(false);

         try {
            JComboBox var3 = this.comboInputField.getInputComponent();
            var3.setSelectedItem(var2);
            if (var3.getSelectedItem() != var2) {
               JTextField var4 = (JTextField)var3.getEditor().getEditorComponent();
               var4.setText(var2.toString());
            }
         } finally {
            this.setAutoCompletionEnabled(true);
         }
      }
   }

   @Override
   public File getSelectedFile() {
      Object var1 = this.comboInputField.getSelectedItem();
      if (var1 instanceof FileUi) {
         File var2 = ((FileUi)var1).getFile();
         this.updateFileStore(var2);
         return var2;
      } else {
         return super.getSelectedFile();
      }
   }

   @Override
   public void setSelectedFile(File var1) {
      this.setAutoCompletionEnabled(false);

      try {
         super.setSelectedFile(var1);
         this.comboInputField.setSelectedItem(var1 != null ? new FileUi(var1) : null);
      } finally {
         this.setAutoCompletionEnabled(true);
      }
   }

   @Override
   protected JComponent getFileInputFieldForLayout() {
      return this.comboInputField.getInputComponent();
   }

   @Override
   protected JTextField createTextField() {
      this.createComboInputField();
      ComboBoxEditor var1 = this.comboInputField.getInputComponent().getEditor();
      JTextField var2 = (JTextField)var1.getEditorComponent();
      if (!(var2 instanceof KongaTextComponent)) {
         KongaTextComponentCustomizer.customize(var2);
      }

      return var2;
   }

   private void createComboInputField() {
      ComboBoxModel var1 = this.createComboBoxModel();
      RecentFileSelectorPanel.ComboBoxImpl var2 = new RecentFileSelectorPanel.ComboBoxImpl(var1);
      var2.setEditable(true);
      var2.setSelectedIndex(-1);
      var2.setRenderer(new DefaultListDecorRenderer());
      this.comboInputField = new ComboBoxInputField("Recent files", var2);
      this.getInputPanel().addInputField(this.comboInputField);
   }

   private ComboBoxModel createComboBoxModel() {
      RecentFileStore var1 = this.getRecentFiles();
      return new DefaultComboBoxModel<>(var1.recentFiles());
   }

   private class ComboBoxImpl extends JComboBox {
      public ComboBoxImpl(ComboBoxModel var2) {
         super(var2);
      }

      @Override
      public void setSelectedIndex(int var1) {
         RecentFileSelectorPanel.this.setAutoCompletionEnabled(false);

         try {
            super.setSelectedIndex(var1);
         } finally {
            RecentFileSelectorPanel.this.setAutoCompletionEnabled(true);
         }
      }

      @Override
      public void setSelectedItem(Object var1) {
         RecentFileSelectorPanel.this.setAutoCompletionEnabled(false);

         try {
            super.setSelectedItem(var1);
         } finally {
            RecentFileSelectorPanel.this.setAutoCompletionEnabled(true);
         }
      }
   }
}
