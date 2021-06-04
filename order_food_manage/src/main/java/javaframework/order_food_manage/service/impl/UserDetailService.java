package javaframework.order_food_manage.service.impl;

import javaframework.order_food_manage.dto.MyUser;
import javaframework.order_food_manage.entities.UserEntity;
import javaframework.order_food_manage.repository.UserRepos;
import org.hibernate.cache.spi.entry.StructuredCacheEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepos userRepos;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepos.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        List<GrantedAuthority> grantedAuthorities = userEntity.getRoleEntities().stream()
                .map(role-> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toList());
        MyUser myUser = new MyUser(userEntity.getUsername(), userEntity.getPassword(),
                true,true,true,true, grantedAuthorities);
        myUser.setFullName(userEntity.getFullName());
        myUser.setAddress(userEntity.getAddress());
        myUser.setPhone(userEntity.getPhone());
        return myUser;
    }
}
