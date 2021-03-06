package vn.hunghd.flutter.plugins.imagecropper;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import androidx.appcompat.app.AppCompatDelegate;

/** ImageCropperPlugin */
public class ImageCropperPlugin implements MethodCallHandler {
  static
  {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }
  private static final String CHANNEL = "plugins.hunghd.vn/image_cropper";

  private final Registrar registrar;
  private final ImageCropperDelegate delegate;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    if (registrar.activity() == null) {
      // If a background flutter view tries to register the plugin, there will be no activity from the registrar,
      // we stop the registering process immediately because the ImageCropper requires an activity.
      return;
    }

    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);

    final ImageCropperDelegate delegate = new ImageCropperDelegate(registrar.activity());
    registrar.addActivityResultListener(delegate);

    channel.setMethodCallHandler(new ImageCropperPlugin(registrar, delegate));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (registrar.activity() == null) {
      result.error("no_activity", "image_cropper plugin requires a foreground activity.", null);
      return;
    }
    if (call.method.equals("cropImage")) {
      delegate.startCrop(call, result);
    }
  }

  ImageCropperPlugin(Registrar registrar, ImageCropperDelegate delegate) {
    this.registrar = registrar;
    this.delegate = delegate;
  }
}
