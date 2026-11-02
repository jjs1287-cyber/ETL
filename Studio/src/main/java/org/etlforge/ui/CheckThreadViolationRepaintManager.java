package org.etlforge.ui;

import java.lang.ref.WeakReference;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

public class CheckThreadViolationRepaintManager extends RepaintManager {
   private boolean completeCheck = true;
   private WeakReference<JComponent> lastComponent;
   private static JButton test;

   public CheckThreadViolationRepaintManager(boolean var1) {
      this.completeCheck = var1;
   }

   public CheckThreadViolationRepaintManager() {
      this(true);
   }

   public boolean isCompleteCheck() {
      return this.completeCheck;
   }

   public void setCompleteCheck(boolean var1) {
      this.completeCheck = var1;
   }

   @Override
   public synchronized void addInvalidComponent(JComponent var1) {
      this.checkThreadViolations(var1);
      super.addInvalidComponent(var1);
   }

   @Override
   public void addDirtyRegion(JComponent var1, int var2, int var3, int var4, int var5) {
      this.checkThreadViolations(var1);
      super.addDirtyRegion(var1, var2, var3, var4, var5);
   }

   private void checkThreadViolations(JComponent var1) {
      if (!SwingUtilities.isEventDispatchThread() && (this.completeCheck || var1.isShowing())) {
         boolean var2 = false;
         boolean var3 = false;
         boolean var4 = false;
         StackTraceElement[] var5 = Thread.currentThread().getStackTrace();

         for (StackTraceElement var9 : var5) {
            if (var2 && var9.getClassName().startsWith("javax.swing.") && !var9.getClassName().startsWith("javax.swing.SwingWorker")) {
               var3 = true;
            }

            if (var2 && "imageUpdate".equals(var9.getMethodName())) {
               var4 = true;
            }

            if ("repaint".equals(var9.getMethodName())) {
               var2 = true;
               var3 = false;
            }
         }

         if (var4) {
            return;
         }

         if (var2 && !var3) {
            return;
         }

         if (this.lastComponent != null && var1 == this.lastComponent.get()) {
            return;
         }

         this.lastComponent = new WeakReference<>(var1);
         this.violationFound(var1, var5);
      }
   }

   protected void violationFound(JComponent var1, StackTraceElement[] var2) {
      System.out.println();
      System.out.println("EDT violation detected");
      System.out.println(var1);

      for (StackTraceElement var6 : var2) {
         System.out.println("\tat " + var6);
      }
   }

   public static void main(String[] var0) throws Exception {
      RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
      SwingUtilities.invokeAndWait(new Runnable() {
         @Override
         public void run() {
            CheckThreadViolationRepaintManager.test();
         }
      });
      System.out.println("Valid code passed...");
      repaintTest();
      System.out.println("Repaint test - correct code");
      test();
   }

   static void test() {
      JFrame var0 = new JFrame("Am I on EDT?");
      var0.setDefaultCloseOperation(3);
      var0.add(new JButton("JButton"));
      var0.pack();
      var0.setVisible(true);
      var0.dispose();
   }

   static void imageUpdateTest() {
      JFrame var0 = new JFrame();
      var0.setDefaultCloseOperation(3);
      JEditorPane var1 = new JEditorPane();
      var0.setContentPane(var1);
      var1.setContentType("text/html");
      var1.setText("<html><img src=\"file:\\lala.png\"></html>");
      var0.setSize(300, 200);
      var0.setVisible(true);
   }

   static void repaintTest() {
      try {
         SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
               CheckThreadViolationRepaintManager.test = new JButton();
               CheckThreadViolationRepaintManager.test.setSize(100, 100);
            }
         });
      } catch (Exception var1) {
         var1.printStackTrace();
      }

      test.repaint(test.getBounds());
      test.repaint(0, 0, 100, 100);
      test.repaint();
   }
}
