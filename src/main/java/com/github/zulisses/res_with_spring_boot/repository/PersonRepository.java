package com.github.zulisses.res_with_spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.zulisses.res_with_spring_boot.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
