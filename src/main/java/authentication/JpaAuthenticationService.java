package authentication;

//import data.UserModel;
//import data.UsersRepository;
import data.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaAuthenticationService implements AuthenticationService{

    private final UsersRepository usersRepository;

    @Autowired
    public JpaAuthenticationService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public void Authenticate(Login login) {

        //List<UserModel> userModels = usersRepository.findAll();

    }
}
