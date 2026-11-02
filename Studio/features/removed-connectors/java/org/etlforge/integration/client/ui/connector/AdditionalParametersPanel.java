package org.etlforge.integration.client.ui.connector;

import com.google.common.collect.ImmutableList;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.NColumnPanel;

@ParametersAreNonnullByDefault
public interface AdditionalParametersPanel {
   Properties getParameters();

   void setParameters(@Nullable Properties var1);

   void layoutIn(NColumnPanel var1);

   void registerAsChangeSource(InputPanel var1);

   ImmutableList<BoundProperty> getBoundProperties();

   Disposable installAutoCompletion(ValueCache<? extends ConnectorEndpoint> var1);
}
