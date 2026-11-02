package org.etlforge.ui.util;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.text.JTextComponent;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.widget.ComponentTraverser;

public final class UiUtils {
   public static void setOpaqueRecursively(Component var0, boolean var1) {
      setOpaqueRecursively(var0, var1, new Class[0]);
   }

   public static void setOpaqueRecursively(Component var0, boolean var1, Class[] var2) {
      if (var0 instanceof JComponent) {
         boolean var3 = true;

         for (Class var7 : var2) {
            if (var7.isAssignableFrom(var0.getClass())) {
               var3 = false;
               break;
            }
         }

         if (var3) {
            ((JComponent)var0).setOpaque(var1);
         }
      }

      if (var0 instanceof Container) {
         for (Component var11 : ((Container)var0).getComponents()) {
            setOpaqueRecursively(var11, var1, var2);
         }
      }
   }

   public static void setOpaqueRecursivelySkipDefaults(Component var0, boolean var1) {
      if (!(var0 instanceof JTextComponent) && !(var0 instanceof JScrollPane) && !(var0 instanceof JComboBox) && !(var0 instanceof BasicSplitPaneDivider)) {
         if (var0 instanceof JComponent) {
            ((JComponent)var0).setOpaque(var1);
         }

         if (var0 instanceof Container) {
            for (Component var5 : ((Container)var0).getComponents()) {
               setOpaqueRecursivelySkipDefaults(var5, var1);
            }
         }
      }
   }

   public static void setEnabledRecursively(final Container var0, final boolean var1) {
      runOnEventThread(new Runnable() {
         @Override
         public void run() {
            UiUtils.setEnabledRecursivelyImpl(var0, var1);
         }
      });
   }

   private static void setEnabledRecursivelyImpl(Container var0, final boolean var1) {
      ComponentTraverser.traverse(var0, new Acceptor<Component>() {
         public boolean accept(Component var1x) {
            if (var1x instanceof AbstractButton) {
               Action var2 = ((AbstractButton)var1x).getAction();
               if (var2 != null) {
                  var1x.setEnabled(var1 && var2.isEnabled());
                  return true;
               }
            }

            var1x.setEnabled(var1);
            return true;
         }
      });
   }

   public static void runOnEventThread(Runnable var0) {
      if (EventQueue.isDispatchThread()) {
         var0.run();
      } else {
         EventQueue.invokeLater(var0);
      }
   }

   public static void runOnEventThreadAndWait(Runnable var0, ExceptionHandler<? super Throwable> var1) {
      if (EventQueue.isDispatchThread()) {
         var0.run();
      } else {
         try {
            EventQueue.invokeAndWait(var0);
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            ExceptionUtils.handle(var3, var1);
         } catch (InvocationTargetException var4) {
            ExceptionUtils.handle(var4.getCause(), var1);
         }
      }
   }

   public static JFrame getActiveFrame() {
      for (Frame var3 : Frame.getFrames()) {
         if (var3.isActive()) {
            return var3 instanceof JFrame ? (JFrame)var3 : null;
         }
      }

      return null;
   }

   public static Window getActiveWindow() {
      JFrame var0 = getActiveFrame();
      if (var0 != null) {
         return var0;
      }

      for (Window var4 : Window.getWindows()) {
         if (var4.isActive()) {
            return var4;
         }
      }

      return null;
   }

   public static void disableDoubleBuffering(Component var0) {
      setDoubleBufferingEnabled(var0, false);
   }

   public static void enableDoubleBuffering(Component var0) {
      setDoubleBufferingEnabled(var0, true);
   }

   private static void setDoubleBufferingEnabled(Component var0, boolean var1) {
      RepaintManager var2 = RepaintManager.currentManager(var0);
      var2.setDoubleBufferingEnabled(var1);
   }

   public static void makeDefaultButton(JButton var0) {
      JRootPane var1 = SwingUtilities.getRootPane(var0);
      if (var1 != null) {
         var1.setDefaultButton(var0);
      }
   }

   public static JButton findButtonForAction(Container var0, Action var1) {
      for (Component var5 : var0.getComponents()) {
         if (var5 instanceof JButton && ((JButton)var5).getAction() == var1) {
            return (JButton)var5;
         }

         if (var5 instanceof Container) {
            JButton var6 = findButtonForAction((Container)var5, var1);
            if (var6 != null) {
               return var6;
            }
         }
      }

      return null;
   }

   public static void makeDefaultButton(Container var0, Action var1) {
      JButton var2 = findButtonForAction(var0, var1);
      if (var2 != null) {
         makeDefaultButton(var2);
      }
   }

   public static void setEnabled(boolean var0, Component... var1) {
      for (Component var5 : var1) {
         if (var5 != null) {
            var5.setEnabled(var0);
         }
      }
   }

   public static void setPanelBackgrounds(Container var0, final Color var1) {
      ComponentTraverser.traverse(var0, new Acceptor<Component>() {
         public boolean accept(Component var1x) {
            if (this.isAffected(var1x)) {
               Color var2 = var1x.getBackground();
               if (var2 == null || var2 instanceof UIResource) {
                  var1x.setBackground(var1);
               }
            }

            return true;
         }

         private boolean isAffected(Component var1x) {
            return var1x instanceof JPanel || var1x instanceof JCheckBox || var1x instanceof JRadioButton;
         }
      });
   }

   public static void nestedInvokeLater(int var0, Runnable var1) {
      Preconditions.checkArgument(var0 > 0, "depth must be > 0 (was " + var0 + ")");
      Preconditions.checkNotNull(var1, "job");
      UiUtils.NestedInvokeLater var2 = new UiUtils.NestedInvokeLater(var0, var1);
      var2.run();
   }

   public static void revalidate(Component var0) {
      Component var1 = var0.getParent();
      if (var1 == null) {
         var1 = var0;
      }

      if (var1 instanceof JComponent) {
         ((JComponent)var1).revalidate();
      } else {
         var1.invalidate();
         var1.validate();
      }

      var1.repaint();
   }

   public static Optional<Component> getComponentSource(EventObject var0) {
      Object var1 = var0.getSource();
      return var1 instanceof Component ? Optional.of((Component)var1) : Optional.absent();
   }

   public static Optional<Window> getWindowAncestor(EventObject var0) {
      Component var1 = getComponentSource(var0).orNull();
      if (var1 != null) {
         for (Component var2 = parentOf(var1); var2 != null; var2 = parentOf(var2)) {
            if (var2 instanceof Window) {
               return Optional.of((Window)var2);
            }
         }
      }

      return Optional.absent();
   }

   private static Component parentOf(Component var0) {
      return var0 instanceof JPopupMenu ? ((JPopupMenu)var0).getInvoker() : var0.getParent();
   }

   public static void removeFromParent(Component var0) {
      Container var1 = var0.getParent();
      if (var1 != null) {
         var1.remove(var0);
         revalidate(var1);
      }
   }

   public static void makeAntiAliased(Graphics2D var0) {
      var0.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var0.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      var0.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
   }

   private UiUtils() {
   }

   private static class NestedInvokeLater implements Runnable {
      private final Runnable job;
      private final int depth;
      private int level;

      public NestedInvokeLater(int var1, Runnable var2) {
         this.depth = var1;
         this.job = var2;
      }

      @Override
      public void run() {
         if (this.level < this.depth) {
            this.level++;
            EventQueue.invokeLater(this);
         } else {
            this.job.run();
         }
      }
   }
}
