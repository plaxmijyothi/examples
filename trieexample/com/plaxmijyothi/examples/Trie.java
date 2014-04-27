package com.plaxmijyothi.examples;

import java.io.Console;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * A simple Trie that provides lookup and autocomplete features.
 * 
 * To play around with it, run it on the console and pass a path to a file with
 * words (one on each line). If the given path doesn't exist, it will use a
 * default list of words.
 * 
 * (TIP): On an Ubuntu machine, you can pass the arg /usr/share/dict/words.
 */
public class Trie {

  private TrieNode root;

  public Trie() {
    root = new TrieNode();
  }

  /**
   * Insert the given word into the trie. It will be a no-op if the string
   * already exists.
   * 
   * @param str The string to insert.
   */
  public void insert(String str) {
    root.insert(str);
  }

  /**
   * Checks if the given string exists in the current trie as a prefix or a
   * word.
   * 
   * @param str The string to lookup.
   * @return {@code true} If the string exists as a word or a prefix, otherwise
   *         {@code false}.
   */
  public boolean isPrefix(String str) {
    return root.lookup(str, true);
  }

  /**
   * Looks up the given string in the current trie.
   * 
   * @param str The string to lookup.
   * @return {@code true} If the string exists as a word in the given trie,
   *         otherwise {@code false}.
   */
  public String lookup(String str) {
    boolean found = root.lookup(str);
    if (found) {
      return str;
    }
    return null;
  }

  /**
   * Returns a list of words that start with the given prefix.
   * 
   * @param prefix The string to autocomplete.
   * @return {@code List<String>} of words that start with the given prefix.
   */
  public List<String> autoComplete(String prefix) {
    TrieNode prefixNode = root.getPrefixEndNode(prefix);
    if (prefixNode != null) {
      return prefixNode.getWords(prefix);
    }
    return null;
  }

  public List<String> getAllWords() {
    return root.getWords("");
  }

  public static void main(String[] args) {
    Trie exampleTrie = new Trie();
    List<String> allWords = null;
    if (args.length > 0) {
      Path dictFile = Paths.get(args[0]);
      try {
        allWords = Files.readAllLines(dictFile, StandardCharsets.UTF_8);
      } catch (IOException e) {
        allWords = Arrays.asList("Dopamine", "Adrenalin", "Endorphine", "Advil", "End", "Dope");
        System.err.println("Could not find file, using default set of words: "
            + allWords.toString());
      }
    } else {
      allWords = Arrays.asList("Dopamine", "Adrenalin", "Endorphine", "Advil", "End", "Dope");
      System.err.println("No path given, using default set of words: " + allWords.toString());
    }
    for (String word : allWords) {
      exampleTrie.insert(word);
    }
    assert exampleTrie.getAllWords().size() == allWords.size();
    System.out.println("Inserted " + String.valueOf(exampleTrie.getAllWords().size())
        + " words into our Trie.");
    Console console = System.console();
    if (console == null) {
      System.err.println("Could not find a console, quitting!");
      System.exit(99);
    }
    while (true) {
      String prefix = console.readLine("Enter a prefix to auto complete (ctrl-c to quit): ");
      if (prefix != null) {
        List<String> words = exampleTrie.autoComplete(prefix);
        if (words != null) {
          console.writer().write(
              "Prefix " + prefix + " matched the following words: " + words.toString());
          console.writer().println();
          console.writer().flush();
        }
      }
    }
  }
}
