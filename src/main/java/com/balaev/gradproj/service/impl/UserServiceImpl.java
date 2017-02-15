package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.service.api.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
}
