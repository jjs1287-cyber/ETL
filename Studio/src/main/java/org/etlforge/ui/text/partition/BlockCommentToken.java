package org.etlforge.ui.text.partition;

public class BlockCommentToken extends StartEndBoundaryToken {
   public BlockCommentToken() {
      super("/*", "*/");
   }
}
