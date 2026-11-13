package io.vanillabp.cockpit.camunda7;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Which delete reason of the engine a person wrote, and which one the engine wrote itself.
 * <p>
 * Somebody who cancels a process instance in Camunda 7 may give a reason. The engine stores it on
 * the process instance and hands it to every user task the cancellation deletes. The cockpit
 * shows it as the comment of the case and of those tasks, unless the workflow module sets a
 * comment of its own.
 * <p>
 * The engine fills the same field in other cases, too. A task which is completed gets
 * <code>completed</code>. A task whose process instance is deleted without a reason gets
 * <code>deleted</code>. A boundary event, an event subprocess, a multi-instance completion
 * condition, a migration or a process instance modification each leave a sentence of the engine's
 * own, like <code>Interrupting activity Activity(Approve) executed.</code>. None of these is
 * something a person said, and a modelled path out of a task is the normal end of business, not a
 * cancellation by somebody. So these are not handed to the cockpit.
 * <p>
 * There is no flag which tells the two apart. The task listener of a deleted task runs before the
 * engine marks the process instance as deleted, so the reason itself is the only thing at hand.
 * The texts here are the ones Camunda 7.24 writes. A text the engine adds later reaches the cockpit
 * as a comment until it is added here, which a reader sees at once and which harms nothing else.
 */
public final class Camunda7DeleteReasons {

  /** The fixed texts the engine writes on its own. */
  private static final Set<String> WORDS_OF_THE_ENGINE = Set
      .of(
          "completed", "deleted", "migration", "Multi instance completion condition satisfied.",
          // the spelling mistake is the engine's
          "Cancellation due to process instance modifcation");

  /** The sentences the engine writes on its own, which name the activity. */
  private static final List<Pattern> SENTENCES_OF_THE_ENGINE = List
      .of(
          Pattern.compile("Interrupting activity .* executed\\."), Pattern
              .compile("Cancel scope activity .* executed\\."),
          Pattern.compile("Cancelling activity .* executed\\."), Pattern.compile("Scope .* cancelled\\."));

  private Camunda7DeleteReasons() {
  }

  /**
   * The delete reason, where a person wrote it.
   *
   * @param deleteReason The delete reason the engine stored for a process instance or a task
   * @return The same text, or <code>null</code> where there is none or the engine wrote it itself
   */
  public static String writtenByAPerson(
      final String deleteReason) {

    if ((deleteReason == null) || deleteReason.isBlank()) {
      return null;
    }
    if (WORDS_OF_THE_ENGINE.contains(deleteReason)) {
      return null;
    }
    if (SENTENCES_OF_THE_ENGINE.stream().anyMatch(sentence -> sentence.matcher(deleteReason).matches())) {
      return null;
    }
    return deleteReason;

  }

}
