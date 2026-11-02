package org.etlforge.integration.client.entity.search;

import java.util.Set;

public interface SearchableEnumProperty<E extends Enum<E>> extends SearchableProperty<E> {
   Set<E> getSupportedValues();
}
