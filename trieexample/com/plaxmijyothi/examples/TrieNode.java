package com.plaxmijyothi.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Object that represents a node in a Trie.
 * 
 * Each node contains a map of children which has one char as the key and the
 * corresponding node as it's value.
 */
public class TrieNode {
	
  private Map<String, TrieNode> children;
  private boolean terminal;

  public TrieNode() {
    children = new HashMap<String, TrieNode>();
  }

  /**
   * Inserts the given string into the current trie.
   * 
   * Traverses through the trie to insert the word, creating new nodes as
   * appropriate. If the word already exists, this will be a no-op.
   * 
   * @param str The string that should be inserted into the trie.
   */
  public void insert(String str) {
    if (str == null || str.length() == 0) {
      this.terminal = true;
      return;
    } else {
      String head = String.valueOf(str.charAt(0));
      String tail = str.length() == 1 ? "" : str.substring(1);
      if (!children.containsKey(head)) {
        children.put(head, new TrieNode());
      }
      children.get(head).insert(tail);
    }
  }

  /**
   * Looks up the given string in the current trie.
   * 
   * Will return true iff the given string is a word, and not a prefix. For
   * prefix matches, see {@link #lookup(String, boolean)}.
   * 
   * @param str The string to lookup.
   * @return {@code true} if the string exists as a word in the current trie,
   *         otherwise returns {@code false}.
   */
  public boolean lookup(String str) {
    return this.lookup(str, false);
  }

  /**
   * Looks up the given string in the current trie.
   * 
   * This method can be used to lookup words or prefixes.
   * 
   * @param str The string to lookup.
   * @param matchPrefix {@code boolean} Indicate if this is a prefix match or a
   *          full word match.
   * @return if {@code matchPrefix} is {@code true}, will return {@code true} if
   *         the string exists either as a word or a prefix. if
   *         {@code matchPrefix} is {@code false}, will return {@code true} only
   *         if the string exists as a word.
   */
  public boolean lookup(String str, boolean matchPrefix) {
    TrieNode node = this.getPrefixEndNode(str);
    if (node != null && matchPrefix) {
      return true;
    } else if (node != null && !matchPrefix && node.terminal) {
      return true;
    }
    return false;
  }

  /**
   * Returns the node where the given string ends.
   * 
   * @param str The string to get the end node for.
   * @return {@code TrieNode} The node where this given string ends. If the
   *         string does not exist in the trie, returns {@code null}.
   */
  public TrieNode getPrefixEndNode(String str) {
    if (str == null || str.length() == 0) {
      return this;
    } else {
      String head = String.valueOf(str.charAt(0));
      String tail = str.length() == 1 ? "" : str.substring(1);
      if (this.children.containsKey(head)) {
        return this.children.get(head).getPrefixEndNode(tail);
      }
    }
    return null;
  }

  /**
   * Returns a {@code List} of words that start with the given prefix.
   * 
   * @param prefix The prefix to search.
   * @return {@code List<String>} of words that start with the given prefix.
   */
  public List<String> getWords(String prefix) {
    List<String> words = new ArrayList<String>();
    if (this.terminal) {
      words.add(prefix);
    }
    for (Entry<String, TrieNode> entry : this.children.entrySet()) {
      String newPrefix = prefix + entry.getKey();
      words.addAll(entry.getValue().getWords(newPrefix));
    }
    return words;
  }

}
