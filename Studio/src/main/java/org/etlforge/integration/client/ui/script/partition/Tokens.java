package org.etlforge.integration.client.ui.script.partition;

import org.etlforge.ui.text.partition.BlockCommentToken;
import org.etlforge.ui.text.partition.BooleanConstant;
import org.etlforge.ui.text.partition.OneOfToken;
import org.etlforge.ui.text.partition.RegExToken;
import org.etlforge.ui.text.partition.SingleLineCommentToken;
import org.etlforge.ui.text.partition.StringLiteralToken;
import org.etlforge.ui.text.partition.Token;
import org.etlforge.ui.text.partition.WordToken;

public final class Tokens {
   public static final Token NO_CODE = new NoCodeToken();
   public static final Token BEGIN_TRANS = new OneOfToken(new String[]{"<trans>", "<javascript>"}, true);
   public static final Token END_TRANS = new OneOfToken(new String[]{"</trans>", "</javascript>"}, true);
   public static final Token SINGLE_LINE_COMMENT = new SingleLineCommentToken();
   public static final Token BLOCK_COMMENT = new BlockCommentToken();
   public static final Token STRING = new StringLiteralToken();
   public static final Token RAISE_ERROR = new RaiseErrorToken();
   public static final Token FUNCTION = new FunctionToken();
   public static final Token OPERATOR = new OperatorToken();
   public static final Token BOOLEAN = new BooleanConstant();
   public static final Token GROUPING = new GroupingToken();
   public static final Token SOURCE_PATH = new SourcePathToken();
   public static final Token GLOBAL_DATA_ELEMENT = new GlobalDataElementToken();
   public static final Token NUMERIC_CONSTANT = new RegExToken("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\b");
   public static final Token WORD_TOKEN = new WordToken();
   public static final Token JAVASCRIPT_KEYWORD = new OneOfToken(new String[]{"break","case","catch","continue","debugger","default","delete","do","else","finally","for","function","if","in","instanceof","new","return","switch","this","throw","try","typeof","var","void","while","with","null","undefined"},true,true);

   private Tokens() {
   }
}
