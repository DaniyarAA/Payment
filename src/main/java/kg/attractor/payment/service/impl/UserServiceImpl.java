package kg.attractor.payment.service.impl;

import kg.attractor.payment.dao.UserDao;
import kg.attractor.payment.dto.UserDto;
import kg.attractor.payment.model.User;
import kg.attractor.payment.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDto userDto){
        User user = User.builder()
                .phoneNumber(userDto.getPhoneNumber())
                .name(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enabled(true)
                .authorityId(2)
                .build();
        userDao.createUser(user);
    }
}
