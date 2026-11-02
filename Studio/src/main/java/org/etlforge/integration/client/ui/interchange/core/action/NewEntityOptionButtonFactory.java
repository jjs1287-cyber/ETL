package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.widget.button.StaticOptionButton;

public final class NewEntityOptionButtonFactory {
   private final List<ApplicationAction> actions = Lists.newArrayList();
   private final Icon icon;
   private final String text;
   private Action defaultAction;

   public NewEntityOptionButtonFactory(EntityConfigLookup var1, Icon var2, String var3, Iterable<EntityType> var4, EntityType var5) {
      this.icon = var2;
      this.text = var3;
      this.defaultAction = this.createActions(var1, var4, var5);
   }

   private ApplicationAction createActions(EntityConfigLookup var1, Iterable<EntityType> var2, EntityType var3) {
      ApplicationAction var4 = null;

      for (EntityType var6 : var2) {
         if (var6 == null) {
            this.actions.add(null);
         } else {
            EntityConfig var7 = var1.getEntityConfig(var6);
            ApplicationAction var8 = var7.newEntityAction();
            if (var8 != null) {
               this.actions.add(var8);
               if (var6 == var3) {
                  var4 = var8;
               }
            }
         }
      }

      return var4;
   }

   public void setDefaultAction(Action var1) {
      this.defaultAction = var1;
   }

   public StaticOptionButton getButton() {
      StaticOptionButton var1 = new StaticOptionButton(this.defaultAction, this.actions.toArray(new Action[this.actions.size()]), true);
      var1.setMainButtonIcon(this.icon);
      var1.setMainButtonText(this.text);
      return var1;
   }
}
