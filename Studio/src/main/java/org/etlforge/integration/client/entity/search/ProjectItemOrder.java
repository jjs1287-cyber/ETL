package org.etlforge.integration.client.entity.search;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface ProjectItemOrder<T extends IntegrationEntity> {
   T next();

   T getFirst();

   T previous();

   T getLast();

   boolean isBefore(T var1, T var2);
}
