package org.etlforge.ui.util.focus;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class FocusDispatcher implements FocusListener {
   private final List<FocusListener> receivers = new CopyOnWriteArrayList<>();

   public void addReceiver(FocusListener var1) {
      this.receivers.add(var1);
   }

   public void removeReceiver(FocusListener var1) {
      this.receivers.remove(var1);
   }

   @Override
   public void focusGained(FocusEvent var1) {
      for (FocusListener var3 : this.receivers) {
         var3.focusGained(var1);
      }
   }

   @Override
   public void focusLost(FocusEvent var1) {
      for (FocusListener var3 : this.receivers) {
         var3.focusLost(var1);
      }
   }
}
