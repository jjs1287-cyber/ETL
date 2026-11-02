package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import org.etlforge.ui.UiProvider;

interface DataEntryUi<T> extends UiProvider {
   EnteredDataValidation validate();

   Optional<T> getEnteredValue();

   void focusInvalidComonent();
}
