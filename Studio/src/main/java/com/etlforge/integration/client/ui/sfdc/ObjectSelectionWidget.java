package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Predicate;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.selection.SelectionWidget;

public interface ObjectSelectionWidget extends UiProvider, SelectionWidget {
   void setDefaultAction(SelectionAction var1);

   void setNameFilter(Predicate<? super SObjectSchema> var1);
}
