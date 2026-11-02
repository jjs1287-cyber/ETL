package org.etlforge.ui.util;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import org.etlforge.system.Disposable;
import org.etlforge.ui.event.MouseActionListener;

public final class PopupMenuRequestListener extends MouseActionListener implements Disposable {
   private final Component source;
   private final PopupMenuRequestHandler handler;

   public static Disposable install(Component var0, PopupMenuRequestHandler var1) {
      PopupMenuRequestListener var2 = new PopupMenuRequestListener(Preconditions.checkNotNull(var0, "source"), Preconditions.checkNotNull(var1, "handler"));
      var0.addMouseListener(var2);
      return var2;
   }

   private PopupMenuRequestListener(Component var1, PopupMenuRequestHandler var2) {
      this.source = var1;
      this.handler = var2;
   }

   @Override
   public void dispose() {
      this.source.removeMouseListener(this);
   }

   @Override
   protected void onPopupTrigger(final MouseEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PopupMenuRequestListener.this.handler.popupMenuRequested(var1.getComponent(), var1);
         }
      });
   }
}
