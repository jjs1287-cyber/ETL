package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import java.util.Collection;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.ui.UiThreadSupplier;

public final class TypeSelectorUiFunction implements Function<Collection<DataLocationType>, DataLocationType> {
   public DataLocationType apply(final Collection<DataLocationType> var1) {
      return UiThreadSupplier.call(new Supplier<DataLocationType>() {
         public DataLocationType get() {
            LocationTypeSelectorDialog var1x = new LocationTypeSelectorDialog(var1);
            Optional var2 = var1x.selectType();
            return var2.isPresent() ? (DataLocationType)var2.get() : null;
         }
      });
   }
}
