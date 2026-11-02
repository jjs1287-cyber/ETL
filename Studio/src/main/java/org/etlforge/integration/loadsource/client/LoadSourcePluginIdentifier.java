package org.etlforge.integration.loadsource.client;

import com.google.common.base.Preconditions;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.RelativePosition;

public final class LoadSourcePluginIdentifier {
   private final PluginIdentifier id;
   private final RelativePosition position;

   public LoadSourcePluginIdentifier(PluginIdentifier var1, RelativePosition var2) {
      Preconditions.checkNotNull(var1, "id");
      Preconditions.checkNotNull(var2, "position");
      this.id = var1;
      this.position = var2;
   }

   public PluginIdentifier getId() {
      return this.id;
   }

   public RelativePosition getPosition() {
      return this.position;
   }
}
