package vn.com.la.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vn.com.la.domain.Authority;
import vn.com.la.domain.Team;
import vn.com.la.domain.User;
import vn.com.la.repository.AuthorityRepository;
import vn.com.la.config.Constants;
import vn.com.la.repository.TeamRepository;
import vn.com.la.repository.UserRepository;
import vn.com.la.security.AuthoritiesConstants;
import vn.com.la.security.SecurityUtils;
import vn.com.la.service.specification.UserSpecifications;
import vn.com.la.service.util.RandomUtil;
import vn.com.la.service.dto.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.la.web.rest.vm.ManagedUserVM;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static vn.com.la.service.specification.UserSpecifications.loginOrEmailContainsIgnoreCase;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final TeamRepository teamRepository;

    private final EntityManager em;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager, TeamRepository teamRepository, EntityManager em) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.teamRepository = teamRepository;
        this.em = em;
    }


    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                cacheManager.getCache("users").evict(user.getLogin());
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                cacheManager.getCache("users").evict(user.getLogin());
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                cacheManager.getCache("users").evict(user.getLogin());
                return user;
            });
    }

    public User createUser(String login, String password, String firstName, String lastName, String email,
        String imageUrl, String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setImageUrl(imageUrl);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUserWithPassword(ManagedUserVM managedUserVM) {
        User user = buildUser(managedUserVM);
        user.setPassword(passwordEncoder.encode(managedUserVM.getPassword()));

        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    private User buildUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userDTO.getAuthorities().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setStartDate(userDTO.getStartDate());
        Team team = null;
        if(userDTO.getTeamId() != null) {
            team = teamRepository.findOne(userDTO.getTeamId());
            team.addMember(user);
        }
        user.setTeam(team);
        user.setCapacity(userDTO.getCapacity());
        user.setStatus(userDTO.getStatus());
        return user;
    }

    public User createUser(UserDTO userDTO) {
        User user = buildUser(userDTO);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (last name) for the current user.
     *
     * @param lastName last name of user
     */
    public void updateUser(String lastName) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setLastName(lastName);
            cacheManager.getCache("users").evict(user.getLogin());
            log.debug("Changed Information for User: {}", user);
        });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                cacheManager.getCache("users").evict(user.getLogin());
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public Optional<UserDTO> updateUser2(UserDTO userDTO, String oldPassword, String newPassword, boolean isNotChangePassword) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());

                if(isNotChangePassword) {
                    user.setPassword(oldPassword);
                }else {
                    user.setPassword(passwordEncoder.encode(newPassword));
                }
                user.setStartDate(userDTO.getStartDate());

                Team newTeam = null;
                if(userDTO.getTeamId() != null) {
                    newTeam = teamRepository.findOne(userDTO.getTeamId());
                    if(user.getTeam() == null) {
                        newTeam.addMember(user);
                    }
                }

                if((user.getTeam() != null && newTeam == null) || (user.getTeam() != null && newTeam != null && (newTeam.getId() != user.getTeam().getId()))) {
                    Team oldTeam = user.getTeam();
                    oldTeam.getMembers().remove(user);
                    teamRepository.save(oldTeam);
                }

                user.setTeam(newTeam);
                user.setCapacity(userDTO.getCapacity());


                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                Set<String> authorities = userDTO.getAuthorities();
                if(authorities == null || authorities.isEmpty()) {
                    authorities = new HashSet<>();
                    authorities.add(AuthoritiesConstants.USER);
                }

                authorities.stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);

                user.setStatus(userDTO.getStatus());

                cacheManager.getCache("users").evict(user.getLogin());
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            cacheManager.getCache("users").evict(login);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String newPassword) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            cacheManager.getCache("users").evict(user.getLogin());
            log.debug("Changed password for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findBySearchTerm(Pageable pageable, String searchTerm) {
        Specification<User> searchSpec = loginOrEmailContainsIgnoreCase(searchTerm);
        Page<UserDTO> page = userRepository.findAll(searchSpec,pageable).map(UserDTO::new);
        return page;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            cacheManager.getCache("users").evict(user.getLogin());
        }
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }
}
