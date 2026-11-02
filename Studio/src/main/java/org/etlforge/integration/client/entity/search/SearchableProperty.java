package org.etlforge.integration.client.entity.search;

import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface SearchableProperty<T> {
   String getName();

   Class<T> getValueType();

   @Nullable
   T getValue(IntegrationEntity var1);
}
