
package GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Service;

import GitHub.PR.Reviewer.Checklist.Bot.Pr.Checklist.Util.ChecklistValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.core.ParameterizedTypeReference;


import java.util.Map;
import java.util.List;


@Service
public class GitHubService {

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.repo.owner}")
    private String repoOwner;

    @Value("${github.repo.name}")
    private String repoName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ChecklistValidator validator = new ChecklistValidator();

    public void validatePullRequest(int prNumber) {
        String prUrl = String.format("https://api.github.com/repos/%s/%s/pulls/%d", repoOwner, repoName, prNumber);
        String commentsUrl = String.format("https://api.github.com/repos/%s/%s/issues/%d/comments", repoOwner, repoName, prNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Fetch PR description
ResponseEntity<Map<String, Object>> prResponse = restTemplate.exchange(prUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        String prBody = (String) prResponse.getBody().get("body");

        // Fetch PR comments
        ResponseEntity<List<Map<String, Object>>> commentsResponse = restTemplate.exchange(commentsUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
        StringBuilder allComments = new StringBuilder();
        for (Map comment : commentsResponse.getBody()) {
            allComments.append(comment.get("body")).append("\n");
        }

        // Combine PR body and comments
        String combinedText = prBody + "\n" + allComments;

        // Validate checklist
        if (!validator.isChecklistComplete(combinedText)) {
            postComment(commentsUrl, "⚠️ Checklist incomplete. Please ensure the PR includes a JIRA ticket (e.g., CPLAT-12345) and mentions unit test cases.");
        }
    }

    private void postComment(String commentsUrl, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = String.format("{\"body\": \"%s\"}", message);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        restTemplate.postForEntity(commentsUrl, entity, String.class);
    }
}
//dummychange
