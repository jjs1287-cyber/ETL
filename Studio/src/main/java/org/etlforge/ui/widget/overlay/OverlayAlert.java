package org.etlforge.ui.widget.overlay;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public class OverlayAlert {
   private final OverlayDecorator decorator;

   public OverlayAlert(String var1) {
      this(var1, null, null);
   }

   public OverlayAlert(String var1, String var2) {
      this(var1, null, var2);
   }

   public OverlayAlert(String var1, Icon var2, String var3) {
      JLabel var4 = new JLabel(var1, var2, 10);
      var4.setVerticalAlignment(1);
      var4.setVerticalTextPosition(1);
      var4.setBorder(Empty.border(10));
      this.decorator = this.createOverlay(var4, var3);
   }

   public OverlayAlert(UiProvider var1, String var2) {
      JPanel var3 = BorderLayoutBuilder.fillWith(var1);
      var3.setBorder(Empty.border(10));
      this.decorator = this.createOverlay(var3, var2);
   }

   public void setTitleIcon(Icon var1) {
      this.decorator.setTitleIcon(var1);
   }

   public void show(OverlayContainer var1) {
      this.show(var1, new OverlayCenterPositioner());
   }

   public void show(OverlayContainer var1, OverlayPositioner var2) {
      this.decorator.show(var1, var2);
   }

   public void hide() {
      this.decorator.hide();
   }

   public void setAlpha(float var1) {
      this.decorator.setAlpha(var1);
   }

   protected void onClosed() {
   }

   protected void configureOverlay(OverlayDecorator var1) {
      var1.useCloseOnly();
      var1.setBlocking(true);
   }

   private OverlayDecorator createOverlay(JComponent var1, String var2) {
      OverlayAlert.OverlayImpl var3 = new OverlayAlert.OverlayImpl(var1, var2);
      this.configureOverlay(var3);
      return var3;
   }

   private class OverlayImpl extends OverlayDecorator {
      public OverlayImpl(JComponent var2, String var3) {
         super(var2, var3);
      }

      @Override
      protected void onOk() {
         OverlayAlert.this.onClosed();
      }

      @Override
      protected void onCancel() {
         OverlayAlert.this.onClosed();
      }

      @Override
      protected void onHidden() {
         OverlayAlert.this.onClosed();
      }
   }
}
