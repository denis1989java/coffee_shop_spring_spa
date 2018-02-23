package ru.mail.coffee.service;

import ru.mail.coffee.model.Coffee;

import java.util.List;

/**
 * @author Denis Monich
 */
public interface CoffeeDAO {

    List<Coffee> list();
}
