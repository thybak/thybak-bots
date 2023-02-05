package com.thybak.bots.kkbot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PooRepository extends CrudRepository<Poo, Long> { }
