package demo.bean;

import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public class UserService {
    public void saySth(){
        System.out.println("call UserService.saySth()");
    }
}
