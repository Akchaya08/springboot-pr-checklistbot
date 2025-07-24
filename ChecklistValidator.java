package GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Util;

import java.util.regex.Pattern;

public class ChecklistValidator {

    private static final Pattern JIRA_PATTERN = Pattern.compile("CPLAT-\\d+");
    private static final Pattern TEST_CASE_PATTERN = Pattern.compile("(?i)test cases?");

    public boolean hasJiraTicket(String text) {
        return JIRA_PATTERN.matcher(text).find();
    }

    public boolean hasTestCasesMentioned(String text) {
        return TEST_CASE_PATTERN.matcher(text).find();
    }

    public boolean isChecklistComplete(String text) {
        return hasJiraTicket(text) && hasTestCasesMentioned(text);
    }
}
