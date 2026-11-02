package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Optional;
import java.util.List;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ProjectItemTransformDrop implements TreeDropStrategy {
   private final ProjectItemTransform transform;

   public ProjectItemTransformDrop(ProjectItemTransform var1) {
      this.transform = var1;
   }

   @Override
   public boolean canDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2) {
      Folder var3 = this.getFolder(var1);
      if (var3 != null && var3.getItemType() == this.transform.getDestinationType()) {
         IntegrationEntity var4 = this.getDraggedItem(var2);
         if (var4 != null) {
            return this.transform.isSupported(var4, Optional.of(var3));
         }
      }

      return false;
   }

   private Folder getFolder(IntegrationEntity var1) {
      if (var1 instanceof Folder) {
         return (Folder)var1;
      }

      IntegrationEntity var2 = var1.getParent();

      while (var2 != null && !(var2 instanceof Folder)) {
         var2 = var2.getParent();
      }

      return (Folder)var2;
   }

   private IntegrationEntity getDraggedItem(List<? extends IntegrationEntity> var1) {
      return var1.size() == 1 ? (IntegrationEntity)var1.get(0) : null;
   }

   @Override
   public boolean handleDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2) {
      Folder var3 = this.getFolder(var1);
      if (var3 == null) {
         return false;
      }

      IntegrationEntity var4 = this.getDraggedItem(var2);
      this.transform.doTransform(var4, var3);
      return true;
   }
}
