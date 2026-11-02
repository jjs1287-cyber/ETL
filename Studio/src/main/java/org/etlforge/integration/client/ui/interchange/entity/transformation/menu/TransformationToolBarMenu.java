package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.integration.client.ui.interchange.component.menu.InterchangeMenuDictionary;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class TransformationToolBarMenu extends AbstractMenu {
   private final TransformationPage page;

   public TransformationToolBarMenu(TransformationPage var1) {
      super("menu.ic.tx.Tools");
      this.page = var1;
   }

   @Override
   protected List<? extends JMenuItem> getMenuItems() {
      ArrayList var1 = Lists.newArrayList();
      this.addPageAction("action.ic.tx.LoadSource", var1);
      this.addPageAction("action.ic.tx.TestTransformation", var1);
      this.addPageAction("action.ic.tx.TestOperation", var1);
      var1.add(null);
      this.addSubMenu(new TransformationShowDataMenu(this.page), var1);
      this.addPageAction("action.ic.tx.ViewSourceFile", var1);
      this.addPageAction("action.ic.tx.ViewTargetFile", var1);
      this.addPageAction("action.ic.tx.ViewLinkFile", var1);
      this.addPageAction("action.ic.tx.RemoveData", var1);
      var1.add(null);
      this.addSubMenu(new EditTransformationMenu(this.page), var1);
      this.addPageAction("action.ic.tx.Migrate", var1);
      var1.add(null);
      this.addSubMenu(new ImportMenu(this.page), var1);
      this.addSubMenu(new ExportMenu(this.page), var1);
      this.addSubMenu(new ShowMappedNodesMenu(this.page), var1);
      this.addSubMenu(new SimpleNodeDisplayMenu(this.page), var1);
      this.addSubMenu(new SimpleXmlElementDisplaySettingsMenu(this.page), var1);
      this.addSubMenu(new MapAreaSettingsMenu(this.page), var1);
      var1.add(null);
      this.addPageAction("action.ic.tx.CreateOperation", var1);
      this.addPageAction("action.ic.tx.TextWizard", var1);
      return var1;
   }

   private void addPageAction(String var1, List<JMenuItem> var2) {
      ApplicationAction var3 = this.page.getPageAction(var1);
      if (var3 != null && var3.isSupported()) {
         var2.add(new KongaMenuItem(var3));
      }
   }

   private void addSubMenu(Menu var1, List<JMenuItem> var2) {
      JMenu var3 = var1.createJMenu(InterchangeMenuDictionary.DICTIONARY);
      var2.add(var3);
   }
}
