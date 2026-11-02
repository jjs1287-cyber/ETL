package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import java.awt.Component;
import javax.swing.InputMap;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.ui.util.KeyUtils;

public final class TreeComponentScrollPane extends JScrollPane {
   private static final int THRESHOLD = 40;
   private int verticalValue;

   public TreeComponentScrollPane(Component var1) {
      super(var1);
      JScrollBar var2 = this.getVerticalScrollBar();
      this.verticalValue = var2.getValue();
      var2.getModel().addChangeListener(new TreeComponentScrollPane.Listener());
      InputMap var3 = this.getInputMap(1);
      var3.put(KeyUtils.getCtrlDown(38), "unitScrollUp");
      var3.put(KeyUtils.getCtrlDown(40), "unitScrollDown");
      var3.put(KeyUtils.getCtrlDown(37), "unitScrollLeft");
      var3.put(KeyUtils.getCtrlDown(39), "unitScrollRight");
   }

   private class Listener implements ChangeListener {
      private Listener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         int var2 = TreeComponentScrollPane.this.getVerticalScrollBar().getValue();
         if (Math.abs(var2 - TreeComponentScrollPane.this.verticalValue) >= 40) {
            TreeComponentScrollPane.this.getViewport().getView().repaint();
         }

         TreeComponentScrollPane.this.verticalValue = var2;
      }
   }
}
