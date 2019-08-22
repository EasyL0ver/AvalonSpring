package authentication;

//import data.UserModel;
//import data.UsersRepository;
import data.Account;
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
    public void Authenticate(Login login) throws Exception {

        List<Account> allAccounts = usersRepository.findAll();

        Account thisLoginAccount = null;

        for(Account account : allAccounts) {
            if(account.getUsername().equals(login.getUsername()))
                thisLoginAccount = account;
        }

        if(thisLoginAccount == null)
            throw new Exception("account doesnt exist");

        if(!thisLoginAccount.getPassword().equals(login.getPassword()))
            throw new Exception("invalid password");

    }
}
