package org.etlforge.integration.client.ui.script.builder;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.QuickFrame;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.button.KongaButton;

public final class OperatorButtonPanel implements UiProvider {
   private static final String[] OPERATORS = new String[]{"<=", ">=", "==", ">", "<", "!=", "(", ")", "!", "&", "|", "^", "+", "-", "*", "/", ";"};
   private static final int COLUMNS = 3;
   private final JButton[] buttons;

   public OperatorButtonPanel(Receiver<String> var1) {
      this.buttons = this.createButtons(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = new Grid(0, 3, 3, 3).addAll(this.buttons);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(var2);
      var3.withEmptyBorder(10, 0, 10, 0);
      var3.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.buttons[0].requestFocusInWindow();
   }

   private JButton[] createButtons(final Receiver<String> var1) {
      ActionListener var2 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.handle(var1x.getActionCommand());
         }
      };
      JButton[] var3 = new JButton[OPERATORS.length];

      for (int var4 = 0; var4 < OPERATORS.length; var4++) {
         String var5 = OPERATORS[var4];
         KongaButton var6 = new KongaButton(var5);
         TextStyles.DefaultBoldLarger.makeOver(var6);
         var6.setActionCommand(var5);
         var6.setMargin(new Insets(1, 1, 1, 1));
         var6.addActionListener(var2);

         for (int var10 : new int[]{38, 40, 37, 39}) {
            OperatorButtonPanel.ArrowNavigator var11 = new OperatorButtonPanel.ArrowNavigator(var10);
            ActionKeyBinding.install(var11, var6);
         }

         var3[var4] = var6;
      }

      return var3;
   }

   public static void main(String[] var0) {
      OperatorButtonPanel var1 = new OperatorButtonPanel(new Receiver<String>() {
         public void handle(String var1) {
            System.out.println(var1);
         }
      });
      QuickFrame.show(var1, "Arithmetic Button Panel");
   }

   private final class ArrowNavigator extends AbstractAction {
      private final int direction;

      public ArrowNavigator(int var2) {
         this.direction = var2;
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(var2, 0));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JButton var2 = (JButton)var1.getSource();
         JButton var3 = this.nextButton(var2, this.direction);
         var3.requestFocusInWindow();
      }

      private JButton nextButton(JButton var1, int var2) {
         String var3 = var1.getText();
         int[] var4 = this.rowAndColumnFor(var3);
         switch (var2) {
            case 37:
               var4[1] = this.getNextColumnLeft(var4);
               break;
            case 38:
               var4[0] = this.getNextRowUp(var4);
               break;
            case 39:
               var4[1] = this.getNextColumnRight(var4);
               break;
            case 40:
               var4[0] = this.getNextRowDown(var4);
         }

         int var5 = this.toIndex(var4);
         return OperatorButtonPanel.this.buttons[var5];
      }

      private int[] rowAndColumnFor(String var1) {
         int var2 = Arrays.asList(OperatorButtonPanel.OPERATORS).indexOf(var1);
         assert var2 >= 0;
         int var3 = var2 / 3;
         int var4 = var2 - 3 * var3;
         return new int[]{var3, var4};
      }

      private int getNextRowUp(int[] var1) {
         if (var1[0] > 0) {
            return var1[0] - 1;
         }

         int var2 = this.numberOfRows() - 1;
         if (var1[1] > this.lastColumnOfRow(var2)) {
            var2--;
         }

         return var2;
      }

      private int lastColumnOfRow(int var1) {
         if (var1 == this.numberOfRows() - 1) {
            int var2 = OperatorButtonPanel.OPERATORS.length % 3 - 1;
            if (var2 >= 0) {
               return var2;
            }
         }

         return 2;
      }

      private int numberOfRows() {
         return 1 + OperatorButtonPanel.OPERATORS.length / 3;
      }

      private int getNextRowDown(int[] var1) {
         if (var1[0] < this.numberOfRows() - 1) {
            int var2 = var1[0] + 1;
            if (var1[1] <= this.lastColumnOfRow(var2)) {
               return var1[0] + 1;
            }
         }

         return 0;
      }

      private int getNextColumnLeft(int[] var1) {
         return var1[1] > 0 ? var1[1] - 1 : this.lastColumnOfRow(var1[0]);
      }

      private int getNextColumnRight(int[] var1) {
         return var1[1] < this.lastColumnOfRow(var1[0]) ? var1[1] + 1 : 0;
      }

      private int toIndex(int[] var1) {
         int var2 = var1[0];
         assert var2 >= 0 && var2 < this.numberOfRows();
         int var3 = var1[1];
         assert var3 >= 0 && var3 < 3;
         return var2 * 3 + var3;
      }
   }
}
