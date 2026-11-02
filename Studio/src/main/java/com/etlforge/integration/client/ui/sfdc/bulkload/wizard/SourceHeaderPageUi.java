package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

final class SourceHeaderPageUi implements UiProvider {
   private final Receiver<Boolean> changeListener;
   private HeaderInputChoice headerInput;
   private JComponent ui;

   public SourceHeaderPageUi(Receiver<Boolean> var1) {
      this.changeListener = var1;
      this.headerInput = new HeaderInputChoice(var1, new ManualHeaderInput(var1));
   }

   public void setSampleFile(SampleFile var1) {
      if (var1 == null) {
         this.headerInput = new HeaderInputChoice(this.changeListener, new ManualHeaderInput(this.changeListener));
      } else {
         this.headerInput = new HeaderInputChoice(this.changeListener, new SampleFileHeaderInput(var1), new ManualHeaderInput(this.changeListener));
      }

      if (this.ui != null) {
         this.ui.removeAll();
         BorderLayoutBuilder.from(this.ui).north(this.headerInput);
         UiUtils.revalidate(this.ui);
      }
   }

   public List<String> getHeaders() {
      assert this.headerInput.isValid() : "getHeaders should not be called on invalid input";
      return this.headerInput.getHeaders();
   }

   public boolean doesSourceFileHaveHeader() {
      return !(this.headerInput.getChoice() instanceof NoHeaderInput);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui = new JPanel();
      BorderLayoutBuilder.from(this.ui).north(this.headerInput);
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.headerInput.requestFocus();
   }
}
