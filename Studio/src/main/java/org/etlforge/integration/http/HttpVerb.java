package org.etlforge.integration.http;

import org.etlforge.system.EnumWithId;

public enum HttpVerb implements EnumWithId<Integer> {
   GET,
   HEAD,
   POST,
   PUT,
   DELETE,
   TRACE,
   CONNECT;

   public Integer getId() {
      return this.ordinal();
   }
}
