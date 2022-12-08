package com.baitapnhom.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baitapnhom.entity.User;
import com.baitapnhom.renpository.RoleRepository;
import com.baitapnhom.renpository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository appUserDAO;

    @Autowired
    private RoleRepository appRoleDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       User appUser = appUserDAO.findByUsername(userName).orElseThrow();

        if (appUser == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

        System.out.println("Found User: " + appUser.getUsername());

        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = appRoleDAO.getRoleNames(appUser.getId());
System.out.println(roleNames.get(0));
        List<GrantedAuthority> grantList = new ArrayList<>();
        if (roleNames != null) {
            for (String role : roleNames) {

                    if(role.equalsIgnoreCase("2"))
                        role="ROLE_ADMIN";
                    else
                        role="ROLE_USER";
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
                System.out.println(role);
                grantList.add(authority);
            }
        }
        

        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(appUser.getUsername(), //
                appUser.getPassword(), grantList);
        System.out.println(userDetails.toString());

        return userDetails;
    }

}

