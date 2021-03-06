// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.story.data;

/**
 * Static methods to help end a story naturally
 */
public class StoryEndingTools {
  /** the endings to add to the story (should be one sentence) */
  private static String[] ENDINGS = {
      "The End.",
      "They lived happily ever after.",
      "Then, everything went horribly wrong.",
      "And--- that's a wrap.",
      "Goodbye!",
      "Then the director screamed \"CUT!\".",
      "With that, our story draws to a close.",
      "We'll never know what happened next.",
  };

  /** an array of sentence enders */
  public static final String[] SENTENCE_ENDERS = {".", "?", "!"};

  /**
   * Overrides default constructor to ensure class can't be instantiated.
   */
  private StoryEndingTools() {
    throw new AssertionError();
  }

  /**
   * Takes a story from GPT-2 and returns ones with a more natural ending.
   *
   * @param story the story to return with a natural ending
   * @return story with ending
   * @throws IllegalArgumentException if story is null
   */
  public static String endStory(String story) {
    validateStory(story);

    story = removeSentenceFragmentAtEnd(story);
    story = addEnding(story);

    return story;
  }

  /**
   * Remove a sentence fragment at the end of the story (if there is one)
   * as that commonly occurs with GPT2-generated text. Returns the story
   * up to the last sentence-ending punctuation (period, exclamation point,
   * or question mark).
   *
   * @param story to remove the sentence fragment from
   * @return the passed-in story without the last sentence fragment
   * @throws IllegalArgumentException if story is null
   */
  public static String removeSentenceFragmentAtEnd(String story) {
    validateStory(story);

    // find the last sentence-ending punctuation
    final int NOT_FOUND = -1;
    int lastSentenceEnder = NOT_FOUND;

    for (String ender : SENTENCE_ENDERS) {
      int lastIndex = story.lastIndexOf(ender);

      if (lastIndex > lastSentenceEnder) {
        lastSentenceEnder = lastIndex;
      }
    }

    // if sentence-ending punctuation coincides with end of story, then return story as is
    if (lastSentenceEnder != NOT_FOUND && lastSentenceEnder == story.length() - 1) {
      return story;
    }

    // else, chop off story after last period
    return story.substring(0, lastSentenceEnder + 1);
  }

  /**
   * Add one of a list of pre-written endings to the story, and return
   * the story with that ending.
   *
   * @param story the story to add an ending to
   * @return story + ending
   * @throws IllegalArgumentException if story is null
   */
  public static String addEnding(String story) {
    validateStory(story);

    return story + " " + ENDINGS[(int) (Math.random() * ENDINGS.length)];
  }

  /**
   * Helper method to validate that a story is null.
   * Throws IllegalArgumentException if it's not valid.
   *
   * @param story the story to validate
   * @throws IllegalArgumentException if story is null
   */
  private static void validateStory(String story) throws IllegalArgumentException {
    if (story == null) {
      throw new IllegalArgumentException("Story should not be null.");
    }
  }
}
