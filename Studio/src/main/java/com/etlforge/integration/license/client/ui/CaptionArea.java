package com.etlforge.integration.license.client.ui;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class CaptionArea implements UiProvider {
   private final KongaTextArea textArea;

   public CaptionArea(String var1) {
      this(var1, 2, 50);
   }

   public CaptionArea(String var1, int var2, int var3) {
      this.textArea = KongaTextArea.viewer(var1, Size.rows(var2), Size.columns(var3));
      this.textArea.setBackground(new Color(new JPanel().getBackground().getRGB()));
      this.textArea.setBorder(Empty.border());
   }

   public void setBackgroundColor(Color var1) {
      this.textArea.setBackground(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JScrollPane var2 = new JScrollPane(this.textArea);
      var2.setBorder(Empty.border());
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocusInWindow();
   }
}
