package org.etlforge.integration.client.ui.interchange.content;

import com.google.common.base.Optional;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.entity.tree.TopLevelNode;
import org.etlforge.integration.client.ui.interchange.component.tree.TreeDropStrategy;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

@ParametersAreNonnullByDefault
public interface ContentViewerCategory extends TopLevelNode {
   String getName();

   Icon getIcon(EntityIcons.Size var1, EntityIcons.Category var2);

   List<EntityType> getTypes();

   Optional<? extends JPopupMenu> getRootMenu(EntityConfigLookup var1);

   List<TreeDropStrategy> getTreeDropStrategies(EntityConfigLookup var1);
}
