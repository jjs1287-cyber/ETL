package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractActiveObjectAction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.project.IntegrationProject;

public abstract class ActiveOperationsAction extends AbstractActiveObjectAction {
   public ActiveOperationsAction(ActiveObjectService var1, String var2, ActionDictionary var3) {
      super(var1, var2, var3);
   }

   public ActiveOperationsAction(String var1, ActiveObjectService var2, String var3, Icon var4) {
      super(var1, var2, var3, var4);
   }

   @Override
   public final boolean accepts(Object var1) {
      if (var1 instanceof Operation) {
         return true;
      } else if (var1 instanceof IntegrationProject) {
         return this.isProjectApplicable();
      } else {
         return var1 instanceof Folder ? ((Folder)var1).getItemType() == EntityType.Operation : false;
      }
   }

   @Override
   protected final void updateEnabledState(List<? extends Object> var1) {
      int var2 = var1 != null ? var1.size() : 0;
      if (var2 == 1) {
         this.setEnabled(true);
      } else if (var2 > 1) {
         this.setEnabled(this.isOnlyOperations(var1));
      } else {
         this.setEnabled(false);
      }
   }

   private boolean isOnlyOperations(List<?> var1) {
      for (Object var3 : var1) {
         if (!(var3 instanceof Operation)) {
            return false;
         }
      }

      return true;
   }

   protected boolean isProjectApplicable() {
      return true;
   }

   protected final List<Operation> getActiveOperations() {
      HashSet var1 = Sets.newHashSet();

      for (Object var3 : this.getActiveObjects()) {
         if (var3 instanceof Operation) {
            var1.add((Operation)var3);
         } else if (var3 instanceof Folder) {
            this.collectOperations((Folder)var3, var1);
         } else if (var3 instanceof IntegrationProject && this.isProjectApplicable()) {
            this.collectOperations((IntegrationProject)var3, var1);
            break;
         }
      }

      return Lists.newArrayList(var1);
   }

   private void collectOperations(Folder var1, Set<Operation> var2) {
      for (IntegrationEntity var4 : var1.getAllChildren()) {
         if (var4 instanceof Operation) {
            var2.add((Operation)var4);
         } else if (var4 instanceof Folder) {
            this.collectOperations((Folder)var4, var2);
         }
      }
   }

   private void collectOperations(IntegrationProject var1, Set<Operation> var2) {
      Iterables.addAll(var2, var1.getAllEntitiesOfClass(Operation.class));
   }
}
