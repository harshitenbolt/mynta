package com.canvascoders.opaper.cropview.callback;

import android.net.Uri;



public interface SaveCallback extends Callback {
  void onSuccess(Uri uri);
}
