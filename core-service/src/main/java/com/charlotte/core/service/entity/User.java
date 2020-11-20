package com.charlotte.core.service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;

@Data
@EqualsAndHashCode
public class User /*implements UserDetails*/ {

    private String id;
    private String name;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public static void main(String[] args) throws ParseException {

//        Calendar calendar = Calendar.getInstance();
//        System.out.println(calendar.getTime());
//        Date date = new SimpleDateFormat("yyyy/mm/dd").parse("2019/04/15");
//        System.out.println(new SimpleDateFormat("yyyy/mm/dd").format(date));

        Thread thread;

        User u1 = new User("111");
        User u2 = new User("111");
        System.out.println("u1.toString() = " + u1.toString() + ", u1.hashCode() = " + u1.hashCode());
        System.out.println("u2.toString() = " + u2.toString() + ", u2.hashCode() = " + u2.hashCode());
        System.out.println(u1.canEqual(u2));
        System.out.println(u1.equals(u2));
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public String getPassword() {
//        return "user";
//    }
//
//    @Override
//    public String getUsername() {
//        return "user";
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
