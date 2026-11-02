package org.etlforge.ui.text.partition;

public interface Token {
   String lexeme();

   int offset();

   boolean matches(String var1, int var2);

   boolean isWhitespace();
}
