package org.etlforge.integration.client.ui.interchange.core.action.popup;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class SetPermissionsItemFactory extends SingleItemPopupMenuFactory {
   public SetPermissionsItemFactory(Action var1) {
      super(var1, IntegrationEntity.class);
      this.addUnsupportedSite(PopupMenuSites.DETACHED_PAGE);
   }
}
