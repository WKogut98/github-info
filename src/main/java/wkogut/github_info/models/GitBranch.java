package wkogut.github_info.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitBranch {
    String name;
    String lastCommitSha;


    public GitBranch(String name, String lastCommitSha)
    {
        this.name = name;
        this.lastCommitSha = lastCommitSha;
    }
    public GitBranch()
    {

    }
}
