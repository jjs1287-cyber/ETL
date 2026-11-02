package org.etlforge.integration.client.admin.engine;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.GetStatusCallback;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.admin.StartCallback;
import org.etlforge.integration.client.admin.StopCallback;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public abstract class ServerEngine extends AbstractPropertyBasedModel {
   public static final String STATUS = "status";
   public static final String DETAILS = "details";
   private final String name;
   private final Property<EnumSet<ServerStatus>> status;
   private final Property<String> details;
   private volatile boolean gettingStatus;

   public ServerEngine(String var1) {
      Preconditions.checkNotNull(var1, "name");
      this.name = var1;
      this.status = this.newProperty("status", EnumSet.of(ServerStatus.UNDEFINED));
      this.status.setAllowsNull(false);
      this.details = this.newProperty("details", "");
      this.details.setAllowsNull(false);
   }

   public final String getName() {
      return this.name;
   }

   public final void setStatus(EnumSet<ServerStatus> var1) {
      this.status.set(var1.clone());
      this.setGettingStatus(false);
   }

   public final EnumSet<ServerStatus> getStatus() {
      return this.status.get().clone();
   }

   public final void setDetails(String var1) {
      this.details.set(var1 != null ? var1 : "");
   }

   public final String getDetails() {
      return this.details.get();
   }

   public abstract void start(Admin var1, StartCallback var2);

   public abstract void stop(Admin var1, StopCallback var2);

   public abstract void requestStatus(Admin var1, Admin.StatusDetail var2, GetStatusCallback var3);

   public final void setGettingStatus(boolean var1) {
      this.gettingStatus = var1;
   }

   public final boolean isGettingStatus() {
      return this.gettingStatus;
   }
}
