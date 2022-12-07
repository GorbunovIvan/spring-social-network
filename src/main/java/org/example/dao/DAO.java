package org.example.dao;

import java.util.List;

public interface DAO<Model> {

    int create(Model model);
    Model read(int id);
    List<Model> readALl();
    void update(int id, Model model);
    void delete(int id);
}
