package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public interface ResponseTargetSelector extends Disposable {
   Target getSelectedTarget();

   void reset(HttpLocation var1);

   void register(InputPanel var1);

   void layout(String var1, TwoColumnLayout var2);
}
