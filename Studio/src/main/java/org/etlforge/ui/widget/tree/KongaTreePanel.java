package org.etlforge.ui.widget.tree;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.etlforge.ui.Empty;
import org.etlforge.ui.util.KongaTitlePanel;
import org.etlforge.ui.util.focus.FocusUtils;

public class KongaTreePanel extends JPanel implements FocusListener {
   private KongaTitlePanel titlePanel;
   private JScrollPane scroll;

   public KongaTreePanel(KongaTree var1) {
      this(null, var1);
   }

   public KongaTreePanel(KongaTree var1, int var2) {
      this(null, var1, var2);
   }

   public KongaTreePanel(String var1, KongaTree var2) {
      this(var1, var2, 30);
   }

   public KongaTreePanel(String var1, KongaTree var2, int var3) {
      this.setLayout(new BoxLayout(this, 3));
      this.titlePanel = new KongaTitlePanel(var1, 10);
      if (var1 != null) {
         this.add(this.titlePanel);
      }

      JPanel var4 = new JPanel();
      var2.setBorder(Empty.border(0, 5, 0, 0));
      var4.setBackground(var2.getBackground());
      this.scroll = new JScrollPane() {
         @Override
         public Dimension getPreferredSize() {
            int var1x = KongaTreePanel.this.getWidth();
            int var2x = KongaTreePanel.this.getHeight() - KongaTreePanel.this.titlePanel.getHeight() - 5;
            return new Dimension(var1x, var2x);
         }
      };
      this.scroll.setHorizontalScrollBarPolicy(var3);
      var2.setHorizontalScrollbarPolicy(var3);
      this.scroll.setBorder(null);
      this.scroll.setViewportView(var2);
      var4.add(this.scroll);
      this.add(var4);
      FocusUtils.makeComponentMouseFocusable(this);
   }

   public void setTree(KongaTree var1) {
      this.scroll.setViewportView(var1);
   }

   @Override
   public void focusGained(FocusEvent var1) {
      this.titlePanel.focusGained(var1);
   }

   @Override
   public void focusLost(FocusEvent var1) {
      this.titlePanel.focusLost(var1);
   }
}
