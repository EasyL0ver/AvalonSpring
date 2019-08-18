package authentication;

public interface UserFactory {
    User Build(Login login);
}
