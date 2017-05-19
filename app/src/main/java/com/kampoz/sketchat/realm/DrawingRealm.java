package com.kampoz.sketchat.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by wasili on 2017-05-19.
 */

public class DrawingRealm extends RealmObject {

  long id;
  RealmList<LayerRealm> layers = new RealmList<>();

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
}
