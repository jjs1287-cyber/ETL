package org.etlforge.integration.client.ui.interchange.core.action.popup;

import com.google.common.base.Predicate;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ViewDependenciesItemFactory extends SingleItemPopupMenuFactory {
   public ViewDependenciesItemFactory(Action var1) {
      super(var1, new ViewDependenciesItemFactory.Filter());
      this.addUnsupportedSite(PopupMenuSites.DETACHED_PAGE);
   }

   private static class Filter implements Predicate<Object> {
      private Filter() {
      }

      @Override
      public boolean apply(Object var1) {
         return var1 instanceof IntegrationEntity && !(var1 instanceof Folder) && !(var1 instanceof IntegrationProject);
      }
   }
}
