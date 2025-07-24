package GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Controller;

import GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Service.GitHubService;
import GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Service.PrChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checklist")
public class PrChecklistController {
@Autowired
private GitHubService githubService;

public void validatePr(int prNumber) {
    githubService.validatePullRequest(prNumber);
}

    @Autowired
    private PrChecklistService checklistService;

    @PostMapping("/validate")
    public String validateChecklist(@RequestBody ChecklistRequest request) {
        String message = checklistService.getChecklistStatusMessage(request.getText());
        return message != null ? message : "✅ Checklist complete!";
    }

    public static class ChecklistRequest {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
