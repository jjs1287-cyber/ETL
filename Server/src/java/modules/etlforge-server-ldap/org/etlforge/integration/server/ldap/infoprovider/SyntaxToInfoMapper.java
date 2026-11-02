package org.etlforge.integration.server.ldap.infoprovider;

import org.etlforge.integration.ldap.structure.AttributeSyntaxInfo;

public interface SyntaxToInfoMapper {
   AttributeSyntaxInfo fromSyntax(String var1);
}
