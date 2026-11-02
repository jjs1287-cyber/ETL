package com.etlforge.integration.client.ui.interchange.project.share;

import com.etlforge.integration.client.project.share.ConflictResolution;
import com.etlforge.integration.client.project.share.ConflictResolver;
import com.etlforge.integration.client.project.share.DefaultConflictResolver;
import com.etlforge.integration.client.project.share.IdConflict;
import com.etlforge.integration.client.project.share.NameConflict;
import com.etlforge.integration.client.project.share.history.ImportHistoryEntry;
import org.etlforge.integration.client.project.ManagedProject;

class ConflictResolverImpl implements ConflictResolver {
   private final ConflictResolver nameResolver;
   private final IdConflictResolver idResolver;

   public ConflictResolverImpl(ManagedProject var1, EntityOverwriter var2, int var3) {
      this.nameResolver = new DefaultConflictResolver(var1);
      this.idResolver = new IdConflictResolver(var2, var3);
   }

   @Override
   public ConflictResolution resolve(NameConflict var1) {
      return this.nameResolver.resolve(var1);
   }

   @Override
   public ConflictResolution resolve(IdConflict var1) {
      return this.idResolver.resolve(var1);
   }

   public void setHistoryEntry(ImportHistoryEntry var1) {
      this.idResolver.setHistoryEntry(var1);
   }
}
