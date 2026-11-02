package org.etlforge.integration.client.ui.de.global;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.selection.SelectionWidget;

public interface DataElementsWidget extends UiProvider, SelectionSource, SelectionWidget {
   void setDataElements(GlobalDataElements var1);

   void update();

   void setDefaultAction(SelectionAction var1);

   void setIncludeBuiltInDataElements(boolean var1);

   void setActionProvider(Supplier<List<Action>> var1);

   void setFilter(Predicate<String> var1);

   void setProcess(IntegrationEntity var1);
}
