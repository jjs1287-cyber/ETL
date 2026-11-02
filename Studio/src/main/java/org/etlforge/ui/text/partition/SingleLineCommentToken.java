package org.etlforge.ui.text.partition;

public class SingleLineCommentToken extends StartEndBoundaryToken {
   public SingleLineCommentToken() {
      super("//", "\n");
   }
}
