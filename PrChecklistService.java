package GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Service;

import GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Util.ChecklistValidator;
import org.springframework.stereotype.Service;

@Service
public class PrChecklistService {

    private final ChecklistValidator validator = new ChecklistValidator();

    public boolean isChecklistComplete(String prText) {
        return validator.isChecklistComplete(prText);
    }

    public String getChecklistStatusMessage(String prText) {
        if (!isChecklistComplete(prText)) {
            return "⚠️ Checklist incomplete. Please ensure the PR includes a JIRA ticket (e.g., CPLAT-12345) and mentions unit test cases.";
        }
        return null;
    }
}
