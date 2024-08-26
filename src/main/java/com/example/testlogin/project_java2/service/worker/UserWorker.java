package com.example.testlogin.project_java2.service.worker;
import com.example.testlogin.project_java2.constant.EnumConstant;
import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.mapper.UserMapper;
import com.example.testlogin.project_java2.model.Role;
import com.example.testlogin.project_java2.model.UserAccount;
import com.example.testlogin.project_java2.model.object.VerifyAccount;
import com.example.testlogin.project_java2.repo.RoleRepo;
import com.example.testlogin.project_java2.repo.UserRepo;
import com.example.testlogin.project_java2.repo.VerifyAccountRepo;
import com.example.testlogin.project_java2.security.Security;
import com.example.testlogin.project_java2.service.SendMailService;
import com.example.testlogin.project_java2.service.UserService;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.Cipher;

@Service
public class UserWorker implements UserService{

    @Value("${app.KEY_ENCRYPT}")
    public String KEY_ENCRYPT;

    private final UserRepo userRepository;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final SendMailService sendMailService;
    private final VerifyAccountRepo  verifyAccountRepo;

    @Autowired
    public UserWorker(UserRepo userRepository, RoleRepo roleRepo, PasswordEncoder passwordEncoder, SendMailService sendMailService, VerifyAccountRepo verifyAccountRepo) {
        this.userRepository = userRepository;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.sendMailService = sendMailService;
        this.verifyAccountRepo = verifyAccountRepo;
    }

    public String encryptPassword(String password, String key) {
        try {
            byte[] iv = new byte[16];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes) + generateNumber();
        } catch (Exception exception) {

            System.out.println("ERROR: "+exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public long generateNumber() {
        return ThreadLocalRandom.current().nextLong(10000L, 100000L);
    }


    @Override
    public int countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    @Override
    public void sendMailToVerifyUser(UserDto userDto){
        if(userDto != null){
            String token = null;
            try{
                verifyAccountRepo.save(new VerifyAccount(
                    userDto.getEmail(),
                    token = encryptPassword(userDto.getPassword(),
                    this.KEY_ENCRYPT)
                ));
                sendMailService.setMailSender(userDto.getEmail(),
        "VERIFY YOUR ACCOUNT", "Verify token: " +
                token);
            }catch (Exception exception){
                System.err.println("Lỗi "+exception.getMessage());
            }
        }
    }

    public boolean checkVerifyToken(String email, String token) {
//            return true;
        return verifyAccountRepo.countVerifyAccountByEmailAndVerifyToken(email, token) > 0;
    }

    @Override
    public boolean verifyAccount(String email, String password,String token){
        if(email != null || password != null || token != null)
            if(checkVerifyToken(email, token))
                return createUser(email, password);
        return false;
    }

    @Override
    public int countVerifyAccountByEmail(String email) {
        return verifyAccountRepo.countVerifyAccountByEmail(email);
    }

    private boolean createUser(String email, String password){
        try{
            UserAccount user = new UserAccount();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setType(EnumConstant.NORMAL);
            user.setActive(EnumConstant.isACTIVE);
            Role role = roleRepo.findByName("USER");
            user.setRoles(Collections.singletonList(role));
            userRepository.save(user);
            verifyAccountRepo.deleteVerifyAccountEmail(email);
            return true;
        }catch (Exception exception){
            System.err.println(exception.getMessage());
            return false;
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        UserAccount user = new UserAccount();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setType(EnumConstant.NORMAL);
        user.setActive(EnumConstant.isACTIVE);
        Role role = roleRepo.findByName("USER");
        user.setRoles(Collections.singletonList(role));

        UserAccount newUser = userRepository.save(user);
        UserDto userResponse = new UserDto();
        userResponse.setId(newUser.getId());
        userResponse.setEmail(newUser.getEmail());
        userResponse.setPassword(newUser.getPassword());
        userResponse.setType(newUser.getType());
        userResponse.setActive(newUser.getActive());

        return userResponse;
    }


    @Override
    public UserDto update_name_user(String name) {

        UserAccount user = userRepository.findByEmail(Security.getSessionUser());
        user.setName(name);
        UserAccount userUpdated = userRepository.save(user);

        UserDto userResponse = new UserDto();
        userResponse.setId(userUpdated.getId());
        userResponse.setEmail(userUpdated.getEmail());
        userResponse.setName(userUpdated.getName());
        userResponse.setType(userUpdated.getType());
        userResponse.setActive(userUpdated.getActive());

        System.out.println("User updated: " + userResponse);

        return userResponse;
    }


    @Override
    public List<UserDto> listUsersApi() {

        List<UserAccount> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::mapToUserApiDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserAccount findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkActiveUser() {
        UserAccount account = userRepository.findByEmail(Security.getSessionUser());

        if(account != null){
            System.out.println(account.getActive());
            return account.getActive() == EnumConstant.LOCKED;
        }
        return false;
    }


}
