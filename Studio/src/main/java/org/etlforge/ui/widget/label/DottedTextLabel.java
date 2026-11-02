package org.etlforge.ui.widget.label;

import com.google.common.base.Preconditions;
import java.awt.Container;
import javax.swing.border.Border;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.progress.ProgressMessage;
import org.etlforge.ui.widget.LabelAdapter;
import org.etlforge.ui.widget.LabelDecorator;

public final class DottedTextLabel implements UiProvider {
   private static final int MAX_DOTS = 8;
   private final KongaLabel label;
   private final ProgressMessage message;

   public DottedTextLabel() {
      this(8, null);
   }

   public DottedTextLabel(int var1) {
      this(var1, null);
   }

   public DottedTextLabel(LabelDecorator var1) {
      this(8, var1);
   }

   public DottedTextLabel(int var1, LabelDecorator var2) {
      Preconditions.checkArgument(var1 >= 1, "maxDots must be >= 1 (was " + var1 + ")");
      this.label = new KongaLabel(" ");
      if (var2 != null) {
         var2.decorate(new LabelAdapter(this.label));
      }

      this.message = new ProgressMessage("", this.label);
      this.message.setMaxDots(var1);
   }

   public void addToParent(Container var1, Object var2) {
      var1.add(this.label, var2);
   }

   public void setText(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.message.setText(var1);
   }

   public String getBaseText() {
      return this.message.getBaseMessage();
   }

   public String getCurrentText() {
      return this.label.getText();
   }

   public void update() {
      this.message.update();
   }

   public void setUpdateAutomatically(int var1) {
      this.message.setUpdateAutomatically(var1);
   }

   public void stopAutomaticUpdate() {
      this.message.stopAutomaticUpdate();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.label);
   }

   @Override
   public void requestFocus() {
      this.label.requestFocusInWindow();
   }

   public void setBorder(Border var1) {
      this.label.setBorder(var1);
   }

   public void setVisible(boolean var1) {
      this.label.setVisible(var1);
   }
}
