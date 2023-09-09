package cs3500.pa03.view;

import java.io.IOException;

/**
 * Represents a viewer used when Input/Output for user is necessary. Either for setting up the
 * game or the salvo stage.
 */
public class GameViewer {
  /**
   * The appendable to use.
   */
  private Appendable appendable;

  /**
   * Constructs a GameViewer with the given appendable.
   *
   * @param toUseAppendable - the appendable to use
   */
  public GameViewer(Appendable toUseAppendable) {
    this.appendable = toUseAppendable;
  }

  /**
   * Tells the user they input an invalid number and should put a number between given low
   * and high, inclusive.
   *
   * @param low  - the lowest number the user may input
   * @param high - the highest number the user may input
   */
  public void scold(int low, int high) {
    try {
      appendable.append("Invalid input. Must be between " + low + " to " + high + ", inclusive.\n");
    } catch (IOException e) {
      this.issueWithAppendable(e);
    }
  }

  /**
   * Instructs user to input a number between given low and high integers (inclusive) for
   * given purpose.
   *
   * @param purpose - a brief description as to what the user is inputting a number for.
   * @param low     - the lowest number the user may put in.
   * @param high    - the highest number the user may put in.
   */
  public void promptInputFor(String purpose, int low, int high) {
    try {
      appendable.append(
          "Enter a number between " + low + " and " + high + " for " + purpose + ": ");
    } catch (IOException e) {
      this.issueWithAppendable(e);
    }
  }

  /**
   * Displays given text to the user.
   *
   * @param text - String to be displayed to the user
   */
  public void display(String text) {
    try {
      appendable.append(text + "\n");
    } catch (IOException e) {
      this.issueWithAppendable(e);
    }
  }

  /**
   * Indicates directly to the console that there is an issue with this viewer's appendable.
   *
   * @param e - the issue noted when trying to append to this's appendable
   */
  private void issueWithAppendable(IOException e) {
    System.err.println("Problem with viewer: " + e);
  }
}
