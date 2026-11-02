package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.awt.Color;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.text.KongaTextArea;

final class SampleFileHeaderInput implements HeaderInput {
   private final SampleFile sampleFile;
   private final KongaTextArea fileViewer;

   public SampleFileHeaderInput(SampleFile var1) {
      this.sampleFile = var1;
      this.fileViewer = this.createFileViewer();
   }

   private KongaTextArea createFileViewer() {
      KongaTextArea var1 = KongaTextArea.viewer(this.sampleFile.getTopOfFile(), Size.rows(10), Size.columns(60));
      var1.setLabel("Use the first row in the sample file:");
      var1.setCaretPosition(0);
      this.highlightFirstRow(var1);
      return var1;
   }

   private void highlightFirstRow(KongaTextArea var1) {
      Document var2 = var1.getDocument();
      if (var2.getLength() != 0) {
         int var3 = DocumentUtils.getRowEnd(var2, 1);

         try {
            Highlighter var4 = var1.getHighlighter();
            var4.addHighlight(0, var3, new DefaultHighlightPainter(new Color(208, 208, 208)));
         } catch (BadLocationException var5) {
            var5.printStackTrace();
         }
      }
   }

   @Override
   public boolean isValid() {
      return this.sampleFile.hasHeader();
   }

   @Override
   public List<String> getHeaders() {
      return this.sampleFile.getHeader();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.fileViewer.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fileViewer.requestFocusInWindow();
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.fileViewer.addFocusListener(var1);
   }
}
