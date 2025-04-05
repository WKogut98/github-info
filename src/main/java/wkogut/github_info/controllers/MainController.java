package wkogut.github_info.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wkogut.github_info.exception.UsernameNotFoundException;
import wkogut.github_info.models.GitBranch;
import wkogut.github_info.models.GitRepository;
import wkogut.github_info.services.APIService;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private APIService apiService;

    @GetMapping("/")
    public String index()
    {
        return "index";
    }

    @PostMapping("/repos")
    public String getUserRepos(@RequestParam("username") String username, Model model)
    {
        try
        {
            List<GitRepository> repositories = apiService.getUserRepositories(username);
            for (GitRepository repository : repositories) {
                List<GitBranch> branches = apiService.getRepositoryBranches(repository.getOwner(), repository.getName());
                repository.setBranches(branches);
            }
            model.addAttribute("repositories", repositories);
            model.addAttribute("username", username);
        }
        catch (UsernameNotFoundException e)
        {
            model.addAttribute("error", "User not found: " + username);
            model.addAttribute("repositories", Collections.emptyList());
        }
        return "index";
    }
}
