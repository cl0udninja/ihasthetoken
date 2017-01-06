package com.jelohazi.ihasthetoken.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jelohazi.ihasthetoken.domain.User;
import com.jelohazi.ihasthetoken.jdbc.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<User> get(
            @RequestParam(value = "id", required = false) String id) {
        if (id == null) {
            return userRepository.list();
        }
        User u = userRepository.get(UUID.fromString(id));
        return Arrays.asList(new User[] { u });
    }

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestBody User user) {
        if (user.getId() == null || userRepository.get(user.getId()) == null) {
            assert (user.getName() != null && user.getEmail() != null);
            userRepository.createOrUpdate(new User(user.getName(), user.getEmail()));
            return;
        }
        assert (user.getId() != null);
        User u = userRepository.get(user.getId());
        if (user.getName() != null) {
            u.setName(user.getName());
        }
        if (user.getEmail() != null) {
            u.setEmail(user.getEmail());
        }
        userRepository.createOrUpdate(u);
    }
}
