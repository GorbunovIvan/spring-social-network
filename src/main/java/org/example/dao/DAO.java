package org.example.dao;

import java.util.List;

public interface DAO<Model> {

    void create(Model model);
    Model read(int id);
    List<Model> readALl();
    void update(Model model);
    void delete(Model model);
}
