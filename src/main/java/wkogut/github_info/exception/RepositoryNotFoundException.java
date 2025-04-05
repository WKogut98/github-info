package wkogut.github_info.exception;

public class RepositoryNotFoundException extends RuntimeException
{
    public RepositoryNotFoundException(String message)
    {
        super(message);
    }
}
