package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JLabel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.focus.FocusUtils;

final class NoHeaderInput implements HeaderInput {
   private final JLabel label = new JLabel("The source file does not have a header");

   public NoHeaderInput() {
      FocusUtils.makeComponentMouseFocusable(this.label);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.label);
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public boolean isValid() {
      return true;
   }

   @Override
   public List<String> getHeaders() {
      return null;
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.label.addFocusListener(var1);
   }
}
