package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.text.KongaTextField;

final class ManualHeaderInput implements HeaderInput {
   private final KongaTextField textField;
   private final Receiver<Boolean> changeListener;

   public ManualHeaderInput(Receiver<Boolean> var1) {
      this.changeListener = var1;
      this.textField = new KongaTextField(30);
      TextStyles.DefaultMonoSpaced.makeOver(this.textField);
      if (var1 != null) {
         this.listenToChanges();
      }
   }

   private void listenToChanges() {
      DelayedDocumentContentChangeReaction var1 = new DelayedDocumentContentChangeReaction(100, new Runnable() {
         @Override
         public void run() {
            ManualHeaderInput.this.changeListener.handle(ManualHeaderInput.this.isValid());
         }
      });
      var1.install(this.textField);
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.setWeightX(1.0);
      var2.withInsets(0, 0, 3, 0);
      var2.addAll("Enter the list of headers (comma-separated):", this.textField);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textField.requestFocusInWindow();
   }

   @Override
   public boolean isValid() {
      return this.getText().length() > 0;
   }

   private String getText() {
      return this.textField.getText().trim();
   }

   @Override
   public List<String> getHeaders() {
      Preconditions.checkState(this.isValid(), "getHeaders should not be called with invalid input");
      String[] var1 = this.getText().split(",");
      ArrayList var2 = Lists.newArrayList();

      for (String var6 : var1) {
         var2.add(var6.trim());
      }

      return var2;
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.textField.addFocusListener(var1);
   }
}
