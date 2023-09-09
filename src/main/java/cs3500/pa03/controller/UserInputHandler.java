package cs3500.pa03.controller;

import java.util.Scanner;

/**
 * Reads input from the user, throws exception if invalid (when appropriate)
 */
public class UserInputHandler {
  private final Scanner sc;

  /**
   * Builds a new UIH to read from System.in
   *
   * @param readable - the readable to read from
   */
  public UserInputHandler(Readable readable) {
    this.sc = new Scanner(readable);
  }

  /**
   * Returns the input read by this scanner between the given low and high values (inclusive),
   * or if the value is not between those values, then an exception is thrown.
   *
   * @param low  - the lowest the input may be
   * @param high - the highest the input may be
   * @return - an integer argument input between the low and high values
   * @throws IllegalArgumentException - if input read is not between the low and high values.
   */
  public int nextInput(int low, int high) throws IllegalArgumentException {
    int result = sc.nextInt();

    if (result >= low && result <= high) {
      return result;
    } else {
      throw new IllegalArgumentException("Input " + result + " not between " + low + " & " + high);
    }
  }
}
