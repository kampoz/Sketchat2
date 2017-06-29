package com.kampoz.sketchat.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-05-19.
 */

public class DrawingRealm extends RealmObject {

  @PrimaryKey
  private long id;
  RealmList<LayerRealm> layers = new RealmList<>();
  RealmList<DrawPathRealm> paths = new RealmList<>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public RealmList<LayerRealm> getLayers() {
    return layers;
  }

  public void setLayers(RealmList<LayerRealm> layers) {
    this.layers = layers;
  }

  public RealmList<DrawPathRealm> getPaths() {
    return paths;
  }

  public void setPaths(RealmList<DrawPathRealm> paths) {
    this.paths = paths;
  }

}
