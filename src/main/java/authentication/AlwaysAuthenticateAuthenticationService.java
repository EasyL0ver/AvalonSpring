package authentication;

import org.springframework.stereotype.Component;

@Component
public class AlwaysAuthenticateAuthenticationService implements AuthenticationService {
    @Override
    public void Authenticate(Login login) {
        //do nothing dont throw
    }
}
