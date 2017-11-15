package model.commons;

import util.Utilities;

import java.util.*;

/**
 * Represents a base interface that should be implemented by all classes that is searchable.
 *
 * @version 1.0
 * @since 2017-10-20
 */
public interface Searchable {

    /**
     * The threshold score for the searching similarity is 5.
     */
    int SEARCH_SIMILARITY_THRESHOLD = 5;

    /**
     * Gets this searchable's search tag.
     *
     * @return this searchable's search tag.
     */
    List<String> getSearchTags();

    /**
     * Gets the list of searchable for the given keywords.
     * @param searchables The given searchable word.
     * @param keyword The given keyword
     * @return matched results according to the fuzzySearch algorithm.
     */
    static List<Searchable> fuzzySearch(List<Searchable> searchables, String keyword) {
        Hashtable<Searchable, Integer> searchResults = new Hashtable<>();
        for (Searchable item: searchables) {
            int minDistance = Integer.MAX_VALUE;
            for (String tag : item.getSearchTags()) {
                int tagMaxScore = tag.length() + (tag.length() - 1) * 2;
                int score = Utilities.fuzzyScore(keyword, tag);
                if (score > 0) {
                    int distance = tagMaxScore - score;
                    if (minDistance > distance)
                        minDistance = distance;
                }
            }
            if (minDistance <= SEARCH_SIMILARITY_THRESHOLD)
                searchResults.put(item, minDistance);
        }
        ArrayList<Searchable> matches = new ArrayList<>(searchResults.keySet());
        Collections.sort(matches, Comparator.comparingInt(searchResults::get).reversed());
        return matches;
    }
}
