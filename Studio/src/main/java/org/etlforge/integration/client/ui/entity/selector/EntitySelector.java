package org.etlforge.integration.client.ui.entity.selector;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.SelectorTable;
import org.etlforge.ui.widget.list.ListSelectionMode;

public interface EntitySelector<T extends IntegrationEntity> extends UiProvider {
   T getSelectedEntity();

   void setSelectedEntity(T var1);

   List<T> getSelectedEntities();

   void setFilter(EntityFilter var1);

   SelectionSource getSelectionSource();

   void setDefaultAction(SelectionAction var1);

   enum SelectionMode {
      SINGLE,
      MULTIPLE;

      ListSelectionMode asListSelectionMode() {
         switch (this) {
            case SINGLE:
               return ListSelectionMode.SINGLE_SELECTION;
            case MULTIPLE:
               return ListSelectionMode.MULTIPLE_INTERVAL_SELECTION;
            default:
               throw new RuntimeException("Unexpected SelectionMode encountered: " + this);
         }
      }

      SelectorTable.Mode asSelectorTableMode() {
         switch (this) {
            case SINGLE:
               return SelectorTable.Mode.SINGLE;
            case MULTIPLE:
               return SelectorTable.Mode.MULTIPLE;
            default:
               throw new RuntimeException("Unexpected SelectionMode encountered: " + this);
         }
      }
   }
}
