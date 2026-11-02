package org.etlforge.integration.client.entity.search;

import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface ReplaceableProperty<T> extends SearchableProperty<T> {
   void setValue(IntegrationEntity var1, @Nullable T var2);
}
