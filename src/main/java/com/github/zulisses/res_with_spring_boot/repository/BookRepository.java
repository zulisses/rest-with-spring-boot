package com.github.zulisses.res_with_spring_boot.repository;

import com.github.zulisses.res_with_spring_boot.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}