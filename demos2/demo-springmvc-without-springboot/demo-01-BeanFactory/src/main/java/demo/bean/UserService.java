package demo.bean;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserService
 */
public class UserService {

    @Setter
    private UserDao userDao;

    @Setter
    private ProfileDao profileDao;

    public void saySth(){
        System.out.println("call UserService.saySth()");
        userDao.saySth();
        profileDao.saySth();
    }
}
