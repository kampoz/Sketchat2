package com.kampoz.sketchat.realm;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by wasili on 2017-05-19.
 */

public class LayerRealm extends RealmObject {

  long id;
  RealmList<DrawPathRealm> paths = new RealmList<>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public RealmList<DrawPathRealm> getPaths() {
    return paths;
  }

  public void setPaths(RealmList<DrawPathRealm> paths) {
    this.paths = paths;
  }

  public static int generateId() {
    Realm defaultInstance = Realm.getDefaultInstance();
    int newId = 0;
    Number oldMaxId = defaultInstance.where(LayerRealm.class).max("id");
    if(oldMaxId==null){
      return newId;
    }else
      return oldMaxId.intValue()+1;
  }

}
