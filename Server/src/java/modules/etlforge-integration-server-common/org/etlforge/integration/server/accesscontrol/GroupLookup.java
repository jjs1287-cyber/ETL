package org.etlforge.integration.server.accesscontrol;

import java.util.List;

public interface GroupLookup {
   List<Group> getGroups(User var1) throws AccessLookupException;
}
