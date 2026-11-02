package org.etlforge.integration.client.ui.interchange.location.ldap;

import org.etlforge.integration.client.datalocation.cache.LdapLocationCache;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.panel.CacheBasedLoginNameHistory;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.LdapLocation;

public class LdapLocationPanel extends DataLocationPanel {
   private final LdapLocationPanelPart[] parts;
   private final ServerDefiner serverDefiner = new ServerDefiner(this, 20);
   private final LoginDefiner loginDefiner = new LoginDefiner(this);
   private final BaseNameDefiner baseNameDefiner = new BaseNameDefiner(this, 35);
   private LdapLocationCache cache;

   public LdapLocationPanel(LdapLocation var1, SourceTarget var2) {
      super(var1, var2);
      this.parts = new LdapLocationPanelPart[]{
         this.serverDefiner, this.loginDefiner, new PortDefiner(this), this.baseNameDefiner, new SecurityLevelDefiner(this)
      };
      this.reset();
   }

   @Override
   public void display() {
      this.doLayout();
      super.display();
   }

   @Override
   public void setInitialFocus() {
      this.serverDefiner.requestFocus();
   }

   public void setCache(LdapLocationCache var1) {
      if (var1 == null) {
         throw new NullPointerException("cache cannot be null");
      }

      this.cache = var1;
      this.loginDefiner.installAutoCompletionInLoginField(new CacheBasedLoginNameHistory(var1));
      this.serverDefiner.setHistory(new CacheBasedServerHistory(var1));
      this.baseNameDefiner.setHistory(new CacheBasedBaseNameHistory(var1));
   }

   public LdapLocation getDisplayedObject() {
      return (LdapLocation)super.getDisplayedObject();
   }

   @Override
   public void apply() throws IntegrationDataPanelException {
      LdapLocation var1 = this.getDisplayedObject();

      for (LdapLocationPanelPart var5 : this.parts) {
         var5.applyTo(var1);
      }

      if (this.cache != null) {
         this.cache.update(var1);
      }
   }

   @Override
   public void reset() {
      LdapLocation var1 = this.getDisplayedObject();

      for (LdapLocationPanelPart var5 : this.parts) {
         var5.reset(var1);
      }
   }

   private void doLayout() {
      DataLocationPanelLayout var1 = new DataLocationPanelLayout();

      for (LdapLocationPanelPart var5 : this.parts) {
         var5.addToLayout(var1);
      }

      this.wrap(var1);
   }
}
