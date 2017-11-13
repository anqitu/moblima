package model.movie;

import model.commons.Person;
import model.commons.Searchable;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a movie-related person who can be a director or an actor.
 *
 * @version 1.0
 * @since 2017-10-20
 */
public class MoviePerson extends Person implements Searchable {

    /**
     * Creates a movie person by a given full name.
     *
     * @param fullName The full name of this movie person.
     */
    public MoviePerson(String fullName) {
        super(fullName);
    }

    /**
     * Creates a movie person by a given first name and last name.
     *
     * @param firstName The first name of this movie person.
     * @param lastName  The last name of this movie person.
     */
    public MoviePerson(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public List<String> getSearchTags() {
        return Arrays.asList(firstName, lastName, getFullName());
    }
}
