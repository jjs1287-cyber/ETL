package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TargetFolders;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public final class SourceToTargetFactory extends ProjectItemTransform {
   public SourceToTargetFactory(UiEntityFactory var1) {
      super(var1, EntityType.Target);
   }

   @Override
   public boolean isSupported(IntegrationEntity var1, Optional<Folder> var2) {
      if (!(var1 instanceof Source)) {
         return false;
      } else if (var2.isPresent()) {
         IntegrationProject var3 = ProjectUtils.getProject(var1);
         RootFolder var4 = var3.getRootFolder(EntityType.Target);
         TargetFolders var5 = new TargetFolders(var4);
         DataLocationType var6 = ((Source)var1).getLocation().getDataLocationType();
         return var5.isTypeAllowed(var6, (Folder)var2.get());
      } else {
         return true;
      }
   }

   @Override
   protected void configureDestination(IntegrationEntity var1, IntegrationEntity var2) {
      DataLocation var3 = LocatableTransformHelper.cloneLocation((Source)var1);
      ((Target)var2).setLocation(var3);
   }
}
