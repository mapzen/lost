package com.mapzen.android.lost.api;

import android.content.Intent;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A data class representing a geographic location result from the fused location provider.
 */
public final class LocationResult implements Parcelable {
  static final String EXTRA_LOCATION_RESULT = "com.mapzen.android.lost.EXTRA_LOCATION_RESULT";

  private final List<Location> locations;

  /**
   * Creates a {@link LocationResult} for the given locations.
   */
  public static LocationResult create(List<Location> locations) {
    if (locations == null) {
      locations = Collections.emptyList();
    }

    return new LocationResult(locations);
  }

  /**
   * Package private constructor.
   */
  LocationResult(List<Location> locations) {
    this.locations = locations;
  }

  /**
   * Returns locations computed, ordered from oldest to newest.
   */
  @NonNull public List<Location> getLocations() {
    return locations;
  }

  /**
   * Returns the most recent location available in this result, or null if no locations are
   * available.
   */
  public Location getLastLocation() {
    if (locations.size() == 0) {
      return null;
    }

    return locations.get(locations.size() - 1);
  }

  /**
   * Returns true if an {@link Intent} contains a {@link LocationResult}.
   *
   * @return {@code true} if the {@link Intent} contains a {@link LocationResult}, {@code false}
   * otherwise.
   */
  public static boolean hasResult(Intent intent) {
    return intent != null && intent.hasExtra(EXTRA_LOCATION_RESULT);
  }

  /**
   * Extracts the {@link LocationResult} from an {@link Intent}.
   *
   * @return a {@link LocationResult}, or {@code null} if the {@link Intent} doesn't contain a
   * result.
   */
  public static LocationResult extractResult(Intent intent) {
    if (!hasResult(intent)) {
      return null;
    }

    return intent.getParcelableExtra(EXTRA_LOCATION_RESULT);
  }

  /**
   * Two instances of {@link LocationResult} are considered to be equal if they have the same number
   * of locations and the timestamp on each location matches the timestamp of its counterpart in the
   * other instance.
   */
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final LocationResult that = (LocationResult) o;
    if (this.locations.size() != that.locations.size()) return false;

    final Iterator<Location> thisIterator = this.locations.iterator();
    final Iterator<Location> thatIterator = that.locations.iterator();
    while (thisIterator.hasNext()) {
      if (thisIterator.next().getTime() != thatIterator.next().getTime()) {
        return false;
      }
    }

    return true;
  }

  @Override public int hashCode() {
    return locations.hashCode();
  }

  // Parcelable implementation auto-generated by Android Studio

  protected LocationResult(Parcel in) {
    locations = in.createTypedArrayList(Location.CREATOR);
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(locations);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static final Creator<LocationResult> CREATOR = new Creator<LocationResult>() {
    @Override public LocationResult createFromParcel(Parcel in) {
      return new LocationResult(in);
    }

    @Override public LocationResult[] newArray(int size) {
      return new LocationResult[size];
    }
  };
}
