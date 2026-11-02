package org.etlforge.ui.text.partition;

public class BooleanConstant extends OneOfToken implements Token {
   public BooleanConstant() {
      super(new String[]{"true", "false"}, false, true);
   }
}
