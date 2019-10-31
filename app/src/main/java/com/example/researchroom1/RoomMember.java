package com.example.researchroom1;

/**
 * @author Konstantin Epifanov
 * @since 31.10.2019
 */
public class RoomMember {
  public final boolean isHoster;
  public final boolean isMe;
  public final boolean isEmpty;

  RoomMember(boolean isHoster, boolean isMe) {
    this.isHoster = isHoster;
    this.isMe = isMe;
    this.isEmpty = false;
  }

  private RoomMember() {
    this.isHoster = false;
    this.isMe = false;
    this.isEmpty = true;
  }

  public static RoomMember getEmptySlot() {
    return new RoomMember();
  }

  @Override
  public String toString() {
    return "RoomMember{" +
      "isHoster=" + isHoster +
      ", isMe=" + isMe +
      '}';
  }
}
