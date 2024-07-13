package com.upwork.config;

import com.upwork.entity.Person;
import com.upwork.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PersonRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(PersonRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.guestRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Person createPerson(Person signupRequest) {
        //Check if customer already exist
        if (guestRepository.existsByCustomerEmail(signupRequest.getCustomer().getEmail())) {
            return null;
        }

        Person guest = new Person( null,
                signupRequest.getName(),
                signupRequest.getCustomer());

        String hashPassword = passwordEncoder.encode(signupRequest.getCustomer().getPassword());
        guest.getCustomer().setPassword(hashPassword);
        Person createdCustomer = guestRepository.save(guest);
        guest.setId(createdCustomer.getId());

        return guest;
    }

}