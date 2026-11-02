package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import java.awt.Color;
import org.etlforge.ui.text.highlight.AbstractTokenStyles;

public class FilterTokenStyles extends AbstractTokenStyles {
   public FilterTokenStyles(FilterExpressionTokens var1) {
      this.addStyle(var1.getAttributeToken(), new Color(153, 0, 102));
      this.addStyle(var1.getOperatorToken(), new Color(51, 102, 255));
   }
}
