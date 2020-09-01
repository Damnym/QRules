package com.plastic305.web.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Client;

public interface IClientDAO extends CrudRepository<Client, Long> {

}
