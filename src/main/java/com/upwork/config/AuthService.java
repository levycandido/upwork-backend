package com.upwork.config;

import com.upwork.entity.Person;

public interface AuthService {
    Person createPerson(Person signupRequest);
}