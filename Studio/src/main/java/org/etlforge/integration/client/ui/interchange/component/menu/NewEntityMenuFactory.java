package org.etlforge.integration.client.ui.interchange.component.menu;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class NewEntityMenuFactory {
   private final ImmutableList<NewEntityMenuEntry> entries;

   public NewEntityMenuFactory(Iterable<NewEntityMenuEntry> var1) {
      this.entries = ImmutableList.copyOf(var1);
   }

   public final List<? extends JMenuItem> createMenuItems(EntityConfigLookup var1) {
      ArrayList var2 = Lists.newArrayList();
      this.addNonProjectTypes(var1, var2);
      this.addProject(var1, var2);
      return var2;
   }

   private void addNonProjectTypes(EntityConfigLookup var1, List<JMenuItem> var2) {
      for (NewEntityMenuEntry var4 : this.entries) {
         JMenuItem var5 = var4.getNewEntityMenuItem(var1);
         var2.add(var5);
      }
   }

   private void addProject(EntityConfigLookup var1, List<JMenuItem> var2) {
      var2.add(null);
      EntityConfig var3 = var1.getEntityConfig(EntityType.Project);
      var2.add(new KongaMenuItem(var3.newEntityAction()));
   }
}
