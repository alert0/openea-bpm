package org.openbpm.sys.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.sys.core.model.WorkbenchLayout;

import java.util.List;

public interface WorkbenchLayoutManager extends Manager<String, WorkbenchLayout> {

    List<WorkbenchLayout> getByUserId(String currentUserId);

    void savePanelLayout(List<WorkbenchLayout> layOutList, String layoutKey);

}
