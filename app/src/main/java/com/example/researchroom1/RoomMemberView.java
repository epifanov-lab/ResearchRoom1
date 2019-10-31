package com.example.researchroom1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @author Konstantin Epifanov
 * @since 31.10.2019
 */
public class RoomMemberView extends RelativeLayout {

  private RoomMember mRoomMember;

  private TextView mTextView;

  public RoomMemberView(Context context) {
    this(context, null);
  }

  public RoomMemberView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RoomMemberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mTextView = findViewById(R.id.text);
  }

  @Override
  public void setTag(Object tag) {
    mRoomMember = (RoomMember) tag;
    update();
  }

  private void update(){
    if (mRoomMember != null) {

      StringBuilder text = new StringBuilder();
      text.append(mRoomMember.isEmpty ? "[empty]" :
        mRoomMember.isHoster ? "[host]" : "[member]");

      if (mRoomMember.isMe) text.append("[me]");
      mTextView.setText(text);

      setBackgroundColor(getResources().getColor(
        mRoomMember.isEmpty ? R.color.slot :
          mRoomMember.isHoster ? R.color.host : R.color.member));
    }
  }
}
