package org.etlforge.integration.client.entity.valuecache;

import com.google.common.collect.ImmutableSet;

public interface History {
   ImmutableSet<String> getValues();

   boolean delete(String var1);
}
