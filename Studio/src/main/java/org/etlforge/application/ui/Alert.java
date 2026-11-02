package org.etlforge.application.ui;

import com.google.common.base.Supplier;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import org.etlforge.application.NonInteractiveMode;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.HtmlLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class Alert {
   private static final boolean USE_HTML = true;
   static final int MAX_WIDTH = 400;

   private Alert() {
      throw new UnsupportedOperationException();
   }

   public static void info(Object var0, String var1) {
      info(UiUtils.getActiveFrame(), var0, var1);
   }

   public static void info(Component var0, Object var1, String var2) {
      showMessageImpl(getOwnerWindow(var0), var1, var2, Alert.Type.INFORMATION);
   }

   public static void warning(Object var0, String var1) {
      warning(UiUtils.getActiveFrame(), var0, var1);
   }

   public static void warning(Component var0, Object var1, String var2) {
      showMessageImpl(getOwnerWindow(var0), var1, var2, Alert.Type.WARNING);
   }

   public static void error(Object var0, String var1) {
      error(UiUtils.getActiveFrame(), var0, var1);
   }

   public static void error(Component var0, Object var1, String var2) {
      showMessageImpl(getOwnerWindow(var0), var1, var2, Alert.Type.ERROR);
   }

   public static void message(Component var0, Object var1, String var2, Alert.Type var3) {
      showMessageImpl(getOwnerWindow(var0), var1, var2, var3);
   }

   private static void showMessageImpl(final Window var0, final Object var1, final String var2, final Alert.Type var3) {
      if (!NonInteractiveMode.on()) {
         if (EventQueue.isDispatchThread()) {
            createAndShowDisplayer(var0, var1, var2, var3);
         } else {
            try {
               EventQueue.invokeAndWait(new Runnable() {
                  @Override
                  public void run() {
                     Alert.createAndShowDisplayer(var0, var1, var2, var3);
                  }
               });
            } catch (InterruptedException var5) {
               Thread.currentThread().interrupt();
            } catch (InvocationTargetException var6) {
               ErrorLog.log(Alert.class, "An unpexpected error occurred", var6.getCause());
            }
         }
      }
   }

   private static void createAndShowDisplayer(Window var0, Object var1, String var2, Alert.Type var3) {
      Alert.Displayer var4 = new Alert.Displayer(var0, var1, var2, var3);
      var4.run();
   }

   public static Alert.Option ask(String var0, String var1, Alert.Option var2, Alert.Option var3) {
      return ask(UiUtils.getActiveFrame(), var0, var1, var2, var3, Alert.Type.QUESTION);
   }

   public static Alert.Option ask(Component var0, String var1, String var2, Alert.Option var3, Alert.Option var4) {
      return ask(var0, var1, var2, var3, var4, Alert.Type.QUESTION);
   }

   public static Alert.Option ask(String var0, String var1, Alert.Option var2, Alert.Option var3, Alert.Type var4) {
      return ask(UiUtils.getActiveFrame(), var0, var1, var2, var3, var4);
   }

   public static Alert.Option ask(
      final Component var0, final Object var1, final String var2, final Alert.Option var3, final Alert.Option var4, final Alert.Type var5
   ) {
      return UiThreadSupplier.call(new Supplier<Alert.Option>() {
         public Alert.Option get() {
            return Alert.askImpl(Alert.getOwnerWindow(var0), var1, var2, var3, var4, var5);
         }
      });
   }

   private static Alert.Option askImpl(Window var0, Object var1, String var2, Alert.Option var3, Alert.Option var4, Alert.Type var5) {
      if (NonInteractiveMode.on()) {
         return var4;
      }

      Alert.OptionDisplayer var6 = new Alert.OptionDisplayer(var0, var1, var2, var3, var4, var5);
      var6.run();
      return var6.getResult();
   }

   public static Object getDisplayMessage(Object var0) {
      if (var0 instanceof String) {
         return getDisplayObjectFromString((String)var0);
      } else if (var0 instanceof Component) {
         Component var1 = (Component)var0;
         return var1 instanceof Scrollable ? new JScrollPane(var1) : var0;
      } else if (var0 instanceof UiProvider) {
         return InvisiblePanel.of((UiProvider)var0);
      } else {
         return var0 != null ? var0.toString() : "It was a dark and stormy night...";
      }
   }

   private static Object getDisplayObjectFromString(String var0) {
      return new HtmlLabel(var0, 400);
   }


   private static Window getOwnerWindow(Component var0) {
      if (var0 instanceof Window) {
         return (Window)var0;
      }

      Window var1 = null;
      if (var0 != null) {
         var1 = SwingUtilities.getWindowAncestor(var0);
      }

      if (var1 == null) {
         var1 = UiUtils.getActiveFrame();
      }

      return var1;
   }

   private static class Displayer implements Runnable {
      private final Window owner;
      private final Object message;
      private final String title;
      private final Alert.Type type;

      public Displayer(Window var1, Object var2, String var3, Alert.Type var4) {
         this.owner = var1;
         this.message = Alert.getDisplayMessage(var2);
         this.title = var3;
         this.type = var4;
      }

      @Override
      public void run() {
         JOptionPane.showMessageDialog(this.owner, this.message, this.title, this.type.getOptionPaneValue());
      }
   }

   public enum Option {
      YES(UiResourceBundle.getString("Yes")),
      NO(UiResourceBundle.getString("No")),
      CANCEL(UiResourceBundle.getString("Cancel")),
      OK(UiResourceBundle.getString("Ok")),
      CONTINUE(UiResourceBundle.getString("Continue")),
      YES_NO(null),
      YES_NO_CANCEL(null),
      OK_CANCEL(null),
      CONTINUE_CANCEL(null);

      private final String display;

      Option(String var3) {
         this.display = var3;
      }

      Alert.Option translateSelection(int var1) {
         switch (var1) {
            case -1:
               if (this != OK_CANCEL && this != YES_NO_CANCEL && this != CONTINUE_CANCEL) {
                  return NO;
               }

               return CANCEL;
            case 0:
               if (this == OK_CANCEL) {
                  return OK;
               } else {
                  if (this == CONTINUE_CANCEL) {
                     return CONTINUE;
                  }

                  return YES;
               }
            case 1:
               if (this != OK_CANCEL && this != CONTINUE_CANCEL) {
                  return NO;
               }

               return CANCEL;
            case 2:
               return CANCEL;
            default:
               throw new IllegalStateException("I do not know how to translate the JOptionPane return value " + var1 + " to an Option");
         }
      }

      public boolean isAffirmative() {
         return this == YES || this == OK || this == CONTINUE;
      }

      @Override
      public String toString() {
         return this.display != null ? this.display : super.toString();
      }

      public Alert.Option[] toArray() {
         switch (this) {
            case YES_NO:
               return new Alert.Option[]{YES, NO};
            case YES_NO_CANCEL:
               return new Alert.Option[]{YES, NO, CANCEL};
            case OK_CANCEL:
               return new Alert.Option[]{OK, CANCEL};
            case CONTINUE_CANCEL:
               return new Alert.Option[]{CONTINUE, CANCEL};
            default:
               return new Alert.Option[]{this};
         }
      }

      int getOptionPaneValue() {
         switch (this) {
            case YES_NO:
               return 0;
            case YES_NO_CANCEL:
               return 1;
            case OK_CANCEL:
            case CONTINUE_CANCEL:
               return 2;
            case OK:
            case CONTINUE:
               return 0;
            case YES:
               return 0;
            case NO:
               return 1;
            case CANCEL:
               return 2;
            default:
               throw new IllegalStateException("No corresponding option pane value exists for the option " + this + ". This is a programming error.");
         }
      }
   }

   private static class OptionDisplayer implements Runnable {
      private final Window owner;
      private final String title;
      private final Object message;
      private final Alert.Option options;
      private final Alert.Option initial;
      private final Alert.Type type;
      private Alert.Option selected;

      public OptionDisplayer(Window var1, Object var2, String var3, Alert.Option var4, Alert.Option var5, Alert.Type var6) {
         this.owner = var1;
         this.message = Alert.getDisplayMessage(var2);
         this.title = var3;
         this.options = var4;
         this.initial = var5;
         this.type = var6;
      }

      @Override
      public void run() {
         int var1 = JOptionPane.showOptionDialog(
            this.owner, this.message, this.title, this.options.getOptionPaneValue(), this.type.getOptionPaneValue(), null, this.options.toArray(), this.initial
         );
         this.selected = this.options.translateSelection(var1);
      }

      public Alert.Option getResult() {
         return this.selected;
      }
   }

   public enum Type {
      INFORMATION(1, AlertIcons.INFO),
      WARNING(2, AlertIcons.WARNING),
      ERROR(0, AlertIcons.ERROR),
      PLAIN(-1, null),
      QUESTION(3, AlertIcons.QUESTION);

      private final int value;
      private final Icon icon;

      public Icon getIcon() {
         return this.icon;
      }

      int getOptionPaneValue() {
         return this.value;
      }

      Type(int var3, Icon var4) {
         this.value = var3;
         this.icon = var4;
      }
   }
}
