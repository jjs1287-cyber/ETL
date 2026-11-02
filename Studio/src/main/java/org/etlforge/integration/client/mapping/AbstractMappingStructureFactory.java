package org.etlforge.integration.client.mapping;

import com.google.common.base.Preconditions;
import java.util.logging.Logger;

public abstract class AbstractMappingStructureFactory implements MappingStructureFactory {
   private volatile Logger logger = Logger.getAnonymousLogger();

   public final void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   public final Logger getLogger() {
      return this.logger;
   }
}
