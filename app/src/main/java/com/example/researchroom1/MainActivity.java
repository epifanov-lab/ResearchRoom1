package com.example.researchroom1;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  public static final int ROOM_MEMBERS_MAX_COUNT = 8;

  private UniversalRecycler mRecycler;
  private View mButtonAdd, mButtonReroll;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.room_root);

    mRecycler = findViewById(R.id.recycler);
    mButtonAdd = findViewById(R.id.button_add);
    mButtonReroll = findViewById(R.id.button_reroll);

    mRecycler.accept(generateRoomMembers(), value -> R.layout.room_member);
    mRecycler.addItemDecoration(UniversalRecycler.decorator(4));

    mButtonAdd.setOnClickListener(v -> addMember());
    mButtonReroll.setOnClickListener(v -> reroll());
  }

  private RoomMember[] generateRoomMembers() {
    List<RoomMember> result = new ArrayList<>();

    int count = 1 + new Random().nextInt(ROOM_MEMBERS_MAX_COUNT - 1);
    int mePos = new Random().nextInt(count);

    for (int i = 0; i < ROOM_MEMBERS_MAX_COUNT; i++) {
      if (i < count) result.add(new RoomMember(i == 0, i == mePos));
      else result.add(RoomMember.getEmptySlot());
    }

    return result.toArray(new RoomMember[0]);
  }

  private void addMember() {

  }

  private void reroll() {
    mRecycler.accept(generateRoomMembers(), value -> R.layout.room_member);
  }

}
