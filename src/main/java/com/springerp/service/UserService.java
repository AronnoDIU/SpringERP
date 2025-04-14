package com.springerp.service;

import com.springerp.entity.User;

public interface UserService {
    /**
     * Retrieves a user.
     *
     * @return the user with the specified ID
     */
    User getUser();

    /**
     * Creates a new user in the database.
     *
     * @param user the user to create
     * @return the created user
     */
    User createUser(User user);

    /**
     * Updates an existing user in the database.
     *
     * @param user the updated user information
     * @return the updated user
     */
    User updateUser(User user);

    /**
     * Deletes a user from the database.
     */
    void deleteUser();

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the currently logged-in user
     */
    User getLoggedInUser();
}
