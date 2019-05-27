package org.openbpm.bpm.core.manager;

import org.openbpm.bpm.core.model.overallview.BpmOverallView;
import java.util.List;
import java.util.Map;

public interface BpmDefOverallManager {
    Map<String, String> exportBpmDefinitions(String... strArr) throws Exception;

    BpmOverallView getBpmOverallView(String str);

    Map<String, List<BpmOverallView>> importPreview(String str) throws Exception;

    void importSave(List<BpmOverallView> list);

    void saveBpmOverallView(BpmOverallView bpmOverallView);
}
