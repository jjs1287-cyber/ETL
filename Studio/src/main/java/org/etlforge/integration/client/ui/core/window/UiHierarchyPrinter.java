package org.etlforge.integration.client.ui.core.window;

import com.google.common.collect.Lists;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.ui.util.KeyUtils;

final class UiHierarchyPrinter {
   private final ApplicationWindow appWin;

   public UiHierarchyPrinter(ApplicationWindow var1) {
      this.appWin = var1;
   }

   public void install() {
      this.appWin.addGlobalKeyDispatcher(KeyUtils.getCtrlShiftDown(113), new UiHierarchyPrinter.Dispatcher());
   }

   private static class Dispatcher implements WindowKeyDispatcher {
      private AWTEventListener awtListener;

      private Dispatcher() {
      }

      @Override
      public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
         if (this.awtListener == null) {
            this.installListener();
         } else {
            this.removeListener();
         }

         return false;
      }

      private void installListener() {
         this.awtListener = new UiHierarchyPrinter.Reporter();
         Toolkit.getDefaultToolkit().addAWTEventListener(this.awtListener, 16L);
      }

      private void removeListener() {
         Toolkit.getDefaultToolkit().removeAWTEventListener(this.awtListener);
         this.awtListener = null;
      }

      @Override
      public String getId() {
         return UiHierarchyPrinter.class.getName();
      }
   }

   private static class Reporter implements AWTEventListener {
      private Reporter() {
      }

      @Override
      public void eventDispatched(AWTEvent var1) {
         MouseEvent var2 = (MouseEvent)var1;
         if (this.isApplicable(var2)) {
            Component var3 = var2.getComponent();
            this.report(var3);
         }
      }

      private boolean isApplicable(MouseEvent var1) {
         return var1.getID() == 500 && var1.getClickCount() == 2;
      }

      private void report(Component var1) {
         List var2 = this.createHierarchy(var1);
         this.printToConsole(var2);
      }

      private List<Component> createHierarchy(Component var1) {
         LinkedList var2 = Lists.newLinkedList();

         for (Component var3 = var1; var3 != null; var3 = var3.getParent()) {
            var2.addFirst(var3);
         }

         return var2;
      }

      private void printToConsole(List<Component> var1) {
         StringBuilder var2 = new StringBuilder();

         for (Component var4 : var1) {
            System.out.println(var2 + " " + var4);
            var2.append("--");
         }
      }
   }
}
