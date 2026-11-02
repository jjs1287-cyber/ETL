package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionCategory;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.ui.widget.menu.KongaMenu;

public final class FunctionMenu {
   private final ExpressionArea expressionArea;
   private final JMenu menu;
   private final ActionListener handler;

   public FunctionMenu(ExpressionArea var1, boolean var2) {
      Preconditions.checkNotNull(var1, "expressionArea");
      this.expressionArea = var1;
      this.handler = new FunctionMenu.Handler();
      this.menu = new KongaMenu(Strings.get("FunctionMenu.Name"));
      this.menu.setIcon(Icons.FORMULA_ICON);
      this.populateMenu(var2);
   }

   public JMenu getMenu() {
      return this.menu;
   }

   private void populateMenu(boolean var1) {
      FunctionRegistry var2 = FunctionRegistry.getRegistry();

      for (FunctionCategory var4 : var2.getCategories()) {
         this.addCategoryMenu(this.menu, var4, var1);
      }
      for(FunctionCategory category:org.etlforge.integration.data.script.JavaScriptFunctions.categories())this.addCategoryMenu(this.menu,category,var1);
   }

   private void addCategoryMenu(JMenu var1, FunctionCategory var2, boolean var3) {
      JMenu var4 = new JMenu(var2.getName());

      for (Function var6 : var2.getFunctions()) {
         if (var3 || !var6.isInstanceFunction()) {
            this.addFunctionItem(var6, var4);
         }
      }

      if (var4.getMenuComponentCount() > 0) {
         var1.add(var4);
      }
   }

   private void addFunctionItem(Function var1, JMenu var2) {
      JMenuItem var3 = new JMenuItem(var1.getName(), Icons.FORMULA_ICON);
      String var4 = this.getToolTip(var1);
      var3.setToolTipText("<html><table width=400px>" + var4 + "</table></html>");
      var3.putClientProperty("function", var1);
      var3.addActionListener(this.handler);
      var2.add(var3);
   }

   private String getToolTip(Function var1) {
      FunctionHelpHtml var2 = new FunctionHelpHtml(var1);
      return var2.getFirstSentenceOfDocumentation();
   }

   private class Handler implements ActionListener {
      private Handler() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JMenuItem var2 = (JMenuItem)var1.getSource();
         Function var3 = (Function)var2.getClientProperty("function");
         FunctionMenu.this.expressionArea.insertFunction(var3);
      }
   }
}
