package javaframework.order_food_manage.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class UserEntity extends BaseEntity{

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roleEntities = new ArrayList<>();

    @OneToMany(mappedBy = "userManagerEntity")
    private List<OrderEntity> orderEntities_Manager = new ArrayList<>();

    @OneToMany(mappedBy = "userCustomerEntity")
    private List<OrderEntity> orderEntities_Customer = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(List<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public List<OrderEntity> getOrderEntities_Manager() {
        return orderEntities_Manager;
    }

    public void setOrderEntities_Manager(List<OrderEntity> orderEntities_Manager) {
        this.orderEntities_Manager = orderEntities_Manager;
    }

    public List<OrderEntity> getOrderEntities_Customer() {
        return orderEntities_Customer;
    }

    public void setOrderEntities_Customer(List<OrderEntity> orderEntities_Customer) {
        this.orderEntities_Customer = orderEntities_Customer;
    }
}
