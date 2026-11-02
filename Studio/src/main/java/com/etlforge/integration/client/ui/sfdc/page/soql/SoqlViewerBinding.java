package com.etlforge.integration.client.ui.sfdc.page.soql;

import java.awt.EventQueue;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.property.TextBinding;

public final class SoqlViewerBinding extends TextBinding {
   public static SoqlViewerBinding installAndSyncUi(SoqlModel var0, JTextComponent var1) {
      String var2 = var0.getQueryString();
      String var3 = collapseTabs(var2);
      var1.setText(var3);
      SoqlViewerBinding var4 = new SoqlViewerBinding(var0, var1);
      var1.setCaretPosition(0);
      return var4;
   }

   private SoqlViewerBinding(SoqlModel var1, JTextComponent var2) {
      super(var1, "soql", var2);
   }

   @Override
   protected void updateUiWithValue(String var1) {
      final String var2 = collapseTabs(var1);
      this.getTextComponent().requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SoqlViewerBinding.this.setUiToModelEnabled(false);
            SoqlViewerBinding.super.updateUiWithValue(var2);
            SoqlViewerBinding.this.setUiToModelEnabled(true);
         }
      });
   }

   private static String collapseTabs(String var0) {
      return var0.replaceAll("(\\t)+", " ");
   }
}
