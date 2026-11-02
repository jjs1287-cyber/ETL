package org.etlforge.integration.client.ui.interchange.location.fileshare;

import com.google.common.base.Preconditions;
import java.io.File;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.DebugMode;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.application.ui.window.action.OpenLocalFolderAction;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.KongaTextField;

public final class FileSharePathInputField extends DefaultNonEmptyTextInputField {
   private TokenListCompletionSupport autoCompleteSupport;

   public FileSharePathInputField(String var1, String var2, int var3) {
      super(var1, createInputComponent(var3), var2);
      this.setTextWhenEmpty(LocationResources.getString("FileShare.Path.Example"));
      this.installOpenFolderAction();
   }

   public void installAutoCompletion(StringValueHistory var1) {
      Preconditions.checkNotNull(var1, "history");
      this.autoCompleteSupport = new TextFieldCompletionSupport(this, var1);
   }

   @Override
   public void dispose() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }

      super.dispose();
   }

   private void installOpenFolderAction() {
      if (DebugMode.IS_ENABLED && SystemUtils.IS_OS_WINDOWS) {
         KongaTextField var1 = (KongaTextField)this.getInputComponent();
         FileSharePathInputField.GoToLocationAction var2 = new FileSharePathInputField.GoToLocationAction(var1);
         KeyBindingInstaller var3 = new KeyBindingInstaller(this.getInputComponent());
         var3.addBinding(null, "open", var2);
         var3.install();
         var1.addActionToContextMenu(var2);
         var1.addActionToContextMenu(null);
      }
   }

   private static JTextField createInputComponent(int var0) {
      return new KongaTextField(var0);
   }

   private static class GoToLocationAction extends OpenLocalFolderAction {
      private final KongaTextField field;

      public GoToLocationAction(KongaTextField var1) {
         super(FileSharePathInputField.GoToLocationAction.class.getName(), LocationResources.getString("FileShare.Path.Explore"), null);
         this.field = var1;
         var1.getDocument().addDocumentListener(new DocumentContentChangeListener() {
            @Override
            public void contentChanged(DocumentEvent var1) {
               GoToLocationAction.this.updateEnabledState();
            }
         });
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(LocationResources.getString("FileShare.Path.Explore.Key")));
         this.updateEnabledState();
      }

      @Override
      protected File getFolderToOpen() {
         String var1 = this.getPath();
         return StringUtils.isNotEmpty(var1) ? new File(var1) : null;
      }

      private void updateEnabledState() {
         String var1 = this.getPath();
         this.setEnabled(!var1.isEmpty());
      }

      private String getPath() {
         return this.field.getText().trim();
      }
   }
}
