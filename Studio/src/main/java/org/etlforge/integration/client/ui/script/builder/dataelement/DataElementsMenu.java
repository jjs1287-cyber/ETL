package org.etlforge.integration.client.ui.script.builder.dataelement;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.resources.ScriptResources;
import org.etlforge.integration.data.script.de.BuiltInDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.MenuAdapter;

public final class DataElementsMenu {
   private final ExpressionArea expressionArea;
   private final GlobalDataElements projectVariables;
   private final JMenu menu;

   public DataElementsMenu(GlobalDataElements var1, ExpressionArea var2) {
      Preconditions.checkNotNull(var2, "expressionArea");
      Preconditions.checkNotNull(var1, "projectVariables");
      this.expressionArea = var2;
      this.projectVariables = var1;
      this.menu = new KongaMenu(ScriptResources.getString("DataElement.Menu.Name"));
      this.menu.addMenuListener(new DataElementsMenu.MenuPopulator());
   }

   public JMenu getMenu() {
      return this.menu;
   }

   private class Handler implements ActionListener {
      private Handler() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = this.getDataElement(var1);
         this.insertDataElement(var2);
      }

      private String getDataElement(ActionEvent var1) {
         JMenuItem var2 = (JMenuItem)var1.getSource();
         return var2.getText();
      }

      private void insertDataElement(String var1) {
         DataElementsMenu.this.expressionArea.insertGlobalDataElement(var1);
      }
   }

   private class MenuPopulator extends MenuAdapter {
      private ActionListener handler;

      private MenuPopulator() {
      }

      @Override
      public void menuSelected(MenuEvent var1) {
         this.handler = DataElementsMenu.this.new Handler();
         DataElementsMenu.this.menu.removeAll();
         DataElementsMenu.this.menu.add(this.projectVariables());
         DataElementsMenu.this.menu.add(this.builtInVariables());
      }

      private JMenu projectVariables() {
         return this.buildSubMenu(ScriptResources.getString("DataElement.Menu.ProjectVariables"), DataElementsMenu.this.projectVariables.getAllDataElements());
      }

      private JMenu builtInVariables() {
         return this.buildSubMenu(ScriptResources.getString("DataElement.Menu.BuiltIn"), BuiltInDataElements.getBuiltInDataElements());
      }

      private JMenu buildSubMenu(String var1, Collection<String> var2) {
         KongaMenu var3 = new KongaMenu(var1);
         this.addMenuItems(var3, var2);
         return var3;
      }

      private void addMenuItems(JMenu var1, Collection<String> var2) {
         for (String var4 : var2) {
            JMenuItem var5 = this.createMenuItem(var4);
            var1.add(var5);
         }
      }

      private JMenuItem createMenuItem(String var1) {
         JMenuItem var2 = new JMenuItem(var1);
         var2.addActionListener(this.handler);
         return var2;
      }
   }
}
