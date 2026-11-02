package org.etlforge.ui.widget;

import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;

public class KongaScrollPane extends JScrollPane implements KongaWidget {
   public KongaScrollPane() {
   }

   public KongaScrollPane(Component var1, int var2, int var3) {
      super(var1, var2, var3);
      this.customize(var1);
   }

   public KongaScrollPane(Component var1) {
      super(var1);
      this.customize(var1);
   }

   public KongaScrollPane(UiProvider var1) {
      var1.decorate(new UiCanvas() {
         @Override
         public void display(Component var1) {
            KongaScrollPane.this.getViewport().setView(var1);
         }
      });
      this.customize(var1);
   }

   public KongaScrollPane(int var1, int var2) {
      super(var1, var2);
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
   }

   private void customize(Object var1) {
      if (!(var1 instanceof Scrollable)) {
         this.getHorizontalScrollBar().setUnitIncrement(10);
         this.getVerticalScrollBar().setUnitIncrement(10);
      }
   }

   public KongaScrollPane makeInconspicuous() {
      this.getViewport().setOpaque(false);
      this.setBorder(Empty.border());
      this.setViewportBorder(Empty.border());
      return this;
   }
}
