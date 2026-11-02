package org.etlforge.integration.salesforce.parser;

import org.etlforge.ui.text.partition.Token;

interface SoqlToken extends Token {
   SoqlTokenId getId();
}
