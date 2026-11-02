package com.etlforge.integration.client.ui.sfdc.connector.page;

import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.LoginCredentialsOAuthUi;
import java.awt.Insets;
import java.util.Collections;
import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.*;
import org.etlforge.ui.Size;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.util.focus.TextComponentFocusTraversal;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

                                                                       
public final class SalesforceConnectorPanel extends IntegrationEntityPanel {
   private final LoginCredentialsOAuthUi oauthUi;
   private Action testConnectionAction;
   private ValueCache<SalesforceConnector> cache;

   public SalesforceConnectorPanel(ApplicationWindow window, SalesforceConnector connector) {
      super(window, connector);
      oauthUi = new LoginCredentialsOAuthUi(Size.columns(35));
      LoginCredentials credentials = connector.getLoginCredentials();
      if (credentials != null && credentials.isOAuth()) oauthUi.setCredentials(credentials);
      oauthUi.addToInputPanel(this);
      oauthUi.setEnabled(true);
   }
   public SalesforceConnector getDisplayedObject() { return (SalesforceConnector)super.getDisplayedObject(); }
   public void setCache(ValueCache<SalesforceConnector> value) { cache = value; }
   public void setTestConnectionAction(Action action) { testConnectionAction = action; }
   @Override protected void applyOtherPropertiesThanTheName() {
      SalesforceConnector connector = getDisplayedObject();
      LoginCredentials credentials = oauthUi.getCredentials(false);
      connector.setOrgIdentifier(new OrgIdentifier(ServerHost.defaultHost(), SalesforceVersion.V_61, credentials));
      if (!isNameFieldIncluded() && credentials != null) connector.setName(EntityUtils.getAvailableName(connector, credentials.instanceUrl));
      if (cache != null) cache.update(Collections.singleton(connector));
   }
   public OrgIdentifier getOrgIdentifier() {
      return new OrgIdentifier(ServerHost.defaultHost(), SalesforceVersion.V_61, oauthUi.getCredentials(false));
   }
   @Override public void dispose() { super.dispose(); oauthUi.dispose(); }
   @Override public void display() {
      super.display();
      NColumnPanel grid = new NColumnPanel(3);
      grid.setXWeights(0.0, 1.0, 0.0).setYWeights(0.0, 0.0, 0.0);
      grid.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE);
      grid.setInsets(new Insets(0, 0, 10, 5)); grid.setOpaque(false, false);
      grid.container().setFocusTraversalPolicyProvider(true);
      grid.container().setFocusTraversalPolicy(new TextComponentFocusTraversal());
      grid.addRow("Authentication:", "OAuth 2.0 Client Credentials", null);
      grid.addRow("API version:", SalesforceVersion.V_61.getId(), null);
      oauthUi.layoutIn(grid);
      KongaButton button = new KongaButton(testConnectionAction); button.setUseMediumActionIcon(true);
      grid.addRow(null, InvisiblePanel.of(button), null);
      IntegrationDataPanel.Layout layout = getDefaultLayout(); layout.add(grid); layout.glue();
   }
   @Override public void setInitialFocus() { oauthUi.requestFocus(); }
   public Iterable<BoundProperty> getBoundPropertyControls() { return ImmutableList.copyOf(oauthUi.getBoundProperties()); }
}