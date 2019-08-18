package authentication;

import org.springframework.stereotype.Component;

@Component
public class BasicUserFactory implements UserFactory {
    @Override
    public User Build(Login login) {
        return new BasicUser(login);
    }
}
