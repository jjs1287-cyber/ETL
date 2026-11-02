package org.etlforge.integration.client.ui.interchange.entity.target;

import com.google.common.base.Function;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.AbstractNewLocatableCallback;
import org.etlforge.integration.data.entity.LocatableFolders;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.TargetFolders;
import org.etlforge.integration.data.location.DataLocationType;

final class NewTargetCallback extends AbstractNewLocatableCallback {
   public NewTargetCallback(Function<Collection<DataLocationType>, DataLocationType> var1) {
      super(var1);
   }

   @Override
   protected LocatableFolders createTypeFolders(RootFolder var1) {
      return new TargetFolders(var1);
   }
}
