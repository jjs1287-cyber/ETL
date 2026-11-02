package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;

public final class LinkSaveConsequence implements SaveConsequence {
   private final ImmutableList<LinkToImplementingObject> links;
   private final Operation op;
   private final EntityPersistor persistor;

   public LinkSaveConsequence(ImmutableList<LinkToImplementingObject> var1, Operation var2, EntityPersistor var3) {
      this.links = var1;
      this.op = var2;
      this.persistor = var3;
   }

   @Override
   public boolean isAllowed() {
      return true;
   }

   @Override
   public void apply() {
      HashSet var1 = Sets.newHashSet();

      for (LinkToImplementingObject var3 : this.links) {
         List var4 = var3.applyToOperation(this.op);
         var1.addAll(var4);
      }

      this.persistChanges(var1);
   }

   private void persistChanges(Collection<IntegrationEntity> var1) {
      for (IntegrationEntity var3 : var1) {
         try {
            this.persistor.save(var3);
         } catch (InterchangeSavingException var5) {
            ErrorLog.attention(this.getClass(), null, var5);
         }
      }
   }
}
