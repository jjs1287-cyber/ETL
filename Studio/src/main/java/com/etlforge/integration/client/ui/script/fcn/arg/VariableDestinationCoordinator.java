package com.etlforge.integration.client.ui.script.fcn.arg;

import com.etlforge.integration.client.ui.script.fcn.VariableSelector;
import java.awt.EventQueue;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.system.callback.Receiver;

final class VariableDestinationCoordinator {
   private final ArgumentFieldsPanel fields;

   public static void install(VariableSelector var0, ArgumentFieldsPanel var1) {
      new VariableDestinationCoordinator(var0, var1);
   }

   private VariableDestinationCoordinator(VariableSelector var1, ArgumentFieldsPanel var2) {
      this.fields = var2;
      var1.setGlobalDataElementReceiver(new VariableDestinationCoordinator.GlobalVariableReceiver());
      var1.setSourceDataElementReceiver(new VariableDestinationCoordinator.SourceVariableReceiver());
   }

   private void insertText(final JTextComponent var1, final String var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.requestFocusInWindow();
            var1.replaceSelection(var2);
         }
      });
   }

   private class GlobalVariableReceiver implements Receiver<String> {
      private GlobalVariableReceiver() {
      }

      public void handle(String var1) {
         JTextComponent var2 = VariableDestinationCoordinator.this.fields.getLastFocusedField();
         if (var2 != null) {
            String var3 = GlobalDE.forScript(var1);
            VariableDestinationCoordinator.this.insertText(var2, var3);
         }
      }
   }

   private class SourceVariableReceiver implements Receiver<String> {
      private SourceVariableReceiver() {
      }

      public void handle(String var1) {
         JTextComponent var2 = VariableDestinationCoordinator.this.fields.getLastFocusedField();
         if (var2 != null) {
            VariableDestinationCoordinator.this.insertText(var2, var1);
         }
      }
   }
}
