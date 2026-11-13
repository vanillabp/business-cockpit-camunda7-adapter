package io.vanillabp.cockpit.camunda7.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.vanillabp.cockpit.camunda7.Camunda7DeleteReasons;
import io.vanillabp.integration.test.utils.SuppressOutputExtension;

/**
 * Only a delete reason a person wrote becomes the comment in the cockpit. The texts the engine
 * writes on its own are left out, the way Camunda 7.24 spells them.
 */
@ExtendWith(SuppressOutputExtension.class)
public class Camunda7DeleteReasonsTest {

  @Test
  @DisplayName("A reason a person wrote is handed on as it is")
  public void aReasonOfAPersonIsHandedOn() {

    assertEquals("the customer withdrew", Camunda7DeleteReasons.writtenByAPerson("the customer withdrew"));

  }

  @Test
  @DisplayName("No reason, or an empty one, is no comment")
  public void noReasonIsNoComment() {

    assertNull(Camunda7DeleteReasons.writtenByAPerson(null));
    assertNull(Camunda7DeleteReasons.writtenByAPerson(" "));

  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "completed", "deleted", "migration", "Multi instance completion condition satisfied.", "Cancellation due to process instance modifcation", "Interrupting activity Activity(Approve) executed.", "Cancel scope activity Activity(Approve) executed.", "Cancelling activity Activity(Approve) executed.", "Scope Activity(SubProcess) cancelled."
      })
  @DisplayName("A text the engine writes on its own is no comment")
  public void theWordsOfTheEngineAreNoComment(
      final String deleteReason) {

    assertNull(Camunda7DeleteReasons.writtenByAPerson(deleteReason));

  }

}
