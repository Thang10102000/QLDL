/**
 *
 */
package com.neo.vas.service.impl;

import com.neo.vas.domain.GroupsUsers;
import com.neo.vas.domain.Groupss;
import com.neo.vas.domain.Users;
import com.neo.vas.domain.UsersLevelsFunctional;
import com.neo.vas.repository.*;
import com.neo.vas.service.UsersService;
import com.neo.vas.service.specification.UsersSpecification;
import com.neo.vas.util.VNCharacterUtils;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Service
public class UsersServiceImpl implements UsersService {

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_NOT_ACTIVE = 0;
    private final UsersRepository usersRepository;
    private final SystemConfigRepository systemConfigRepository;
    private final LevelsRepository levelsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsersLevelsFunctionalRepository usersLevelsFunctionalRepository;
    private final GroupsUsersRepository groupsUsersRepository;
    private final GroupsRepository groupsRepository;
    private final AgencyAreaRepository agencyAreaRepository;

    public UsersServiceImpl(UsersRepository usersRepository, SystemConfigRepository systemConfigRepository,
                            LevelsRepository levelsRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                            UsersLevelsFunctionalRepository usersLevelsFunctionalRepository,
                            GroupsUsersRepository groupsUsersRepository, GroupsRepository groupsRepository,
                            AgencyAreaRepository agencyAreaRepository, AgencyRepository agencyRepository) {
        this.usersRepository = usersRepository;
        this.systemConfigRepository = systemConfigRepository;
        this.levelsRepository = levelsRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.usersLevelsFunctionalRepository = usersLevelsFunctionalRepository;
        this.groupsUsersRepository = groupsUsersRepository;
        this.groupsRepository = groupsRepository;
        this.agencyAreaRepository = agencyAreaRepository;
    }

    @Override
    public Users getUsersByUsername(String username) {
        Optional<Users> optUsers = usersRepository.findById(username);
        Users users = null;
        if (optUsers.isPresent()) {
            users = optUsers.get();
        } else {
            throw new RuntimeException("Không tìm thấy user " + username);
        }
        return users;
    }

    @Override
//	@Cacheable(value = "pageUser", key = "{#user.username,#user.email, #user.phone, #user.fax, #page, #pageSize}")
    @Transactional(readOnly = true)
    public Page<Users> searchUsers(String username, String fullname, String email, String phone, String levelId,
                                   String groupId, String areaId, String agencyId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createDate").descending().and(Sort.by("levelUsers").descending()).and(Sort.by("fullname").descending()));
        if ((username != null && !username.equals("")) || (fullname != null && !fullname.equals("")) || (email != null && !email.equals(""))
                || (phone != null && !phone.equals("")) || !levelId.equals("") || !groupId.equals(""))
            return usersRepository.searchUser(VNCharacterUtils.removeAccent(username.toUpperCase()),
                    VNCharacterUtils.removeAccent(fullname.toUpperCase()),
                    VNCharacterUtils.removeAccent(email.toUpperCase()), phone, levelId, groupId, areaId, agencyId,
                    pageable);
        return usersRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public String saveNewUser(JSONObject data, Principal principal) {
        try {
            Users newUser = new Users();
            if (!data.getString("username").isEmpty()) {
                if (!usersRepository.existsById(data.getString("username"))) {
                    newUser.setUsername(data.getString("username").trim());
                } else {
                    System.err.println("Tài khoản đã tồn tại");
                    return "EXISTING_ACC";
                }
            } else {
                System.err.println("Tên đăng nhập rỗng");
                return "FALSE";
            }
            if (!data.getString("password").isEmpty()
                    && data.getString("password").equals(data.getString("re-password"))) {
                if (data.getString("password").length() < systemConfigRepository.findAll().get(0).getMinPwLength()) {
                    System.err.println("Password không được nhỏ hơn "
                            + systemConfigRepository.findAll().get(0).getMinPwLength() + " ký tự");
                    return "WRONG_PASSWORD";
                } else {
                    String encodedPassword = bCryptPasswordEncoder.encode(data.getString("password"));
                    newUser.setPassword(encodedPassword);
                }
            }
            if (!data.getString("fullname").isEmpty()) {
                newUser.setFullname(data.getString("fullname").trim());
            }
            if (!data.getString("email").isEmpty()) {
                newUser.setEmail(data.getString("email").trim());
            }
            if (!data.getString("phone").isEmpty()) {
                newUser.setPhone(data.getString("phone").trim());
            }
            if (!data.getString("fax").isEmpty()) {
                newUser.setFax(data.getString("fax").trim());
            }
            if (!data.getString("address").isEmpty()) {
                newUser.setAddress(data.getString("address").trim());
            }
            if (!data.getString("levels").isEmpty()) {
                try {
                    newUser.setLevelUsers(levelsRepository.getOne(Long.parseLong(data.getString("levels"))));
                } catch (Exception e) {
                    System.err.println(e);
                    newUser.setLevelUsers(null);
                }
            } else {
                newUser.setLevelUsers(null);
            }
            if (!data.getString("areaId").isEmpty()) {
                try {
                    newUser.setAreaId(agencyAreaRepository.getOne(Long.parseLong(data.getString("areaId"))));
                } catch (Exception e) {
                    System.err.println(e);
                    newUser.setAreaId(null);
                }
            } else {
                newUser.setAreaId(null);
            }
            if (!data.getString("agencyId").isEmpty()) {
                try {
                    Users us = usersRepository.getUsersByAgencyId(Long.parseLong(data.getString("agencyId")));
                    if (us != null)
                        return "ExistUserInAgency";
                    newUser.setAgencyId(Long.parseLong(data.getString("agencyId")));
                } catch (Exception e) {
                    System.err.println(e);
                    newUser.setAgencyId(null);
                }
            } else {
                newUser.setAgencyId(null);
            }
            newUser.setAuthorized(1);
            newUser.setStatus(STATUS_ACTIVE);
            newUser.setLastLoginDate(new Date());
            newUser.setCreateDate(new Date());
            newUser.setCreateBy(principal.getName());
            try {
                if (!data.getString("never-expired").isEmpty()) {
                    newUser.setPasswordNeverExpired(1);
                } else {
                    newUser.setPasswordNeverExpired(0);
                }
            } catch (Exception e) {
                System.err.println(e);
                newUser.setPasswordNeverExpired(0);
            }
            usersRepository.saveAndFlush(newUser);
            if (!data.getString("groups").isEmpty()) {
                try {
                    GroupsUsers groupsUsers = new GroupsUsers();
                    groupsUsers.setGroupIdGU(groupsRepository.findByGroupId(data.getString("groups")));
                    groupsUsers.setUsersGU(newUser);
                    groupsUsersRepository.save(groupsUsers);
                } catch (Exception e) {
                    System.err.println(e);
                    return "FALSE";
                }
            } else {
                newUser.setGroupsUsers(null);
            }
            return "TRUE";
        } catch (Exception e) {
            System.err.println(e);
            return "FALSE";
        }
    }

    @Override
    @Transactional
    public String editUser(JSONObject data, Principal principal) {
        try {
            if (!data.getString("username").isEmpty()) {
                if (usersRepository.existsById(data.getString("username"))) {
                    Users newUser = usersRepository.findUsersByUsername(data.getString("username"));
                    newUser.setUsername(data.getString("username").trim());
                    if (!data.getString("password").isEmpty()
                            && data.getString("password").equals(data.getString("re-password"))) {
                        if (data.getString("password").length() >= systemConfigRepository.findAll().get(0)
                                .getMinPwLength()) {
                            String encodedPassword = bCryptPasswordEncoder.encode(data.getString("password"));
                            newUser.setPassword(encodedPassword);
                        } else {
                            System.out.println("Password không được nhỏ hơn "
                                    + systemConfigRepository.findAll().get(0).getMinPwLength() + " ký tự");
                            return "WRONG_PASSWORD";
                        }
                    }
                    if (!data.getString("fullname").isEmpty()) {
                        newUser.setFullname(data.getString("fullname").trim());
                    }
                    if (!data.getString("email").isEmpty()) {
                        newUser.setEmail(data.getString("email").trim());
                    }
                    if (!data.getString("phone").isEmpty()) {
                        newUser.setPhone(data.getString("phone").trim());
                    }
                    if (!data.getString("fax").isEmpty()) {
                        newUser.setFax(data.getString("fax").trim());
                    }
                    if (!data.getString("address").isEmpty()) {
                        newUser.setAddress(data.getString("address").trim());
                    }
                    if (!data.getString("levels").isEmpty()) {
                        try {
                            newUser.setLevelUsers(levelsRepository.getOne(Long.parseLong(data.getString("levels"))));
                        } catch (Exception e) {
                            System.out.println(e);
                            newUser.setLevelUsers(null);
                        }
                    } else {
                        newUser.setLevelUsers(null);
                    }
                    if (!data.getString("areaId").isEmpty()) {
                        try {
                            newUser.setAreaId(agencyAreaRepository.getOne(Long.parseLong(data.getString("areaId"))));
                        } catch (Exception e) {
                            System.err.println(e);
                            newUser.setAreaId(null);
                        }
                    } else {
                        newUser.setAreaId(null);
                    }
                    if (!data.getString("agencyId").isEmpty()) {
                        try {
                            Users us = usersRepository.getUsersByAgencyId(Long.parseLong(data.getString("agencyId")));
                            if (us != null)
                                return "ExistUserInAgency";
                            newUser.setAgencyId(Long.parseLong(data.getString("agencyId")));
                        } catch (Exception e) {
                            System.err.println(e);
                            newUser.setAgencyId(null);
                        }
                    } else {
                        newUser.setAgencyId(null);
                    }
                    newUser.setAuthorized(1);
                    if (data.getInt("status") == 1)
                        newUser.setStatus(STATUS_ACTIVE);
                    else
                        newUser.setStatus(STATUS_NOT_ACTIVE);
                    if (!data.getString("checkExpired").isEmpty()) {
                        if (data.getInt("checkExpired") == 1) {
                            newUser.setPasswordNeverExpired(1);
                        } else {
                            newUser.setPasswordNeverExpired(0);
                        }

                    }
                    newUser.setLastModifyDate(new Date());
                    newUser.setLastModifyBy(principal.getName());
                    if (data.has("groups") && !data.getString("groups").isEmpty()) {
                        try {
                            Groupss groupss = groupsRepository.findByGroupId(data.getString("groups"));
                            if (null != groupsUsersRepository.findByUsersGUAndGroupIdGU(newUser, groupss)) {
                                System.out.println("User is already in groups");
                            } else {
                                for (GroupsUsers gu : newUser.getGroupsUsers()) {
                                    groupsUsersRepository.delete(gu);
                                }
                                GroupsUsers groupsUsers = new GroupsUsers();
                                groupsUsers.setGroupIdGU(groupss);
                                groupsUsers.setUsersGU(newUser);
                                groupsUsersRepository.save(groupsUsers);
                            }
                        } catch (Exception e) {
                            System.err.println(e);
                            return "FALSE";
                        }
                    } else {
                        for (GroupsUsers gu : newUser.getGroupsUsers()) {
                            groupsUsersRepository.delete(gu);
                        }
                        newUser.setGroupsUsers(null);
                    }
                    usersRepository.saveAndFlush(newUser);
                    return "TRUE";
                } else {
                    System.out.println("Tài khoản không tồn tại");
                    return "INVALID_ACC";
                }
            } else {
                System.out.println("Tên đăng nhập rỗng");
                return "INVALID_ACC";
            }
        } catch (Exception e) {
            System.out.println(e);
            return "FALSE";
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(String username, Principal principal) {
        Users user = usersRepository.findUsersByUsername(username);
        // try to delete user
        try {
            for (UsersLevelsFunctional ulf : user.getUsersULF()) {
                usersLevelsFunctionalRepository.delete(ulf);
            }
            for (GroupsUsers gu : user.getGroupsUsers()) {
                groupsUsersRepository.delete(gu);
            }
            usersRepository.delete(user);
        } catch (Exception e) {
            System.out.println(e);
        }
        // check delete
        return !usersRepository.existsById(username);
    }

    @Override
    public boolean checkExistById(String username) {
        return usersRepository.existsById(username);
    }

    @Override
    @Transactional
    public String changePassword(JSONObject data, Principal principal) {
        try {
            Users newUser = usersRepository.findUsersByUsername(principal.getName());
            String crtPass = newUser.getPassword();
            if (!bCryptPasswordEncoder.matches(data.getString("old-password"), crtPass))
                return "WRONG_OLD_PASS";
            if (!data.getString("password").isEmpty()
                    && data.getString("password").equals(data.getString("re-password"))) {
                if (data.getString("password").length() >= systemConfigRepository.findAll().get(0)
                        .getMinPwLength()) {
                    String encodedPassword = bCryptPasswordEncoder.encode(data.getString("password"));
                    newUser.setPassword(encodedPassword);
                    newUser.setLastModifyDate(new Date());
                    newUser.setLastModifyBy(principal.getName());
                    usersRepository.saveAndFlush(newUser);
                    return "TRUE";
                } else {
                    System.out.println("Password không được nhỏ hơn "
                            + systemConfigRepository.findAll().get(0).getMinPwLength() + " ký tự");
                    return "WRONG_PASSWORD";
                }
            }
            return "FALSE";
        } catch (Exception e) {
            System.out.println(e);
            return "FALSE";
        }
    }

    @Override
    @Transactional(readOnly = true)
    // @Cacheable(value = "data", key = "{#username,#page,#pageSize}")
    public Page<Users> loadUsers(String username, int page, int pageSize) {
        Specification<Users> conditions = Specification
                .where(UsersSpecification.hasUsername(VNCharacterUtils.removeAccent(username)));
        Page<Users> data = usersRepository.findAll(conditions, PageRequest.of(page, pageSize));
        return data;
    }

    @Override
//	@Cacheable(value = "userList")
    public List<Users> getAll() {
        List<Users> userList = usersRepository.findAll();
        return userList;
    }

}
