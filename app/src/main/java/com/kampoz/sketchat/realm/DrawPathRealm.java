/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kampoz.sketchat.realm;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class DrawPathRealm extends RealmObject {

  private long id;
  private boolean completed;
  private int color;
  private RealmList<DrawPointRealm> points;

  public DrawPathRealm() {
    setId(generateId());
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public RealmList<DrawPointRealm> getPoints() {
    return points;
  }

  public void setPoints(RealmList<DrawPointRealm> points) {
    this.points = points;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long generateId() {
    long newId;
    Realm defaultInstance = Realm.getDefaultInstance();
    Number oldMaxIdNumber = defaultInstance.where(DrawPathRealm.class).max("id");
    //Long oldMaxId = oldMaxIdNumber.longValue();
    if (oldMaxIdNumber == null) {
      newId = 1;
    } else {
      newId = oldMaxIdNumber.intValue() + 1;
    }
    defaultInstance.close();
    return newId;
  }
}
