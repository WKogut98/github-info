package wkogut.github_info.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GitRepository {
    private String name;
    private String owner;
    private List<GitBranch> branches;

    public GitRepository(String name,String owner)
    {
        this.name=name;
        this.owner=owner;
    }
}
