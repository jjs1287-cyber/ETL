package org.etlforge.integration.client.ui.script.partition;

import org.etlforge.ui.text.partition.OneOfToken;
import org.etlforge.ui.text.partition.Token;

class GroupingToken extends OneOfToken implements Token {
   public GroupingToken() {
      super(new String[]{"(", ")"}, true);
   }
}
