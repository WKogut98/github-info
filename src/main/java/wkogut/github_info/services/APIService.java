package wkogut.github_info.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import wkogut.github_info.exception.RepositoryNotFoundException;
import wkogut.github_info.exception.UsernameNotFoundException;
import wkogut.github_info.models.GitBranch;
import wkogut.github_info.models.GitRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class APIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<GitRepository> getUserRepositories(String username)
    {
        String URL = "https://api.github.com/users/"+username+"/repos";
        try
        {
            ResponseEntity<Map[]> response = restTemplate.getForEntity(URL, Map[].class, username);
            if(response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            List<Map> repositories = Arrays.asList(response.getBody());
            return repositories.stream().filter(repo -> !(boolean)repo.get("fork")).
                    map(repo -> new GitRepository(
                            (String) repo.get("name"),
                            (String) ((Map) repo.get("owner")).get("login")
                    )).
                    collect(Collectors.toList());
        }
        catch (HttpClientErrorException.NotFound e)
        {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
    public List<GitBranch> getRepositoryBranches(String owner, String repositoryName)
    {
        String URL = "https://api.github.com/repos/" + owner + "/" + repositoryName + "/branches";
        try{
            ResponseEntity<Map[]> response = restTemplate.getForEntity(URL, Map[].class);
            List<Map> branches = Arrays.asList(response.getBody());
            return branches.stream().map(
                    branch -> new GitBranch((String) branch.get("name"),
                            (String) ((Map)branch.get("commit")).get("sha")
                    )).collect(Collectors.toList());
        }
        catch (HttpClientErrorException.NotFound e)
        {
            throw new RepositoryNotFoundException("Repository not found: " + repositoryName);
        }
    }
}
