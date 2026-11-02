package org.etlforge.ui.util.focus;

import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.KeyUtils;

public final class FocusUtils {
   public static void makeComponentMouseFocusable(final Component var0) {
      var0.setFocusable(true);
      var0.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent var1) {
            var0.requestFocus();
         }
      });
   }

   public static void changeFocusAcrossWindows(final Component var0, int var1) {
      final Window var2 = SwingUtilities.getWindowAncestor(var0);
      DelayedJob.createAndSchedule(var1, new Runnable() {
         @Override
         public void run() {
            if (var2 != null) {
               var2.toFront();
               var2.requestFocus();
            }

            FocusUtils.requestFocusLater(var0, 150);
         }
      });
   }

   public static void requestFocusLater(final Component var0, int var1) {
      DelayedJob.createAndSchedule(var1, new Runnable() {
         @Override
         public void run() {
            var0.requestFocusInWindow();
         }
      });
   }

   public static void requestFocusLater(final UiProvider var0, int var1) {
      DelayedJob.createAndSchedule(var1, new Runnable() {
         @Override
         public void run() {
            var0.requestFocus();
         }
      });
   }

   public static void installArrowDownAndUpAsFocusTraversalKeys(JComponent var0) {
      HashSet var1 = Sets.newHashSet(var0.getFocusTraversalKeys(0));
      var1.add(KeyUtils.DOWN);
      var0.setFocusTraversalKeys(0, var1);
      HashSet var2 = Sets.newHashSet(var0.getFocusTraversalKeys(1));
      var2.add(KeyUtils.UP);
      var0.setFocusTraversalKeys(1, var2);
   }

   public static <T extends Focusable> Disposable bindChoiceComponents(final JToggleButton var0, final T var1) {
      final FocusAdapter var2 = new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent var1) {
            var0.setSelected(true);
         }
      };
      var1.addFocusListener(var2);
      final ActionListener var3 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.requestFocus();
         }
      };
      var0.addActionListener(var3);
      return new Disposable() {
         @Override
         public void dispose() {
            var1.removeFocusListener(var2);
            var0.removeActionListener(var3);
         }
      };
   }

   private FocusUtils() {
   }
}
