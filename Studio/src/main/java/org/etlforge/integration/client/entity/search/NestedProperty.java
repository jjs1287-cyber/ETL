package org.etlforge.integration.client.entity.search;

import java.util.List;

public interface NestedProperty<E> {
   List<SearchableProperty> getAssociatedProperties(E var1);
}
