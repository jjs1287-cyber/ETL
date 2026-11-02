package org.etlforge.integration.client.ui.interchange.entity.source;

import com.google.common.base.Function;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.AbstractNewLocatableCallback;
import org.etlforge.integration.data.entity.LocatableFolders;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.SourceFolders;
import org.etlforge.integration.data.location.DataLocationType;

final class NewSourceCallback extends AbstractNewLocatableCallback {
   public NewSourceCallback(Function<Collection<DataLocationType>, DataLocationType> var1) {
      super(var1);
   }

   @Override
   protected LocatableFolders createTypeFolders(RootFolder var1) {
      return new SourceFolders(var1);
   }
}
