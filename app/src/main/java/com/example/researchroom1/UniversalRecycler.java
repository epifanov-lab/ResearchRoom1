/*
 * UniversalRecycler.java
 * webka
 *
 * Copyright (C) 2019, Realtime Technologies Ltd. All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the
 * property of Realtime Technologies Limited and its SUPPLIERS, if any.
 *
 * The intellectual and technical concepts contained herein are
 * proprietary to Realtime Technologies Limited and its suppliers and
 * may be covered by Russian Federation and Foreign Patents, patents
 * in process, and are protected by trade secret or copyright law.
 *
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Realtime Technologies Limited.
 */

package com.example.researchroom1;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.annotation.StyleableRes;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.webka.sdk.schedulers.Schedulers;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * @author Konstantin Epifanov
 * @since 17.09.2019
 */
public class UniversalRecycler extends RecyclerView implements BiConsumer<Object[], ToIntFunction<Object>> {

  /** View Name */
  public static String NAME = "UniversalRecycler";

  /**
   * Global AsyncDifferConfig
   * */
  public static final AsyncDifferConfig<?> ASYNC_DIFFER_CONFIG =
    new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<Object>() {
      @Override public final boolean areItemsTheSame(Object oldItem, Object newItem) { return oldItem.hashCode() == newItem.hashCode(); }
      @Override public final boolean areContentsTheSame(Object oldItem, Object newItem) { return Objects.equals(oldItem, newItem); }
      @Nullable
      @Override public Object getChangePayload(@NonNull Object oldItem, @NonNull Object newItem) { return newItem; }})
      .setBackgroundThreadExecutor(Schedulers.WORK_EXECUTOR)
      .build();

  /* TODO приходиться делать setTag вместо accept */

  /** The default attr resource. */
  @AttrRes
  private static final int DEFAULT_ATTRS = 0;

  /** The empty style resource. */
  @StyleRes
  private static final int DEFAULT_STYLE = 0;

  /** Default styleable attributes */
  @StyleableRes
  private static final int[] DEFAULT_STYLEABLE = new int[0];

  private static final Item[] EMPTY_ITEMS = new Item[0];

  private Item[] items = EMPTY_ITEMS;

  public UniversalRecycler(@NonNull Context context) {
    this(context, null);
  }

  public UniversalRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, DEFAULT_ATTRS);
  }

  public UniversalRecycler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    final Resources.Theme theme = context.getTheme();
    final TypedArray attributes = theme.obtainStyledAttributes
      (attrs, DEFAULT_STYLEABLE, defStyle, DEFAULT_STYLE);
    try {

    } finally {
      attributes.recycle();
    }

    setItemViewCacheSize(0);
    setHasFixedSize(true);
    setOverScrollMode(OVER_SCROLL_NEVER);

    setAdapter(new Adapter() {
      LayoutInflater inflater = LayoutInflater.from(context);

      { setHasStableIds(false); }

      @NonNull
      @Override
      public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(viewType, parent, false)) {};
      }

      @SuppressWarnings("unchecked")
      @Override
      public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        //if (payloads.isEmpty()) onBindViewHolder(holder, position);
        //else {
        //  //final Consumer<T> view = ((Consumer<T>) holder.itemView);
        //  //payloads.forEach(item -> view.accept((T) item));
        //}
    }

      @SuppressWarnings("unchecked")
      @Override
      public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(items[position].object);
      }

      @Override
      public int getItemCount() {
        return items.length;
      }

      @Override
      public int getItemViewType(int position) {
        return items[position].layout;
      }

      @Override
      public long getItemId(int position) {
        return items[position].object.hashCode();
      }
    });

  }

  @Override
  public void accept(Object[] items, ToIntFunction<Object> mapper) {
    this.items = items == null ?
      EMPTY_ITEMS : Stream.of(items)
      .map(o -> new Item(o, mapper.applyAsInt(o)))
      .toArray(Item[]::new);
    requireNonNull(getAdapter()).notifyDataSetChanged();
  }

  private static final class Item {
    final Object object;
    final int layout;

    Item(Object object, int layout) {
      this.object = object;
      this.layout = layout;
    }

    private ViewHolder getViewHolder(LayoutInflater inflater) {
      return new ViewHolder(inflater.inflate(layout, null, false)) {};
    }
  }

  public static RecyclerView.ItemDecoration decorator(int offsetDp) {
    float density = Resources.getSystem().getDisplayMetrics().density;
    int offsetPx = (int) (offsetDp * density);

    return new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        outRect.top = offsetPx;

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
          GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
          int spanCount = manager.getSpanCount();

          if (manager.getOrientation() == RecyclerView.VERTICAL) {
            boolean even = position == 0 || position % spanCount == 0;
            outRect.left = offsetPx / (even ? 1 : 2);
            outRect.right = offsetPx / (even ? 2 : 1);

            if (position == parent.getAdapter().getItemCount() - 1) {
              outRect.bottom = offsetPx;
            }

          } else {
            outRect.left = offsetPx;
          }

        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
          outRect.left = offsetPx;
          outRect.right = offsetPx;
        }
      }
    };
  }

}
