package org.etlforge.integration.client.ui.deploy.option;

import javax.swing.JComponent;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.ui.util.option.Option;

public interface DeployOption extends Option {
   String getId();

   DeployData getDeployData();

   void execute(String var1);

   void setActive(boolean var1);

   boolean requiresValidProject();

   boolean isAdvanced();

   void addAdditionalUi(JComponent var1);
}
