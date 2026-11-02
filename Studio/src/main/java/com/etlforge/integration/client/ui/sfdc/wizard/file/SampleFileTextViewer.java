package com.etlforge.integration.client.ui.sfdc.wizard.file;

import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class SampleFileTextViewer implements SampleFileViewer {
   private final KongaTextArea viewer = KongaTextArea.viewer(Size.Rows.rows(10), Size.Columns.columns(40));

   public SampleFileTextViewer(SampleFile var1) {
      this.viewer.setLineWrap(false);
      TextStyles.DefaultMonoSpaced.makeOver(this.viewer);
      String var2 = var1.getTopOfFile();
      if (!var1.isComplete()) {
         var2 = var2 + "\n...";
      }

      this.viewer.setText(var2);
      this.viewer.setCaretPosition(0);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.viewer.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.viewer.requestFocusInWindow();
   }
}
