package org.etlforge.integration.data.entity.sfdc;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.StrongEntityLink;
import org.etlforge.integration.data.entity.link.WeakEntityLink;

public final class WizardEntityLinkCollector<T extends IntegrationEntity & SalesforceWizardEntity> {
   private final IntegrationEntityLookup lookup;
   private final T wizard;
   private final List<EntityLink> links = Lists.newArrayList();

   public static <E extends IntegrationEntity & SalesforceWizardEntity> List<EntityLink> collectLinks(E var0, IntegrationEntityLookup var1) {
      WizardEntityLinkCollector var2 = new WizardEntityLinkCollector<>(var0, var1);
      return var2.collectLinks();
   }

   private WizardEntityLinkCollector(T var1, IntegrationEntityLookup var2) {
      this.wizard = (T)var1;
      this.lookup = var2;
   }

   public List<EntityLink> collectLinks() {
      synchronized (this.wizard.getDataLock()) {
         this.linkToConnector();
         Operation var2 = this.lookup.getEntity(this.wizard.getImplementingOperationId(), Operation.class);
         if (var2 != null) {
            this.links.add(new StrongEntityLink(var2, this.wizard));
            this.linkToSchedule(var2);
         }
      }

      return this.links;
   }

   private void linkToConnector() {
      SalesforceConnector var1 = this.lookup.getEntity(this.wizard.getConnectorId(), SalesforceConnector.class);
      if (var1 != null) {
         this.links.add(new SalesforceConnectorLink(var1, this.wizard));
      }
   }

   private void linkToSchedule(Operation var1) {
      Schedule var2 = this.lookup.getEntity(var1.getScheduleId(), Schedule.class);
      if (var2 != null) {
         this.links.add(new WeakEntityLink(var2, this.wizard));
      }
   }
}
